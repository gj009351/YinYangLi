package com.duke.yinyangli.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.StringUtils;
import com.duke.yinyangli.R;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.utils.NameUtils;
import com.duke.yinyangli.utils.ToastUtil;
import com.tencent.mmkv.MMKV;

public class EditNameDialog extends Dialog {

    private TextView confirm;
    private TextView cancel;
    private EditText editText;

    public EditNameDialog(@NonNull Context context) {
        this(context, "", null);
    }

    public EditNameDialog(Context context, String content, OnClickListener listener) {
        super(context, R.style.dialog_style);
        initView(context, content, listener);
    }

    private void initView(Context context, String contentText, OnClickListener listener) {
        setContentView(R.layout.dialog_edit_name);

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

        TextView titleView = findViewById(R.id.title);
        titleView.setText(R.string.edit_name);

        editText = findViewById(R.id.edit_text);
        editText.setText(contentText);

        View random = findViewById(R.id.random);
        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(NameUtils.getRandomName());
            }
        });

        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(editText.getText()) || StringUtils.isTrimEmpty(editText.getText().toString())) {
                    ToastUtil.show(R.string.tip_input_name);
                    return;
                }
                dismiss();
                String name = editText.getText().toString().trim();
                MMKV.defaultMMKV().encode(Constants.SP_KEY.USER_INFO_NAME, name);
                if (listener != null) {
                    listener.onConfirm(name);
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

    }

    public EditNameDialog showCancel(boolean show) {
        if (cancel != null) {
            cancel.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public EditNameDialog setConfirmText(String confirmText) {
        if (confirm != null) {
            confirm.setText(confirmText);
        }
        return this;
    }

    public EditNameDialog setConfirmTextColor(int resId) {
        if (confirm != null) {
            confirm.setTextColor(confirm.getResources().getColor(resId));
        }
        return this;
    }
    public EditNameDialog setConfirmText(int resId) {
        if (confirm != null) {
            confirm.setText(confirm.getResources().getString(resId));
        }
        return this;
    }

    public EditNameDialog showDialog() {
        super.show();
        return this;
    }

    public static EditNameDialog init(Context context, String content, OnClickListener listener) {
        return new EditNameDialog(context, content, listener);
    }

    public interface OnClickListener{
        void onConfirm(String text);
    }
}
