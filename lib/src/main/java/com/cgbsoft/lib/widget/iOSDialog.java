package com.cgbsoft.lib.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;


/**
 * 防IOS对话框
 * @author chenlong
 */
public abstract class IOSDialog extends BaseDialog {
    private String title, content, left, right;
    private boolean showTitle = false;
    private CharSequence charSequence;
    private boolean hasChar = false;

    public IOSDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public IOSDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 自定义的带小狗的对话框
     *
     * @param context
     * @param title   对话框标题
     * @param content 对话框内容
     * @param left    左按钮显示内容
     * @param right   右按钮显示内容
     */
    public IOSDialog(Context context, String title, String content, String left, String right) {
        this(context, R.style.ios_dialog_alpha);
        this.title = title;
        this.content = content;
        this.left = left;
        this.right = right;
        hasChar = false;
    }

    public IOSDialog(Context context, String title, String content, String left, String right, boolean showTitle) {
        this(context, R.style.ios_dialog_alpha);
        this.showTitle = showTitle;
        this.title = title;
        this.content = content;
        this.left = left;
        this.right = right;
        hasChar = false;
    }

    public IOSDialog(Context context, String title, CharSequence charSequence, String left, String right, boolean showTitle) {
        this(context, R.style.ios_dialog_alpha);
        this.showTitle = showTitle;
        this.title = title;
        this.charSequence = charSequence;
        this.left = left;
        this.right = right;
        hasChar = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_ios);
        bindViews();
        init();
    }

    private void init() {
        mTitle.setText(title);
        if (hasChar) {
            mContent.setText(charSequence);
        } else {
            mContent.setText(content);
        }
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

    private TextView mTitle;
    private TextView mContent;
    private TextView mQuxiao;
    private TextView mQueren;

    private void bindViews() {

        mTitle = (TextView) findViewById(R.id.title);
        mContent = (TextView) findViewById(R.id.content);
        mQuxiao = (TextView) findViewById(R.id.quxiao);
        mQueren = (TextView) findViewById(R.id.queren);
        int identify = SPreference.getIdtentify(getContext().getApplicationContext());

        if (identify == Constant.IDS_ADVISER) {
            mQueren.setBackgroundResource(R.drawable.ios_right_btn_select_adviser);
        } else {
            mQueren.setBackgroundResource(R.drawable.ios_right_btn_select_investor);
        }

        if (showTitle) {
            mTitle.setVisibility(View.VISIBLE);
            mContent.setTypeface(mContent.getTypeface(), Typeface.NORMAL);
        } else {
            mTitle.setVisibility(View.GONE);
        }
    }

    public void setContent(String content) {
        this.content = content;
        mContent.setText(content);
    }


    public abstract void left();

    public abstract void right();

}
