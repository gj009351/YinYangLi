package com.duke.yinyangli.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.duke.yinyangli.MyApplication;
import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.AllResultAdapter;
import com.duke.yinyangli.base.BaseActivity;
import com.duke.yinyangli.bean.database.DaoSession;
import com.duke.yinyangli.bean.database.Zhuge;
import com.duke.yinyangli.bean.database.ZhugeDao;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.dialog.SimpleDialog;
import com.duke.yinyangli.utils.LimitInputTextWatcher;
import com.duke.yinyangli.utils.ToastUtil;
import com.duke.yinyangli.utils.core.mingzidafen.BhFTWxLib;
import com.haibin.calendarview.library.Article;
import com.luhuiguo.chinese.ChineseUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class ZhuGeShenSuanActivity extends BaseActivity {

    @BindView(R.id.edit_text)
    EditText mEditText;
    @BindView(R.id.divider_edit)
    View mDivider;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.submit)
    View mSubmit;


    private AllResultAdapter mAdapter;
    private Article mAriticle;

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
    }


    @Override
    public void initData() {
        super.initData();
        mAriticle = (Article) getIntent().getSerializableExtra(Constants.INTENT_KEY.KEY_MODEL);
        title.setText(mAriticle.getTitle());

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDialog.init(ZhuGeShenSuanActivity.this, mAriticle.getTitle()
                        , getString(R.string.tips_zhugeshensuan), null)
                        .showCancel(false)
                        .setConfirmText(R.string.known)
                        .setConfirmTextColor(R.color.blue_2288BB)
                        .showDialog();
            }
        });
    }

    @OnClick(R.id.submit)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                if (TextUtils.isEmpty(mEditText.getText())
                        || TextUtils.isEmpty(mEditText.getText().toString())
                        || TextUtils.isEmpty(mEditText.getText().toString().trim())) {
                    ToastUtil.show(this, "请输入三个汉字");
                    return;
                }
                String text = mEditText.getText().toString();
                if (text.length() == 3) {
                    String result = ChineseUtils.toSimplified(text);
                    BhFTWxLib lib = new BhFTWxLib();
                    int total = 0;
                    int first = lib.getStringLibs(result.charAt(0));
                    int second = lib.getStringLibs(result.charAt(1));
                    int third = lib.getStringLibs(result.charAt(2));
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
                            addTestCount(mAriticle);
                            dismissProgressDialog();
                        }
                    });
                } else {
                    ToastUtil.show(this, "请输入三个汉字");
                }
                break;
            default:break;
        }
    }

}
