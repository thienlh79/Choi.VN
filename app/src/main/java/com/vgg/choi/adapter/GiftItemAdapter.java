package com.vgg.choi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vgg.choi.R;
import com.vgg.choi.data.GiftItem;

/**
 * Created by LeHai on 3/8/2017.
 */

public class GiftItemAdapter extends RecyclerView.Adapter<GiftItemAdapter.ViewHolder>{
    GiftItem[] items;
    public GiftItemAdapter(GiftItem[] items) {
        this.items = items;
    }

    @Override
    public GiftItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.gift_item_as_list, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GiftItemAdapter.ViewHolder holder, int position) {
        items[position].setView(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return items == null? 0 : items.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
