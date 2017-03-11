package com.vgg.choi.data;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;
import com.kazan.util.AppsUtils;
import com.kazan.util.TypefaceUtil;
import com.vgg.choi.R;
import com.vgg.choi.data.host.CachedData;
import com.vgg.sdk.ActionCallback;
import com.vgg.sdk.ApiObject;
import com.vgg.sdk.SdkHelper;

/**
 * Created by LeHai on 1/17/2017.
 */

public class GiftItem implements ChoiItem {
    static class GiftCodeValue {
        @SerializedName("Value")
        String value;
    }
    @SerializedName("Id")
    String id;

    @SerializedName("Icon")
    String thumbnails;

    @SerializedName("Name")
    String title;

    @SerializedName("Description")
    String desc;

    @SerializedName("Total")
    int total;

    @SerializedName("UsedCount")
    int used;

    @SerializedName("Price")
    int price;

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
        return desc;
    }

    @Override
    public String getItemThumbnails() {
        // TODO Auto-generated method stub
        return thumbnails;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setView(final View view) {
        if (view == null) {
            //do nothing
            return;
        }
        Context context = view.getContext();
        String priceString = price > 0? price + " " + context.getString(R.string.game_coin) : context.getString(R.string.message_free);
        String remainString = context.getString(R.string.message_gift_remain) + ": " + (used) + "/" + total;
        TypefaceUtil.setText(AppsUtils.getView(view, R.id.gift_item_desc), desc);
        TypefaceUtil.setText(AppsUtils.getView(view, R.id.gift_item_price), priceString);
        TypefaceUtil.setText(AppsUtils.getView(view, R.id.gift_item_remain), remainString);
        TypefaceUtil.setText(AppsUtils.getView(view, R.id.gift_item_title), title);
        TypefaceUtil.setText(AppsUtils.getView(view, R.id.gift_item_buy), total - used > 0? R.string.message_get_giftcode : R.string.message_gift_end);
        if (total - used > 0) {
            AppsUtils.getView(view, R.id.gift_item_buy).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getGiftCode(view);
                }
            });
        } else {
            TextView tv = AppsUtils.getView(view, R.id.gift_item_buy);
            tv.setBackgroundColor(0);
            tv.setTextColor(Color.parseColor("#90C840"));
        }
        ImageView imageView = AppsUtils.getView(view, R.id.gift_item_icon);
        if (imageView != null) Glide
                .with(context)
                .load(thumbnails)
                .into(imageView);
    }
    void getGiftCode(final View view) {
        final Context context = view.getContext();
        SdkHelper helper = new SdkHelper();
        if (!helper.isLogin()) {
            Toast.makeText(context, R.string.sdk_login_require, Toast.LENGTH_SHORT).show();
            return;
        }
        CachedData.INSTANCE.getGift(getId(), helper.getAccessToken(), new ActionCallback<ApiObject>() {
            @Override
            public void onAction(ApiObject action) {

            }
        });
/*
        SdkHttpHelper.requestApi(url, body, new SdkResultCallback<SdkApiObject>() {

            @Override
            public void onResult(SdkApiObject result) {
                if (result != null && result.getCode() == VggSdkData.RESULT_SUCCESS) {
                    used++;
                    String remainString = context.getString(R.string.message_gift_remain) + ": " + (used) + "/" + total;
                    TypefaceUtil.setText(AppsUtils.getView(view, R.id.gift_item_remain), remainString);

                    final String value = result.getData(GiftCodeValue.class).value;
                    VggDialog dialog = new VggDialog(context);
                    dialog.setTitle(result.getMessage());
                    dialog.setMessage(value);
                    dialog.setSmallDialog(true);
                    dialog.setButton(SdkBaseDialog.BUTTON_NEGATIVE, "Copy", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("giftcode", value);
                            clipboard.setPrimaryClip(clip);
                        }
                    });
                    dialog.setButton(SdkBaseDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    });
                    dialog.show();
                } else {
                    VggDialog dialog = new VggDialog(context);
                    dialog.setTitle(context.getString(R.string.message_error));
                    if (result != null) {
                        dialog.setMessage(context.getString(R.string.message_error) +": " + result.getMessage());
                    } else {
                        dialog.setMessage(context.getString(R.string.message_unknow));
                    }
                    dialog.setSmallDialog(true);
                    dialog.setButton(SdkBaseDialog.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    });
                    dialog.show();
                }
            }
        });
*/
    }
}
