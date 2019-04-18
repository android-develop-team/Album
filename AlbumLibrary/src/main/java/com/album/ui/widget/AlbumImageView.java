package com.album.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * by y on 11/08/2017.
 */

public class AlbumImageView extends AppCompatImageView {
    public AlbumImageView(Context context) {
        super(context);
    }

    public AlbumImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlbumImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
