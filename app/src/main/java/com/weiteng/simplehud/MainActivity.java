package com.weiteng.simplehud;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.weiteng.hud.SimpleHUD;
import com.weiteng.hud.interf.OnHudFinishListener;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnHudFinishListener {

    private static final int MESSAGE_DELAY = 3000;
    private static final int DISMISS_LOADING = 1;

    private static class SimpleHandler extends Handler {
        WeakReference<MainActivity> mWeakReference;

        SimpleHandler(MainActivity activity){
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DISMISS_LOADING) {
                mWeakReference.get().dismiss();
                SimpleHUD.showSuccessMessage(mWeakReference.get(), "load finish", mWeakReference.get());
            }
        }
    }

    private SimpleHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new SimpleHandler(this);

        findViewById(R.id.show_loading).setOnClickListener(this);
        findViewById(R.id.show_success).setOnClickListener(this);
        findViewById(R.id.show_info).setOnClickListener(this);
        findViewById(R.id.show_error).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_loading:
                SimpleHUD.showLoadingMessage(this, "loading");
                mHandler.sendEmptyMessageDelayed(DISMISS_LOADING, MESSAGE_DELAY);
                break;

            case R.id.show_success:
                SimpleHUD.showSuccessMessage(this, "上传成功");
                break;

            case R.id.show_info:
                SimpleHUD.showInfoMessage(this, "删除成功");
                break;

            case R.id.show_error:
                SimpleHUD.showErrorMessage(this, "这个数据异常，具体原因是什么我也不知道，哈哈");
                break;
        }
    }

    @Override
    public void onHudFinish() {
        Toast.makeText(MainActivity.this, "dismiss", Toast.LENGTH_SHORT).show();
    }

    public void dismiss() {
        SimpleHUD.dismiss();
    }
}
