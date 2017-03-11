package com.vgg.choi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.vgg.choi.activity.GameDetailActivity;
import com.vgg.choi.adapter.GameItemAdapter;
import com.vgg.choi.data.GameItem;
import com.vgg.choi.data.host.CachedData;
import com.vgg.sdk.ActionCallback;

/**
 * Created by LeHai on 3/8/2017.
 */

public class GameListFragment extends RecyclerFragment {
    int numOfColumns = 2;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CachedData.INSTANCE.getGameItems(new ActionCallback<GameItem[]>() {
            @Override
            public void onAction(GameItem[] action) {
                mAdapter = new GameItemAdapter(action);
                setAdapter(mAdapter);
            }
        }, false);
        setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                GameItem item = ((GameItemAdapter)mAdapter).getItem(position);
                Intent intent = new Intent(getContext(), GameDetailActivity.class);
                intent.putExtra("DATA", new Gson().toJson(item));
                getActivity().startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), numOfColumns);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mAdapter.getItemViewType(position) == GameItemAdapter.HEADER_ITEM? numOfColumns : 1;
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        super.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        CachedData.INSTANCE.getGameItems(new ActionCallback<GameItem[]>() {
            @Override
            public void onAction(GameItem[] action) {
                mAdapter = new GameItemAdapter(action);
                setAdapter(mAdapter);
                setRefreshing(false);
            }
        }, true);
    }
}
