package com.duke.yinyangli.utils.generateImage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.duke.yinyangli.MyApplication;
import com.duke.yinyangli.R;
import com.duke.yinyangli.calendar.Lunar;
import com.duke.yinyangli.calendar.Solar;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.utils.FileUtils;
import com.duke.yinyangli.utils.StringUtils;
import com.duke.yinyangli.utils.UserInfo;
import com.duke.yinyangli.utils.core.ImageUtil;
import com.duke.yinyangli.view.VerticalTextView;
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
        TextView ym = (TextView) findViewById(R.id.ym);
        ym.setText(mSelectCalendar.getYear() + "年" + mSelectCalendar.getMonth() + "月");
        TextView date = (TextView) findViewById(R.id.date);
        date.setText(Integer.toString(mSelectCalendar.getDay()));
        TextView lunar = (TextView) findViewById(R.id.lunar);
        lunar.setText(mCurrentLunar.getMonthInChinese2() + mCurrentLunar.getDayInChinese());
        TextView lunarText = (TextView) findViewById(R.id.lunar_text);
        lunarText.setText(mCurrentLunar.getYearInGanZhi() + "年 "
                + mCurrentLunar.getMonthInGanZhi() + "月 "
                + mCurrentLunar.getDayInGanZhi() + "日");

        VerticalTextView currentTime = (VerticalTextView) findViewById(R.id.current_time);
        currentTime.setText("当前时辰：" + mCurrentLunar.getTimeZhi() + "时");
        VerticalTextView currentTimeDes = (VerticalTextView) findViewById(R.id.current_time_des);
        currentTimeDes.setText(mCurrentLunar.getTimeZhiContent());

        TextView yi = (TextView) findViewById(R.id.txt_yi);
        yi.setText(getStringFromList(mCurrentLunar.getDayYi()));
        TextView ji = (TextView) findViewById(R.id.txt_ji);
        ji.setText(getStringFromList(mCurrentLunar.getDayJi()));

        TextView taishen = (TextView) findViewById(R.id.tai_shen);
        taishen.setText(mCurrentLunar.getDayPositionTai());
        TextView xingxiu = (TextView) findViewById(R.id.xing_xiu);
        xingxiu.setText(mCurrentLunar.getXiu());
        TextView wuxing = (TextView) findViewById(R.id.wu_xing);
        wuxing.setText(getStringFromList(mCurrentLunar.getBaZiWuXing()));
        TextView chongsha = (TextView) findViewById(R.id.chong_sha);
        chongsha.setText("日冲：" + mCurrentLunar.getDayChongDesc() + "\n日煞：" + mCurrentLunar.getDaySha());

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
}
