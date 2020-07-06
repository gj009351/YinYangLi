package com.duke.yinyangli.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.AllResultAdapter;
import com.duke.yinyangli.base.BaseActivity;
import com.duke.yinyangli.bean.ChengGuItem;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.dialog.DialogUtils;
import com.duke.yinyangli.interfaces.OnLoadListener;
import com.duke.yinyangli.utils.core.ChengguUtils;
import com.duke.yinyangli.view.spiderview.SpiderWebView;
import com.haibin.calendarview.library.Article;

import java.util.Calendar;
import java.util.Date;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChengGuActivity extends BaseActivity {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Article mAriticle;
    private AllResultAdapter mAdapter;

    public static void start(Context context, Article article) {
        context.startActivity(new Intent(context, ChengGuActivity.class)
                .putExtra(Constants.INTENT_KEY.KEY_MODEL, article));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cheng_gu;
    }

    @Override
    public boolean requestButterKnife() {
        return true;
    }

    @Override
    public void initView() {
        super.initView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new AllResultAdapter(this));
    }

    @Override
    public void initData() {
        super.initData();
        mHandler = new MyHandler(this);
        mAriticle = (Article) getIntent().getSerializableExtra(Constants.INTENT_KEY.KEY_MODEL);
        title.setText(mAriticle.getTitle());
        image.setImageResource(R.mipmap.yuantiangang);
        DialogUtils.showBirthdayPicker(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int[] result = ChengguUtils.getInstance().chenggu(calendar.get(Calendar.YEAR)
                        , calendar.get(Calendar.MONTH)
                        , calendar.get(Calendar.DAY_OF_MONTH)
                        , calendar.get(Calendar.HOUR));
                showProgressDialog();
                mHandler.postDelayed(() -> ChengguUtils.getInstance()
                        .getJson(ChengGuActivity.this
                                , result[0] + "." + result[1]
                                , (json, chengGuItem) -> runOnUiThread(
                                        () -> {
                                            mAdapter.setResult(calendar, result, chengGuItem);
                                            dismissProgressDialog();
                                        }
                                )), 2000);
            }
        });
    }
}
