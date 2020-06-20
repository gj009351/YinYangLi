package com.duke.yinyangli.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.AllResultAdapter;
import com.duke.yinyangli.adapter.GuaXiangAdapter;
import com.duke.yinyangli.base.BaseActivity;
import com.duke.yinyangli.bean.JieGuaItem;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.utils.ImageUtils;
import com.duke.yinyangli.utils.JieGuaUtils;
import com.duke.yinyangli.utils.ZhanBuUtils;
import com.duke.yinyangli.view.spiderview.SpiderWebView;
import com.haibin.calendarview.library.Article;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultActivity extends BaseActivity {


    private static final long DURATION_DALEY_NRXT = 800;
    @BindView(R.id.image_left)
    ImageView imageLeft;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.image_right)
    ImageView imageRight;
    @BindView(R.id.result_origin)
    TextView resultView;
    @BindView(R.id.result_master)
    TextView resultMaster;
    @BindView(R.id.result_changed)
    TextView resultChanged;
    @BindView(R.id.result_content_list)
    RecyclerView resultAll;
    @BindView(R.id.result_origin_list)
    RecyclerView resultOriginList;
    @BindView(R.id.result_master_list)
    RecyclerView resultMasterList;
    @BindView(R.id.result_changed_list)
    RecyclerView resultChangedList;
    @BindView(R.id.spider_view)
    SpiderWebView spiderView;

    private int mCaoCount;
    private List<Integer> list;
    private GuaXiangAdapter mOriginAdapter;
    private GuaXiangAdapter mMasterAdapter;
    private GuaXiangAdapter mChangedAdapter;
    private AllResultAdapter mAllAdapter;
    private Article mAriticle;

    public static void start(Context context, Article article) {
        context.startActivity(new Intent(context, ResultActivity.class)
                .putExtra(Constants.INTENT_KEY.KEY_MODEL, article));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_result;
    }

    @Override
    public void initView() {
        super.initView();
        resultOriginList.setLayoutManager(new LinearLayoutManager(this));
        resultOriginList.setAdapter(mOriginAdapter = new GuaXiangAdapter(this, true));
        resultMasterList.setLayoutManager(new LinearLayoutManager(this));
        resultMasterList.setAdapter(mMasterAdapter = new GuaXiangAdapter(this, false));
        resultChangedList.setLayoutManager(new LinearLayoutManager(this));
        resultChangedList.setAdapter(mChangedAdapter = new GuaXiangAdapter(this, false));
        resultAll.setLayoutManager(new LinearLayoutManager(this));
        resultAll.setAdapter(mAllAdapter = new AllResultAdapter(this));
    }

    @Override
    public void initData() {
        super.initData();
        mHandler = new MyHandler(this);
        mAriticle = (Article) getIntent().getSerializableExtra(Constants.INTENT_KEY.KEY_MODEL);
        title.setText(mAriticle.getTitle());
        image.setImageResource(mAriticle.getImgRes());
        setResult();
        if (mAriticle.getType() == Constants.TYPE.TYPE_QIAN) {
            imageLeft.setImageResource(mAriticle.getImgRes());
            imageRight.setImageResource(mAriticle.getImgRes());
            playFlip(imageLeft);
            playFlip(image);
            playFlip(imageRight);
        }
    }

    private void playFlip(ImageView imageView) {
        AnimatorSet animatorSetOut = (AnimatorSet) AnimatorInflater
                .loadAnimator(this, R.animator.flip_out);

        final AnimatorSet animatorSetIn = (AnimatorSet) AnimatorInflater
                .loadAnimator(this, R.animator.flip_in);

        animatorSetOut.setTarget(imageView);
        animatorSetIn.setTarget(imageView);

        animatorSetOut.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {// 翻转90度之后，换图
                animatorSetIn.start();
            }
        });

        animatorSetIn.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSetOut.start();
            }
        });
        animatorSetOut.start();
    }

    private void setResult() {
        switch (mAriticle.getType()) {
            case Constants.TYPE.TYPE_CAO:
                getResultCao();
                break;
            case Constants.TYPE.TYPE_QIAN:
                getResultQian();
                break;
            default:
                break;
        }
    }

    private void getResultQian() {
        mCaoCount = 0;
        if (list == null) {
            list = new ArrayList<>();
        } else {
            list.clear();
        }
        int result = ZhanBuUtils.getResultQian();
        list.add(result);
        mOriginAdapter.refreshData(ZhanBuUtils.getGua(list, 0));
        mHandler.sendEmptyMessageDelayed(0, DURATION_DALEY_NRXT);
    }

    private void getResultCao() {
        mCaoCount = 0;
        if (list == null) {
            list = new ArrayList<>();
        } else {
            list.clear();
        }
        int result = ZhanBuUtils.getResultCao();
        list.add(result);
        mOriginAdapter.refreshData(ZhanBuUtils.getGua(list, 0));
        mHandler.sendEmptyMessageDelayed(0, DURATION_DALEY_NRXT);
    }

    @Override
    public void onHandleMessage(Message msg) {
        super.onHandleMessage(msg);
        mCaoCount++;
        if (mCaoCount >= 6) {
            mHandler.removeMessages(0);
            Collections.reverse(list);

            mOriginAdapter.refreshData(ZhanBuUtils.getGua(list, 1));
            mMasterAdapter.refreshData(ZhanBuUtils.getGua(list, 2));
            mChangedAdapter.refreshData(ZhanBuUtils.getGua(list, 3));
            JieGuaUtils.getInstance().getGuaJson(this, ZhanBuUtils.getCode(list, false), new JieGuaUtils.OnLoadListener() {
                @Override
                public void onLoad(String json, JieGuaItem jieGuaItem) {
                    if (jieGuaItem != null) {
                        resultMaster.setText("主卦：" + jieGuaItem.getName());
                        mAllAdapter.setResult(jieGuaItem);
                    }
                }
            });
            JieGuaUtils.getInstance().getGuaJson(this, ZhanBuUtils.getCode(list, true), new JieGuaUtils.OnLoadListener() {
                @Override
                public void onLoad(String json, JieGuaItem jieGuaItem) {
                    if (jieGuaItem != null) {
                        resultChanged.setText("变卦：" + jieGuaItem.getName());
                    }
                }
            });
        } else {
            int result = mAriticle.getType() == Constants.TYPE.TYPE_CAO ? ZhanBuUtils.getResultCao()
                    : ZhanBuUtils.getResultQian();
            list.add(result);
            mOriginAdapter.refreshData(ZhanBuUtils.getGua(list, 0));
            mHandler.sendEmptyMessageDelayed(0, DURATION_DALEY_NRXT);
        }
    }

}
