package com.cgbsoft.lib.widget.dialog;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.cgbsoft.lib.R;

/**
 * desc  ${DESC}
 * author wangyongkui
 */
public abstract class BankNumberDialog extends BaseDialog {
    private String content;
    private boolean showTitle = false;


    public BankNumberDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public BankNumberDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 默认对话框
     *
     * @param context
     * @param content 对话框内容
     */
    public BankNumberDialog(Context context, String content) {
        this(context, R.style.dialog_comment_style);
        this.content = content;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_banknumber);
        bindViews();
        init();
        initConfig();

    }

    private void initConfig() {
        //配置信息
        WindowManager.LayoutParams wparams = getWindow().getAttributes();
        wparams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(wparams);
        //开始初始化

        getWindow().setWindowAnimations(R.style.dialog_commont_anims_style);
    }

    private void init() {
        mContent.setText(content);

        mQueren1.setOnClickListener(v -> affirm());


    }


    private TextView mContent;

    private TextView mQueren1;

    private void bindViews() {

        mContent = (TextView) findViewById(R.id.default_dialog_content);
        mQueren1 = (TextView) findViewById(R.id.default_dialog_queren1);


        mContent.setTypeface(mContent.getTypeface(), Typeface.NORMAL);
    }

    public abstract void affirm();


}
