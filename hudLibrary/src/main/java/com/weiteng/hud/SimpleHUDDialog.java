package com.weiteng.hud;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weiteng.hud.widget.SpinView;

/**
 * Created by weiTeng on 2016/3/17.
 */
public class SimpleHUDDialog extends Dialog {

    public SimpleHUDDialog(Context context, int theme) {
        super(context, theme);
    }

    public SimpleHUDDialog(Context context, int theme, OnCancelListener cancelListener) {
        super(context, theme);
        this.setOnCancelListener(cancelListener);
    }

    public static SimpleHUDDialog createDialog(Context context) {
        SimpleHUDDialog dialog = new SimpleHUDDialog(context, R.style.SimpleHUD);
        dialog.setContentView(R.layout.simplehud);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return dialog;
    }

    public static SimpleHUDDialog createDialog(Context context, OnCancelListener cancelListener) {
        SimpleHUDDialog dialog = new SimpleHUDDialog(context, R.style.SimpleHUD, cancelListener);
        dialog.setContentView(R.layout.simplehud);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return dialog;
    }

    public void setMessage(String message) {
        TextView msgView = (TextView) findViewById(R.id.simplehud_message);
        msgView.setText(message);
    }

    public void setImage(int resId) {
        SpinView spinView = (SpinView) findViewById(R.id.simplehud_spin_view);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) spinView.getLayoutParams();
        // show loading SpinView
        if (resId == R.drawable.kprogresshud_spinner) {
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
        spinView.setImageResource(resId);
    }
}
