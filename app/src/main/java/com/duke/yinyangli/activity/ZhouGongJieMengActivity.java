package com.duke.yinyangli.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.ChooseMeng1Adapter;
import com.duke.yinyangli.base.BaseActivity;
import com.duke.yinyangli.base.BaseResultActivity;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.dialog.SimpleDialog;
import com.haibin.calendarview.library.Article;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class ZhouGongJieMengActivity extends BaseResultActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private ChooseMeng1Adapter mAdapter;
    private Article mAriticle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_zhougongjiemeng;
    }

    public static void start(Context context, Article article) {
        context.startActivity(new Intent(context, ZhouGongJieMengActivity.class)
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
    }


    @Override
    public void initData() {
        super.initData();
        mAriticle = (Article) getIntent().getSerializableExtra(Constants.INTENT_KEY.KEY_MODEL);
        title.setText(mAriticle.getTitle());

        List<Article> list = new ArrayList<>();
        list.add(Article.create("人物（父母、老师、烈士、小贩...）", "", "").setId(1));
        list.add(Article.create("情爱（春梦、告白、恋人、接吻...）", "", "").setId(2));
        list.add(Article.create("生活（表扬、惩罚、饮食、洗浴...）", "", "").setId(3));
        list.add(Article.create("物品（衣服、钱包、行李、家具...）", "", "").setId(4));
        list.add(Article.create("身体（手、脚、牙齿、头发...）", "", "").setId(5));
        list.add(Article.create("动植物（狗、蚊子、猫、老鼠...）", "", "").setId(6));
        list.add(Article.create("鬼神（忏悔、供品、祭品、朝圣...）", "", "").setId(7));
        list.add(Article.create("建筑（房子、高楼、铁路、学校...）", "", "").setId(8));
        list.add(Article.create("自然（海、江河、火、喷泉..）", "", ".").setId(9));
        mAdapter.setData(list);
        mAdapter.setOnItemClickListener(new ChooseMeng1Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Article article) {
                addTestCount(mAriticle);
                ZhouGongJieMengChildActivity.start(ZhouGongJieMengActivity.this, article);
            }
        });
    }


    @Override
    public String getAboutDialogTitle() {
        return mAriticle.getTitle();
    }

    @Override
    public String getAboutDialogContent() {
        return getString(R.string.tip_zhougongjiemeng);
    }
}
