package com.duke.yinyangli.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.base.BaseActivity;
import com.duke.yinyangli.base.BaseResultActivity;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.utils.StringUtils;
import com.haibin.calendarview.library.Article;

import butterknife.BindView;

public class ZhouGongJieMengResultActivity extends BaseResultActivity {

    @BindView(R.id.tips_title)
    TextView mTipsTitle;

    @BindView(R.id.content)
    TextView mContent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_zhougongjiemeng_result;
    }

    public static void start(Context context, Article article) {
        context.startActivity(new Intent(context, ZhouGongJieMengResultActivity.class)
                .putExtra(Constants.INTENT_KEY.KEY_MODEL, article));
    }

    @Override
    public boolean requestButterKnife() {
        return true;
    }

    @Override
    public void initView() {
        super.initView();
        mArticle = (Article) getIntent().getSerializableExtra(Constants.INTENT_KEY.KEY_MODEL);
        title.setText("周公解梦");
        mTipsTitle.setText("梦的类型：" + mArticle.getTitle());
        mContent.setText("解梦：" + StringUtils.getString(mArticle.getContent()).replace("·", ""));
    }

    @Override
    public String getAboutDialogContent() {
        return getString(R.string.tip_zhougongjiemeng);
    }
}
