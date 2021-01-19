package com.duke.yinyangli.constants;

public class Constants {

    public static final String DB_NAME = "suanming.db";
    public static final String PACKAGE_NAME = "com.duke.yinyangli";

    public interface INTENT_KEY {
        String KEY_ID = "KEY_ID";
        String KEY_MODEL = "KEY_MODEL";
    }

    public interface SP_KEY {
        String MAIN_LEFT = "MAIN_LEFT";
        String MAIN_TOP = "MAIN_TOP";
        String MAIN_RIGHT = "MAIN_RIGHT";
        String MAIN_BOTTOM = "MAIN_BOTTOM";
        String CHOOSE_TYPE = "CHOOSE_TYPE";
        String VIP = "VIP";
    }

    public interface TYPE {
        int TYPE_CAO = 0;
        int TYPE_QIAN = 1;
        int TYPE_CHENGGU = 2;
        int TYPE_BAZI = 3;
        int TYPE_XINGMING = 4;
        int TYPE_XINGZUOMINGYUN = 5;
        int TYPE_XINGZUOPEIDUI = 6;
        int TYPE_SHENGXIAOPEIDUI = 7;
        int TYPE_ZHUGESHENSUAN = 8;
        int TYPE_ZHOUGONGJIEMENG = 9;
    }
}
