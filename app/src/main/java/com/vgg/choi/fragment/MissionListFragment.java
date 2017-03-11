package com.vgg.choi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.google.gson.Gson;
import com.vgg.choi.activity.MissionDetailActivity;
import com.vgg.choi.adapter.MissionItemAdapter;
import com.vgg.choi.data.MissionItem;
import com.vgg.choi.data.host.CachedData;
import com.vgg.sdk.ActionCallback;

/**
 * Created by LeHai on 3/9/2017.
 */

public class MissionListFragment extends RecyclerFragment {
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(layoutManager);
        CachedData.INSTANCE.getEvenItems(new ActionCallback<MissionItem[]>() {
            @Override
            public void onAction(MissionItem[] action) {
                setAdapter(new MissionItemAdapter(action));
            }
        }, false);
        setOnItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                MissionItem item = ((MissionItemAdapter)mAdapter).getItem(position);
                Intent intent = new Intent(getContext(), MissionDetailActivity.class);
                intent.putExtra("DATA", new Gson().toJson(item));
                getActivity().startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        CachedData.INSTANCE.getEvenItems(new ActionCallback<MissionItem[]>() {
            @Override
            public void onAction(MissionItem[] action) {
                setAdapter(new MissionItemAdapter(action));
                setRefreshing(false);
            }
        }, true);
    }
}
