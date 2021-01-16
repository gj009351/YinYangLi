package com.duke.yinyangli.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.duke.yinyangli.R;

import org.jetbrains.annotations.NotNull;

public class CenterListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public CenterListAdapter() {
        super(R.layout.item_dialog_center_ist);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, String text) {
        holder.setText(R.id.text, text);
        holder.setTextColor(R.id.text, getContext().getResources().getColor("取消".equals(text) ? R.color.black_666666 : R.color.black_111111));
    }

}
