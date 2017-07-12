package com.cgbsoft.lib.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.R;

/**
 * desc 私享云签到dialog
 * <p>
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/7/12-09:24
 */
public class HomeSignDialog extends BaseDialog implements View.OnClickListener {
    private TextView homesign_title;
    private TextView homesign_data_title;
    private TextView homesign_next_title;
    private ImageView homesign_cancle_iv;

    private Context pContext;
    private View baseView;

    public HomeSignDialog(Context pContext) {
        super(pContext, R.style.dialog_comment_style);
        this.pContext = pContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseView = LayoutInflater.from(pContext).inflate(R.layout.dialog_home_sign, null);
        setContentView(baseView);
        initView();
    }

    private void initView() {
        initfindview();
        initConfig();
    }

    /**
     *
     */
    private void initfindview() {
        homesign_title = (TextView) findViewById(R.id.homesign_title);
        homesign_data_title = (TextView) findViewById(R.id.homesign_data_title);
        homesign_next_title = (TextView) findViewById(R.id.homesign_next_title);
        homesign_cancle_iv = (ImageView) findViewById(R.id.homesign_cancle_iv);
        homesign_cancle_iv.setOnClickListener(this);
    }

    private void initConfig() {
        //配置信息
        WindowManager.LayoutParams wparams = getWindow().getAttributes();
        wparams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(wparams);
        //开始初始化

        getWindow().setWindowAnimations(R.style.dialog_commont_anims_style);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.homesign_cancle_iv) {
            HomeSignDialog.this.dismiss();
        }
    }
}
