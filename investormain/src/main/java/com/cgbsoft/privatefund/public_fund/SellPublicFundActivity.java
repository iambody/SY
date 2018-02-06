package com.cgbsoft.privatefund.public_fund;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by wangpeng on 18-1-29.
 * <p>
 * 卖出公募基金
 */
@Route(RouteConfig.GOTO_PUBLIC_FUND_REDEMPTION)
public class SellPublicFundActivity extends BaseActivity<SellPUblicFundPresenter> implements View.OnClickListener {
    //在进入赎回页面时候需要传进Intent()的参数的key
    public static  final  String Tag_PARAM="tag_param";
    private Button sellFinsh;
    private EditText input;
    private TextView prompt;

    private PayPasswordDialog payPasswordDialog;
    private String fundName; // 基金名字
    private String unit = "份"; // 基金份额单位

    private String custno;
    private String fundcode;
    private String largeredemptionflag;
    private String branchcode;
    private String fastredeemflag; // 1 快速赎回
    private String tano; // TA 代码
    private String availbal; //
    private boolean isFund; // 死否是私享宝
    private String issxb = "";
    private String transactionaccountid; //
    private String limitMoney = ""; //
    @Override
    protected int layoutID() {
        return R.layout.activity_sell_publicfund;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        String data = getIntent().getStringExtra(Tag_PARAM);

        try {
            JSONObject jsonObject =  new JSONObject(data);
            fundcode = jsonObject.getString("fundcode");
            fundName = jsonObject.getString("fundname");
            transactionaccountid = jsonObject.getString("transactionaccountid");
            largeredemptionflag = jsonObject.getString("largeredemptionflag");
            branchcode = jsonObject.getString("branchcode");
            tano = jsonObject.getString("tano");
            availbal = jsonObject.getString("availbal");
            fastredeemflag = jsonObject.getString("fastredeemflag");
            issxb = jsonObject.getString("issxb");
            limitMoney = jsonObject.getString("limitMoney");
           if("1".equals(issxb)){
                isFund = true;
            };
        } catch (JSONException e) {
            e.printStackTrace();
        }

       /* {
            branchcode:branchcode,//份额托管网点编号(必填 String)[H5调取app指令的时候会传入]
                    custno:custno,//客户号(必填 String)[H5调取app指令的时候会传入]
                fundcode:fundcode,//基金代码(必填 String)[H5调取app指令的时候会传入]
                largeredemptionflag:largeredemptionflag,//巨额赎回标志0-放弃超额部分 1-继续赎回[110051](必填 String)[固定传1]
                tano:tano,//TA 代码 (必填 String)[H5调取app指令的时候会传入]
                transactionaccountid:transactionaccountid,//交易账号(必填 String)[H5调取app指令的时候会传入]
                fundname:fundname,
                availbal:availbal,
                issxb:(code == '210013') ? '0':'1'
        }*/
        prompt = (TextView) findViewById(R.id.tv_prompt);
        if(!isFund){
            unit = "元";
        }else{
            unit = "份";
        }

        // 跳转到成功页面
        // UiSkipUtils.gotoRedeemResult(this,"","","","");
        input = (EditText) findViewById(R.id.ev_sell_money_input);
        input.setHint("至少卖出"+limitMoney+unit);
        sellFinsh = (Button) findViewById(R.id.bt_finsh);
        sellFinsh.setOnClickListener(this);

        // 该表标题
        ((TextView) findViewById(R.id.title_mid)).setText("卖基金");
        // 返回键
        findViewById(R.id.title_left).setVisibility(View.VISIBLE);
        findViewById(R.id.title_left).setOnClickListener(this);

        if(!BStrUtils.isEmpty(fastredeemflag)){//
          prompt.setText("本转出为快速到账(一般两小时内)，不享受转出当天收益");
        }else {
            prompt.setText("卖出至原银行卡");
        }
    }

    @Override
    protected SellPUblicFundPresenter createPresenter() {
        return new SellPUblicFundPresenter(this,null);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_finsh:
                String inputText = new DecimalFormat("00.00").format(new BigDecimal(input.getText().toString()));
                if (BStrUtils.isEmpty(inputText)) {
                    Toast.makeText(this, "请输入卖出的基金数量", Toast.LENGTH_LONG).show();
                    return;
                }

                if (payPasswordDialog == null) {
                    payPasswordDialog = new PayPasswordDialog(this, null, fundName, inputText + unit);
                    payPasswordDialog.setmPassWordInputListener(new PayPasswordDialog.PassWordInputListener() {
                        @Override
                        public void onInputFinish(String psw) {
                            starSell(inputText,psw);
                            payPasswordDialog.dismiss();
                        }
                    });
                }
                payPasswordDialog.show();
                break;

            case R.id.title_left:// 返回键
                finish();
                break;
        }
    }


    /**
     * 开始支付
     *
     * @param payPassword
     */
    private void starSell(String money,String payPassword) {
        getPresenter().sureSell(fundcode,this.largeredemptionflag,this.transactionaccountid,branchcode,tano,
                fastredeemflag,money,payPassword,new BasePublicFundPresenter.PreSenterCallBack<String>() {
            @Override
            public void even(String result) {
                BankListOfJZSupport<String> bankListOfJZSupport = new Gson().fromJson(result, new TypeToken<BankListOfJZSupport<String>>(){}.getType());
                if (PublicFundContant.REQEUST_SUCCESS.equals(bankListOfJZSupport.getErrorCode())) { //成功
                    // 跳转到成功页面
                     String successData = bankListOfJZSupport.getDatasets().get(0);
                     UiSkipUtils.gotoRedeemResult(SellPublicFundActivity.this,issxb,money,successData);
                     finish();
                } else if (PublicFundContant.REQEUSTING.equals(bankListOfJZSupport.getErrorCode())) {// 处理中
                    Toast.makeText(SellPublicFundActivity.this, "服务器正在处理中", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SellPublicFundActivity.this, bankListOfJZSupport.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void field(String errorCode, String errorMsg) {
                MToast.makeText(SellPublicFundActivity.this,errorMsg,Toast.LENGTH_LONG);
            }
        });
    }


    private final static String FUND_CODE = "fundCode";
    private final static String FUND_NAME = "fundName";
    private final static String CURRENT_MAX_COUNT = "currentmaxcount"; // 持有当前基金的总数

    public static void startSellPublicFundActivity(Context context, String fundcode, String fundName, String currentMaxCount) {
        Intent intent = new Intent(context, SellPublicFundActivity.class);
        intent.putExtra(FUND_CODE, fundcode);
        intent.putExtra(FUND_NAME, fundName);
        intent.putExtra(CURRENT_MAX_COUNT, currentMaxCount);
        context.startActivity(intent);
    }

}
