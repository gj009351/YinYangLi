package com.duke.yinyangli.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.duke.yinyangli.R;
import com.duke.yinyangli.base.BaseEvent;
import com.duke.yinyangli.base.BaseSettingItem;
import com.duke.yinyangli.bean.BooleanSettingItem;
import com.duke.yinyangli.bean.DefaultSettingItem;
import com.duke.yinyangli.bean.StringSettingItem;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.constants.Event;
import com.duke.yinyangli.utils.NameUtils;
import com.duke.yinyangli.utils.SettingUtil;
import com.suke.widget.SwitchButton;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SettingAdapter extends BaseMultiItemQuickAdapter<BaseSettingItem, BaseViewHolder> {

    private static final int ITEM_TYPE_TITLE = 0;
    private static final int ITEM_TYPE_NORMAL = 1;
    private static final int ITEM_TYPE_TOGGLE = 2;
    private static final int ITEM_TYPE_DIVIDER = 3;
    private ArrayList<BaseSettingItem> mData = new ArrayList<>();

    public SettingAdapter() {
        addItemType(ITEM_TYPE_TITLE, R.layout.item_setting_list_title);
        addItemType(ITEM_TYPE_NORMAL, R.layout.item_setting_list_normal);
        addItemType(ITEM_TYPE_TOGGLE, R.layout.item_setting_list_toggle);
        addItemType(ITEM_TYPE_DIVIDER, R.layout.item_setting_list_divider);
    }

    public void loadSetting() {
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        mData.add(new StringSettingItem(ITEM_TYPE_TITLE, "个人信息（仅用于分享占卜结果时生成图片）"));
        mData.add(new DefaultSettingItem(ITEM_TYPE_DIVIDER));
        mData.add(new StringSettingItem(ITEM_TYPE_NORMAL, Constants.SP_KEY.USER_INFO_NAME, "昵称", NameUtils.getRandomName()));
        mData.add(new StringSettingItem(ITEM_TYPE_TITLE, "底部TAB显示开关"));
        mData.add(new BooleanSettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_TAB_CHOOSE, "显示选择", SettingUtil.getDefaultTabShow()));
        mData.add(new DefaultSettingItem(ITEM_TYPE_DIVIDER));
        mData.add(new BooleanSettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_TAB_BOOK, "显示书籍", SettingUtil.getDefaultTabShow()));
        mData.add(new DefaultSettingItem(ITEM_TYPE_DIVIDER));
        mData.add(new BooleanSettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_TAB_SETTING, "显示设置", SettingUtil.getDefaultTabShow()));
        mData.add(new StringSettingItem(ITEM_TYPE_TITLE, "首页显示开关"));
        mData.add(new BooleanSettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_JRYJ, "今日宜忌", SettingUtil.getDefaultMainItemShow()));
        mData.add(new DefaultSettingItem(ITEM_TYPE_DIVIDER));
        mData.add(new BooleanSettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_SCYJ, "时辰宜忌", SettingUtil.getDefaultMainItemShow()));
        mData.add(new DefaultSettingItem(ITEM_TYPE_DIVIDER));
        mData.add(new BooleanSettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_JSXS, "吉神凶煞", SettingUtil.getDefaultMainItemShow()));
        mData.add(new DefaultSettingItem(ITEM_TYPE_DIVIDER));
        mData.add(new BooleanSettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_XXJX, "星宿吉凶", SettingUtil.getDefaultMainItemShow()));
        mData.add(new DefaultSettingItem(ITEM_TYPE_DIVIDER));
        mData.add(new BooleanSettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.MAIN_SHOW_PZBJ, "彭祖百忌", SettingUtil.getDefaultMainItemShow()));
//        mData.add(new SettingItem(ITEM_TYPE_TITLE, "桌面插件"));
//        mData.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_TIME, "当前时辰", true));
//        mData.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_JRYJ, "今日宜忌", true));
//        mData.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_SCYJ, "时辰宜忌", true));
//        mData.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_JSXS, "吉神凶煞", true));
//        mData.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_XXJX, "星宿吉凶", true));
//        mData.add(new SettingItem(ITEM_TYPE_TOGGLE, Constants.SP_KEY.WIDGET_SHOW_PZBJ, "彭祖百忌", true));
//        mData.add(new SettingItem(ITEM_TYPE_TOGGLE, "", "   ", false));
        setNewInstance(mData);
    }

    public void reloadName() {
        mData.set(3, new StringSettingItem(ITEM_TYPE_NORMAL, Constants.SP_KEY.USER_INFO_NAME, "昵称", NameUtils.getRandomName()));
        notifyItemChanged(2);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getItemType();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, BaseSettingItem settingItem) {
        switch (getItemViewType(holder.getAdapterPosition())) {
            case ITEM_TYPE_TITLE:
                holder.setText(R.id.text, settingItem.getText());
                break;
            case ITEM_TYPE_NORMAL:
                holder.setText(R.id.key, settingItem.getText());
                if (Constants.SP_KEY.USER_INFO_NAME.equals(settingItem.getId())) {
                    holder.setText(R.id.value, ((StringSettingItem) settingItem).getValue());
                }
                break;
            case ITEM_TYPE_TOGGLE:
                holder.setText(R.id.key, settingItem.getText());
                SwitchButton toggle = holder.getView(R.id.toggle);
                if (!TextUtils.isEmpty(settingItem.getId())) {
                    toggle.setVisibility(View.VISIBLE);
                    BooleanSettingItem item = (BooleanSettingItem) settingItem;
                    toggle.setChecked(item.getValue());
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
