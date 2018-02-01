package com.cgbsoft.privatefund.public_fund;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.privatefund.R;

/**
 * Created by wangpeng on 18-1-29.
 *
 * 卖出公募基金
 */

public class SellPublicFundActivity extends BaseActivity implements View.OnClickListener{
    private Button sellFinsh;
    private EditText input;

    private PayPasswordDialog payPasswordDialog;
    private String fundName; // 基金名字
    private String unit = "份"; // 基金份额单位
    @Override
    protected int layoutID() {
        return R.layout.activity_sell_publicfund;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        input = (EditText) findViewById(R.id.ev_sell_money_input);
        sellFinsh = (Button) findViewById(R.id.bt_finsh);
        sellFinsh.setOnClickListener(this);
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_finsh:
                String inputText = input.getText().toString();
                if(BStrUtils.isEmpty(inputText)){
                    Toast.makeText(this,"请输入卖出的基金数量",Toast.LENGTH_LONG).show();
                    return;
                }
                if(payPasswordDialog == null){
                    payPasswordDialog = new PayPasswordDialog(this,null,fundName,inputText+unit);
                    payPasswordDialog.setmPassWordInputListener(new PayPasswordDialog.PassWordInputListener() {
                        @Override
                        public void onInputFinish(String psw) {
                            starPay(psw);
                            payPasswordDialog.dismiss();
                        }
                    });
                }
                payPasswordDialog.show();
                break;
        }
    }


    /**
     *  开始支付
     * @param payPassword
     */
    private void starPay(String payPassword){

    }


    private final static String FUND_CODE = "fundCode";
    private final static String FUND_NAME = "fundName";
    private final static String CURRENT_MAX_COUNT = "currentmaxcount"; // 持有当前基金的总数
    public static void startSellPublicFundActivity(Context context,String fundcode, String fundName, String currentMaxCount){
       Intent intent = new Intent(context,SellPublicFundActivity.class);
       intent.putExtra(FUND_CODE,fundcode);
       intent.putExtra(FUND_NAME,fundName);
       intent.putExtra(CURRENT_MAX_COUNT,currentMaxCount);
       context.startActivity(intent);
    }

}
