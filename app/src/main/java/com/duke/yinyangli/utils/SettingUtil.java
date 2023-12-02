package com.duke.yinyangli.utils;

import com.duke.yinyangli.constants.Constants;
import com.tencent.mmkv.MMKV;

public class SettingUtil {

    public static boolean hasTabChoose() {
        return MMKV.defaultMMKV().decodeBool(Constants.FRAGMENT.TAG_CHOOSE, getDefaultTabShow());
    }

    public static boolean hasFabChoose() {
        return MMKV.defaultMMKV().decodeBool(Constants.SP_KEY.FAB_CHOOSE_SHOW, getDefaultFabShow());
    }

    public static boolean hasTabBook() {
        return MMKV.defaultMMKV().decodeBool(Constants.FRAGMENT.TAG_BOOK, getDefaultTabShow());
    }

    public static boolean hasTabSetting() {
        return MMKV.defaultMMKV().decodeBool(Constants.FRAGMENT.TAG_SETTING, getDefaultTabShow());
    }

    public static boolean hasMainJRYJ() {
        return MMKV.defaultMMKV().decodeBool(Constants.SP_KEY.MAIN_SHOW_JRYJ, getDefaultMainItemShow());
    }
    public static boolean hasMainSCYJ() {
        return MMKV.defaultMMKV().decodeBool(Constants.SP_KEY.MAIN_SHOW_SCYJ, getDefaultMainItemShow());
    }
    public static boolean hasMainJSXS() {
        return MMKV.defaultMMKV().decodeBool(Constants.SP_KEY.MAIN_SHOW_JSXS, getDefaultMainItemShow());
    }
    public static boolean hasMainXXJX() {
        return MMKV.defaultMMKV().decodeBool(Constants.SP_KEY.MAIN_SHOW_XXJX, getDefaultMainItemShow());
    }
    public static boolean hasMainPZBJ() {
        return MMKV.defaultMMKV().decodeBool(Constants.SP_KEY.MAIN_SHOW_PZBJ, getDefaultMainItemShow());
    }
    
    public static Boolean getDefaultTabShow() {
        return Constants.DEFAULTVALUE.TAB_SHOW;
    }
    public static Boolean getDefaultFabShow() {
        return Constants.DEFAULTVALUE.FAB_SHOW;
    }
    public static Boolean getDefaultMainItemShow() {
        return Constants.DEFAULTVALUE.MAIN_ITEM_SHOW;
    }
}
