package com.weiteng.hud;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;

import com.weiteng.hud.interf.OnHudFinishListener;

/**
 * Created by weiTeng on 2016/3/17.
 */
public class SimpleHUD {

    private static SimpleHUDDialog sSimpleHUD;

    public static void showInfoMessage(Context context, String msg) {
        new SimpleHUDDialog.Builder(context)
                .setMessage(msg)
                .setCancel(true)
                .setImage(R.drawable.simplehud_info)
                .dismissMillisecond(2000)
                .create()
                .show();
    }

    public static void showLoadingMessage(Context context, String msg) {
        showLoadingMessage(context, msg, true);
    }

    public static void showLoadingMessage(Context context, String msg, boolean cancel) {
        sSimpleHUD = new SimpleHUDDialog.Builder(context)
                .setMessage(msg)
                .setCancel(cancel)
                .setImage(R.drawable.kprogresshud_spinner)
                .create();


        sSimpleHUD.show();
    }

    public static void showLoadingMessage(Context context, String msg, OnCancelListener cancelListener) {
        sSimpleHUD = new SimpleHUDDialog.Builder(context)
                .setMessage(msg)
                .setCancel(true)
                .setImage(R.drawable.kprogresshud_spinner)
                .setOnCancelListener(cancelListener)
                .create();

        sSimpleHUD.show();
    }

    public static void showErrorMessage(Context context, String msg) {
        new SimpleHUDDialog.Builder(context)
                .setMessage(msg)
                .setCancel(true)
                .setImage(R.drawable.simplehud_error)
                .dismissMillisecond(2000)
                .create()
                .show();
    }

    public static void showSuccessMessage(Context context, String msg) {
        showSuccessMessage(context, msg, null);
    }

    public static void showSuccessMessage(Context context, String msg, OnHudFinishListener listener) {
        new SimpleHUDDialog.Builder(context)
                .setMessage(msg)
                .setCancel(true)
                .setImage(R.drawable.simplehud_success)
                .dismissMillisecond(2000)
                .setHudFinishListener(listener)
                .create()
                .show();
    }

    public static void dismiss() {
        if (sSimpleHUD != null) {
            sSimpleHUD.dismiss();

            sSimpleHUD = null;
        }
    }

    public static boolean isHUDShowing() {
        return sSimpleHUD != null && sSimpleHUD.isShowing();
    }
}
