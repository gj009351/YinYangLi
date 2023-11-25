package com.duke.yinyangli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.bean.GuaXiangItem;

import java.util.ArrayList;
import java.util.List;

public class GuaXiangAdapter extends RecyclerView.Adapter<GuaXiangAdapter.ViewHolder> {

    private boolean mWithText;
    private Context mContext;
    private List<GuaXiangItem> mData;

    public GuaXiangAdapter(Context context, boolean withText) {
        mContext = context;
        mWithText = withText;
    }

    public void refreshData(List<GuaXiangItem> list) {
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public List<GuaXiangItem> getData() {
        return mData;
    }

    public int getGuaXiang() {
        StringBuilder sb = new StringBuilder();
        for (GuaXiangItem item : mData) {
            sb.append(item.getYang());
        }
        return Integer.parseInt(sb.toString());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list_guaxiang, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GuaXiangItem item = mData.get(position);
        if (1 == item.getYang()) {
            holder.yang.setVisibility(View.VISIBLE);
            holder.yin.setVisibility(View.GONE);
        } else {
            holder.yang.setVisibility(View.GONE);
            holder.yin.setVisibility(View.VISIBLE);
        }
        if (mWithText) {
            if (24 == item.getValue()) {
                holder.old.setVisibility(View.VISIBLE);
                holder.old.setText("X");
            } else if (36 == item.getValue()) {
                holder.old.setVisibility(View.VISIBLE);
                holder.old.setText("O");
            } else {
                holder.old.setText("O");
                holder.old.setVisibility(View.INVISIBLE);
            }
            holder.name.setText(item.getValueName());
            holder.name.setVisibility(View.VISIBLE);
        } else {
            holder.old.setVisibility(View.GONE);
            holder.name.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View yin;
        private View yang;
        private TextView old;
        private TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            yin = itemView.findViewById(R.id.yin);
            yang = itemView.findViewById(R.id.yang);
            old = itemView.findViewById(R.id.old);
            name = itemView.findViewById(R.id.name);
        }

    }
}
