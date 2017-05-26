package com.cgbsoft.lib.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.R;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/15-11:39
 */
public abstract class CommentDialog  extends BaseDialog{

    private EditText comment;
    private TextView cancel;
    private TextView send;
    private TextView editListenerHint;

    public CommentDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CommentDialog(Context context, int theme) {
        super(context, R.style.dialog_comment);
    }

    public CommentDialog(Context context) {
        super(context, R.style.dialog_comment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.comment_dialog);
        bindViews();
    }

    private void bindViews() {
        comment = (EditText) findViewById(R.id.comment);
        cancel = (TextView) findViewById(R.id.cancel);
        send = (TextView) findViewById(R.id.send);
        editListenerHint = (TextView) findViewById(R.id.edit_listener_hint);
        comment.requestFocus();
//        showKeyboard();

//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//SOFT_INPUT_ADJUST_PAN
        send.setEnabled(false);
        send.setTextColor(0x99999999);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                right(comment.getText().toString().trim());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                left();
            }
        });

        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String result = s.toString();
                if (result.length() >= 10) {
                    send.setEnabled(true);
                    if (AppManager.isAdViser(getContext())) {
                        send.setTextColor(0xffea1202);
                    } else  {
                        send.setTextColor(0xfff47900);
                    }
                    editListenerHint.setText(new StringBuilder().append(result.length()).append("/1000").toString());
                } else {
                    send.setEnabled(false);
                    send.setTextColor(0x99999999);
                    editListenerHint.setText("加油！还差" + (10 - result.length()) + "个字就能发表评论啦！");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void showKeyboard() {
        if(comment!=null){
            //设置可获得焦点
            comment.setFocusable(true);
            comment.setFocusableInTouchMode(true);
            //请求获得焦点
            comment.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) comment
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(comment, 0);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        left();
    }

    public abstract void left();

    public abstract void right(String extra);

}
