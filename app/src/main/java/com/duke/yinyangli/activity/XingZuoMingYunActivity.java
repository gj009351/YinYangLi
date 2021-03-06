package com.duke.yinyangli.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.duke.yinyangli.MyApplication;
import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.AllResultAdapter;
import com.duke.yinyangli.base.BaseActivity;
import com.duke.yinyangli.bean.database.Astro;
import com.duke.yinyangli.bean.database.AstroDao;
import com.duke.yinyangli.bean.database.DaoSession;
import com.duke.yinyangli.bean.database.XingZuo;
import com.duke.yinyangli.bean.database.XingZuoDao;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.dialog.SimpleDialog;
import com.duke.yinyangli.utils.ThreadHelper;
import com.duke.yinyangli.utils.ToastUtil;
import com.duke.yinyangli.utils.core.ImageUtil;
import com.haibin.calendarview.library.Article;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class XingZuoMingYunActivity extends BaseActivity {

    @BindView(R.id.spinner_xingzuo)
    NiceSpinner mXingZuoSpinner;
    @BindView(R.id.spinner_xuexing)
    NiceSpinner mXueXingSpinner;
    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.submit)
    View mSubmit;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private AllResultAdapter mAdapter;
    private Article mAriticle;
    private String mXingZuo;
    private String mXueXing;

    @Override
    public int getLayoutId() {
        return R.layout.activity_xingzuomingyun;
    }

    public static void start(Context context, Article article) {
        context.startActivity(new Intent(context, XingZuoMingYunActivity.class)
                .putExtra(Constants.INTENT_KEY.KEY_MODEL, article));
    }

    @Override
    public boolean requestButterKnife() {
        return true;
    }

    @Override
    public void initView() {
        super.initView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new AllResultAdapter(this));
    }

    @Override
    public void initData() {
        super.initData();
        mAriticle = (Article) getIntent().getSerializableExtra(Constants.INTENT_KEY.KEY_MODEL);
        title.setText(mAriticle.getTitle());

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDialog.init(XingZuoMingYunActivity.this, mAriticle.getTitle()
                        , getString(R.string.tip_xingzuomingyun), null)
                        .showCancel(false)
                        .setConfirmText(R.string.known)
                        .setConfirmTextColor(R.color.blue_2288BB)
                        .showDialog();
            }
        });

        List<String> xingZuoList = new LinkedList<String>(Arrays.asList(getResources().getStringArray(R.array.array_xingzuo)));
        mXingZuoSpinner.attachDataSource(xingZuoList);
        mXingZuoSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                mXingZuo = (String) parent.getItemAtPosition(position);
            }
        });
        mXingZuo = (String) mXingZuoSpinner.getItemAtPosition(0);

        List<String> xueXingList = new LinkedList<String>(Arrays.asList(getResources().getStringArray(R.array.array_xuexing)));
        mXueXingSpinner.attachDataSource(xueXingList);
        mXueXingSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                mXueXing = (String) parent.getItemAtPosition(position);
            }
        });
        mXueXing = (String) mXueXingSpinner.getItemAtPosition(0);
    }

    @OnClick(R.id.submit)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                if (TextUtils.isEmpty(mXingZuo)) {
                    ToastUtil.show(this, "请选择你的星座");
                    return;
                }
                if (TextUtils.isEmpty(mXueXing)) {
                    ToastUtil.show(this, "请选择你的血型");
                    return;
                }
                showProgressDialog();
                ThreadHelper.INST.execute(new Runnable() {
                    @Override
                    public void run() {
                        DaoSession daoSession = MyApplication.getInstance().getDao();
                        Astro astro = daoSession.getAstroDao().queryBuilder()
                                .where(AstroDao.Properties.Title.eq(mXueXing + mXingZuo)).unique();
                        XingZuo xingZuo = daoSession.getXingZuoDao().queryBuilder()
                                .where(XingZuoDao.Properties.Title.eq(mXingZuo)).unique();

                        List<Article> articles = new ArrayList<>();
                        articles.add(Article.create(xingZuo.getTitle(), xingZuo.getContent(), ""));
                        articles.add(Article.create(astro.getTitle(), astro.getContent(), ""));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.setResult(articles);
                                mXingZuoSpinner.setEnabled(false);
                                mXueXingSpinner.setEnabled(false);
                                mSubmit.setVisibility(View.GONE);
                                ImageUtil.setXingZuoImage(mImage, mXingZuo);
                                addTestCount(mAriticle);
                                dismissProgressDialog();
                            }
                        });
                    }
                });
                break;
            default:break;
        }
    }

}
