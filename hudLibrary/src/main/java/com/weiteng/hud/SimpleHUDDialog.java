package com.weiteng.hud;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weiteng.hud.interf.OnHudFinishListener;
import com.weiteng.hud.widget.SpinView;

import java.lang.ref.WeakReference;

/**
 * Created by weiTeng on 2016/3/17.
 */
public class SimpleHUDDialog extends Dialog {

    private static final int MESSAGE_DISMISS_DIALOG = 0xfffe;

    private Context mContext;

    private int millisecond;
    private boolean cancel;
    private String message;
    private int imageResId;

    private HudHandler mHudHandler;

    private SimpleHUDDialog(Builder builder) {
        super(builder.context, R.style.SimpleHUD);
        this.mContext = builder.context;
        this.message = builder.message;
        this.imageResId = builder.imageResId;
        this.cancel = builder.cancel;
        this.millisecond = builder.millisecond;

        initWindow();
        setOnCancelListener(builder.mCancelListener);
        mHudHandler = new HudHandler(this, builder.mFinishListener);
    }

    private void initWindow() {
        setContentView(R.layout.simplehud);
        getWindow().getAttributes().gravity = Gravity.CENTER;

        setCanceledOnTouchOutside(false);
        setCancelable(cancel);
    }

    public static class Builder {

        private Context context;
        private int millisecond;
        private boolean cancel;
        private String message;
        private int imageResId;

        private OnHudFinishListener mFinishListener;
        private OnCancelListener mCancelListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setImage(int imageResId) {
            this.imageResId = imageResId;
            return this;
        }

        public Builder dismissMillisecond(int millisecond) {
            this.millisecond = millisecond;
            return this;
        }

        public Builder setCancel(boolean cancel) {
            this.cancel = cancel;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener cancelListener) {
            this.mCancelListener = cancelListener;
            return this;
        }

        public Builder setHudFinishListener(OnHudFinishListener hudFinishListener) {
            this.mFinishListener = hudFinishListener;
            return this;
        }

        public SimpleHUDDialog create() {
            return new SimpleHUDDialog(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView messageLabel = (TextView) findViewById(R.id.simplehud_message);
        SpinView spinView = (SpinView) findViewById(R.id.simplehud_spin_view);

        messageLabel.setText(message);
        ViewGroup.LayoutParams params = spinView.getLayoutParams();
        if (imageResId == R.drawable.kprogresshud_spinner) {
            spinView.setState(SpinView.STATE_SPIN);
            int size = getContext().getResources().getDimensionPixelSize(R.dimen.h_dp_38);
            params.height = size;
            params.width = size;
        } else {
            spinView.setState(SpinView.STATE_ICON);
            int size = getContext().getResources().getDimensionPixelSize(R.dimen.h_dp_32);
            params.height = size;
            params.width = size;
        }
        spinView.setImageResource(imageResId);
        dismissAfterTime();
    }

    @Override
    public void show() {
        if (mContext instanceof Activity) {
            if (!((Activity) mContext).isFinishing()) super.show();
        }
    }

    private void dismissAfterTime() {
        if (millisecond > 0) {
            mHudHandler.sendEmptyMessageDelayed(MESSAGE_DISMISS_DIALOG, millisecond);
        }
    }

    private static class HudHandler extends Handler {

        private WeakReference<SimpleHUDDialog> mDialog;
        private OnHudFinishListener mOnHudFinishListener;

        HudHandler(SimpleHUDDialog dialog, OnHudFinishListener onHudFinishListener) {
            this.mDialog = new WeakReference<>(dialog);
            this.mOnHudFinishListener = onHudFinishListener;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_DISMISS_DIALOG) {

                if (mDialog.get() != null && mDialog.get().isShowing()) {
                    mDialog.get().dismiss();

                    if (mOnHudFinishListener != null) {
                        mOnHudFinishListener.onHudFinish();
                    }
                }
            }
        }
    }
}
