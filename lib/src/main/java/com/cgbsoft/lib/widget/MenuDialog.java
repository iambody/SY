package com.cgbsoft.lib.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.R;

/**
 * desc  菜单弹窗
 * Created by yangzonghui on 2017/5/10 22:26
 * Email:yangzonghui@simuyun.com
 *  
 */
public abstract class MenuDialog extends BaseDialog {

    //菜单选项
    private String[] args;
    private LinearLayout ll_menu_layout;
    private Context context;

    public MenuDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public MenuDialog(Context context, int theme) {
        super(context, theme);
    }

    public MenuDialog(Context context, String[] args) {
        this(context, R.style.ios_dialog_alpha);
        this.args = args;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_menu_dialog);
        init();
    }

    private void init() {
        ll_menu_layout = (LinearLayout) findViewById(R.id.ll_menu_layout);
        for (int i = 0; i < args.length; i++) {
            RelativeLayout inflate = (RelativeLayout) LayoutInflater.from(context)
                    .inflate(R.layout.item_menu_dialog, null);
            TextView tv_menu = (TextView) inflate.findViewById(R.id.set_nor);
            tv_menu.setText(args[i]);
            ll_menu_layout.addView(inflate);
            int finalI = i;
            inflate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCheck(args[finalI]);
                }
            });
        }
    }

    public abstract void onCheck(String menuStr);
}
