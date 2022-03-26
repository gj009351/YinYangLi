package com.duke.yinyangli.base;

import com.duke.yinyangli.dialog.DialogUtils;
import android.view.View;
import com.duke.yinyangli.R;
import com.haibin.calendarview.library.Article;

public abstract class BaseResultActivity extends BaseActivity {

    private View about;
    private View share;

    public Article mArticle;

    @Override
    public void initView() {
        super.initView();
        about = findViewById(R.id.about);
        about.setOnClickListener(this);
        about.setVisibility(View.VISIBLE);

        share = findViewById(R.id.share);
        share.setOnClickListener(this);
        share.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.about) {
            DialogUtils.showAbountGuaDialog(this, getAboutDialogTitle(), getAboutDialogContent());
        } else if (v.getId() == R.id.share) {
            startShare();
        } else {
            super.onClick(v);
        }
    }

    public String getAboutDialogTitle() {
        return mArticle.getTitle();
    }

    @Override
    public int getShareType() {
        return mArticle.getType();
    }

    public abstract String getAboutDialogContent();
}
