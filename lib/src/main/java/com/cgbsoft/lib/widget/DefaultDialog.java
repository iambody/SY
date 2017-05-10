package com.cgbsoft.lib.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;


/**
 * 默认对话框
 *
 * @author yangzonghui
 */
public abstract class DefaultDialog extends BaseDialog {
    private String content, left, right;
    private boolean showTitle = false;

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
        this(context, R.style.ios_dialog_alpha);
        this.content = content;
        this.left = left;
        this.right = right;
        if (TextUtils.isEmpty(left)){
            setSingleBtn();
        }
    }

    /**
     * 设置单个按钮
     */
    private void setSingleBtn() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_default);
        bindViews();
        init();
    }

    private void init() {
        mContent.setText(content);
        mQuxiao.setText(left);
        mQueren.setText(right);

        mQuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left();
            }
        });
        mQueren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right();
            }
        });
    }

    private TextView mContent;
    private TextView mQuxiao;
    private TextView mQueren;

    private void bindViews() {
            mContent = (TextView) findViewById(R.id.default_dialog_content);
        mQuxiao = (TextView) findViewById(R.id.default_dialog_quxiao);
        mQueren = (TextView) findViewById(R.id.default_dialog_queren);
        int identify = SPreference.getIdtentify(getContext().getApplicationContext());

        if (identify == Constant.IDS_ADVISER) {
            mQueren.setBackgroundResource(R.drawable.default_dialog_right_btn_select_adviser);
        } else {
            mQueren.setBackgroundResource(R.drawable.ios_right_btn_select_investor);
        }
        mContent.setTypeface(mContent.getTypeface(), Typeface.NORMAL);
    }

    public abstract void left();

    public abstract void right();

}
