package com.vgg.choi.data;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;
import com.kazan.util.AppsUtils;
import com.kazan.util.TypefaceUtil;
import com.vgg.choi.common.BroadcastManager;
import com.vgg.choi.R;

import java.io.File;
import java.util.Random;

/**
 * Created by LeHai on 1/17/2017.
 */

public class GameItem implements ChoiItem {
    @SerializedName("AppId")
    String id;
    @SerializedName("Name")
    String title;
    @SerializedName("Description")
    String description;
    @SerializedName("Icon")
    String thumbnails;
    @SerializedName("MediaUrl")
    String[] mediaUrl;
    String directDownloadUrl;
    boolean downloading;
    @SerializedName("IsHot")
    boolean hotItem;
    String type;
    String size;
    String downloadedCount;
    public String getDirectDownloadUrl() {
        return directDownloadUrl;
    }
    public void setDirectDownloadUrl(String directDownloadUrl) {
        this.directDownloadUrl = directDownloadUrl;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getDownloadedCount() {
        return downloadedCount;
    }
    public void setDownloadedCount(String downloadedCount) {
        this.downloadedCount = downloadedCount;
    }
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    String rating;

    public boolean isHotItem() {
        return hotItem;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getThumbnails() {
        return thumbnails;
    }
    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }
    public String[] getMediaUrl() {
        return mediaUrl;
    }
    public void setMediaUrl(String[] mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    private String getUnknowMessage(Context context, String in) {
        if (TextUtils.isEmpty(in)) {
            return context.getString(R.string.message_unknow);
        }
        return in;
    }
    public void setView(View view) {
        if (view == null) {
            // Cannot do anything with null view
        }
        Context context = view.getContext();
        String actionString = context.getString(AppsUtils.isAppInstalled(view.getContext(), getId())? R.string.game_action_play : R.string.game_action_install);
        String sizeString = context.getString(R.string.game_size) + ": " + getUnknowMessage(context, size);
        String downloadedString = context.getString(R.string.game_download) + ": " + (new Random().nextInt(1000000) + 1000);
        String rateString = context.getString(R.string.game_rating) + ": " + getUnknowMessage(context, rating);
        String typeString = context.getString(R.string.game_type) + ": " + getUnknowMessage(context, type);
        TypefaceUtil.setText(AppsUtils.getView(view, R.id.game_item_title), getTitle());
        TypefaceUtil.setText(AppsUtils.getView(view, R.id.game_item_desc), getDescription());
        TypefaceUtil.setText(AppsUtils.getView(view, R.id.game_item_download_text), downloadedString);
        TypefaceUtil.setTextBold(AppsUtils.getView(view, R.id.game_item_action), actionString);
        TypefaceUtil.setText(AppsUtils.getView(view, R.id.game_item_size), sizeString);
        TypefaceUtil.setText(AppsUtils.getView(view, R.id.game_item_rating), rateString);
        TypefaceUtil.setText(AppsUtils.getView(view, R.id.game_item_type), typeString);

        View actionView = AppsUtils.getView(view, R.id.game_item_action);
        if (actionView != null)	actionView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (downloading) {
                    return;
                }
                if (AppsUtils.isAppInstalled(v.getContext(), getId())) {
                    Intent intent = v.getContext().getPackageManager().getLaunchIntentForPackage(getId());
                    if (intent != null) {
                        v.getContext().startActivity(intent);
                    }
                } else if (TextUtils.isEmpty(getDirectDownloadUrl())) {
                    AppsUtils.openAppMarket(v.getContext(), getId());
                } else {
                    downloadAndInstall(v.getContext());
                    TypefaceUtil.setText(v, v.getContext().getString(R.string.game_action_download));
                }
            }
        });
        ImageView thumbsView = AppsUtils.getView(view, R.id.game_item_image);
        //thumbsView.getLayoutParams().height = thumbsView.getLayoutParams().width;
        if (thumbsView != null)
            Glide
                    .with(context)
                    .load(getThumbnails())
                    .into(thumbsView);
        ViewPager pager = AppsUtils.getView(view, R.id.game_item_image_pager);
        if (pager != null) {
            DisplayMetrics metrics = view.getContext().getResources().getDisplayMetrics();
            pager.getLayoutParams().height = metrics.widthPixels/3 * 2;
        }
        if (downloading) {
            TypefaceUtil.setText(AppsUtils.getView(view, R.id.game_item_action), view.getContext().getString(R.string.game_action_download));
            return;
        }

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(
                DownloadManager.STATUS_PAUSED
                        | DownloadManager.STATUS_PENDING
                        | DownloadManager.STATUS_RUNNING
        );

        DownloadManager dlMgr = (DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        Cursor c = dlMgr.query(query);
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        Uri uri = Uri.parse("file://" + destination + getId() + ".apk");
        while (c.moveToNext()) {

            String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            downloading = uriString.equalsIgnoreCase(uri.toString());
            if (downloading) {
                Log.d("Download", "Downloading: " + uriString);
                TypefaceUtil.setText(AppsUtils.getView(view, R.id.game_item_action), view.getContext().getString(R.string.game_action_download));
                BroadcastReceiver onDownloadCompleted = BroadcastManager.getDownloadBroadcast(id);

                if (onDownloadCompleted == null) {
                    Log.d("Download", "Register Receiver");
                    long downloadId = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID));
                    onDownloadCompleted = getReceiver(downloadId, uri);
                    BroadcastManager.registerDownloadBroadcast(id, onDownloadCompleted);
                } else {
                    Log.d("Download", "Result: " + onDownloadCompleted.getResultData());
                }
                break;
            }
        }
        c.close();
    }

    public void downloadAndInstall(Context context) {
        if (downloading || context == null) {
            return;
        }
        downloading = true;
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + getId() + ".apk";
        final Uri uri = Uri.parse("file://" + destination);// + getId() + ".apk");

        //Delete update file if exists
        File file = new File(destination);
        if (file.exists()) {
            file.delete();
        }

        //set downloadmanager
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(getDirectDownloadUrl()));
        request.setDescription(getDescription());
        request.setTitle(getTitle());
        //set destination
        request.setDestinationUri(uri);

        // get download service and enqueue file
        final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        BroadcastManager.registerDownloadBroadcast(id, getReceiver(downloadId, uri));
    }

    private BroadcastReceiver getReceiver(final long downloadId, final Uri uri) {
        return new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                install.setDataAndType(uri, //"application/vnd.android.package-archive");
                        manager.getMimeTypeForDownloadedFile(downloadId));
                context.startActivity(install);
                downloading = false;
                BroadcastManager.unregisterDownloadBroadcast(id);
            }
        };
    }
    @Override
    public String getItemId() {
        // TODO Auto-generated method stub
        return id;
    }
    @Override
    public String getItemTitle() {
        // TODO Auto-generated method stub
        return title;
    }
    @Override
    public String getItemDescription() {
        // TODO Auto-generated method stub
        return description;
    }
    @Override
    public String getItemThumbnails() {
        // TODO Auto-generated method stub
        return thumbnails;
    }
}
