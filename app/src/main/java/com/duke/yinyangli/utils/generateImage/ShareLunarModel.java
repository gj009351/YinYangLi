package com.duke.yinyangli.utils.generateImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.duke.yinyangli.MyApplication;
import com.duke.yinyangli.R;
import com.duke.yinyangli.calendar.Lunar;
import com.duke.yinyangli.calendar.Solar;
import com.duke.yinyangli.calendar.util.LunarUtil;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.utils.FileUtils;
import com.duke.yinyangli.utils.StringUtils;
import com.duke.yinyangli.utils.UserInfo;
import com.duke.yinyangli.utils.core.ImageUtil;
import com.duke.yinyangli.view.VerticalTextView;
import com.duke.yinyangli.view.itemdecoration.VerticalDividerItemDecoration;
import com.haibin.calendarview.library.Calendar;

import java.util.List;


/**
 * Created by HomgWu on 2017/11/29.
 */

public class ShareLunarModel extends GenerateModel {

    private View mSharePicView;
    private Lunar mCurrentLunar;
    private Calendar mSelectCalendar;

    public ShareLunarModel(ViewGroup rootView) {
        super(rootView);
    }

    @Override
    protected void startPrepare(GeneratePictureManager.OnGenerateListener listener) throws Exception {
        mSharePicView = LayoutInflater.from(mContext).inflate(R.layout.share_current_lunar, mRootView, false);

        ImageView dateBackground = (ImageView) findViewById(R.id.date_background);
        ImageUtil.setShuXiangImage(dateBackground, mCurrentLunar.getYearShengXiao());

        TextView ym = (TextView) findViewById(R.id.ym);
        ym.setText(mSelectCalendar.getYear() + "年" + mSelectCalendar.getMonth() + "月");
        TextView date = (TextView) findViewById(R.id.date);
        date.setText(Integer.toString(mSelectCalendar.getDay()));
        TextView lunar = (TextView) findViewById(R.id.lunar);
        lunar.setText(mCurrentLunar.getMonthInChinese2() + mCurrentLunar.getDayInChinese());
        TextView lunarText = (TextView) findViewById(R.id.lunar_text);

        lunarText.setText(mCurrentLunar.getYearInGanZhi()+"年 属"+mCurrentLunar.getYearShengXiao()+" "+mCurrentLunar.getYearNaYin() + "\n"
                + mCurrentLunar.getMonthInGanZhi()+"月 属"+mCurrentLunar.getMonthShengXiao()+" "+mCurrentLunar.getMonthNaYin() + "\n"
                + mCurrentLunar.getDayInGanZhi()+"日 属"+mCurrentLunar.getDayShengXiao()+" "+mCurrentLunar.getDayNaYin());

        VerticalTextView currentTime = (VerticalTextView) findViewById(R.id.current_time);
        currentTime.setText("当前时辰  " + mCurrentLunar.getTimeZhi() + "时");
        VerticalTextView currentTimeDes = (VerticalTextView) findViewById(R.id.current_time_des);
        currentTimeDes.setText(mCurrentLunar.getTimeZhiContent());

        TextView yi = (TextView) findViewById(R.id.txt_yi);
        yi.setText(getStringFromList(mCurrentLunar.getDayYi()));
        TextView ji = (TextView) findViewById(R.id.txt_ji);
        ji.setText(getStringFromList(mCurrentLunar.getDayJi()));

        TextView jishen = (TextView) findViewById(R.id.ji_shen);
        jishen.setText("阳贵神：" + mCurrentLunar.getPositionYangGuiDesc()
                + "\n阴贵神：" + mCurrentLunar.getPositionYinGuiDesc()
                + "\n喜神：" + mCurrentLunar.getPositionXiDesc()
                + "\n福神：" + mCurrentLunar.getDayPositionFuDesc()
                + "\n财神：" + mCurrentLunar.getPositionCaiDesc());

        TextView taishen = (TextView) findViewById(R.id.tai_shen);
        taishen.setText(mCurrentLunar.getDayPositionTai());
        TextView xingxiu = (TextView) findViewById(R.id.xing_xiu);
        xingxiu.setText(mCurrentLunar.getXiuDirection() + mCurrentLunar.getXiu() + mCurrentLunar.getZheng() + mCurrentLunar.getAnimal() + "-" + mCurrentLunar.getXiuLuck());
        TextView wuxing = (TextView) findViewById(R.id.wu_xing);
        wuxing.setText(getStringFromList(mCurrentLunar.getBaZiWuXing()));
        TextView chongsha = (TextView) findViewById(R.id.chong_sha);
        chongsha.setText(mCurrentLunar.getDayShengXiao() + "日 冲" + mCurrentLunar.getChongDesc() + "\n煞" + mCurrentLunar.getDaySha());

        TextView zhixing = (TextView) findViewById(R.id.zhi_xing);
        zhixing.setText(mCurrentLunar.getZhiXing());

        TextView pengzu1 = (TextView) findViewById(R.id.peng_zu1);
        pengzu1.setText(mCurrentLunar.getPengZuGan().substring(0, 4) + "\n" + mCurrentLunar.getPengZuGan().substring(4));
        TextView pengzu2 = (TextView) findViewById(R.id.peng_zu2);
        pengzu2.setText(mCurrentLunar.getPengZuZhi().substring(0, 4) + "\n" + mCurrentLunar.getPengZuZhi().substring(4));

        TextView jiuxing = (TextView) findViewById(R.id.jiu_xing);
        jiuxing.setText(mCurrentLunar.getDayNineStar().toFullString());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.time_jixiong);
        recyclerView.setLayoutManager(new LinearLayoutManager(getView().getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.addItemDecoration(new VerticalDividerItemDecoration.Builder(getView().getContext()).colorResId(R.color.black_343339).build());
        recyclerView.setAdapter(new TimeJiXiongAdapter(getView().getContext(), mCurrentLunar));

        ImageView qrcode = mSharePicView.findViewById(R.id.imgQrCode);
        prepared(listener);
    }

    @Override
    public View getView() {
        return mSharePicView;
    }

    @Override
    public String getSavePath() {
        return FileUtils.getTimeNameImage(getView().getContext());
    }


    private int getColor(int colorResId) {
        return getView().getResources().getColor(colorResId);
    }

    public void setLunar(Lunar currentLunar) {
        mCurrentLunar = currentLunar;
    }

    public View findViewById(int id) {
        return mSharePicView.findViewById(id);
    }

    public void setDate(Calendar calendar) {
        mSelectCalendar = calendar;
    }

    public static String getStringFromList(List<String> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && !list.isEmpty()) {
            for (String item : list) {
                sb.append(item);
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private static class TimeJiXiongAdapter extends RecyclerView.Adapter<ViewHolder> {

        private Context context;
        private java.util.Calendar calendar;

        public TimeJiXiongAdapter(Context context, Lunar currentLunar) {
            this.context = context;
            calendar = currentLunar.getSolar().getCalendar();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.share_current_lunar_time_item, parent, false);
            int width = SizeUtils.dp2px(39);
            view.getLayoutParams().width = width;
            return new ViewHolder(view, width);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            calendar.set(java.util.Calendar.HOUR_OF_DAY, position * 2);
            holder.textView.setText(LunarUtil.convertTime(position + 1) + Lunar.fromDate(calendar.getTime()).getTimeTianShenLuck());
        }

        @Override
        public int getItemCount() {
            return 12;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        VerticalTextView textView;

        public ViewHolder(@NonNull View itemView, int width) {
            super(itemView);
            textView = itemView.findViewById(R.id.time_jixiong_item);
        }
    }
}
