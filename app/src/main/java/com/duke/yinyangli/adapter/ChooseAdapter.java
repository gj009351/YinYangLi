package com.duke.yinyangli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.activity.BaZiResultActivity;
import com.duke.yinyangli.activity.ChengGuActivity;
import com.duke.yinyangli.activity.GuaResultActivity;
import com.duke.yinyangli.activity.ShengXiaoPeiDuiActivity;
import com.duke.yinyangli.activity.XingMingDaFenActivity;
import com.duke.yinyangli.activity.XingZuoMingYunActivity;
import com.duke.yinyangli.activity.XingZuoPeiDuiActivity;
import com.duke.yinyangli.activity.ZhouGongJieMengActivity;
import com.duke.yinyangli.activity.ZhuGeShenSuanActivity;
import com.duke.yinyangli.bean.TimeCount;
import com.duke.yinyangli.calendar.Solar;
import com.duke.yinyangli.calendar.util.LunarUtil;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.utils.StringUtils;
import com.duke.yinyangli.utils.ToastUtil;
import com.haibin.calendarview.library.Article;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ViewHolder> {

    private Context mContext;
    private List<Article> mData = new ArrayList<>();
    private Solar mSolar;

    public ChooseAdapter(Context context) {
        mContext = context;
        mSolar = new Solar();
        loadRes();
    }

    private void loadRes() {
        mData.add(Article.create("蓍草占卜", "", R.mipmap.zhanbushicao, R.mipmap.cao_black)
                .setType(Constants.TYPE.TYPE_CAO).setCount(3).setPrice(1));
        mData.add(Article.create("掷钱占卜", "", R.mipmap.qian, R.mipmap.qian_black)
                .setType(Constants.TYPE.TYPE_QIAN).setCount(3).setPrice(1));
        mData.add(Article.create("称骨算命", "", R.mipmap.chenggu, R.mipmap.chenggu_black)
                .setType(Constants.TYPE.TYPE_CHENGGU).setCount(1).setPrice(8.88f));
        mData.add(Article.create("生辰八字", "", R.mipmap.bazipan, R.mipmap.bazi)
                .setType(Constants.TYPE.TYPE_BAZI).setPrice(88.8f));
        mData.add(Article.create("姓名打分", "", R.mipmap.dafen, R.mipmap.dafen)
                .setType(Constants.TYPE.TYPE_XINGMING).setCount(3).setPrice(1));
        mData.add(Article.create("星座命运", "", R.mipmap.xingzuoyunshi, R.mipmap.xingzuoyunshi)
                .setType(Constants.TYPE.TYPE_XINGZUOMINGYUN).setCount(1).setPrice(8.88f));
        mData.add(Article.create("星座配对", "", R.mipmap.xingzuopeidui, R.mipmap.xingzuopeidui)
                .setType(Constants.TYPE.TYPE_XINGZUOPEIDUI).setCount(3).setPrice(1));
        mData.add(Article.create("生肖配对", "", R.mipmap.shengxiaopeidui, R.mipmap.shengxiaopeidui)
                .setType(Constants.TYPE.TYPE_SHENGXIAOPEIDUI).setCount(3).setPrice(1));
        mData.add(Article.create("诸葛神算", "", R.mipmap.zhugeshensuan, R.mipmap.zhugeshensuan)
                .setType(Constants.TYPE.TYPE_ZHUGESHENSUAN).setCount(3).setPrice(1));
        mData.add(Article.create("周公解梦", "", R.mipmap.zhougongjiemeng, R.mipmap.zhougongjiemeng)
                .setType(Constants.TYPE.TYPE_ZHOUGONGJIEMENG).setCount(3).setPrice(1));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list_choose, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = mData.get(position);
        holder.imageView.setImageResource(article.getLogoRes());
        holder.titleView.setText(article.getTitle());
        StringUtils.setTextTwoLast(mContext, holder.descView
                , article.getCount() > 0 ? "（每天限免" + article.getCount() + "次，每次" : "（每次"
                , Float.toString(article.getPrice()), "元）", R.color.red_D81B60);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeCount timeCount = MMKV.defaultMMKV()
                        .decodeParcelable(Constants.SP_KEY.CHOOSE_TYPE + article.getType()
                                , TimeCount.class);
                if (timeCount != null && timeCount.getYear() == mSolar.getYear()
                        && timeCount.getMonth() == mSolar.getMonth()
                        && timeCount.getDay() == mSolar.getDay()
                        && timeCount.getCount() >= article.getCount()) {
                    ToastUtil.show(mContext, "您今日该项测算服务免费次数已用光，请先前往付费");
                    return;
                }
                switch (article.getType()) {
                    case Constants.TYPE.TYPE_CAO:
                    case Constants.TYPE.TYPE_QIAN:
                        //占卜结果
                        GuaResultActivity.start(mContext, article);
                        break;
                    case Constants.TYPE.TYPE_CHENGGU:
                        //称骨算命
                        ChengGuActivity.start(mContext, article);
                        break;
                    case Constants.TYPE.TYPE_BAZI:
                        //八字算命
                        BaZiResultActivity.start(mContext, article);
                        break;
                    case Constants.TYPE.TYPE_XINGMING:
                        //姓名打分
                        XingMingDaFenActivity.start(mContext, article);
                        break;
                    case Constants.TYPE.TYPE_XINGZUOMINGYUN:
                        //星座命运
                        XingZuoMingYunActivity.start(mContext, article);
                        break;
                    case Constants.TYPE.TYPE_XINGZUOPEIDUI:
                        //星座配对
                        XingZuoPeiDuiActivity.start(mContext, article);
                        break;
                    case Constants.TYPE.TYPE_SHENGXIAOPEIDUI:
                        //生肖配对
                        ShengXiaoPeiDuiActivity.start(mContext, article);
                        break;
                    case Constants.TYPE.TYPE_ZHUGESHENSUAN:
                        //诸葛神算
                        ZhuGeShenSuanActivity.start(mContext, article);
                        break;
                    case Constants.TYPE.TYPE_ZHOUGONGJIEMENG:
                        //周公解梦
                        ZhouGongJieMengActivity.start(mContext, article);
                        break;
                    default:
                        ToastUtil.show(mContext, R.string.wait_open);
                        break;
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView titleView;
        private TextView descView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            titleView = itemView.findViewById(R.id.item_title);
            descView = itemView.findViewById(R.id.item_desc);
        }
    }
}
