package com.duke.yinyangli.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.ChooseAdapter;
import com.duke.yinyangli.base.BaseActivity;
import com.duke.yinyangli.base.BaseEvent;
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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ChooseActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tips)
    TextView tips;

    private RewardedAd mRewardedAd;
    private CenterListDialog mPayDialog;

    private int mFailedCount = 0;
    private boolean mWillShowReward;

    private final RewardedAdLoadCallback mRewardLoadCallback = new RewardedAdLoadCallback() {
        @Override
        public void onRewardedAdLoaded() {
            // Ad successfully loaded.
            LogUtils.d("onRewardedAdLoaded");
            if (mWillShowReward) {
                mRewardedAd.show(ChooseActivity.this, mRewardCallback);
                mWillShowReward = false;
                dismissProgressDialog();
            }
        }

        @Override
        public void onRewardedAdFailedToLoad(LoadAdError adError) {
            // Ad failed to load.
            LogUtils.d("onRewardedAdFailedToLoad:" + mFailedCount + ", adError:" + adError);
            mFailedCount ++;
            if (mFailedCount < 3 && mRewardedAd != null && AdmobUtils.isInit()) {
                loadNewRewardAd();
            } else {
                dismissProgressDialog();
            }
        }

    } ;

    private final RewardedAdCallback mRewardCallback = new RewardedAdCallback() {
        @Override
        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
            LogUtils.d("onUserEarnedReward");
            ChooseCostUtils.getInstance().addPayCount();
            ToastUtil.show(ChooseActivity.this, "恭喜可测算次数+1 ！");
            EventBus.getDefault().post(new BaseEvent(Event.CODE_COUNT_CHANGE));
            loadNewRewardAd();
        }

        @Override
        public void onRewardedAdFailedToShow(AdError adError) {
            super.onRewardedAdFailedToShow(adError);
            LogUtils.d("onRewardedAdFailedToShow");
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
        setTips();
        title.setText(R.string.select);
        right.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ChooseAdapter mChooseAdapter;
        recyclerView.setAdapter(mChooseAdapter = new ChooseAdapter(this));
    }

    @Override
    public void initData() {
        super.initData();
        LogUtils.d("initData");
        loadNewRewardAd();
    }

    @Override
    public void onReceiveEvent(BaseEvent event) {
        if (event.getCode() == Event.CODE_PAY_OR_AD) {
            List<String> list = new ArrayList<>();
            list.add("去付费");
            list.add("去看广告");
            list.add("取消");
            mPayDialog = CenterListDialog.init(this, "提示"
                    , "您今日剩余免费次数已用光，请付费或观看广告增加次数。"
                    , new CenterListDialog.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (position == 0) {
                        //付费
                    } else if (position == 1) {
                        //看广告
                        if (AdmobUtils.isInit() && mRewardedAd != null) {
                            if (mRewardedAd.isLoaded()) {
                                mRewardedAd.show(ChooseActivity.this, mRewardCallback);
                            } else {
                                mWillShowReward = true;
                                showProgressDialog();
                            }
                        }
                    }
                    mPayDialog.dismiss();
                }
            })
                    .setList(list)
                    .showDialog();
        } else if (event.getCode() == Event.CODE_COUNT_CHANGE) {
            setTips();
        } else {
            super.onReceiveEvent(event);
        }
    }

    private void loadNewRewardAd() {
        mRewardedAd = new RewardedAd(ChooseActivity.this, getString(R.string.admob_reward_unit_id));
        mRewardedAd.loadAd(new AdRequest.Builder().build(), mRewardLoadCallback);
    }

    private void setTips() {
        if (ChooseCostUtils.getInstance().isVIP()) {
            tips.setText("尊贵的一卦用户，祝您大吉大利，万事如意！");
        } else {
            tips.setText("今日剩余免费次数：" + ChooseCostUtils.getInstance().getTodayCount() + "次");
        }
    }
}
