package com.vgg.choi.adapter.base;

import android.support.v7.widget.RecyclerView;

/**
 * Created by LeHai on 3/9/2017.
 */

public abstract class ChoiAdapter extends RecyclerView.Adapter {
    abstract public Object getItem(int position);
}
