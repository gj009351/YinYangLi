package com.duke.yinyangli.activity;

import android.content.Context;
import android.content.Intent;

import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.ChooseAdapter;
import com.duke.yinyangli.base.BaseActivity;
import com.duke.yinyangli.utils.LogUtils;
import com.haibin.calendarview.library.Article;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class ChooseActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private int mFailedCount = 0;
    private Article mChooseArticle;
    private ChooseAdapter mChooseAdapter;

    public static void start(Context context) {
        context.startActivity(new Intent(context, ChooseActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose;
    }

    @Override
    public void initView() {
        super.initView();
        title.setText(R.string.select);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ChooseAdapter(this));
        recyclerView.setAdapter(mChooseAdapter = new ChooseAdapter(this));
    }

    @Override
    public void initData() {
        super.initData();
        LogUtils.d("chooseactivity:initData");
    }

}
