package com.duke.yinyangli.view.share;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.AllResultAdapter;
import com.duke.yinyangli.adapter.GuaXiangAdapter;
import com.duke.yinyangli.adapter.ShareContentAdapter;
import com.duke.yinyangli.bean.GuaXiangItem;
import com.haibin.calendarview.library.Article;

import java.util.List;

import butterknife.BindView;

public class ShareGuaResultView extends LinearLayout {

    private TextView resultView;
    private TextView resultMaster;
    private TextView resultChanged;
    private RecyclerView resultAll;
    private RecyclerView resultOriginList;
    private RecyclerView resultMasterList;
    private RecyclerView resultChangedList;

    private GuaXiangAdapter mOriginAdapter;
    private GuaXiangAdapter mMasterAdapter;
    private GuaXiangAdapter mChangedAdapter;
    private ShareContentAdapter mAllAdapter;

    public ShareGuaResultView(Context context) {
        super(context);
    }

    public ShareGuaResultView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShareGuaResultView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView() {
        resultView = findViewById(R.id.result_origin);
        resultMaster = findViewById(R.id.result_master);
        resultChanged = findViewById(R.id.result_changed);
        resultOriginList = findViewById(R.id.result_origin_list);
        resultMasterList = findViewById(R.id.result_master_list);
        resultChangedList = findViewById(R.id.result_changed_list);
        resultAll = findViewById(R.id.result_content_list);

        resultOriginList.setLayoutManager(new LinearLayoutManager(getContext()));
        resultOriginList.setAdapter(mOriginAdapter = new GuaXiangAdapter(getContext(), true));

        resultMasterList.setLayoutManager(new LinearLayoutManager(getContext()));
        resultMasterList.setAdapter(mMasterAdapter = new GuaXiangAdapter(getContext(), false));

        resultChangedList.setLayoutManager(new LinearLayoutManager(getContext()));
        resultChangedList.setAdapter(mChangedAdapter = new GuaXiangAdapter(getContext(), false));

        resultAll.setLayoutManager(new LinearLayoutManager(getContext()));
        resultAll.setAdapter(mAllAdapter = new ShareContentAdapter(getContext()));
    }

    public void setInfo(CharSequence originText, CharSequence masterText, CharSequence changedText
            , List<GuaXiangItem> originList, List<GuaXiangItem> masterList
            , List<GuaXiangItem> changedList, List<Article> contentList) {
        initView();
        resultView.setText(originText);
        resultMaster.setText(masterText);
        resultChanged.setText(changedText);
        mOriginAdapter.refreshData(originList);
        mMasterAdapter.refreshData(masterList);
        mChangedAdapter.refreshData(changedList);
        mAllAdapter.setResult(contentList);
    }

}
