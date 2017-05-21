package com.cgbsoft.lib.widget;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.dialog.BaseDialog;

/**
 * @author chenlong
 *
 * 单个对话框
 */
public abstract class DubButtonWithLinkDialog extends BaseDialog {
    private String title, content, left, right;
    private boolean showTitle = false;
    private CharSequence charSequence;
    private boolean hasChar = false;

    public DubButtonWithLinkDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public DubButtonWithLinkDialog(Context context, int theme) {
        super(context, theme);
    }

    public DubButtonWithLinkDialog(Context context, String title, String content, String left, String right) {
        this(context, R.style.dialog_alpha);
        this.title = title;
        this.content = content;
        this.left = left;
        this.right = right;
        hasChar = false;
    }

    public DubButtonWithLinkDialog(Context context, String title, String content, String left, String right, boolean showTitle) {
        this(context, R.style.dialog_alpha);
        this.showTitle = showTitle;
        this.title = title;
        this.content = content;
        this.left = left;
        this.right = right;
        hasChar = false;
    }

    public DubButtonWithLinkDialog(Context context, String title, CharSequence charSequence, String left, String right, boolean showTitle) {
        this(context, R.style.dialog_alpha);
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
        setContentView(R.layout.dialog_dub_button_with_link);
        bindViews();
        init();
    }

    private void init() {
        mTitle.setText(title);
        if (hasChar){
            mContent.setText(charSequence);
        }else {
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
//        if (TextUtils.isEmpty(identify)){
//            identify = SPSave.getInstance(getContext()).getString("proId");
//        }
        if (AppManager.isAdViser(getContext())) {
            mQueren.setBackgroundResource(R.drawable.right_btn_select_tob);
        } else {
            mQueren.setBackgroundResource(R.drawable.right_btn_select_toc);
        }

        mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.startDialgTelephone(getContext(), getContext().getResources().getString(R.string.hotline));
            }
        });
    }

    public abstract void left();

    public abstract void right();

}
