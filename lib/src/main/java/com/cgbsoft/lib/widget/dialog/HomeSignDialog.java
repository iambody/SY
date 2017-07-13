package com.cgbsoft.lib.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.SpannableUtils;
import com.cgbsoft.privatefund.bean.commui.SignBean;

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

    //签到的数据
    private SignBean mysSignBean;
    private Context pContext;
    private View baseView;

    public HomeSignDialog(Context pContext, SignBean signBeans) {
        super(pContext, R.style.dialog_comment_style);
        this.pContext = pContext;
        this.mysSignBean = signBeans;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseView = LayoutInflater.from(pContext).inflate(R.layout.dialog_home_sign, null);
        setContentView(baseView);
        initView();
    }

    private void initView() {

        initConfig();
        initfindview();
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
        String award = mysSignBean.coinNum + "";
        String left = "今日奖励 ";
        String right = " 云豆";
        String content = left + award + right;
//填充数据
        SpannableString spannableString = SpannableUtils.setTextColorSize(pContext, content, left.length(), left.length() + award.length(), R.color.app_golden, 140);
        homesign_data_title.setText(spannableString);
        homesign_title.setText(String.format("尊敬的%s,欢迎来到私享云", BStrUtils.isEmpty(AppManager.getUserInfo(pContext).getRealName())?"用户":AppManager.getUserInfo(pContext).getRealName()));
        homesign_next_title.setText(String.format("明日奖励%s云豆", (mysSignBean.coinNum + 1) + ""));
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
