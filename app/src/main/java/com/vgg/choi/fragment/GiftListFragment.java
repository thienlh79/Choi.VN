package com.vgg.choi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.vgg.choi.adapter.GiftItemAdapter;
import com.vgg.choi.data.GiftItem;
import com.vgg.choi.data.host.CachedData;
import com.vgg.sdk.ActionCallback;

/**
 * Created by LeHai on 3/9/2017.
 */

public class GiftListFragment extends RecyclerFragment {
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(layoutManager);
        CachedData.INSTANCE.getGiftItems(new ActionCallback<GiftItem[]>() {
            @Override
            public void onAction(GiftItem[] action) {
                setAdapter(new GiftItemAdapter(action));
            }
        }, false);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        CachedData.INSTANCE.getGiftItems(new ActionCallback<GiftItem[]>() {
            @Override
            public void onAction(GiftItem[] action) {
                setAdapter(new GiftItemAdapter(action));
                setRefreshing(false);
            }
        }, true);
    }
}
