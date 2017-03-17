package com.vgg.choi.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.internal.LikeContent;
import com.facebook.share.internal.LikeDialog;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.kazan.util.AppsUtils;
import com.kazan.util.TypefaceUtil;
import com.vgg.choi.R;
import com.vgg.choi.common.CustomLikeView;
import com.vgg.choi.data.MissionItem;
import com.vgg.choi.data.host.CachedData;
import com.vgg.sdk.ActionCallback;
import com.vgg.sdk.SdkHelper;

import java.util.Arrays;

public class MissionDetailActivity extends AppCompatActivity {

    private CallbackManager fbCallbackManager;
    private ShareDialog fbShareDialog;

    private MissionItem mItem;
    private SdkHelper mSdkHelper = new SdkHelper();
    private static class ImageDetailAdapter extends FragmentPagerAdapter {
        String[] imageUrls;
        public ImageDetailAdapter(FragmentManager fm, String[] urls) {
            super(fm);
            imageUrls = urls;
        }

        @Override
        public Fragment getItem(int position) {
            return ImageFragment.newInstance(imageUrls[position]);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imageUrls.length;
        }

    }
    public static class ImageFragment extends Fragment {
        String url;
        static ImageFragment newInstance(String url) {
            ImageFragment f = new ImageFragment();
            f.url = url;
            return f;
        }
        @Override
        @Nullable
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            ImageView image = new ImageView(getContext());
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide
                    .with(getContext())
                    .load(url)
                    .into(image);

            return image;
            //return super.onCreateView(inflater, container, savedInstanceState);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fbCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_game_detail);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mItem = new Gson().fromJson(getIntent().getStringExtra("DATA"), MissionItem.class);
        mItem.setView(findViewById(R.id.game_detail_layout));
        setTitle("");
        showToast("Mission Secsion");

        TypefaceUtil.setTextBold(findViewById(R.id.game_item_description_label), getString(R.string.game_desc));
        TypefaceUtil.setTextBold(findViewById(R.id.game_item_screenshot_label), getString(R.string.game_screenshot));
        CachedData.INSTANCE.getMissionDetail(mItem.getId(), new ActionCallback<MissionItem>() {
            @Override
            public void onAction(MissionItem action) {
                if (MissionDetailActivity.this != null) {
                    updateData(action);
                }
            }
        });
    }
    void updateData(MissionItem item) {
        mItem = item;
        item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mSdkHelper.isLogin()) {
                    loginToFb();
                    return;
                } else {
                    showToast("Đăng nhập để có thể nhận nhiệm vụ");
                    startActivity(new Intent(MissionDetailActivity.this, LoginActivity.class));
                }
			}
        });
        item.setView(findViewById(R.id.game_detail_layout));

        TypefaceUtil.setTextBold(findViewById(R.id.game_item_description_label), getString(R.string.game_desc));
        TypefaceUtil.setTextBold(findViewById(R.id.game_item_screenshot_label), getString(R.string.game_screenshot));

        String[] urls = item.getMediaUrl();
        if (urls == null) {

            return;
        }
        ViewPager pager = (ViewPager) findViewById(R.id.game_item_image_pager);
        ImageDetailAdapter adapter = new ImageDetailAdapter(getSupportFragmentManager(), urls);
        pager.setAdapter(adapter);

    }
    void loginToFb() {
        Log.d("Hash", AppsUtils.getHashKey(this));
        if (AccessToken.getCurrentAccessToken() != null) {
            startMission();
        } else {
            showToast("Xac nhan tai khoan facebook de lam nhiem vu");

            LoginManager.getInstance().registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    startMission();
                }

                @Override
                public void onCancel() {
                    showToast("Cần phải đăng nhập Facebook để nhận nhiệm vụ");
                }

                @Override
                public void onError(FacebookException error) {
                    showToast("Vui lòng đăng nhập lại");
                }
            });
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        }
    }
    void startMission() {
        switch (mItem.getType()) {
            case 1:// Install
                missionInstallStart();
                break;
            case 3://Like
                missionLikeStart();
                break;
            case 4://Share
                missionShareStart();
                break;
            case 6://Invite
                missionInviteStart();
                break;
            default:
                missionInviteStart();
                break;
        }
        //CustomLikeView likeView = new CustomLikeView();
        //likeView.init("http://www.vgg.vn/", LikeView.ObjectType.OPEN_GRAPH);
        //likeView.toggleLike(MissionDetailActivity.this);


    }
    void missionLikeStart() {
        LikeContent likeContent = new LikeContent.Builder()
                .setObjectId(mItem.getShareLink())
                .build();
        LikeDialog likeDialog = new LikeDialog(this);
        likeDialog.registerCallback(fbCallbackManager, new FacebookCallback<LikeDialog.Result>() {
            @Override
            public void onSuccess(LikeDialog.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        likeDialog.show(likeContent);
    }
    void missionInstallStart() {

    }
    void missionInviteStart() {
        AppInviteContent inviteContent = new AppInviteContent.Builder()
                .setApplinkUrl(mItem.getShareLink())
                .setPreviewImageUrl(mItem.getItemThumbnails())
                .build();
        AppInviteDialog inviteDialog = new AppInviteDialog(this);
        inviteDialog.registerCallback(fbCallbackManager, new FacebookCallback<AppInviteDialog.Result>() {
            @Override
            public void onSuccess(AppInviteDialog.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        inviteDialog.show(inviteContent);
    }
    void missionShareStart() {
        showToast("Misstion Start");
        if (!TextUtils.isEmpty(mItem.getShareLink())) {
            ShareLinkContent share = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(mItem.getShareLink()))
                    .build();
            fbShareDialog = new ShareDialog(this);
            fbShareDialog.registerCallback(fbCallbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });
            fbShareDialog.show(share, ShareDialog.Mode.AUTOMATIC);
        }
    }
    void missionCompleted() {
        //Toast.makeText(this, "Mission Completed!", Toast.LENGTH_LONG).show();
        /*String url = HostData.getMissionConfirmUrl() + mSdkHelper.getAccessToken();
        FormBody body = new FormBody.Builder()
                .add("offer_id", mItem.getId())
                .build();
        SdkHttpHelper.requestApiObject(url, body, new SdkResultCallback<SdkApiObject>() {

            @Override
            public void onResult(SdkApiObject result) {
                if (result != null) {
                    if (result.getCode() == VggSdkData.RESULT_SUCCESS) {
                        showToast("Mission Completed!");

                    } else {
                        showToast(result.getMessage());
                    }
                }
            }
        });*/
        //mItem.downloadAndInstall(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fbCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    Toast mToast;
    void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
