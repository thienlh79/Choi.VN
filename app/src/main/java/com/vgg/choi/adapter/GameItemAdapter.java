package com.vgg.choi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vgg.choi.R;
import com.vgg.choi.data.GameItem;

/**
 * Created by LeHai on 3/8/2017.
 */

public class GameItemAdapter extends RecyclerView.Adapter<GameItemAdapter.ViewHolder> {
    public static final int HEADER_ITEM = 1;
    public static final int NORMAL_ITEM = 0;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if (viewType == HEADER_ITEM) {
            view = inflater.inflate(R.layout.game_item_header, null);
            ImageView image = (ImageView) view.findViewById(R.id.game_item_image);
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            image.getLayoutParams().height = metrics.widthPixels / 10 * 6;
            image.getLayoutParams().width = metrics.widthPixels;

        } else {
            view = inflater.inflate(R.layout.game_item_dark_as_grid, null);
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            ImageView thumbsView = (ImageView) view.findViewById(R.id.game_item_image);
            int percent = metrics.widthPixels /100;
            int height = 64 * percent;//(metrics.widthPixels - (getNumColumns() + 1) * AppsUtils.dp2px(getContext(), 4)) / 3 * 2;//getNumColumns();
            //height = height / 2;
            thumbsView.getLayoutParams().height = height;
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        items[position].setView(holder.itemView);
    }

    @Override
    public int getItemCount() {
        int count = items == null? 0 : items.length;
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0? HEADER_ITEM : NORMAL_ITEM;
    }

    public GameItem getItem(int position) {
        return items[position];
    }
    public GameItemAdapter(GameItem[] items) {
        this.items = items;
    }
    GameItem[] items;
}

