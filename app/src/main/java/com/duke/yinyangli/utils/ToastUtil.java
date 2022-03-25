package com.duke.yinyangli.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import com.duke.yinyangli.MyApplication;
import com.duke.yinyangli.R;

/**
 * UI通用方法类
 */
public class ToastUtil {

    private static final String TOAST_TYPEFACE = "sans-serif-condensed";

    private static Toast currentToast;
    //*******************************************在顶部显示********************************************


    public static void show(@NonNull CharSequence message) {
        show(MyApplication.getInstance().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void show(int resId) {
        show(MyApplication.getInstance().getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void show(@NonNull Context context, @NonNull CharSequence message) {
        if (!TextUtils.isEmpty(message)) {
            show(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void show(@NonNull Context context, int resId) {
        if (resId > 0) {
            show(context, resId, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showLong(@NonNull Context context, @NonNull int resId) {
        if (resId > 0) {
            show(context, resId, Toast.LENGTH_LONG).show();
        }
    }

    public static void showLong(@NonNull Context context, @NonNull CharSequence message) {
        if (!TextUtils.isEmpty(message)) {
            show(context, message, Toast.LENGTH_LONG).show();
        }
    }



    @CheckResult
    protected static Toast show(@NonNull Context context, int resId, int duration) {
        if (resId > 0) {
            return show(context, context.getString(resId), duration);
        }
        return null;
    }

    @CheckResult
    protected static Toast show(@NonNull Context context, @NonNull CharSequence message, int duration) {
        if (currentToast == null) {
            currentToast = new Toast(context);
        } else {
            currentToast.cancel();
            currentToast = new Toast(context);
        }
        final View toastLayout = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.toast_layout, null);
        final TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);

        toastTextView.setText(message);
        toastTextView.setTypeface(Typeface.create(TOAST_TYPEFACE, Typeface.NORMAL));

        currentToast.setView(toastLayout);
        currentToast.setGravity(Gravity.BOTTOM, 0, DisplayUtils.dp2px(context, 20));
        currentToast.setDuration(duration);
        return currentToast;
    }



}
