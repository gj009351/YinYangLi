package com.duke.yinyangli.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.duke.yinyangli.MyApplication;
import com.duke.yinyangli.R;

import java.util.Calendar;

public class DialogUtils {


    public static Dialog progress(Context context) {
        if (context == null) {
            return null;
        }
        LoadingDialog progress = new LoadingDialog(context, R.style.progressDialogTheme);
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(false);
        if (context instanceof Activity && !((Activity) context).isFinishing()) {
            progress.show();
        }
        return progress;
    }

    public static Dialog showDatePicker(Context context, OnTimeSelectListener listener) {
        Dialog dialog = null;
        Calendar start = Calendar.getInstance();
        start.set(Calendar.YEAR, 1900);
        Calendar end = Calendar.getInstance();
        TimePickerView pvTime = new TimePickerBuilder(context, listener)
                .setCancelText(" ")
                .setTitleBgColor(0xFFFAFAFC)
                .setBgColor(0xFFEEEEEE)
                .setSubmitColor(0xFF202020)
                .setRangDate(start, end)
                .setDate(end)
                .setLunarCalendar(false)
                .setType(new boolean[]{true, true, true, true, false, false})//分别对应年月日时分秒，默认全部显示
                .isCyclic(true)
                .isDialog(true)
                .build();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM);

        params.leftMargin = 0;
        params.rightMargin = 0;
        pvTime.getDialogContainerLayout().setLayoutParams(params);
        dialog = pvTime.getDialog();
        if (dialog != null) {
            Window dialogWindow = dialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
            dialog.show();
        }
        return dialog;
    }

}