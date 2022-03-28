package com.duke.yinyangli.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.MyApplication;
import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.AllResultAdapter;
import com.duke.yinyangli.base.BaseResultActivity;
import com.duke.yinyangli.bean.database.DaoSession;
import com.duke.yinyangli.bean.database.ShuXiangLove;
import com.duke.yinyangli.bean.database.ShuXiangLoveDao;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.utils.ThreadHelper;
import com.duke.yinyangli.utils.ToastUtil;
import com.duke.yinyangli.utils.core.ImageUtil;
import com.duke.yinyangli.view.share.ShengXiaoPeiduiView;
import com.haibin.calendarview.library.Article;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShengXiaoPeiDuiActivity extends BaseResultActivity {

    @BindView(R.id.spinner_first)
    NiceSpinner mSpinner1;
    @BindView(R.id.spinner_second)
    NiceSpinner mSpinner2;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.image_left)
    ImageView mLeftImage;
    @BindView(R.id.image_center)
    ImageView mCenterImage;
    @BindView(R.id.image_right)
    ImageView mRightImage;
    @BindView(R.id.submit)
    View mSubmit;

    private AllResultAdapter mAdapter;
    private String mShengxiaoNv;
    private String mShengxiaoNan;

    @Override
    public int getLayoutId() {
        return R.layout.activity_shengxiaopeidui;
    }

    public static void start(Context context, Article article) {
        context.startActivity(new Intent(context, ShengXiaoPeiDuiActivity.class)
                .putExtra(Constants.INTENT_KEY.KEY_MODEL, article));
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
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mArticle = (Article) getIntent().getSerializableExtra(Constants.INTENT_KEY.KEY_MODEL);
        title.setText(mArticle.getTitle());
    }

    @Override
    public String getAboutDialogContent() {
        return getString(R.string.tip_shengxiaopeidui);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.submit) {
            mShengxiaoNv = (String) mSpinner1.getSelectedItem();
            mShengxiaoNan = (String) mSpinner2.getSelectedItem();
            if (TextUtils.isEmpty(mShengxiaoNv)) {
                ToastUtil.show(this, "请选择女方的属相");
            }
            if (TextUtils.isEmpty(mShengxiaoNan)) {
                ToastUtil.show(this, "请选择男方的属相");
            }
            showProgressDialog();
            ThreadHelper.INST.execute(new Runnable() {
                @Override
                public void run() {
                    DaoSession daoSession = MyApplication.getInstance().getDao();
                    ShuXiangLove shengxiaoLove = daoSession.getShuXiangLoveDao().queryBuilder()
                            .where(ShuXiangLoveDao.Properties.Shengxiao1.eq(mShengxiaoNan)
                                    , ShuXiangLoveDao.Properties.Shengxiao2.eq(mShengxiaoNv)).unique();

                    List<Article> articles = new ArrayList<>();
                    int titleIndex = shengxiaoLove.getContent1().indexOf("：");
                    int length = shengxiaoLove.getContent1().length();
                    String title = "女" + mShengxiaoNv + " + " + "男" + mShengxiaoNan;
                    String content = shengxiaoLove.getContent1().substring(titleIndex + 1, length);
                    articles.add(Article.create(title, content, ""));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setResult(articles);
                            mSpinner1.setEnabled(false);
                            mSpinner2.setEnabled(false);
                            mSubmit.setVisibility(View.GONE);
                            ImageUtil.setShuXiangImage(mLeftImage, mShengxiaoNv);
                            ImageUtil.setShuXiangImage(mRightImage, mShengxiaoNan);
                            mCenterImage.setVisibility(View.VISIBLE);
                            addTestCount(mArticle);
                            dismissProgressDialog();
                        }
                    });
                }
            });
        } else {
            super.onClick(view);
        }
    }

    @Override
    public View getShareContentView() {
        ShengXiaoPeiduiView view = (ShengXiaoPeiduiView) LayoutInflater.from(this).inflate(R.layout.share_shengxiao_peidui, null);
        view.setInfo(mShengxiaoNv, mShengxiaoNan, mAdapter.getShareData(getShareType()));
        return view;
    }
}
