package com.cgbsoft.lib.widget.dialog;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.R;

/**
 * 默认对话框
 *
 * @author yangzonghui
 */
public abstract class DefaultDialog extends BaseDialog {
    private String content, left, right;
    private boolean showTitle = false;
    private LinearLayout doubleBottomLayout, singleBottomLayout;

    public DefaultDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public DefaultDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 默认对话框
     *
     * @param context
     * @param content 对话框内容
     * @param left    左按钮显示内容
     * @param right   右按钮显示内容
     */
    public DefaultDialog(Context context, String content, String left, String right) {
        this(context, R.style.dialog_comment_style);
        this.content = content;
        this.left = left;
        this.right = right;
    }

    /**
     * 设置单个按钮
     */
    private void setSingleBtn() {
        singleBottomLayout.setVisibility(View.VISIBLE);
        doubleBottomLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_default);
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
        if (TextUtils.isEmpty(left)) {
            setSingleBtn();
        }
        mContent.setText(content);
        mQuxiao.setText(left);
        mQueren.setText(right);
        mQueren1.setText(right);

        mQueren1.setOnClickListener(v -> right());
        mQuxiao.setOnClickListener(v -> left());
        mQueren.setOnClickListener(v -> right());
    }

    private TextView mContent;
    private TextView mQuxiao;
    private TextView mQueren;
    private TextView mQueren1;

    private void bindViews() {
        mContent = (TextView) findViewById(R.id.default_dialog_content);
        mQuxiao = (TextView) findViewById(R.id.default_dialog_quxiao);
        mQueren = (TextView) findViewById(R.id.default_dialog_queren);
        mQueren1 = (TextView) findViewById(R.id.default_dialog_queren1);
        doubleBottomLayout = (LinearLayout) findViewById(R.id.dialog_bottom_layout);
        singleBottomLayout = (LinearLayout) findViewById(R.id.dialog_single_layout);

        mQueren.setBackgroundResource(R.drawable.ios_right_btn_select_investor);
        mContent.setTypeface(mContent.getTypeface(), Typeface.NORMAL);
    }

    public abstract void left();

    public abstract void right();

}
