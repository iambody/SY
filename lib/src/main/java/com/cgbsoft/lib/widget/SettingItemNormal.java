package com.cgbsoft.lib.widget;

import android.app.Service;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.R;

/**
 * 自定义设置页面的条目
 * Created by sunfei on 2017/6/28 0028.
 */
public class SettingItemNormal extends LinearLayout {
    private final Context context;
    private TextView titleTV;
    private TextView tipTV;
    private OnSwitchButtonChangeListener switchButtonChangeListener;
    private SwitchButton sButton;

    public SettingItemNormal(Context context) {
        this(context,null);
    }

    public SettingItemNormal(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemNormal(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.setting_item, this,false);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.setting_item);
        String title = t.getString(R.styleable.setting_item_setting_item_title);
        String tip = t.getString(R.styleable.setting_item_setting_item_tip);
        boolean showSwitchBt = t.getBoolean(R.styleable.setting_item_setting_item_show_switchbutton, false);
        titleTV = (TextView) view.findViewById(R.id.tv_setting_item_left_title);
        tipTV = (TextView) view.findViewById(R.id.tv_setting_item_right_tip);
        sButton = (SwitchButton) view.findViewById(R.id.setting_item_switchButton);
        LinearLayout right = (LinearLayout) view.findViewById(R.id.ll_setting_item_right_part);
        titleTV.setText(title);
        if (!TextUtils.isEmpty(tip)) {
            tipTV.setVisibility(View.VISIBLE);
            tipTV.setText(tip);
        }
        if (showSwitchBt) {
            right.setVisibility(View.GONE);
            sButton.setVisibility(View.VISIBLE);
            sButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (null != switchButtonChangeListener) {
                        switchButtonChangeListener.change(buttonView,isChecked);
                    }
                }
            });
        } else {
            right.setVisibility(View.VISIBLE);
            sButton.setVisibility(View.GONE);
        }
        t.recycle();
        addView(view);
    }
    public void setTitle(String title){
        tipTV.setVisibility(View.GONE);
        titleTV.setText(title);
    }

    public void setTip(String tip) {
        tipTV.setVisibility(View.VISIBLE);
        tipTV.setText(tip);
    }
    public void setTitleAndTip(String title,String tip){
        titleTV.setText(title);
        setTip(tip);
    }
    public void setTipColor(int color){
        tipTV.setTextColor(color);
    }
    public void setTitleColor(int color){
        titleTV.setTextColor(color);
    }

    public void setSwitchCheck(boolean check) {
        sButton.setChecked(check);
    }
    public void setSwitchButtonChangeListener(OnSwitchButtonChangeListener listener){
        this.switchButtonChangeListener=listener;
    }
    public interface OnSwitchButtonChangeListener{
        void change(CompoundButton buttonView, boolean isChecked);
    }
}
