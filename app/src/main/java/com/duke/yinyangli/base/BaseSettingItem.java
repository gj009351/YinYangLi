package com.duke.yinyangli.base;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tencent.mmkv.MMKV;

public abstract class BaseSettingItem<T> implements MultiItemEntity{

    private int type;
    private String id;
    private String text;
    private T value;

    public BaseSettingItem(int type, String id, String text, T value) {
        this.type = type;
        this.id = id;
        this.text = text;
        this.value = decode(id, value);
    }

    public abstract T decode(String id, T value);

    public BaseSettingItem(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public BaseSettingItem(int type) {
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

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
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
