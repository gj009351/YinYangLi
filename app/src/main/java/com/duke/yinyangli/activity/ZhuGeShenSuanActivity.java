package com.duke.yinyangli.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.MyApplication;
import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.AllResultAdapter;
import com.duke.yinyangli.base.BaseResultActivity;
import com.duke.yinyangli.bean.database.DaoSession;
import com.duke.yinyangli.bean.database.Zhuge;
import com.duke.yinyangli.bean.database.ZhugeDao;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.utils.LimitInputTextWatcher;
import com.duke.yinyangli.utils.ToastUtil;
import com.duke.yinyangli.utils.core.mingzidafen.BhFTWxLib;
import com.duke.yinyangli.view.share.ZhuGeResultView;
import com.haibin.calendarview.library.Article;
import com.luhuiguo.chinese.ChineseUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ZhuGeShenSuanActivity extends BaseResultActivity {

    @BindView(R.id.edit_text)
    EditText mEditText;
    @BindView(R.id.divider_edit)
    View mDivider;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.submit)
    View mSubmit;


    private AllResultAdapter mAdapter;
    private String mResult;

    @Override
    public int getLayoutId() {
        return R.layout.activity_zhugeshensuan;
    }


    public static void start(Context context, Article article) {
        context.startActivity(new Intent(context, ZhuGeShenSuanActivity.class)
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
        mEditText.addTextChangedListener(new LimitInputTextWatcher(mEditText));
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
        return getString(R.string.tips_zhugeshensuan);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit) {
            if (TextUtils.isEmpty(mEditText.getText())
                    || TextUtils.isEmpty(mEditText.getText().toString())
                    || TextUtils.isEmpty(mEditText.getText().toString().trim())) {
                ToastUtil.show(this, "请输入三个汉字");
                return;
            }
            String text = mEditText.getText().toString();
            if (text.length() == 3) {
                mResult = ChineseUtils.toSimplified(text);
                BhFTWxLib lib = new BhFTWxLib();
                int total = 0;
                int first = lib.getStringLibs(mResult.charAt(0));
                int second = lib.getStringLibs(mResult.charAt(1));
                int third = lib.getStringLibs(mResult.charAt(2));
                first = first % 10;
                second = second % 10;
                third = third % 10;
                total = first * 100 + second * 10 + third;
                total = total % 384;
                if (total < 1) {
                    total = 1;
                }
                String id;
                if (total < 10) {
                    id = "00" + total;
                } else if (total < 100) {
                    id = "0" + total;
                } else {
                    id = Integer.toString(total);
                }
                DaoSession daoSession = MyApplication.getInstance().getDao();
                Zhuge zhuge = daoSession.getZhugeDao().queryBuilder()
                        .where(ZhugeDao.Properties.Id.eq(id)).unique();
                List<Article> list = new ArrayList<>();
                list.add(Article.create("第" + id + "签\n" + zhuge.getTitle(), zhuge.getContent(), ""));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setResult(list);
                        mEditText.setEnabled(false);
                        mSubmit.setVisibility(View.GONE);
                        mDivider.setBackgroundColor(mEditText.getCurrentTextColor());
                        dismissProgressDialog();
                    }
                });
            } else {
                ToastUtil.show(this, "请输入三个汉字");
            }
        } else {
            super.onClick(view);
        }
    }

    @Override
    public View getShareContentView() {
        ZhuGeResultView view = (ZhuGeResultView)(LayoutInflater.from(this).inflate(R.layout.share_zhugeshensuan, null));
        view.setInfo(mResult, mAdapter.getShareData(getShareType()));
        return view;
    }

}
