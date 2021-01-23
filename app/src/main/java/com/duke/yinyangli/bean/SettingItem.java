package com.duke.yinyangli.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tencent.mmkv.MMKV;

public class SettingItem implements MultiItemEntity{

    private int type;
    private String id;
    private String text;
    private boolean value;

    public SettingItem(int type, String id, String text, boolean value) {
        this.type = type;
        this.id = id;
        this.text = text;
        this.value = MMKV.defaultMMKV().decodeBool(id, value);
    }

    public SettingItem(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
