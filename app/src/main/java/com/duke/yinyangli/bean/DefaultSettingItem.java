package com.duke.yinyangli.bean;

import com.duke.yinyangli.base.BaseSettingItem;
import com.tencent.mmkv.MMKV;

public class DefaultSettingItem<T> extends BaseSettingItem<T> {


    public DefaultSettingItem(int type, String text) {
        super(type, text);
    }

    public DefaultSettingItem(int type) {
        super(type);
    }

    @Override
    public T decode(String id, T value) {
        return value;
    }
}
