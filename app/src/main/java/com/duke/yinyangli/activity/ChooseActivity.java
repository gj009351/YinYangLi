package com.duke.yinyangli.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.ChooseAdapter;
import com.duke.yinyangli.base.BaseActivity;

import butterknife.BindView;

public class ChooseActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

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
        right.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ChooseAdapter(this));
    }

}
