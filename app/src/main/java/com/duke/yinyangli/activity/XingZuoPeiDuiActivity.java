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
import com.duke.yinyangli.bean.database.XingZuoLove;
import com.duke.yinyangli.bean.database.XingZuoLoveDao;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.utils.ThreadHelper;
import com.duke.yinyangli.utils.ToastUtil;
import com.duke.yinyangli.utils.core.ImageUtil;
import com.duke.yinyangli.view.share.XingZuoPeiduiView;
import com.haibin.calendarview.library.Article;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class XingZuoPeiDuiActivity extends BaseResultActivity {

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
    private String mXingzuoNv;
    private String mXingzuoNan;

    @Override
    public int getLayoutId() {
        return R.layout.activity_xingzuopeidui;
    }

    public static void start(Context context, Article article) {
        context.startActivity(new Intent(context, XingZuoPeiDuiActivity.class)
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
        return getString(R.string.tip_xingzuopeidui);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit) {
            mXingzuoNv = (String) mSpinner1.getSelectedItem();
            mXingzuoNan = (String) mSpinner2.getSelectedItem();
            if (TextUtils.isEmpty(mXingzuoNv)) {
                ToastUtil.show(this, "请选择女方的星座");
            }
            if (TextUtils.isEmpty(mXingzuoNan)) {
                ToastUtil.show(this, "请选择男方的星座");
            }
            showProgressDialog();
            ThreadHelper.INST.execute(new Runnable() {
                @Override
                public void run() {
                    DaoSession daoSession = MyApplication.getInstance().getDao();
                    XingZuoLove xingZuoLove = daoSession.getXingZuoLoveDao().queryBuilder()
                            .where(XingZuoLoveDao.Properties.Xingzuo1.eq(mXingzuoNv)
                                    , XingZuoLoveDao.Properties.Xingzuo2.eq(mXingzuoNan)).unique();

                    List<Article> articles = new ArrayList<>();
                    articles.add(Article.create(xingZuoLove.getTitle(), xingZuoLove.getContent1(), ""));
                    articles.add(Article.create(xingZuoLove.getContent2(), "", ""));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mOkToShare = true;
                            mAdapter.setResult(articles);
                            mSpinner1.setEnabled(false);
                            mSpinner2.setEnabled(false);
                            mSubmit.setVisibility(View.GONE);
                            ImageUtil.setXingZuoImage(mLeftImage, mXingzuoNv);
                            ImageUtil.setXingZuoImage(mRightImage, mXingzuoNan);
                            mCenterImage.setVisibility(View.VISIBLE);
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
        XingZuoPeiduiView view = (XingZuoPeiduiView) LayoutInflater.from(this).inflate(R.layout.share_xingzuo_peidui, null);
        view.setInfo(mXingzuoNv, mXingzuoNan, mAdapter.getShareData(getShareType()));
        return view;
    }
}
