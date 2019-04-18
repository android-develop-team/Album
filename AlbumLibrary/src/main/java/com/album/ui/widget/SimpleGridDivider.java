package com.album.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * by y on 21/08/2017.
 */

public class SimpleGridDivider extends RecyclerView.ItemDecoration {

    private final int divider;

    public SimpleGridDivider(int divider) {
        this.divider = divider;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = layoutManager.getSpanCount();
        int i = position % spanCount;
        boolean first = (position / spanCount + 1) == 1;
        int top = first ? divider / 2 : 0;
        int left = i + 1 == 1 ? divider : divider / 2;
        int right = i + 1 == spanCount ? divider : divider / 2;
        outRect.set(left, top, right, divider);

    }
}