package com.duke.yinyangli.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.duke.yinyangli.R;
import com.duke.yinyangli.view.MyProgressBar;

public class LoadingDialog extends Dialog {

    private Context mContext;
    private MyProgressBar myProgressBar;
    private MyProgressBar myProgressBackground;

    public LoadingDialog(Context context) {
        super(context);
        mContext = context;
        init(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.comm_progress_layout);
        myProgressBar = (MyProgressBar) findViewById(R.id.progress);
        myProgressBackground = (MyProgressBar) findViewById(R.id.progress_background);
    }

    @Override
    public void show() {
        if (!((Activity) mContext).isFinishing()) {
            myProgressBar.show();
            myProgressBackground.show();
            super.show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        myProgressBar.stop();
        myProgressBackground.stop();
    }

    @Override
    public void dismiss() {
        myProgressBar.stop();
        myProgressBackground.stop();
        super.dismiss();
    }
}