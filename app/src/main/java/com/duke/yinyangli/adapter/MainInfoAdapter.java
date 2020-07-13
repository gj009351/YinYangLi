package com.duke.yinyangli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.base.BaseViewHolder;
import com.duke.yinyangli.bean.MainInfoModel;
import com.duke.yinyangli.calendar.Lunar;
import com.haibin.calendarview.library.Article;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 适配器
 * Created by huanghaibin on 2017/12/4.
 */

public class MainInfoAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String KEY_CURRENT_TIME = "KEY_CURRENT_TIME";

    private static final int TYPE_CURRENT_TIME = 0;
    private static final int TYPE_MAIN_LIST = 1;

    private final Context mContext;
    protected LayoutInflater mInflater;
    private Lunar mLunar;
    private List<MainInfoModel> mData;

    public MainInfoAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setLunar(Lunar lunar) {
        mLunar = lunar;
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        mData.add(new MainInfoModel(KEY_CURRENT_TIME, new ArrayList<>()));
        mData.add(new MainInfoModel("今日宜忌", create(0)));
        mData.add(new MainInfoModel("时辰宜忌", create(1)));
        mData.add(new MainInfoModel("吉神凶煞", create(2)));
        mData.add(new MainInfoModel("星宿吉凶", create(3)));
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_CURRENT_TIME;
        } else {
            return TYPE_MAIN_LIST;
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_MAIN_LIST) {
            return new MainListHolder(mContext, mInflater.inflate(R.layout.item_list_main_parent, parent, false));
        } else {
            return new CurrentTimeHolder(mInflater.inflate(R.layout.item_current_time, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (holder instanceof MainListHolder) {
            MainListHolder mainListHolder = (MainListHolder) holder;
            mainListHolder.updateView(mData.get(position));
        } else if (holder instanceof CurrentTimeHolder) {
            CurrentTimeHolder currentTimeHolder = (CurrentTimeHolder) holder;
            currentTimeHolder.updateView(mContext, mLunar);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static final class MainListHolder extends BaseViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;

        private MainItemAdapter mItemAdapter;

        public MainListHolder(Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            mItemAdapter = new MainItemAdapter(context);
            recyclerView.setAdapter(mItemAdapter);
        }

        public void updateView(MainInfoModel mainInfoModel) {
            title.setText(mainInfoModel.getTitle());
            mItemAdapter.refreshData(mainInfoModel.getList());
        }
    }

    static final class CurrentTimeHolder extends BaseViewHolder {

        @BindView(R.id.current_lunar_date)
        TextView currentLunarDate;
        @BindView(R.id.current_lunar_time)
        TextView currentLunarTime;
        @BindView(R.id.current_lunar_time_content)
        TextView currentLunarTimeContent;
        @BindView(R.id.current_lunar_time_description)
        TextView currentLunarTimeDescription;

        public CurrentTimeHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void updateView(Context context, Lunar lunar) {
            currentLunarDate.setText(lunar.getYearInGanZhi() + "年 "
                    + lunar.getMonthInGanZhi() + "月 "
                    + lunar.getDayInGanZhi() + "日");
            String timeGan = lunar.getTimeZhi();
            currentLunarTime.setText("当前时辰：" + timeGan + "时");
            currentLunarTimeContent.setText(lunar.getTimeZhiContent());
            currentLunarTimeDescription.setText(lunar.getTimeZhiDescription());
        }
    }

    private List<Article> create(int p) {
        List<Article> list = new ArrayList<>();
        if (p == 0) {
            list.add(Article.create("今日宜", mLunar.getDayYi(), R.mipmap.jinriyi));
            list.add(Article.create("今日忌", mLunar.getDayJi(), R.mipmap.jinriji));
        } else if (p == 1) {
            list.add(Article.create("当前时辰宜", mLunar.getTimeYi(), R.mipmap.jinriyi));
            list.add(Article.create("当前时辰忌", mLunar.getTimeJi(), R.mipmap.jinriji));
        } else if (p == 2) {
            list.add(Article.create("今日吉神", mLunar.getDayJiShen(), R.mipmap.ji));
            list.add(Article.create("今日凶煞", mLunar.getDayXiongSha(), R.mipmap.xiong));
        } else if (p == 3) {
            int image = R.mipmap.ji;
            if ("吉".equals(mLunar.getXiuLuck())) {
                image = R.mipmap.ji;
            } else {
                image = R.mipmap.xiong;
            }
            String title = "宿：" + mLunar.getXiu() + "  " + mLunar.getAnimal() + "  " + mLunar.getXiuLuck();
            list.add(Article.create(title, mLunar.getXiuSong().replace(",", ",\n")
                    .replace("，", "，\n"), image));
        }


        return list;
    }
}
