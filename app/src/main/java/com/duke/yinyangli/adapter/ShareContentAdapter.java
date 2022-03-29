package com.duke.yinyangli.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.bean.ChengGuItem;
import com.duke.yinyangli.bean.JieGuaItem;
import com.duke.yinyangli.bean.database.Rgnm;
import com.duke.yinyangli.bean.database.Rysmn;
import com.duke.yinyangli.bean.database.ShuXiang;
import com.duke.yinyangli.calendar.Lunar;
import com.duke.yinyangli.calendar.Solar;
import com.duke.yinyangli.utils.StringUtils;
import com.haibin.calendarview.library.Article;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ShareContentAdapter extends RecyclerView.Adapter<ShareContentAdapter.ViewHolder> {

    private Context mContext;
    private List<Article> mData;

    public ShareContentAdapter(Context context) {
        mContext = context;
    }

    public void setResult(JieGuaItem item) {
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        mData.add(Article.create("解卦：" + item.getLevel() + " " + item.getDescription()
                + " " + item.getSecondName(), item.getJiegua(), 0));
        mData.add(Article.create("周易卦爻辞原文", item.getYuanwen(), 0));
        mData.add(Article.create("周易卦爻辞解文", item.getJiewen(), 0));
        mData.add(Article.create("卦爻辞注解", item.getZhujie(), 0));
        mData.add(Article.create("卦爻卦辞诗", item.getGuacishi(), 0));
        notifyDataSetChanged();
    }

    public List<Article> getShareData() {
        return mData.subList(0, 2);
    }

    public void setResult(Calendar calendar, int[] result, ChengGuItem chengGuItem) {
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        Lunar lunar = Lunar.fromDate(calendar.getTime());
        mData.add(Article.create("出生时间", lunar.getYearInChinese()
                + " " + lunar.getMonthInChinese()
                + " " + lunar.getDayInChinese()
                + " " + lunar.getTimeZhi() + "时", 0));
        mData.add(Article.create("骨重", "你的命有" + result[0] + "两" + result[1]
                + "钱\n\n", 0));
        mData.add(Article.create("称骨歌", chengGuItem.getChengguge(), 0));
        if (!TextUtils.isEmpty(chengGuItem.getZhujie())) {
            mData.add(Article.create("注解", chengGuItem.getZhujie(), 0));
        }
        notifyDataSetChanged();
    }

    public void setResult(List<Article> list) {
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        mData.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list_share_content, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.updateView(mContext, position, mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, content, tips;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            content = itemView.findViewById(R.id.tv_content);
            tips = itemView.findViewById(R.id.tv_tips);
        }

        public void updateView(Context context, int position, Article article) {
            title.setText(article.getTitle());
            if (!TextUtils.isEmpty(article.getContent())) {
                content.setText(StringUtils.getString(article.getContent()));
                content.setVisibility(View.VISIBLE);
            } else {
                content.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(article.getDesc())) {
                tips.setVisibility(View.VISIBLE);
                tips.setText(article.getDesc());
            } else {
                tips.setVisibility(View.GONE);
            }
        }
    }
}
