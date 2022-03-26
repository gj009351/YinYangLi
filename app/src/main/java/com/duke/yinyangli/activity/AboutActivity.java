package com.duke.yinyangli.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.duke.yinyangli.R;
import com.duke.yinyangli.base.BaseActivity;
import com.duke.yinyangli.dialog.DialogUtils;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    public static void start(Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        super.initView();
        title.setText(R.string.about);
        View noduty = findViewById(R.id.noduty);
        noduty.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.noduty) {
            DialogUtils.showAboutDialog(this);
        } else {
            super.onClick(v);
        }
    }
}
