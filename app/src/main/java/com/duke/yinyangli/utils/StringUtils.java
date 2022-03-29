package com.duke.yinyangli.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.util.List;

public class StringUtils {

    public static String getString(String origin) {
        return origin
                .replace("&nbsp;", " ")
                .replace("<BR>", "\n")
                .replace("<p>", "")
                .replace("</p>", "")
                .replace("<br>", "")
                .replace("<br/>", "")
                .replace("<br />", "")
                .replace("</font>", "")
                .replaceAll("<font.*>", "");
    }

    public static void setTextTwoLast(Context context, TextView tv, String before, String center, String last, int color) {
        SpannableString spanString = new SpannableString(before + center + last);
        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(color))
                , before.length(), (before + center).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
        tv.setText(spanString);
    }

    public static String getStringFromList(List<String> list, boolean huanhang) {
        StringBuilder sb = new StringBuilder();
        if (list != null && !list.isEmpty()) {
            for (String item : list) {
                sb.append(item);
                sb.append(huanhang ? "\n" : " ");
            }
        }
        return sb.toString();
    }

}
