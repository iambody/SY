package com.cgbsoft.privatefund.public_fund;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cgbsoft.lib.widget.dialog.BaseDialog;
import com.cgbsoft.privatefund.R;
import com.jungly.gridpasswordview.GridPasswordView;

/**
 * Created by wangpeng on 18-1-29.
 */

public class PayPasswordDialog extends BaseDialog implements View.OnClickListener {
    private String title,summary, money;
    private PassWordInputListener mPassWordInputListener;

    private GridPasswordView passwordInput;
    private TextView close;
    public PayPasswordDialog(Context context,String title,String summary,String money) {
        super(context, R.style.dialog_alpha);
        this.title = title;
        this.summary = summary;
        this.money = money;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dilog_pay_password);
        initView();
        bindViews();
    }


    /**
     *  实例化view
     */
    private void initView() {
        passwordInput = findViewById(R.id.ev_password_input);
        close = findViewById(R.id.tv_close);
    }

    /**
     *  对view进行数据绑定和监听绑定
     */
    private void bindViews() {
        if(!TextUtils.isEmpty(title)) ((TextView) findViewById(R.id.tv_title)).setText(title);
        if(!TextUtils.isEmpty(summary)) ((TextView) findViewById(R.id.tv_summary)).setText(summary);
        if(!TextUtils.isEmpty(money)) ((TextView) findViewById(R.id.tv_money)).setText(money);
        close.setOnClickListener(this);
        //设置输入密码监听
        passwordInput.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            //正在输入密码时执行此方法
            public void onTextChanged(String psw) {
              // 密码正在输入
            }
            //输入密码完成时执行此方法
            public void onInputFinish(String psw) {
              // 密码输入完成
                if(mPassWordInputListener != null) mPassWordInputListener.onInputFinish(psw);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_close:
                dismiss();
                break;
        }
    }

    public void setmPassWordInputListener(PassWordInputListener mPassWordInputListener) {
        this.mPassWordInputListener = mPassWordInputListener;
    }

    public interface PassWordInputListener {
        void onInputFinish(String psw);
    }
}
