package com.vgg.choi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.vgg.choi.R;

/**
 * Created by LeHai on 3/8/2017.
 */

public class RecyclerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    protected int max_refreshing_time = 5000; //ms

    @Override
    public void onRefresh() {
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, max_refreshing_time);
    }

    public boolean isRefreshing() {
        return mRefreshLayout == null? false : mRefreshLayout.isRefreshing();
    }

    public void setRefreshing(boolean refreshing) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(refreshing);
        }
    }

    public static interface RecyclerItemClickListener {
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }

    public static class RecyclerItemClickListenerImpl implements RecyclerView.OnItemTouchListener {

        GestureDetector mGestureDetector;
        private RecyclerItemClickListener mListener;
        public RecyclerItemClickListenerImpl(Context context, final RecyclerView recyclerView, RecyclerItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && mListener != null) {
                        mListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onClick(childView, view.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}
    }

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerItemClickListener mClickListener;
    SwipeRefreshLayout mRefreshLayout;
    Handler mHandler = new Handler();
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            setRefreshing(false);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.itemsRecyclerView);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    public void setOnItemClickListener(RecyclerItemClickListener listener) {
        mClickListener = listener;
        if (mRecyclerView != null) {
            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListenerImpl(getContext(), mRecyclerView, listener));
        }
    }
}
