package com.vgg.choi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kazan.util.TypefaceUtil;
import com.vgg.choi.R;
import com.vgg.choi.data.MissionItem;

/**
 * Created by LeHai on 3/8/2017.
 */

public class MissionItemAdapter extends RecyclerView.Adapter<MissionItemAdapter.ViewHolder> {
    MissionItem[] items;
    public MissionItemAdapter(MissionItem[] items) {
        this.items = items;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view  = LayoutInflater.from(context).inflate(R.layout.mission_item_as_list, null);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        ImageView thumbsView = (ImageView) view.findViewById(R.id.game_item_image);
        int percent = metrics.widthPixels /100;
        thumbsView.getLayoutParams().width = 23* percent;
        thumbsView.getLayoutParams().height = 23 * percent;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MissionItem item = items[position];
        View view = holder.itemView;
        Context context = view.getContext();
        TypefaceUtil.setText(view.findViewById(R.id.game_item_title), item.getItemTitle());
        TypefaceUtil.setText(view.findViewById(R.id.game_item_desc), item.getItemDescription());
        String pointStr = item.getPoint() + " " + context.getString(R.string.game_coin);
        TypefaceUtil.setTextBold(view.findViewById(R.id.game_item_download_text), pointStr);

        Glide
                .with(context)
                .load(item.getItemThumbnails())
                .into((ImageView)view.findViewById(R.id.game_item_image));
    }

    @Override
    public int getItemCount() {
        return items == null? 0 : items.length;
    }

    public MissionItem getItem(int position) {
        return items[position];
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
