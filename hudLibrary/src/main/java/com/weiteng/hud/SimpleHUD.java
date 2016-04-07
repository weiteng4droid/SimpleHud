package com.weiteng.hud;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;

import com.weiteng.hud.interf.OnHudFinishListener;

import java.lang.ref.WeakReference;

/**
 * Created by weiTeng on 2016/3/17.
 */
public class SimpleHUD {

    private static final int MESSAGE_DISMISS_DIALOG = 10;

    private static SimpleHUDDialog sDialog;
    private static Context sContext;
    private static HudHandler sHandler;

    public static void showLoadingMessage(Context context, String msg) {
        showLoadingMessage(context, msg, true);
    }

    public static void showLoadingMessage(Context context, String msg, boolean cancelable) {
        dismiss();
        setDialog(context, msg, R.drawable.kprogresshud_spinner, cancelable);

        if (sDialog != null) {
            sDialog.show();
        }
    }

    public static void showLoadingMessage(Context context, String msg, OnCancelListener cancelListener) {
        dismiss();
        setDialog(context, msg, R.drawable.kprogresshud_spinner, true, cancelListener);

        if (sDialog != null) {
            sDialog.show();
        }
    }

    public static void showErrorMessage(Context context, String msg) {
        dismiss();
        setDialog(context, msg, R.drawable.simplehud_error, true);

        if (sDialog != null) {
            sDialog.show();
            dismissAfterTime(2000);
        }
    }

    public static void showSuccessMessage(Context context, String msg, OnHudFinishListener listener) {
        showSuccessMessage(context, msg);
        sHandler.mFinishListener = listener;
    }

    public static void showSuccessMessage(Context context, String msg) {
        dismiss();
        setDialog(context, msg, R.drawable.simplehud_success, true);

        if (sDialog != null) {
            sDialog.show();
            dismissAfterTime(1000);
        }
    }

    public static void showInfoMessage(Context context, String msg) {
        dismiss();
        setDialog(context, msg, R.drawable.simplehud_info, true);

        if (sDialog != null) {
            sDialog.show();
            dismissAfterTime(2000);
        }
    }

    private static void setDialog(Context ctx, String msg, int resId, boolean cancelable) {
        sContext = ctx;

        if (!isContextValid())
            return;

        sDialog = SimpleHUDDialog.createDialog(ctx);
        sDialog.setMessage(msg);
        sDialog.setImage(resId);
        sDialog.setCanceledOnTouchOutside(false);
        sDialog.setCancelable(cancelable);

        sHandler = new HudHandler(sDialog);
    }

    private static void setDialog(Context ctx, String msg, int resId, boolean cancelable, OnCancelListener cancelListener) {
        sContext = ctx;

        if (!isContextValid())
            return;

        sDialog = SimpleHUDDialog.createDialog(ctx, cancelListener);
        sDialog.setMessage(msg);
        sDialog.setImage(resId);
        sDialog.setCanceledOnTouchOutside(false);
        sDialog.setCancelable(cancelable);

        sHandler = new HudHandler(sDialog);
    }

    public static void dismiss() {
        if (isContextValid() && sDialog != null && sDialog.isShowing()) {
            sDialog.dismiss();
            sDialog = null;
        }
    }

    private static void dismissAfterTime(final long time) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                dismissAtTime(time);
            }
        }).start();
    }

    private static void dismissAtTime(long time) {
        try {
            Thread.sleep(time);
            sHandler.sendEmptyMessage(MESSAGE_DISMISS_DIALOG);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static boolean isContextValid() {
        if (sContext == null) {
            return false;
        }

        if (sContext instanceof Activity) {
            Activity act = (Activity) sContext;
            if (act.isFinishing()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isHUDShowing() {
        return sDialog != null && sDialog.isShowing();
    }

    static class HudHandler extends Handler {
        WeakReference<SimpleHUDDialog> mDialog;
        OnHudFinishListener mFinishListener;

        HudHandler(SimpleHUDDialog dialog) {
            this.mDialog = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_DISMISS_DIALOG) {
                if (mDialog.get() != null && mDialog.get().isShowing()) {
                    mDialog.get().dismiss();

                    if (mFinishListener != null) {
                        mFinishListener.onHudFinish();
                        mFinishListener = null;
                    }
                }
            }
        }
    }
}
