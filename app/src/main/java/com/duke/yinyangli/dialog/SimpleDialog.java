package com.duke.yinyangli.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.duke.yinyangli.R;
import com.duke.yinyangli.utils.LogUtils;

import androidx.annotation.NonNull;

public class SimpleDialog extends Dialog {

    private TextView title;
    private TextView content;
    private TextView update;
    private TextView cancel;
    private ScrollView scrollView;

    public SimpleDialog(@NonNull Context context) {
        this(context, "", "", null);
    }

    public SimpleDialog(Context context, String title, String content, OnClickListener listener) {
        super(context, R.style.dialog_style);
        initView(context, title, content, listener);
    }

    private void initView(Context context, String titleText, String contentText, OnClickListener listener) {
        setContentView(R.layout.dialog_simple);

        if (context instanceof Activity && !((Activity) context).isFinishing()) {
            Window window = getWindow();
            if (window != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                if (params != null) {
                    params.gravity = Gravity.CENTER;
                    if (contentText.length() <= 50) {
                    } else if (contentText.length() < 100) {
                        params.height = 300;
                    } else if (contentText.length() < 300) {
                        params.height = 600;
                    } else {
                        params.height = 1200;
                    }
                    window.setAttributes(params);
                }
            }
        }
        setCanceledOnTouchOutside(true);
        setCancelable(true);


        title = findViewById(R.id.title);
        title.setText(titleText);
        content = findViewById(R.id.content);
        content.setText(contentText);
        update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onConfirm();
                }
            }
        });
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        scrollView = findViewById(R.id.scrollView);
    }

    public SimpleDialog showCancel(boolean show) {
        if (cancel != null) {
            cancel.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public SimpleDialog setConfirmText(String confirmText) {
        if (update != null) {
            update.setText(confirmText);
        }
        return this;
    }

    public SimpleDialog setConfirmTextColor(int resId) {
        if (update != null) {
            update.setTextColor(update.getResources().getColor(resId));
        }
        return this;
    }
    public SimpleDialog setConfirmText(int resId) {
        if (update != null) {
            update.setText(update.getResources().getString(resId));
        }
        return this;
    }

    public SimpleDialog showDialog() {
        super.show();
        return this;
    }

    public static SimpleDialog init(Context context, String title, String content, OnClickListener listener) {
        SimpleDialog dialog = new SimpleDialog(context, title, content, listener);
        dialog.show();
        return dialog;
    }

    public static SimpleDialog show(Context context, String title, String content, OnClickListener listener) {
        SimpleDialog dialog = new SimpleDialog(context, title, content, listener);
        dialog.show();
        return dialog;
    }

    public interface OnClickListener{
        void onConfirm();
    }
}
