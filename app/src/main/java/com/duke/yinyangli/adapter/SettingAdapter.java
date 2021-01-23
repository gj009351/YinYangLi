package com.duke.yinyangli.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.duke.yinyangli.R;
import com.duke.yinyangli.base.BaseEvent;
import com.duke.yinyangli.bean.SettingItem;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.constants.Event;
import com.suke.widget.SwitchButton;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SettingAdapter extends BaseMultiItemQuickAdapter<SettingItem, BaseViewHolder> {

    private static final int ITEM_TYPE_TITLE = 0;
    private static final int ITEM_TYPE_NORMAL = 1;
    private static final int ITEM_TYPE_TOGGLE = 2;

    public SettingAdapter() {
        addItemType(ITEM_TYPE_TITLE, R.layout.item_setting_list_title);
        addItemType(ITEM_TYPE_NORMAL, R.layout.item_setting_list_normal);
        addItemType(ITEM_TYPE_TOGGLE, R.layout.item_setting_list_toggle);
    }

    public void loadSetting() {
        List<SettingItem> list = new ArrayList<>();
        list.add(new SettingItem(ITEM_TYPE_TITLE, "首页"));
        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_JRYJ, "今日宜忌", true));
        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_SCYJ, "时辰宜忌", true));
        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_JSXS, "吉神凶煞", true));
        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_XXJX, "星宿吉凶", true));
        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_PZBJ, "彭祖百忌", true));
        list.add(new SettingItem(ITEM_TYPE_TITLE, "广告"));
        list.add(new SettingItem(ITEM_TYPE_NORMAL, Constants.SP_KEY.VIP, "获得永久版（去广告）", false));
        list.add(new SettingItem(ITEM_TYPE_TITLE, "桌面插件"));
        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_TIME, "当前时辰", true));
        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_JRYJ, "今日宜忌", true));
        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_SCYJ, "时辰宜忌", true));
        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_JSXS, "吉神凶煞", true));
        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_XXJX, "星宿吉凶", true));
        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_PZBJ, "彭祖百忌", true));
        list.add(new SettingItem(ITEM_TYPE_TOGGLE, "", "   ", false));
        setNewInstance(list);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getItemType();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, SettingItem settingItem) {
        switch (getItemViewType(holder.getAdapterPosition())) {
            case ITEM_TYPE_TITLE:
                holder.setText(R.id.text, settingItem.getText());
                break;
            case ITEM_TYPE_NORMAL:
                holder.setText(R.id.key, settingItem.getText());
                if (Constants.SP_KEY.VIP.equals(settingItem.getId())) {
                    if (settingItem.getValue()) {
                        holder.setText(R.id.value, "永久VIP");
                        holder.setGone(R.id.arrow, true);
                    } else {
                        holder.setText(R.id.value, "去付费");
                        holder.setVisible(R.id.arrow, true);
                    }
                }
                break;
            case ITEM_TYPE_TOGGLE:
                holder.setText(R.id.key, settingItem.getText());
                SwitchButton toggle = holder.getView(R.id.toggle);
                if (!TextUtils.isEmpty(settingItem.getId())) {
                    toggle.setVisibility(View.VISIBLE);
                    toggle.setChecked(settingItem.getValue());
                    toggle.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                            MMKV.defaultMMKV().encode(settingItem.getId(), isChecked);
                            EventBus.getDefault().post(new BaseEvent(Event.CODE_CHANGE_SETTING_MAIN));
                        }
                    });
                } else {
                    toggle.setVisibility(View.GONE);
                }
                break;
        }
    }
}
