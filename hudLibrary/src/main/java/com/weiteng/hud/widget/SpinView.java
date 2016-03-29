package com.weiteng.hud.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.weiteng.hud.R;
import com.weiteng.hud.interf.Indeterminate;


/**
 * Created by weiTeng on 2016/1/25.
 */
public class SpinView extends ImageView implements Indeterminate {

    public static final int STATE_SPIN = 1;
    public static final int STATE_ICON = 2;

    private float mRotateDegrees;
    private int mFrameTime;
    private boolean mNeedToUpdateView;
    private Runnable mUpdateViewRunnable;

    private int mState = STATE_SPIN;

    public SpinView(Context context) {
        super(context);
        init();
    }

    public SpinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        if (mState == STATE_SPIN) {
            setImageResource(R.drawable.kprogresshud_spinner);
            mFrameTime = 1000 / 12;
            mUpdateViewRunnable = new Runnable() {
                @Override
                public void run() {
                    mRotateDegrees += 30;
                    mRotateDegrees = mRotateDegrees < 360 ? mRotateDegrees : mRotateDegrees - 360;
                    invalidate();
                    if (mNeedToUpdateView) {
                        postDelayed(this, mFrameTime);
                    }
                }
            };
        }
    }

    @Override
    public void setAnimationSpeed(float scale) {
        mFrameTime = (int) (1000 / 12 / scale);
    }

    public void setState(int state) {
        mState = state;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mState == STATE_SPIN) {
            canvas.rotate(mRotateDegrees, getWidth() / 2, getHeight() / 2);
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mState == STATE_SPIN) {
            mNeedToUpdateView = true;
            post(mUpdateViewRunnable);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mState == STATE_SPIN) {
            mNeedToUpdateView = false;
        }
        super.onDetachedFromWindow();
    }
}

