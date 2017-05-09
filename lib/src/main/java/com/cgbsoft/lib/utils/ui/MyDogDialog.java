package com.cgbsoft.lib.utils.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.widget.BaseDialog;

/**
 * 自定义的带小狗的对话框
 *
 *
 */
public abstract class MyDogDialog extends BaseDialog {
    private String title,content,left,right;

    public MyDogDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public MyDogDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 自定义的带小狗的对话框
     * @param context
     * @param title 对话框标题
     * @param content 对话框内容
     * @param left 左按钮显示内容
     * @param right 右按钮显示内容
     */
    public MyDogDialog(Context context, String title, String content, String left, String right) {
        this(context, R.style.dialog_baobei);
        this.title = title;
        this.content = content;
        this.left = "";
        this.right = right;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appupdate_dialog_c);
        bindViews();
        init();
    }

    private void init() {

        mTitle.setText(title);
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

    private TextView mTitle;
    private TextView mContent;
    private TextView mQuxiao;
    private TextView mQueren;

    private void bindViews() {

        mTitle = (TextView) findViewById(R.id.title);
        mContent = (TextView) findViewById(R.id.content);
        mQuxiao = (TextView) findViewById(R.id.quxiao);
        mQueren = (TextView) findViewById(R.id.queren);
    }


    public abstract void left();

    public abstract void right();

}
