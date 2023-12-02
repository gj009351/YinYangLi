package com.duke.yinyangli.bean;

import com.duke.yinyangli.base.BaseSettingItem;
import com.tencent.mmkv.MMKV;

public class StringSettingItem extends BaseSettingItem<String> {

    public StringSettingItem(int type, String id, String text, String value) {
        super(type, id, text, value);
    }

    public StringSettingItem(int type, String text) {
        super(type, text);
    }

    public StringSettingItem(int type) {
        super(type);
    }

    @Override
    public String decode(String id, String value) {
        return MMKV.defaultMMKV().decodeString(id, value);
    }
}
