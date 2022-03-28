package com.duke.yinyangli.view.share;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.ShareContentAdapter;
import com.duke.yinyangli.utils.core.ImageUtil;
import com.haibin.calendarview.library.Article;

import java.util.List;

public class ShengXiaoPeiduiView extends LinearLayout {

    private RecyclerView recyclerView;
    private ShareContentAdapter mAdapter;
    ImageView mLeftImage;
    ImageView mCenterImage;
    ImageView mRightImage;

    public ShengXiaoPeiduiView(Context context) {
        super(context);
    }

    public ShengXiaoPeiduiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShengXiaoPeiduiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView() {
        mLeftImage = findViewById(R.id.image_left);
        mCenterImage = findViewById(R.id.image_center);
        mRightImage = findViewById(R.id.image_right);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter = new ShareContentAdapter(getContext()));
    }

    public void setInfo(String shengXiaoNv, String shengXiaoNan, List<Article> contentList) {
        initView();
        ImageUtil.setShuXiangImage(mLeftImage, shengXiaoNv);
        ImageUtil.setShuXiangImage(mRightImage, shengXiaoNan);
        mAdapter.setResult(contentList);
    }

}
