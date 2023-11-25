package com.duke.yinyangli.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.duke.yinyangli.R;
import com.duke.yinyangli.base.BaseEvent;
import com.duke.yinyangli.bean.SettingItem;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.constants.Event;
import com.duke.yinyangli.utils.NameUtils;
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
    private static final int ITEM_TYPE_PICTURE = 3;
    private static final int ITEM_TYPE_DIVIDER = 4;

    public SettingAdapter() {
        addItemType(ITEM_TYPE_TITLE, R.layout.item_setting_list_title);
        addItemType(ITEM_TYPE_NORMAL, R.layout.item_setting_list_normal);
        addItemType(ITEM_TYPE_TOGGLE, R.layout.item_setting_list_toggle);
        addItemType(ITEM_TYPE_PICTURE, R.layout.item_setting_list_picture);
        addItemType(ITEM_TYPE_DIVIDER, R.layout.item_setting_list_divider);
    }

    public void loadSetting() {
        List<SettingItem> list = new ArrayList<>();
        list.add(new SettingItem(ITEM_TYPE_TITLE, "个人信息（仅用于分享图片）"));
        list.add(new SettingItem(ITEM_TYPE_PICTURE, Constants.SP_KEY.USER_INFO_AVATAR, "头像", ""));
        list.add(new SettingItem(ITEM_TYPE_DIVIDER));
        list.add(new SettingItem(ITEM_TYPE_NORMAL, Constants.SP_KEY.USER_INFO_NAME, "昵称", NameUtils.getRandomName()));
        list.add(new SettingItem(ITEM_TYPE_TITLE, "首页显示开关"));
        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_JRYJ, "今日宜忌", Constants.STATUS.NORMAL_SHOW));
        list.add(new SettingItem(ITEM_TYPE_DIVIDER));
        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_SCYJ, "时辰宜忌", Constants.STATUS.NORMAL_SHOW));
        list.add(new SettingItem(ITEM_TYPE_DIVIDER));
        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_JSXS, "吉神凶煞", Constants.STATUS.NORMAL_SHOW));
        list.add(new SettingItem(ITEM_TYPE_DIVIDER));
        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_XXJX, "星宿吉凶", Constants.STATUS.NORMAL_SHOW));
        list.add(new SettingItem(ITEM_TYPE_DIVIDER));
        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_PZBJ, "彭祖百忌", Constants.STATUS.NORMAL_SHOW));
//        list.add(new SettingItem(ITEM_TYPE_TITLE, "桌面插件"));
//        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_TIME, "当前时辰", true));
//        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_JRYJ, "今日宜忌", true));
//        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_SCYJ, "时辰宜忌", true));
//        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_JSXS, "吉神凶煞", true));
//        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_XXJX, "星宿吉凶", true));
//        list.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_PZBJ, "彭祖百忌", true));
//        list.add(new SettingItem(ITEM_TYPE_TOGGLE, "", "   ", false));
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
            case ITEM_TYPE_PICTURE:
                holder.setText(R.id.key, settingItem.getText());
                ImageView imageView = holder.getView(R.id.icon);
                Glide.with(imageView.getContext()).load(settingItem.getValue()).placeholder(R.mipmap.usericon).into(imageView);
                break;
            case ITEM_TYPE_NORMAL:
                holder.setText(R.id.key, settingItem.getText());
                if (Constants.SP_KEY.USER_INFO_NAME.equals(settingItem.getId())) {
                    holder.setText(R.id.value, settingItem.getValue());
                }
                break;
            case ITEM_TYPE_TOGGLE:
                holder.setText(R.id.key, settingItem.getText());
                SwitchButton toggle = holder.getView(R.id.toggle);
                if (!TextUtils.isEmpty(settingItem.getId())) {
                    toggle.setVisibility(View.VISIBLE);
                    toggle.setChecked(Constants.STATUS.NORMAL_SHOW.equals(settingItem.getValue()));
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
            case ITEM_TYPE_DIVIDER:
            default:
                break;
        }
    }
}
