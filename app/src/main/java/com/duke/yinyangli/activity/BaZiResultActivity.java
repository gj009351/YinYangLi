package com.duke.yinyangli.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.duke.yinyangli.MyApplication;
import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.AllResultAdapter;
import com.duke.yinyangli.base.BaseActivity;
import com.duke.yinyangli.base.BaseResultActivity;
import com.duke.yinyangli.bean.database.DaoSession;
import com.duke.yinyangli.bean.database.Rgnm;
import com.duke.yinyangli.bean.database.RgnmDao;
import com.duke.yinyangli.bean.database.Rysmn;
import com.duke.yinyangli.bean.database.RysmnDao;
import com.duke.yinyangli.bean.database.ShuXiang;
import com.duke.yinyangli.bean.database.ShuXiangDao;
import com.duke.yinyangli.calendar.Lunar;
import com.duke.yinyangli.calendar.Solar;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.dialog.DialogUtils;
import com.duke.yinyangli.dialog.SimpleDialog;
import com.duke.yinyangli.view.share.BaZiResultView;
import com.haibin.calendarview.library.Article;

import java.io.File;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class BaZiResultActivity extends BaseResultActivity {


    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private AllResultAdapter mAdapter;
    private Runnable mSuanmingRuannable;
    private Rgnm mRgnm;
    private Rysmn mRysmn;
    private ShuXiang mShuXiang;
    private Rysmn mMonth;
    private Rysmn mDay;
    private Rysmn mTime;
    private Lunar mLunar;
    private Solar mSolar;

    public static void start(Context context, Article article) {
        context.startActivity(new Intent(context, BaZiResultActivity.class)
                .putExtra(Constants.INTENT_KEY.KEY_MODEL, article));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bazi_result;
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
        mArticle = (Article) getIntent().getSerializableExtra(Constants.INTENT_KEY.KEY_MODEL);
        title.setText(mArticle.getTitle());
        image.setImageResource(R.mipmap.bazi);

        DialogUtils.showBirthdayPicker(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mLunar = Lunar.fromDate(date);
                mSolar = Solar.fromDate(date);

                showProgressDialog();
                mHandler.post(mSuanmingRuannable = new Runnable() {
                    @Override
                    public void run() {

                        if (isSafe()) {
                            String ganzhi = mLunar.getDayInGanZhi();
                            File outFileName = getDatabasePath(Constants.DB_NAME);
                            boolean exists = outFileName.exists();
                            long size = outFileName.length();

                            DaoSession daoSession = MyApplication.getInstance().getDao();
                            if (daoSession != null) {
                                mRgnm = daoSession.getRgnmDao().queryBuilder()
                                        .where(RgnmDao.Properties.Rgz.eq(ganzhi)).unique();

                                mMonth = daoSession.getRysmnDao().queryBuilder()
                                        .where(RysmnDao.Properties.Siceng.eq(mLunar.getMonthInChinese2())).unique();
                                mDay = daoSession.getRysmnDao().queryBuilder()
                                        .where(RysmnDao.Properties.Siceng.eq(mLunar.getDayInChinese())).unique();
                                mTime = daoSession.getRysmnDao().queryBuilder()
                                        .where(RysmnDao.Properties.Siceng.eq(mLunar.getTimeZhi2())).unique();

                                mShuXiang = daoSession.getShuXiangDao().queryBuilder()
                                        .where(ShuXiangDao.Properties.Title.eq(mLunar.getYearShengXiao())).unique();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isSafe()) {
                                            mAdapter.setResult(mRgnm, mMonth, mDay, mTime, mShuXiang, mLunar, mSolar);
                                            dismissProgressDialog();
                                        }
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isSafe()) {
                                            dismissProgressDialog();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public String getAboutDialogContent() {
        return getString(R.string.tip_baguasuanming);
    }

    @Override
    public View getShareContentView() {
        BaZiResultView view = (BaZiResultView) LayoutInflater.from(this).inflate(R.layout.share_ba_zi, null);
        view.setInfo(mRgnm, mMonth, mDay, mTime, mShuXiang, mLunar, mSolar);
        return view;
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null && mSuanmingRuannable != null) {
            mHandler.removeCallbacks(mSuanmingRuannable);
        }
        if (image != null) {
            image.clearAnimation();
        }
        super.onDestroy();
    }

}
