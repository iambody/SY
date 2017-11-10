package com.cgbsoft.lib.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.utils.tools.BStrUtils;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/11/8-10:56
 */
public class AlterDialog extends BaseDialog implements View.OnClickListener {
    private String currentContent;
    private String showTitle;

    private TextView dialog_alter_title;
    private EditText contentEdittext;
    private TextView leftBt;
    private TextView rightBt;

    private AlterCommitListener altercommintListener;

    public AlterDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public AlterDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 默认对话框
     *
     * @param context
     * @param content 对话框内容
     */
    public AlterDialog(Context context, String title, String content, AlterCommitListener altercommintlistener) {
        this(context, R.style.dialog_comment);
        this.currentContent = content;
        this.showTitle = title;
        this.altercommintListener = altercommintlistener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alter);
        initConfig();
        initView();
    }

    private void initView() {
        dialog_alter_title = (TextView) findViewById(R.id.dialog_alter_title);
        contentEdittext = (EditText) findViewById(R.id.dialog_alter_contentedittext);

        leftBt = (TextView) findViewById(R.id.dialog_alter_leftbt);
        rightBt = (TextView) findViewById(R.id.dialog_alter_rightbt);
        leftBt.setOnClickListener(this);
        rightBt.setOnClickListener(this);
        BStrUtils.SetTxt1(dialog_alter_title, showTitle);
        if (!BStrUtils.isEmpty(currentContent)) {
            contentEdittext.setText(currentContent);
            contentEdittext.setSelection(currentContent.length());
        } else {
            contentEdittext.setHint("请输入相应修改的内容");
        }
    }

    private void initConfig() {
        //配置信息
        WindowManager.LayoutParams wparams = getWindow().getAttributes();
        wparams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        wparams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        wparams.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wparams);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //开始初始化
        getWindow().setWindowAnimations(R.style.dialog_commont_anims_style);
    }

    @Override
    public void onClick(View v) {
        if (R.id.dialog_alter_leftbt == v.getId()) {//取消
            AlterDialog.this.dismiss();
            altercommintListener = null;
        } else if (R.id.dialog_alter_rightbt == v.getId()) {//确定
            if (null != altercommintListener) {
                AlterDialog.this.dismiss();
                altercommintListener.commitListener(contentEdittext.getText().toString().trim());
            }
        }


    }

    public interface AlterCommitListener {
        public void commitListener(String resultContent);
    }
}
