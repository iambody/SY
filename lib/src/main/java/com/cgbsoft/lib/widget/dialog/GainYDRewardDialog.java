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
 * desc 获得云豆奖励dialog
 * author sunfei
 */
public class GainYDRewardDialog extends BaseDialog implements View.OnClickListener {

    private Context pContext;
    private View baseView;

    public GainYDRewardDialog(Context pContext) {
        super(pContext, R.style.dialog_comment_style);
        this.pContext = pContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseView = LayoutInflater.from(pContext).inflate(R.layout.dialog_gain_yd_reward, null);
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
            GainYDRewardDialog.this.dismiss();
        }
    }
}
