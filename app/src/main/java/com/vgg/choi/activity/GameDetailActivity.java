package com.vgg.choi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kazan.util.TypefaceUtil;
import com.vgg.choi.R;
import com.vgg.choi.data.GameItem;
import com.vgg.choi.data.host.CachedData;
import com.vgg.sdk.ActionCallback;
import com.vgg.sdk.ApiObject;
import com.vgg.sdk.SdkCodeAndMessage;

/**
 * Created by LeHai on 3/9/2017.
 */

public class GameDetailActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_game_detail);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        GameItem item = new Gson().fromJson(getIntent().getStringExtra("DATA"), GameItem.class);
        item.setView(findViewById(R.id.game_detail_layout));
        setTitle("");

        TypefaceUtil.setTextBold(findViewById(R.id.game_item_description_label), getString(R.string.game_desc));
        TypefaceUtil.setTextBold(findViewById(R.id.game_item_screenshot_label), getString(R.string.game_screenshot));
        CachedData.INSTANCE.getGameItemDetail(item.getId(), new ActionCallback<ApiObject>() {
            @Override
            public void onAction(ApiObject result) {
                if (result != null && result.getCode() == SdkCodeAndMessage.RESULT_SUCCESS) {
                    //GameItem[] items = result.getData(GameItem[].class);
                    GameItem item = result.getData(GameItem.class);
                    if (item != null && GameDetailActivity.this != null){ // && items[0] != null) {
                        updateData(item);
                    }
                }
            }
        });

    }
    void updateData(GameItem item) {

        item.setView(findViewById(R.id.game_detail_layout));

        TypefaceUtil.setTextBold(findViewById(R.id.game_item_description_label), getString(R.string.game_desc));
        TypefaceUtil.setTextBold(findViewById(R.id.game_item_screenshot_label), getString(R.string.game_screenshot));

        String[] urls = item.getMediaUrl();
        if (urls == null || urls.length == 0) {
            return;
        }
        ViewPager pager = (ViewPager) findViewById(R.id.game_item_image_pager);
        ImageDetailAdapter adapter = new ImageDetailAdapter(getSupportFragmentManager(), urls);
        pager.setAdapter(adapter);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
