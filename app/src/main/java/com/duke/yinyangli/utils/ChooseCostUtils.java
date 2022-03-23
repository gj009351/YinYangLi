package com.duke.yinyangli.utils;

import com.duke.yinyangli.base.BaseEvent;
import com.duke.yinyangli.bean.TimeCount;
import com.duke.yinyangli.calendar.Solar;
import com.duke.yinyangli.constants.Config;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.constants.Event;
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

    public void addCount() {
        TimeCount timeCount = MMKV.defaultMMKV()
                .decodeParcelable(Constants.SP_KEY.COST_COUNT, TimeCount.class);
        if (timeCount != null && timeCount.getYear() == mSolar.getYear()
                && timeCount.getMonth() == mSolar.getMonth()
                && timeCount.getDay() == mSolar.getDay()) {
            //同一天
            if (timeCount.getCount() < Config.DEFAULT_MAX_COUNT) {
                timeCount.setCount(timeCount.getCount() + 1);
            }
        } else {
            timeCount = new TimeCount(mSolar.getYear(), mSolar.getMonth(), mSolar.getDay(), 1);
        }
        MMKV.defaultMMKV().encode(Constants.SP_KEY.COST_COUNT, timeCount);
        EventBus.getDefault().post(new BaseEvent(Event.CODE_COUNT_CHANGE));
        LogUtils.d("addCount:" + timeCount);
    }

    public void addPayCount() {
        TimeCount timeCount = MMKV.defaultMMKV()
                .decodeParcelable(Constants.SP_KEY.COST_COUNT, TimeCount.class);
        if (timeCount != null && timeCount.getYear() == mSolar.getYear()
                && timeCount.getMonth() == mSolar.getMonth()
                && timeCount.getDay() == mSolar.getDay()) {
            //同一天
            if (timeCount.getCount() <= Config.DEFAULT_MAX_COUNT) {
                timeCount.setCount(timeCount.getCount() - 1);
            }
        } else {
            timeCount = new TimeCount(mSolar.getYear(), mSolar.getMonth(), mSolar.getDay(), 0);
        }
        MMKV.defaultMMKV().encode(Constants.SP_KEY.COST_COUNT, timeCount);
        EventBus.getDefault().post(new BaseEvent(Event.CODE_COUNT_CHANGE));
        LogUtils.d("addPayCount:" + timeCount);
    }

    public int getTodayCount() {
        TimeCount timeCount = MMKV.defaultMMKV()
                .decodeParcelable(Constants.SP_KEY.COST_COUNT, TimeCount.class);
        LogUtils.d("getTodayCount:" + timeCount);
        if (timeCount != null && timeCount.getYear() == mSolar.getYear()
                && timeCount.getMonth() == mSolar.getMonth()
                && timeCount.getDay() == mSolar.getDay()) {
            if (timeCount.getCount() >= Config.DEFAULT_MAX_COUNT) {
                return 0;
            } else {
                return Config.DEFAULT_MAX_COUNT - timeCount.getCount();
            }
        } else {
            return Config.DEFAULT_MAX_COUNT;
        }
    }

    public boolean isVIP() {
//        return MMKV.defaultMMKV().decodeBool(Constants.SP_KEY.VIP, false);
        return true;
    }
}
