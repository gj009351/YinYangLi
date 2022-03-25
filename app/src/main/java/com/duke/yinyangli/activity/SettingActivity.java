package com.duke.yinyangli.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.SettingAdapter;
import com.duke.yinyangli.base.BaseActivity;
import com.duke.yinyangli.view.itemdecoration.HorizontalDividerItemDecoration;
import com.duke.yinyangli.view.itemdecoration.VerticalDividerItemDecoration;

import butterknife.BindView;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private SettingAdapter mAdapter;

    public static void start(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        super.initView();
        title.setText(R.string.setting);
        right.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .marginResId(R.dimen.default_margin, R.dimen.default_margin)
                .build());
        recyclerView.setAdapter(mAdapter = new SettingAdapter());
        mAdapter.loadSetting();
    }
}
