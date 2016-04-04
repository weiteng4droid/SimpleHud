package com.weiteng.hud.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by weiTeng on 16/4/1.
 */
public class SpinLayout extends ViewGroup {

    private static final String TAG = "SpinLayout";

    private int mDisplayWidth;

    public SpinLayout(Context context) {
        this(context, null);
    }

    public SpinLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpinLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mDisplayWidth = metrics.widthPixels;
        Log.d(TAG, "mDisplayWidth = " + mDisplayWidth);
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
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            usedHeight += child.getMeasuredHeight();
            if (i != 0) {
                usedHeight += getPaddingTop();
            }
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
        if (size > mDisplayWidth / 2) {
            size = mDisplayWidth /2;
        }
        width = size + getPaddingLeft() + getPaddingRight();
        height = size + getPaddingBottom() + getPaddingTop();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int width = getMeasuredWidth();
        int usedHeight = 0;
        int  totalHeight = 0;
        for (int i = 0; i < count; i++) {
            if (i != 0) {
                totalHeight += getChildAt(i).getMeasuredHeight() + getPaddingTop();
            } else {
                totalHeight += getChildAt(i).getMeasuredHeight();
            }
        }
        int space = (getMeasuredHeight() - totalHeight - getPaddingTop() - getPaddingBottom()) / 2;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            int left = getPaddingLeft() + (width - getPaddingLeft() - getPaddingRight() - child.getMeasuredWidth()) / 2;
            int right = left + child.getMeasuredWidth();
            int top = 0;
            if (i != 0) {
                top += usedHeight + getPaddingTop() + space + getPaddingTop();
            } else {
                top += getPaddingTop() + space;
            }
            child.layout(left, top, right, top + child.getMeasuredHeight());
            usedHeight += child.getMeasuredHeight();
        }
    }
}
