package com.duke.yinyangli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.duke.yinyangli.R;

public class HomeSettingAdapter extends BaseAdapter {

    private final Context mContext;
    private String[] data;

    public HomeSettingAdapter(Context context) {
        mContext = context;
        data = context.getResources().getStringArray(R.array.setting_list);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public String getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder(convertView = LayoutInflater.from(mContext).inflate(R.layout.item_home_setting_list, parent, false));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(data[position]);
        return convertView;
    }

    public static class ViewHolder {

        TextView textView;

        public ViewHolder(View view) {
            textView = view.findViewById(R.id.text);
        }
    }
}
