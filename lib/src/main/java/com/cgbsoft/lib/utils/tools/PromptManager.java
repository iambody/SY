package com.cgbsoft.lib.utils.tools;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.R;


/**
 * desc
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/3/24-11:50
 */

public class PromptManager {
    private static Toast CustomToast;

    /**
     * 显示自定义的toast
     */
    public static void ShowCustomToast(Context context, String text) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View CustomView = inflater.inflate(R.layout.toast_customtoast, null);
        TextView ToastText = (TextView) CustomView.findViewById(R.id.toast_id);
        ToastText.setText(BStrUtils.NullToStr(text));
        if (CustomToast == null) {
            CustomToast = new Toast(context);
        }
        CustomToast.setGravity(Gravity.CENTER, 0,
                DimensionPixelUtil.dip2px(context, 70));
        CustomToast.setDuration(Toast.LENGTH_SHORT);
        CustomToast.setView(CustomView);
        CustomToast.show();

    }

    /**
     * 显示自定义的toast
     */
    public static void ShowCustomLongToast(Context context, String text, int second) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View CustomView = inflater.inflate(R.layout.toast_customtoast, null);
        TextView ToastText = (TextView) CustomView.findViewById(R.id.toast_id);
        ToastText.setText(BStrUtils.NullToStr(text));
        if (CustomToast == null) {
            CustomToast = new Toast(context);
        }
        CustomToast.setGravity(Gravity.CENTER, 0,
                DimensionPixelUtil.dip2px(context, 70));

        CustomToast.setDuration(second * 1000);
        CustomToast.setView(CustomView);
        CustomToast.show();

    }


}
