package com.duke.yinyangli.bean;

import com.duke.yinyangli.base.BaseSettingItem;
import com.tencent.mmkv.MMKV;

public class BooleanSettingItem extends BaseSettingItem<Boolean> {

    public BooleanSettingItem(int type, String id, String text, Boolean value) {
        super(type, id, text, value);
    }

    public BooleanSettingItem(int type, String text) {
        super(type, text);
    }

    public BooleanSettingItem(int type) {
        super(type);
    }

    @Override
    public Boolean decode(String id, Boolean value) {
        return MMKV.defaultMMKV().decodeBool(id, value);
    }
}
