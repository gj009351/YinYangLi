package com.duke.yinyangli.utils;

import android.os.Bundle;

import com.duke.yinyangli.base.BaseEvent;
import com.duke.yinyangli.bean.TimeCount;
import com.duke.yinyangli.calendar.Solar;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.constants.Event;
import com.haibin.calendarview.library.Article;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

public class ChooseCostUtils {

    private Solar mSolar;

    public ChooseCostUtils() {
        mSolar = new Solar();
    }

    public static ChooseCostUtils getInstance() {
        return ChooseCostUtils.InstanceHolder.instance;
    }

    static class InstanceHolder {
        final static ChooseCostUtils instance = new ChooseCostUtils();
    }

    public int getOriginCostMoney(Article article) {
        switch (article.getType()) {
            case Constants.TYPE.TYPE_BAZI:
            case Constants.TYPE.TYPE_XINGZUOMINGYUN:
                return 3;
            default:
                return 10;
        }
    }

    public void addCount(Article article) {
        TimeCount timeCount = MMKV.defaultMMKV()
                .decodeParcelable(Constants.SP_KEY.CHOOSE_TYPE + article.getType()
                        , TimeCount.class);
        if (timeCount != null && timeCount.getYear() == mSolar.getYear()
                && timeCount.getMonth() == mSolar.getMonth()
                && timeCount.getDay() == mSolar.getDay()) {
            //同一天
            if (timeCount.getCount() < getOriginCostMoney(article)) {
                timeCount.setCount(timeCount.getCount() + 1);
            }
        } else {
            timeCount = new TimeCount(mSolar.getYear(), mSolar.getMonth(), mSolar.getDay(), 1);
        }
        MMKV.defaultMMKV().encode(Constants.SP_KEY.CHOOSE_TYPE + article.getType(), timeCount);
        EventBus.getDefault().post(new BaseEvent(Event.CODE_COUNT_CHANGE));
    }

    public void addPayCount(Article article) {
        TimeCount timeCount = MMKV.defaultMMKV()
                .decodeParcelable(Constants.SP_KEY.CHOOSE_TYPE + article.getType()
                        , TimeCount.class);
        if (timeCount != null && timeCount.getYear() == mSolar.getYear()
                && timeCount.getMonth() == mSolar.getMonth()
                && timeCount.getDay() == mSolar.getDay()) {
            //同一天
            if (timeCount.getCount() < getOriginCostMoney(article)) {
                timeCount.setCount(timeCount.getCount() - 1);
            }
        } else {
            timeCount = new TimeCount(mSolar.getYear(), mSolar.getMonth(), mSolar.getDay(), 0);
        }
        MMKV.defaultMMKV().encode(Constants.SP_KEY.CHOOSE_TYPE + article.getType(), timeCount);
        EventBus.getDefault().post(new BaseEvent(Event.CODE_COUNT_CHANGE));
    }

    public int getTodayCount(Article article) {
        TimeCount timeCount = MMKV.defaultMMKV()
                .decodeParcelable(Constants.SP_KEY.CHOOSE_TYPE + article.getType()
                        , TimeCount.class);
        if (timeCount != null && timeCount.getYear() == mSolar.getYear()
                && timeCount.getMonth() == mSolar.getMonth()
                && timeCount.getDay() == mSolar.getDay()) {
            if (timeCount.getCount() >= getOriginCostMoney(article)) {
                return 0;
            } else {
                return getOriginCostMoney(article) - timeCount.getCount();
            }
        } else {
            return getOriginCostMoney(article);
        }
    }

    public boolean isVIP() {
//        return MMKV.defaultMMKV().decodeBool(Constants.SP_KEY.VIP, false);
        return true;
    }
}
