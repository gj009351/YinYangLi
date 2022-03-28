package com.duke.yinyangli.view.share;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.ShareContentAdapter;
import com.haibin.calendarview.library.Article;

import java.util.List;

public class ZhuGeResultView extends LinearLayout {

    private TextView titleView;
    private RecyclerView recyclerView;
    private ShareContentAdapter mAdapter;

    public ZhuGeResultView(Context context) {
        super(context);
    }

    public ZhuGeResultView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ZhuGeResultView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView() {
        titleView = findViewById(R.id.share_zgss_title);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter = new ShareContentAdapter(getContext()));
    }

    public void setInfo(String title, List<Article> contentList) {
        initView();
        titleView.setText(title);
        mAdapter.setResult(contentList);
    }

}
