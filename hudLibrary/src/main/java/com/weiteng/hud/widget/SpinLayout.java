package com.weiteng.hud.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by weiTeng on 16/4/1.
 */
public class SpinLayout extends ViewGroup {

    private static final String TAG = "SpinLayout";

    public SpinLayout(Context context) {
        super(context);
    }

    public SpinLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpinLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int usedHeight = 0;
        int usedWidth = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            LayoutParams params = child.getLayoutParams();
            if (params == null) {
                throw new IllegalArgumentException("no layout params");
            }

            int childWidthMeasureSpec;
            int childHeightMeasureSpec;
            if (params.width > 0) {
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY);
            } else {
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(9999, MeasureSpec.AT_MOST);
            }

            if (params.height > 0) {
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
            } else {
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(9999, MeasureSpec.AT_MOST);
            }
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            usedHeight += child.getMeasuredHeight();
            if (usedWidth < child.getMeasuredWidth()) {
                usedWidth = child.getMeasuredWidth();
            }

            Log.d(TAG, "child.getMeasuredWidth() = " + child.getMeasuredWidth() + ", child.getMeasuredHeight() = " + child.getMeasuredHeight());
        }

        int width, height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = Math.min(widthSize, usedWidth);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = Math.min(heightSize, usedHeight);
        }
        int size = Math.max(width, height);
        width = size + getPaddingLeft() + getPaddingRight();
        height = size + getPaddingBottom() + getPaddingTop();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int width = getMeasuredWidth();
        int usedHeight = 0;
        Log.d(TAG, "l = " + l + ", t = " + t + ", r = " + r + ", b = " + b);
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            int left = l + getPaddingLeft() + (width - child.getMeasuredWidth()) / 2;
            int right = left + child.getMeasuredWidth();
            int top = t + usedHeight;
            child.layout(left, top, right, top + child.getMeasuredWidth());

            Log.d(TAG, "i = " + i + ", left = " + left + ", top = " + top + ", right = " + right + ", bottom = " + (top + child.getMeasuredWidth()));
            usedHeight += getMeasuredHeight();
        }
    }
}
