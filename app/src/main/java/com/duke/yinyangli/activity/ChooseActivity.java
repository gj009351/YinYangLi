package com.duke.yinyangli.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.ChooseAdapter;
import com.duke.yinyangli.base.BaseActivity;
import com.duke.yinyangli.base.BaseEvent;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.constants.Event;
import com.duke.yinyangli.dialog.CenterListDialog;
import com.duke.yinyangli.utils.AdmobUtils;
import com.duke.yinyangli.utils.ChooseCostUtils;
import com.duke.yinyangli.utils.LogUtils;
import com.duke.yinyangli.utils.ToastUtil;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.haibin.calendarview.library.Article;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ChooseActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private RewardedAd mRewardedAd;
    private CenterListDialog mPayDialog;

    private int mFailedCount = 0;
    private Article mChooseArticle;
    private ChooseAdapter mChooseAdapter;

    private final RewardedAdLoadCallback mRewardLoadCallback = new RewardedAdLoadCallback() {
        @Override
        public void onRewardedAdLoaded() {
            // Ad successfully loaded.
            LogUtils.d("chooseactivity:onRewardedAdLoaded");
        }

        @Override
        public void onRewardedAdFailedToLoad(LoadAdError adError) {
            // Ad failed to load.
            LogUtils.d("chooseactivity:onRewardedAdFailedToLoad:" + mFailedCount + ", adError:" + adError);
            mFailedCount ++;
            if (mFailedCount < 3 && mRewardedAd != null && AdmobUtils.isInit()) {
                mRewardedAd = new RewardedAd(ChooseActivity.this, getString(R.string.admob_reward_unit_id));
                mRewardedAd.loadAd(new AdRequest.Builder().build(), mRewardLoadCallback);
            }
        }

        @Override
        public void onRewardedAdFailedToLoad(int i) {
            super.onRewardedAdFailedToLoad(i);
            LogUtils.d("chooseactivity:onRewardedAdFailedToLoad:" + mFailedCount);
        }
    } ;

    private final RewardedAdCallback mRewardCallback = new RewardedAdCallback() {
        @Override
        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
            LogUtils.d("chooseactivity:onUserEarnedReward");
            if (mChooseArticle != null) {
                ChooseCostUtils.getInstance().addPayCount(mChooseArticle);
                mChooseArticle = null;
            }
//            ToastUtil.show(ChooseActivity.this, "可测算次数+1 ！");
        }

        @Override
        public void onRewardedAdFailedToShow(AdError adError) {
            super.onRewardedAdFailedToShow(adError);
            LogUtils.d("chooseactivity:onRewardedAdFailedToShow");
        }
    };



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
        mRewardedAd = new RewardedAd(this, getString(R.string.admob_reward_unit_id));
        mRewardedAd.loadAd(new AdRequest.Builder().build(), mRewardLoadCallback);
    }

    @Override
    public void onReceiveEvent(BaseEvent event) {
        if (event.getCode() == Event.CODE_PAY_OR_AD) {
            Bundle bundle = event.getBundle();
            mChooseArticle = (Article) bundle.getSerializable(Constants.INTENT_KEY.KEY_MODEL);
            List<String> list = new ArrayList<>();
            list.add("去付费");
            list.add("去看广告");
            list.add("取消");
            mPayDialog = CenterListDialog.init(this, "提示"
                    , "您今日该项测算服务免费次数已用光，请付费或观看广告增加次数。"
                    , new CenterListDialog.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (position == 0) {
                        //付费
                    } else if (position == 1) {
                        //看广告
                        if (AdmobUtils.isInit() && mRewardedAd != null && mRewardedAd.isLoaded()) {
                            mRewardedAd.show(ChooseActivity.this, mRewardCallback);
                        }
                    }
                    mPayDialog.dismiss();
                }
            })
                    .setList(list)
                    .showDialog();
        } else if (event.getCode() == Event.CODE_COUNT_CHANGE) {
            if (mChooseAdapter != null) {
                mChooseAdapter.notifyDataSetChanged();
            }
        } else {
            super.onReceiveEvent(event);
        }
    }

    private void loadNewRewardAd() {
        mRewardedAd = new RewardedAd(ChooseActivity.this, getString(R.string.admob_reward_unit_id));
        mRewardedAd.loadAd(new AdRequest.Builder().build(), mRewardLoadCallback);
    }

}
