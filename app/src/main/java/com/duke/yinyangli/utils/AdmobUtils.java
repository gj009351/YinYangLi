package com.duke.yinyangli.utils;

public class AdmobUtils {

    private static boolean sHasInit;

    public static void setHasInit(boolean hasInit) {
        sHasInit = hasInit;
    }

    public static boolean isInit() {
        return sHasInit;
    }
}
