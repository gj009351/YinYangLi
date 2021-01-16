package com.duke.yinyangli.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.duke.yinyangli.R;
import com.duke.yinyangli.adapter.CenterListAdapter;

import java.util.List;

public class CenterListDialog extends Dialog {

    private TextView title;
    private TextView content;
    private CenterListAdapter mAdapter;
    private OnItemClickListener mListener;

    public CenterListDialog(Context context, String title, String content, OnItemClickListener listener) {
        super(context, R.style.dialog_style);
        initView(context, title, content, listener);
    }

    private void initView(Context context, String titleText, String contentText, OnItemClickListener listener) {
        setContentView(R.layout.dialog_center_list);

        if (context instanceof Activity && !((Activity) context).isFinishing()) {
            Window window = getWindow();
            if (window != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                if (params != null) {
                    params.gravity = Gravity.CENTER;
                    window.setAttributes(params);
                }
            }
        }
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        mListener = listener;
        title = findViewById(R.id.title);
        title.setText(titleText);
        content = findViewById(R.id.content);
        content.setText(contentText);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mAdapter = new CenterListAdapter());
        mAdapter.setOnItemClickListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });
    }

    public CenterListDialog showDialog() {
        super.show();
        return this;
    }

    public CenterListDialog setList(List<String> list) {
        mAdapter.setNewInstance(list);
        return this;
    }

    public static CenterListDialog init(Context context, String title, String content, OnItemClickListener listener) {
        return new CenterListDialog(context, title, content, listener);
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}