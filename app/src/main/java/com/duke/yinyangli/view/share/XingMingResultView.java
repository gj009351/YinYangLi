package com.duke.yinyangli.view.share;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.ShareContentAdapter;
import com.haibin.calendarview.library.Article;

import java.util.List;

public class XingMingResultView extends LinearLayout {

    private RecyclerView recyclerView;
    private ShareContentAdapter mAdapter;

    public XingMingResultView(Context context) {
        super(context);
    }

    public XingMingResultView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public XingMingResultView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter = new ShareContentAdapter(getContext()));
    }

    public void setInfo(List<Article> contentList) {
        initView();
        mAdapter.setResult(contentList);
    }

}
