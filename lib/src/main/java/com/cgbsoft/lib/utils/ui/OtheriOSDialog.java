package com.cgbsoft.lib.utils.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;

import com.cgbsoft.lib.widget.IOSDialog;


/**
 * 防IOS对话框
 */
public abstract class OtheriOSDialog extends IOSDialog {

    public OtheriOSDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public OtheriOSDialog(Context context, int theme) {
        super(context, theme);
    }

    public OtheriOSDialog(Context context, String title, String content, String left, String right) {
        super(context, title, content, left, right);
    }

    public OtheriOSDialog(Context context, String title, String content, String left, String right, boolean showTitle) {
        super(context, title, content, left, right, showTitle);
    }

    public OtheriOSDialog(Context context, String title, CharSequence charSequence, String left, String right, boolean showTitle) {
        super(context, title, charSequence, left, right, showTitle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent.setGravity(Gravity.CENTER);
        initBoldText();
    }

    private void initBoldText() {
        SpannableString textSize = new SpannableString(content);
        int index = content.indexOf("\n");
        textSize.setSpan(new RelativeSizeSpan(1.2f), 0, index, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mContent.setText(textSize);
    }
}
