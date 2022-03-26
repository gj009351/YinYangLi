package com.duke.yinyangli.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.duke.yinyangli.MyApplication;
import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.ChooseMeng1Adapter;
import com.duke.yinyangli.base.BaseActivity;
import com.duke.yinyangli.base.BaseResultActivity;
import com.duke.yinyangli.bean.database.DaoSession;
import com.duke.yinyangli.bean.database.Zgjm;
import com.duke.yinyangli.bean.database.ZgjmDao;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.dialog.SimpleDialog;
import com.duke.yinyangli.utils.ThreadHelper;
import com.haibin.calendarview.library.Article;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class ZhouGongJieMengChildActivity extends BaseResultActivity {

    @BindView(R.id.tips_title)
    TextView mTipTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private ChooseMeng1Adapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_zhougongjiemeng_child;
    }

    public static void start(Context context, Article article) {
        context.startActivity(new Intent(context, ZhouGongJieMengChildActivity.class)
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
        mRecyclerView.setAdapter(mAdapter = new ChooseMeng1Adapter(this));
        mAdapter.setOnItemClickListener(new ChooseMeng1Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Article article) {
                ZhouGongJieMengResultActivity.start(ZhouGongJieMengChildActivity.this, article);
            }
        });
    }


    @Override
    public void initData() {
        super.initData();
        mArticle = (Article) getIntent().getSerializableExtra(Constants.INTENT_KEY.KEY_MODEL);
        title.setText("周公解梦");
        mTipTitle.setText(mArticle.getTitle());

        ThreadHelper.INST.execute(new Runnable() {
            @Override
            public void run() {
                DaoSession daoSession = MyApplication.getInstance().getDao();
                List<Zgjm> zgjmList = daoSession.getZgjmDao().queryBuilder()
                        .where(ZgjmDao.Properties.Jmlb.eq(mArticle.getId())).list();
                List<Article> list = new ArrayList<>();
                if (zgjmList != null && zgjmList.size() > 0) {
                    for (Zgjm zgjm : zgjmList) {
                        list.add(Article.create(zgjm.getTitle(), zgjm.getContent(), "")
                                .setId(Integer.parseInt(Long.toString(zgjm.getId()))));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setData(list);
                        }
                    });
                }
            }
        });
    }

    @Override
    public String getAboutDialogContent() {
        return getString(R.string.tip_zhougongjiemeng);
    }
}
