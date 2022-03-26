package com.duke.yinyangli.activity;

import android.content.Context;
import android.content.Intent;
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

    private Article mAriticle;
    private AllResultAdapter mAdapter;
    private Runnable mSuanmingRuannable;

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
        mAriticle = (Article) getIntent().getSerializableExtra(Constants.INTENT_KEY.KEY_MODEL);
        title.setText(mAriticle.getTitle());
        image.setImageResource(R.mipmap.bazi);

        DialogUtils.showBirthdayPicker(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Lunar lunar = Lunar.fromDate(date);
                Solar solar = Solar.fromDate(date);

                showProgressDialog();
                startRotate();
                mHandler.post(mSuanmingRuannable = new Runnable() {
                    @Override
                    public void run() {

                        if (isSafe()) {
                            String ganzhi = lunar.getDayInGanZhi();
                            File outFileName = getDatabasePath(Constants.DB_NAME);
                            boolean exists = outFileName.exists();
                            long size = outFileName.length();

                            DaoSession daoSession = MyApplication.getInstance().getDao();
                            if (daoSession != null) {

                                List<Rgnm> rgnmList = daoSession.getRgnmDao().queryBuilder().list();

                                Rgnm rgnm = daoSession.getRgnmDao().queryBuilder()
                                        .where(RgnmDao.Properties.Rgz.eq(ganzhi)).unique();

                                Rysmn month = daoSession.getRysmnDao().queryBuilder()
                                        .where(RysmnDao.Properties.Siceng.eq(lunar.getMonthInChinese2())).unique();
                                Rysmn day = daoSession.getRysmnDao().queryBuilder()
                                        .where(RysmnDao.Properties.Siceng.eq(lunar.getDayInChinese2())).unique();
                                Rysmn time = daoSession.getRysmnDao().queryBuilder()
                                        .where(RysmnDao.Properties.Siceng.eq(lunar.getTimeZhi2())).unique();

                                ShuXiang shuXiang = daoSession.getShuXiangDao().queryBuilder()
                                        .where(ShuXiangDao.Properties.Title.eq(lunar.getYearShengXiao())).unique();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isSafe()) {
                                            mAdapter.setResult(rgnm, month, day, time, shuXiang, lunar, solar);
                                            addTestCount(mAriticle);
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

    private void startRotate() {
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        image.startAnimation(operatingAnim);
    }

    public String getAboutDialogTitle() {
        return mAriticle.getTitle();
    }

    public String getAboutDialogContent() {
        return getString(R.string.tip_baguasuanming);
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
