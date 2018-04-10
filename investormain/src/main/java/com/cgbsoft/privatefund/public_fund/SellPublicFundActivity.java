package com.cgbsoft.privatefund.public_fund;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.TrackingDataManger;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wangpeng on 18-1-29.
 * <p>
 * 卖出公募基金
 */
@Route(RouteConfig.GOTO_PUBLIC_FUND_REDEMPTION)
public class SellPublicFundActivity extends BaseActivity<SellPUblicFundPresenter> implements View.OnClickListener {
    //在进入赎回页面时候需要传进Intent()的参数的key
    public static final String Tag_PARAM = "tag_param";
    private Button sellFinsh;
    private EditText input;
    private TextView prompt;

    private String fundName; // 基金名字
    private String unit = "份"; // 基金份额单位

    private String custno;
    private String fundcode;
    private String largeredemptionflag;
    public String fundType;
    private String fastredeemflag = "0"; // 1 快速赎回
    // private String availbal = ""; //
    private boolean isFund; // 是否私享宝
    private String issxb = ""; // 1是基金　　０是盈泰钱包
    private String limitMoney = ""; //

    private List<BuyPublicFundActivity.BankCardInfo> bankcardlist;
    private TextView bankName;
    private ImageView bankIcon;
    private TextView bankTailCode;
    private TextView bankLimit;

    private BuyPublicFundActivity.BankCardInfo curruntBankCard;

    @Override
    protected int layoutID() {
        return R.layout.activity_sell_publicfund;
    }

    /**
     * {
     * branchcode:branchcode,//份额托管网点编号(必填 String)[H5调取app指令的时候会传入]
     * custno:custno,//客户号(必填 String)[H5调取app指令的时候会传入]
     * fundcode:fundcode,//基金代码(必填 String)[H5调取app指令的时候会传入]
     * largeredemptionflag:largeredemptionflag,//巨额赎回标志0-放弃超额部分 1-继续赎回[110051](必填 String)[固定传1]
     * tano:tano,//TA 代码 (必填 String)[H5调取app指令的时候会传入]
     * transactionaccountid:transactionaccountid,//交易账号(必填 String)[H5调取app指令的时候会传入]
     * fundname:fundname,
     * availbal:availbal,
     * issxb:(code == '210013') ? '0':'1'
     * }
     */
    @Override
    protected void init(Bundle savedInstanceState) {
        String data = getIntent().getStringExtra(Tag_PARAM);
     /*
     {
"fundCode":"210012",
"largeRedemptionFlag":1,
"fundName":"盈泰钱包",
"bankCardList":[
{
"branchCode":"370",
"custNo":"212",
"taNo":"21",
"transactionAccountId":"Z008A00000259",
"availBal":"12996",
"availBalMode1":"12996",
"balFund":"12996",
"balFundMode1":"12996",
"channelId":"招商银行",
"depositAcct":"6225881010120920",
"icon":"http:\/\/upload.simuyun.com\/publicfund\/bankicon\/CMB3x.png",
"background":"http:\/\/upload.simuyun.com\/publicfund\/bankicon\/CMB-bg3x.png",
"bankShortName":"招商银行",
"bankLimit":"",
"bankEnableStatus":"0"
}
],
"isSxb":"0",
"limitMoney":"0",
"fundType":"2",
"fastRedeemFlag":"1",
"singleRedeemMaxAmount":"9999999999999.00",
"dailyRedeemLimit":"9999999999999.00",
"dailySurplusAmt":"9999999999999.00",
"dailyRedeemAmt":"0.00"
}
}
      */

        try {
            JSONObject jsonObject = new JSONObject(data);
            fundcode = jsonObject.getString("fundCode");
            fundName = jsonObject.getString("fundName");
            // transactionaccountid = jsonObject.getString("transactionaccountid");
            largeredemptionflag = jsonObject.getString("largeRedemptionFlag");
            fundType = jsonObject.getString("fundType");

            // branchcode = jsonObject.getString("branchcode");
            // availbal = jsonObject.getString("availbal");
            issxb = jsonObject.getString("isSxb");
            limitMoney = jsonObject.getString("limitMoney");
            bankcardlist = new Gson().fromJson(jsonObject.getString("bankCardList"), new TypeToken<List<BuyPublicFundActivity.BankCardInfo>>() {
            }.getType());

            if ("1".equals(issxb)) {
                isFund = true;
            } else {
                isFund = false;
            }
            fastredeemflag = jsonObject.getString("fastRedeemFlag");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        prompt = (TextView) findViewById(R.id.tv_prompt);
        bankIcon = (ImageView) findViewById(R.id.iv_bank_icon);
        bankName = (TextView) findViewById(R.id.tv_bank_name);
        bankTailCode = (TextView) findViewById(R.id.tv_bank_tailcode);
        bankLimit = (TextView) findViewById(R.id.tv_bank_limit);

        sellFinsh = (Button) findViewById(R.id.bt_finsh);
        sellFinsh.setOnClickListener(this);
        input = (EditText) findViewById(R.id.ev_sell_money_input);
        // 跳转到成功页面
        // UiSkipUtils.gotoRedeemResult(this,"","","","");


        // 该表标题
        if (isFund) {
            ((TextView) findViewById(R.id.title_mid)).setText("卖出");
            findViewById(R.id.ll_fundinfo).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tv_fundname)).setText(fundName);
            ((TextView) findViewById(R.id.tv_fundcode)).setText(fundcode);
            ((TextView) findViewById(R.id.tv_sell_money)).setText("份额");

            sellFinsh.setText("确认卖出");
            input.setHint("请输入您要卖出的份额");
        } else {
            ((TextView) findViewById(R.id.title_mid)).setText("盈泰钱包");
            findViewById(R.id.ll_fundinfo).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tv_sell_money)).setText("提现金额");

            input.setHint("请输入您要提现的金额");
            sellFinsh.setText("确认提现");
        }

        if (bankcardlist != null && bankcardlist.size() > 0) {
            curruntBankCard = bankcardlist.get(0);
            this.changeBankInfo(bankcardlist.get(0));
        }
        // 返回键
        findViewById(R.id.title_left).setVisibility(View.VISIBLE);
        findViewById(R.id.title_left).setOnClickListener(this);

        // 银行卡选择
        findViewById(R.id.rl_bank_card).setOnClickListener(this);


        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {//
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {//
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 修改按钮颜色
                if(TextUtils.isEmpty(s)){
                    sellFinsh.setBackgroundResource(R.drawable.public_fund_conrner_gray);
                    sellFinsh.setEnabled(false);
                }else {
                    sellFinsh.setBackgroundResource(R.drawable.public_fund_conrner_golden);
                    sellFinsh.setEnabled(true);
                }
                if (TextUtils.isEmpty(s) || s.equals(input.getText())) return;
                if (curruntBankCard != null && new BigDecimal(s.toString()).subtract(new BigDecimal(curruntBankCard.getAvailBalMode1())).doubleValue() > 0) {
                    if(isFund){
                        MToast.makeText(SellPublicFundActivity.this,"该卡最大可卖出"+curruntBankCard.getAvailBalMode1()+"份",Toast.LENGTH_LONG).show();
                    }else {
                        MToast.makeText(SellPublicFundActivity.this,"该卡最大可提现"+curruntBankCard.getAvailBalMode1()+"元",Toast.LENGTH_LONG).show();
                    }
                    input.setText(curruntBankCard.getAvailBalMode1());
                    input.setSelection(curruntBankCard.getAvailBalMode1().length());
                }
            }
        });
    }

    @Override
    protected SellPUblicFundPresenter createPresenter() {
        return new SellPUblicFundPresenter(this, null);
    }


    /**
     * 修改显示的银行卡信息
     *
     * @param bankCardInfo
     */
    private void changeBankInfo(BuyPublicFundActivity.BankCardInfo bankCardInfo) {
        if (this.bankcardlist != null && this.bankcardlist.size() > 1) {
            findViewById(R.id.iv_direct).setBackgroundResource(R.drawable.direct_right);
        } else {
            findViewById(R.id.iv_direct).setBackgroundResource(0);
        }
        Imageload.display(SellPublicFundActivity.this, bankCardInfo.getIcon(), this.bankIcon, R.drawable.bank_icon, R.drawable.bank_icon);
        this.bankName.setText(bankCardInfo.getBankShortName());
        String bankcode = bankCardInfo.getDepositAcct();
        String tailCode = bankcode.length() > 4 ? bankcode.substring(bankcode.length() - 4) : bankcode;
        this.bankTailCode.setText(tailCode);
        if (isFund) {
            this.bankLimit.setText("可卖出份额" + bankCardInfo.getAvailBalMode1() + "份");
        } else {
            this.bankLimit.setText("可提现金额" + bankCardInfo.getAvailBalMode1() + "元");
        }
        if (isFund) {
            fastredeemflag = "0";
            prompt.setText("卖出至尾号为 " + tailCode + " 的" + bankCardInfo.getBankShortName() + "卡，具体到账时间以银行到账时间为准。");
        } else {
            fastredeemflag = "1";
            prompt.setText("转出至尾号为 " + tailCode + " 的" + bankCardInfo.getBankShortName() + "卡。\n\r·本转出为快速到账（一般两小时内），不享受转出 当天收益，以实际到账时间为准。\n\r·单次转出限额20万；单日转出限额20万。");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_finsh:
                String inputText = input.getText() == null ? "" : input.getText().toString();
                if (BStrUtils.isEmpty(inputText)) {
                    MToast.makeText(this, "请输入卖出的基金数量", Toast.LENGTH_LONG);
                    return;
                }
                BigDecimal bigDecimal = new BigDecimal(inputText);
                String money = new DecimalFormat("0.00").format(bigDecimal);
                if(isFund){
                    unit = "份";
                }else {
                    unit = "元";
                }
                PayPasswordDialog payPasswordDialog = new PayPasswordDialog(this, null, fundName, money + unit);
                payPasswordDialog.setmPassWordInputListener(new PayPasswordDialog.PassWordInputListener() {
                    @Override
                    public void onInputFinish(String psw) {
                        starSell(money, psw);
                        payPasswordDialog.dismiss();
                    }

                    @Override
                    public void onForgetPassWord() {
                        NavigationUtils.startActivityByRouter(SellPublicFundActivity.this, RouteConfig.GOTO_PUBLIC_FUND_TRADE_PWD_MODIFY_ACTIVITY);
                }


                });
                payPasswordDialog.show();
                break;

            case R.id.rl_bank_card:
                if (curruntBankCard == null || bankcardlist == null || bankcardlist.size() <= 1)
                    return;
                new SellFundBankSelectDialog(this, curruntBankCard.getDepositAcct(), bankcardlist, isFund, new PayFundBankSelectDialog.SelectListener() {
                    @Override
                    public void select(int index) {
                        Log.e(this.getClass().getSimpleName(), "选择银行卡" + index);
                        if (index >= 0) {
                            BuyPublicFundActivity.BankCardInfo bankCardInfo = bankcardlist.get(index);
                            if (bankCardInfo == null) return;
                            curruntBankCard = bankCardInfo;
                            changeBankInfo(curruntBankCard);
                        }
                    }
                }).show();
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
    private void starSell(String money, String payPassword) {
        if (curruntBankCard == null) return;
        LoadingDialog loadingDialog = LoadingDialog.getLoadingDialog(this, "正在交易", false, false);
        getPresenter().sureSell(fundcode, this.largeredemptionflag, this.curruntBankCard.getTransactionAccountId(), this.curruntBankCard.getBranchCode(),
                fastredeemflag, money, payPassword, new BasePublicFundPresenter.PreSenterCallBack<String>() {

                    @Override
                    public void even(String result) {
                        loadingDialog.dismiss();

                        String serialNo = "";
                        if(!TextUtils.isEmpty(result)) {
                            try {
                                serialNo = new JSONObject(result).getString("serialNo");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        TrackingDataManger.sellPublicFund(SellPublicFundActivity.this, SellPublicFundActivity.this.fundName);
                        // 跳转到成功页面
                        if (isFund) {
                            UiSkipUtils.gotoNewFundResult(SellPublicFundActivity.this, 2, fundType, money,serialNo);
                        } else {
                            UiSkipUtils.gotoRedeemResult(SellPublicFundActivity.this, issxb, money, result);
                        }

                        finish();
                      /*
                        BankListOfJZSupport bankListOfJZSupport = new Gson().fromJson(result, BankListOfJZSupport.class);
                        if (PublicFundContant.REQEUST_SUCCESS.equals(bankListOfJZSupport.getErrorCode())) { //成功
                            TrackingDataManger.sellPublicFund(SellPublicFundActivity.this, SellPublicFundActivity.this.fundName);
                         *//*   String successData = "";
                            try {
                                successData = new JSONObject(result).getString("datasets");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*//*
                            // 跳转到成功页面
                            if (isFund) {
                                UiSkipUtils.gotoNewFundResult(SellPublicFundActivity.this, 2, fundType, money);
                            } else {
                                UiSkipUtils.gotoRedeemResult(SellPublicFundActivity.this, issxb, money, result);
                            }

                            finish();
                        } else if (PublicFundContant.REQEUSTING.equals(bankListOfJZSupport.getErrorCode())) {// 处理中
                            Toast.makeText(SellPublicFundActivity.this, "服务器正在处理中", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SellPublicFundActivity.this, bankListOfJZSupport.getErrorMessage(), Toast.LENGTH_LONG).show();
                        }*/
                    }

                    @Override
                    public void field(String errorCode, String errorMsg) {
                        loadingDialog.dismiss();
                        Log.e("赎回页面", " 网络异常 " + errorMsg);
                        MToast.makeText(SellPublicFundActivity.this, "交易失败", Toast.LENGTH_LONG);
                    }
                });
        loadingDialog.show();
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
