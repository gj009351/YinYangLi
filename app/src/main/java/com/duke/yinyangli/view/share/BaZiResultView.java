package com.duke.yinyangli.view.share;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.ShareContentAdapter;
import com.duke.yinyangli.bean.database.Rgnm;
import com.duke.yinyangli.bean.database.Rysmn;
import com.duke.yinyangli.bean.database.ShuXiang;
import com.duke.yinyangli.calendar.Lunar;
import com.duke.yinyangli.calendar.Solar;
import com.duke.yinyangli.utils.StringUtils;
import com.haibin.calendarview.library.Article;

import java.util.ArrayList;
import java.util.List;

public class BaZiResultView extends LinearLayout {

    private RecyclerView recyclerView;
    private ShareContentAdapter mAdapter;

    public BaZiResultView(Context context) {
        super(context);
    }

    public BaZiResultView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaZiResultView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter = new ShareContentAdapter(getContext()));
    }

    public void setInfo(Rgnm rgnm, Rysmn month, Rysmn day, Rysmn time, ShuXiang shuXiang, Lunar lunar, Solar solar) {
        initView();
        List<Article> list = new ArrayList<>();
        List<String> birthdays = new ArrayList<>();
        birthdays.add("公历（阳历）：" + solar.getYear() + "年 " + solar.getMonth() + "月 " + solar.getDay() + "日 " + solar.getHour() + "时");
        birthdays.add("农历（阴历）：" + lunar.getYearInChinese() + "年 " + lunar.getMonthInChinese() + "月 " + lunar.getDayInChinese() + " " + lunar.getTimeZhi2());
        list.add(Article.create("出生日期：", StringUtils.getStringFromList(birthdays, true), 0));
        list.add(Article.create("生肖：" + lunar.getYearShengXiao() ,0));
        list.add(Article.create("八字：" + StringUtils.getStringFromList(lunar.getBaZi(), false), 0));
        list.add(Article.create("五行：" + StringUtils.getStringFromList(lunar.getBaZiWuXing(),false), 0));
        list.add(Article.create("纳音：" + StringUtils.getStringFromList(lunar.getBaZiNaYin(), false), 0));
        list.add(Article.create("十二值星：" + lunar.getZhiXing(), 0));
        list.add(Article.create("四宫：" + lunar.getGong(), 0));
        list.add(Article.create("七政：" + lunar.getZheng(), 0));
        list.add(Article.create("四神兽：" + lunar.getShou(), 0));
        list.add(Article.create("十神", "干：" + lunar.getBaZiShiShenGan() + "\n支：" + lunar.getBaZiShiShenZhi(), 0));
        list.add(Article.create("日干心性", rgnm.getRgxx(), 0));
        list.add(Article.create("日干支层次", rgnm.getRgcz(), 0));
        list.add(Article.create("日干支分析", rgnm.getRgzfx(), 0));
        List<String> mingli = new ArrayList<>();
        if (month != null) {
            mingli.add(lunar.getMonthInChinese() + "生：" + month.getMingmi());
        }
        if (day != null) {
            mingli.add(lunar.getDayInChinese() + "生：" + day.getMingmi());
        }
        if (time != null) {
            mingli.add(lunar.getTimeZhi2() + "生：" + time.getMingmi());
        }
        if (mingli.size() > 0) {
            list.add(Article.create("月日时命理", mingli, 0));
        }
        mAdapter.setResult(list);
    }

}
