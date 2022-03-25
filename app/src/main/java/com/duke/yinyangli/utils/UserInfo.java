package com.duke.yinyangli.utils;

import com.duke.yinyangli.constants.Constants;
import com.tencent.mmkv.MMKV;

public class UserInfo {

    public static String getUserName() {
        return MMKV.defaultMMKV().getString(Constants.SP_KEY.USER_INFO_NAME, NameUtils.getRandomName());
    }

    public static String getUserIcon() {
        return MMKV.defaultMMKV().getString(Constants.SP_KEY.USER_INFO_AVATAR, null);
    }

    public static String getUserDescription() {
        return MMKV.defaultMMKV().getString(Constants.SP_KEY.USER_INFO_DESCRIPTION, null);
    }
}
