package com.duke.yinyangli.constants;

public class Constants {

    public static final String DB_NAME = "suanming.db";
    public static final String PACKAGE_NAME = "com.duke.yinyangli";
    public static final String APP_NAME = "yinyangli";

    public interface DEFAULTVALUE {
        Boolean TAB_SHOW = false;
        Boolean FAB_SHOW = true;
        Boolean MAIN_ITEM_SHOW = true;
    }

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

        String USER_INFO_AVATAR = "USER_INFO_AVATAR";
        String USER_INFO_NAME = "USER_INFO_NAME";
        String USER_INFO_DESCRIPTION = "USER_INFO_DESCRIPTION";

        String MAIN_SHOW_JRYJ = "MAIN_SHOW_JRYJ";
        String MAIN_SHOW_SCYJ = "MAIN_SHOW_SCYJ";
        String MAIN_SHOW_JSXS = "MAIN_SHOW_JSXS";
        String MAIN_SHOW_XXJX = "MAIN_SHOW_XXJX";
        String MAIN_SHOW_PZBJ = "MAIN_SHOW_PZBJ";

        String MAIN_SHOW_TAB_CHOOSE = "CHOOSE";
        String MAIN_SHOW_TAB_BOOK = "BOOK";
        String MAIN_SHOW_TAB_SETTING = "SETTING";

        String VIP = "VIP";
        String FAB_CHOOSE_SHOW = "FAB_CHOOSE_SHOW";

        String WIDGET_SHOW_TIME = "WIDGET_SHOW_TIME";
        String WIDGET_SHOW_JRYJ = "WIDGET_SHOW_JRYJ";
        String WIDGET_SHOW_SCYJ = "WIDGET_SHOW_SCYJ";
        String WIDGET_SHOW_JSXS = "WIDGET_SHOW_JSXS";
        String WIDGET_SHOW_XXJX = "WIDGET_SHOW_XXJX";
        String WIDGET_SHOW_PZBJ = "WIDGET_SHOW_PZBJ";

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

    public interface STATUS {
        String NORMAL_SHOW = "1";
        String DELETE_HIDE = "0";
    }

    public interface FRAGMENT {
        String TAG_MAIN = "MAIN";
        String TAG_CHOOSE = "CHOOSE";
        String TAG_BOOK = "BOOK";
        String TAG_SETTING = "SETTING";
        long ID_CALENDAR = 1001;
        long ID_CHOOSE = 1002;
        long ID_BOOK = 1003;
        long ID_SETTING = 1004;

    }
}
