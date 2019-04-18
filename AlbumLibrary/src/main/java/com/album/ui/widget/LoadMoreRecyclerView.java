package com.album.ui.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * by y on 22/08/2017.
 */

public class LoadMoreRecyclerView extends RecyclerView {


    private int lastVisibleItemPosition;
    private LoadMoreListener loadingListener;

    public LoadMoreRecyclerView(Context context) {
        super(context);
    }


    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setLoadingListener(LoadMoreListener loadingListener) {
        this.loadingListener = loadingListener;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        GridLayoutManager layoutManager = (GridLayoutManager) getLayoutManager();
        lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        LayoutManager layoutManager = getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if (loadingListener != null && visibleItemCount > 0 &&
                state == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == totalItemCount - 1) {
            loadingListener.onLoadMore();
        }
    }


    public interface LoadMoreListener {
        void onLoadMore();
    }
}
