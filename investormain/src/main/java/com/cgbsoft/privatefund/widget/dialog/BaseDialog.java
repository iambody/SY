package com.cgbsoft.privatefund.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/22-21:23
 */
public abstract class BaseDialog extends Dialog {
    protected View baseDialogView;
    protected Context dActivity;


    protected abstract int getViewResourceId();

    protected abstract void initView();

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseDialogView = LayoutInflater.from(dActivity).inflate(getViewResourceId(), null);
        setContentView(baseDialogView);
        initBase();
        initView();

    }

    private void initBase() {
        //配置信息
        WindowManager.LayoutParams wparams = getWindow().getAttributes();
        wparams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(wparams);
        //开始初始化

        getWindow().setWindowAnimations(app.privatefund.com.share.R.style.share_comment_anims_style);
    }


}
