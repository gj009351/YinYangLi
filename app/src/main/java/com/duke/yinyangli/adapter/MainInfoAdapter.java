package com.duke.yinyangli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.base.BaseViewHolder;
import com.duke.yinyangli.base.FooterViewHolder;
import com.duke.yinyangli.bean.MainInfoModel;
import com.duke.yinyangli.calendar.Lunar;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.utils.SettingUtil;
import com.haibin.calendarview.library.Article;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 适配器
 * Created by huanghaibin on 2017/12/4.
 */

public class MainInfoAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String KEY_CURRENT_TIME = "KEY_CURRENT_TIME";
    private static final String KEY_OTHER_DAY_INFO = "KEY_OTHER_DAY_INFO";
    private static final String KEY_BOTTOM_EMPTY_FOOTER = "KEY_BOTTOM_EMPTY_FOOTER";

    private static final int TYPE_CURRENT_TIME = 0;
    private static final int TYPE_OTHER_DAY_INFO = 1;
    private static final int TYPE_MAIN_LIST = 2;
    private static final int TYPE_BOTTOM_EMPTY_FOOTER = 3;


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
        resetData();
    }

    public void resetData() {
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        mData.add(new MainInfoModel(KEY_CURRENT_TIME, new ArrayList<>()));
        mData.add(new MainInfoModel(KEY_OTHER_DAY_INFO, new ArrayList<>()));
        if (SettingUtil.hasMainJRYJ()) {
            mData.add(new MainInfoModel("今日宜忌", create(0)));
        }
        if (SettingUtil.hasMainSCYJ()) {
            mData.add(new MainInfoModel("时辰宜忌", create(1)));
        }
        if (SettingUtil.hasMainJSXS()) {
            mData.add(new MainInfoModel("吉神凶煞", create(2)));
        }
        if (SettingUtil.hasMainXXJX()) {
            mData.add(new MainInfoModel("星宿吉凶", create(3)));
        }
        if (SettingUtil.hasMainPZBJ()) {
            mData.add(new MainInfoModel("彭祖百忌", create(4)));
        }
        mData.add(new MainInfoModel(KEY_BOTTOM_EMPTY_FOOTER, new ArrayList<>()));
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        MainInfoModel mainInfoModel = mData.get(position);
        if (KEY_CURRENT_TIME.equals(mainInfoModel.getTitle())) {
            return TYPE_CURRENT_TIME;
        } else if (KEY_OTHER_DAY_INFO.equals(mainInfoModel.getTitle())) {
            return TYPE_OTHER_DAY_INFO;
        } else if (KEY_BOTTOM_EMPTY_FOOTER.equals(mainInfoModel.getTitle())) {
            return TYPE_BOTTOM_EMPTY_FOOTER;
        } else {
            return TYPE_MAIN_LIST;
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_MAIN_LIST) {
            return new MainListHolder(mContext, mInflater.inflate(R.layout.item_list_main_parent, parent, false));
        } else if (viewType == TYPE_OTHER_DAY_INFO) {
            return new OtherDayInfoHolder(mInflater.inflate(R.layout.item_other_day_info, parent, false));
        } else if (viewType == TYPE_BOTTOM_EMPTY_FOOTER) {
            return new FooterViewHolder(mInflater.inflate(R.layout.item_footer_view, parent, false));
        } else {
            //TYPE_CURRENT_TIME
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
        } else if (holder instanceof OtherDayInfoHolder) {
            OtherDayInfoHolder dayInfoHolder = (OtherDayInfoHolder) holder;
            dayInfoHolder.updateView(mLunar);
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
            String title = "宿：" + mLunar.getXiuDirection() + mLunar.getXiu() + mLunar.getZheng() + mLunar.getAnimal() + "-" + mLunar.getXiuLuck();
            list.add(Article.create(title, mLunar.getXiuSong().replace(",", ",\n")
                    .replace("，", "，\n"), image));
        } else if (p == 4) {
            list.add(Article.create("天干百忌", mLunar.getPengZuGan(), R.mipmap.jinriji));
            list.add(Article.create("地支百忌", mLunar.getPengZuZhi(), R.mipmap.jinriji));
        }


        return list;
    }

    static final class OtherDayInfoHolder extends BaseViewHolder {

        @BindView(R.id.tv_zheng)
        TextView tvZheng;
        @BindView(R.id.tv_gong)
        TextView tvGong;
        @BindView(R.id.tv_zhixing)
        TextView tvZhixing;
        @BindView(R.id.tv_shenshou)
        TextView tvShenshou;

        public OtherDayInfoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void updateView(Lunar lunar) {
            tvZheng.setText("七政（七纬/七耀）：" + lunar.getZheng());
            tvGong.setText("四宫：" + lunar.getGong());
            tvShenshou.setText("四神兽：" + lunar.getShou());
            tvZhixing.setText("当日值星（建除十二神）：" + lunar.getZhiXing());
        }
    }
}
