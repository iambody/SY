package com.cgbsoft.lib.widget;

import android.app.Service;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.R;

/**
 * Created by fei on 2017/8/11.
 */

public class SelectIndentityItem extends LinearLayout{

    private TextView titleTv;
    private CheckBox checkBox;
    public SelectIndentityItem(Context context) {
        super(context);
    }

    public SelectIndentityItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectIndentityItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.select_indentity_item, this,false);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.select_indentity_item);
        String title = t.getString(R.styleable.select_indentity_item_indentity_title);
        boolean checked = t.getBoolean(R.styleable.select_indentity_item_indentity_checked, false);
        titleTv = (TextView) view.findViewById(R.id.tv_indentity_item_title);
        checkBox = (CheckBox) view.findViewById(R.id.iv_indentity_item_right_arrow_gender);
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
        checkBox.setChecked(checked);
        t.recycle();
        addView(view);
    }
    public void setTitle(String title){
        if (TextUtils.isEmpty(title)) {
            return;
        }
        titleTv.setText(title);
    }
    public void setChecked(boolean checked){
        checkBox.setChecked(checked);
    }
}
