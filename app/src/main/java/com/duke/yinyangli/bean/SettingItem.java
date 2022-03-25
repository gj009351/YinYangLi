package com.duke.yinyangli.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tencent.mmkv.MMKV;

public class SettingItem implements MultiItemEntity{

    private int type;
    private String id;
    private String text;
    private String value;

    public SettingItem(int type, String id, String text, String value) {
        this.type = type;
        this.id = id;
        this.text = text;
        this.value = MMKV.defaultMMKV().decodeString(id, value);
    }

    public SettingItem(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public SettingItem(int type) {
        this.type = type;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
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
