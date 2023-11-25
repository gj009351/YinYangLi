package com.duke.yinyangli.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.AllResultAdapter;
import com.duke.yinyangli.base.BaseResultActivity;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.utils.ThreadHelper;
import com.duke.yinyangli.utils.ToastUtil;
import com.duke.yinyangli.utils.core.mingzidafen.JavaLuozhuangtestnameClass;
import com.duke.yinyangli.utils.core.mingzidafen.LuozhuangNameClass;
import com.duke.yinyangli.utils.core.mingzidafen.Luozhuangnamewuxing;
import com.duke.yinyangli.view.share.XingMingResultView;
import com.haibin.calendarview.library.Article;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class XingMingDaFenActivity extends BaseResultActivity {

    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.submit)
    View submit;
    @BindView(R.id.divider_edit)
    View divider;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private AllResultAdapter mAdapter;
    private String mTestName;
    private int mZongfen;
    private int tiangeScore;
    private int digeScore;
    private int rengeScore;
    private int waigeScore;
    private int zonggeScore;
    private String[] tiange;
    private String[] dige;
    private String[] renge;
    private String[] waige;
    private String[] zongge;

    public static void start(Context context, Article article) {
        context.startActivity(new Intent(context, XingMingDaFenActivity.class)
                .putExtra(Constants.INTENT_KEY.KEY_MODEL, article));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_xingmingdafen;
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
        submit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mArticle = (Article) getIntent().getSerializableExtra(Constants.INTENT_KEY.KEY_MODEL);
        title.setText(mArticle.getTitle());
        editName.requestFocus();
    }

    @Override
    public String getAboutDialogContent() {
        return getString(R.string.tip_xingmingpingfen);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit) {
            if (!TextUtils.isEmpty(editName.getText())
                    && !TextUtils.isEmpty(editName.getText().toString())
                    && !TextUtils.isEmpty(editName.getText().toString().trim())) {
                mTestName = editName.getText().toString();
                showProgressDialog();

                ThreadHelper.INST.execute(new Runnable() {
                    @Override
                    public void run() {
                        JavaLuozhuangtestnameClass testName = new JavaLuozhuangtestnameClass(mTestName);
                        Luozhuangnamewuxing myLuozhuangnamewuxing = new Luozhuangnamewuxing();
                        LuozhuangNameClass myName = testName.getMyName();
                        List<Article> list = new ArrayList<>();

//                            int[] temp = myLuozhuangnamewuxing.getnameliborder(myName.getName());
//                            int[] wuxing = myLuozhuangnamewuxing.getnameWX(temp);
//                            int[] BH = myLuozhuangnamewuxing.getnameBH(temp);
//                            list.add(Article.create("五行：", myLuozhuangnamewuxing.getnameWXarray(wuxing), 0));
//                            list.add(Article.create("笔画：", myLuozhuangnamewuxing.pringst(BH), 0));
                        tiange = testName.gettotalnameji(myName.getNamesky());
                        dige = testName.gettotalnameji(myName.getNameearth());
                        renge = testName.gettotalnameji(myName.getNamepeople());
                        waige = testName.gettotalnameji(myName.getNameout());
                        zongge = testName.gettotalnameji(myName.getTotal());
                        tiangeScore = getScore(tiange[0], tiange[2]);
                        digeScore = getScore(dige[0], dige[2]);
                        rengeScore = getScore(renge[0], renge[2]);
                        waigeScore = getScore(waige[0], waige[2]);
                        zonggeScore = getScore(zongge[0], zongge[2]);
                        mZongfen = (tiangeScore + digeScore + rengeScore + waigeScore + zonggeScore) / 5;

                        list.add(Article.create(myName.getName() + "的姓名评分：" + mZongfen + "分"
                                , "", "本结果仅供娱乐"));

                        list.add(Article.create(myName.getName() + "的天格，评分："
                                        + tiangeScore + "分   " + tiange[2]
                                , tiange[1], getString(R.string.tips_tiange)));
                        list.add(Article.create(myName.getName() + "的地格，评分："
                                        + digeScore + "分   " + dige[2]
                                , dige[1], getString(R.string.tips_dige)));
                        list.add(Article.create(myName.getName() + "的人格，评分："
                                        + rengeScore + "分   " + renge[2]
                                , renge[1], getString(R.string.tips_renge)));
                        list.add(Article.create(myName.getName() + "的外格，评分："
                                        + waigeScore + "分   " + waige[2]
                                , waige[1], getString(R.string.tips_waige)));
                        list.add(Article.create(myName.getName() + "的总格，评分："
                                        + zonggeScore + "分   " + zongge[2]
                                , zongge[1], getString(R.string.tips_zongge)));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.setResult(list);
                                editName.setEnabled(false);
                                submit.setVisibility(View.GONE);
                                divider.setBackgroundColor(editName.getCurrentTextColor());
                                dismissProgressDialog();
                            }
                        });
//                            System.out.print("此人天地人三才格参考");
//                            myLuozhuangnamewuxing.pringst(testName.getwuxji());
                    }
                });
            } else {
                ToastUtil.show(this, "请输入您的名字");
            }
        } else {
            super.onClick(view);
        }
    }

    public int getScore(String originScoreString, String jixiong) {
        int originScore = Integer.parseInt(originScoreString);
        if (originScore < 1) {
            return 99;
        }
        if (originScore > 81) {
            return 100;
        }
        if (jixiong.contains("半吉")) {
            return 75 + originScore * 3 / 16;
        } else if (jixiong.contains("凶")) {
            return 60 + originScore * 3 / 16;
        } else {
            return 90 + originScore / 8;
        }
    }

    @Override
    public View getShareContentView() {
        XingMingResultView view = (XingMingResultView) LayoutInflater.from(this).inflate(R.layout.share_xing_ming, null);
        List<Article> list = new ArrayList<>();
        list.add(Article.create(mTestName + "的姓名评分：" + mZongfen + "分"
                , "", "本结果仅供娱乐"));
        list.add(Article.create("天格，评分："
                        + tiangeScore + "分   " + tiange[2]
                , tiange[1], getString(R.string.tips_tiange)));
        list.add(Article.create("地格，评分："
                        + digeScore + "分   " + dige[2]
                , dige[1], getString(R.string.tips_dige)));
        list.add(Article.create( "人格，评分："
                        + rengeScore + "分   " + renge[2]
                , renge[1], getString(R.string.tips_renge)));
        list.add(Article.create("外格，评分："
                        + waigeScore + "分   " + waige[2]
                , waige[1], getString(R.string.tips_waige)));
        list.add(Article.create("总格，评分："
                        + zonggeScore + "分   " + zongge[2]
                , zongge[1], getString(R.string.tips_zongge)));
        view.setInfo(list);
        return view;
    }
}
