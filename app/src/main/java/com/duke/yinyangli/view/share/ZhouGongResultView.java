package com.duke.yinyangli.view.share;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.ShareContentAdapter;
import com.haibin.calendarview.library.Article;

import java.util.List;

public class ZhouGongResultView extends RelativeLayout {

    private TextView titleView;
    private TextView contentView;

    public ZhouGongResultView(Context context) {
        super(context);
    }

    public ZhouGongResultView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ZhouGongResultView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView() {
        titleView = findViewById(R.id.share_zgjm_title);
        contentView = findViewById(R.id.content);
    }

    public void setInfo(CharSequence title, CharSequence content) {
        initView();
        titleView.setText(title);
        contentView.setText(content);
    }

}
