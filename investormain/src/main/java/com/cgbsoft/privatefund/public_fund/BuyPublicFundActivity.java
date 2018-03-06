package com.cgbsoft.privatefund.public_fund;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.bean.DataDictionary;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangpeng on 18-1-29.
 */
@Route(RouteConfig.GOTO_PUBLIC_FUND_BUY)
public class BuyPublicFundActivity extends BaseActivity<BuyPublicFundPresenter> implements View.OnClickListener {
    public static final String TAG_FUND_CODE = "tag_fund_code";
    public static final String TAG_FUND_RISK_LEVEL = "tag_fund_risk_level";
    private Button buyConfirm;
    private ImageView bankIcon;
    private TextView bankName;
    private TextView bankTailCode;
    private EditText buyInput;

    private String fundRiskLevel;// 风险级别

    private String fundCode; // 基金号
    private String rate; // 费率
    private String profitDate; // 收益日期
    private String limitOfDay; //银行卡每日限额
    private String limitOfSingle; //银行卡单笔限额
    private String unit = "元"; //银行卡单笔限额

    private Bean bean;
    private List<DataDictionary> channlidDictionarys;
    private BankCardInfo currectPayBank;

    @Override
    protected int layoutID() {
        return R.layout.activity_buy_publicfund;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        fundCode = getIntent().getStringExtra(TAG_FUND_CODE);
        fundRiskLevel = getIntent().getStringExtra(TAG_FUND_RISK_LEVEL);

        buyInput = (EditText) findViewById(R.id.ev_buy_money_input);
        bankIcon = (ImageView) findViewById(R.id.iv_bank_icon);
        bankName = (TextView) findViewById(R.id.tv_bank_name);
        bankTailCode = (TextView) findViewById(R.id.tv_bank_tailcode);
        buyConfirm = (Button) findViewById(R.id.bt_Confirm);

        bindView();
    }

    /**
     * 绑定View的监听与数据
     */
    private void bindView() {
        // 该表标题
        ((TextView) findViewById(R.id.title_mid)).setText("买入");
        // 返回键
        findViewById(R.id.title_left).setVisibility(View.VISIBLE);
        findViewById(R.id.title_left).setOnClickListener(this);
        findViewById(R.id.rl_bank_card).setOnClickListener(this);

        buyInput.setHint("请输入金额");

        buyConfirm.setOnClickListener(this);

        requestData(fundCode);
    }


    @Override
    protected BuyPublicFundPresenter createPresenter() {
        return new BuyPublicFundPresenter(this, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Confirm:
                String money = buyInput.getText().toString();
                if (BStrUtils.isEmpty(money)) {
                    MToast.makeText(this, "请输入金额", Toast.LENGTH_LONG);
                    return;
                }
                if (bean == null || currectPayBank == null) {
                    Log.e(this.getClass().getSimpleName(), " 可能请求申购的数据出现了问题");
                    return;
                }
                String inputText = new DecimalFormat("0.00").format(new BigDecimal(money));
                PayPasswordDialog payPasswordDialog = new PayPasswordDialog(this, null, bean.getFundName(), inputText + unit);
                payPasswordDialog.setmPassWordInputListener(new PayPasswordDialog.PassWordInputListener() {
                    @Override
                    public void onInputFinish(String psw) {
                        starPay(money, psw);
                        payPasswordDialog.dismiss();
                    }
                });
                payPasswordDialog.show();

                break;
            case R.id.rl_bank_card: // 用于支付的银行卡
                if (bean == null) return;
                new PayFundBankSelectDialog(this, bean.getBankCardInfoList(), new PayFundBankSelectDialog.SelectListener() {
                    @Override
                    public void select(int index) {
                        Log.e(this.getClass().getSimpleName(),"选择银行卡"+ index);
                        if(index == -2){
                            Activity activity = BuyPublicFundActivity.this;
                            Intent intent = new Intent(activity,BindingBankCardOfPublicFundActivity.class);
                            intent.putExtra("Style",1);
                            activity.startActivityForResult(intent,PayFundBankSelectDialog.REQUESTCODE);
                        }
                    }
                }).show();

                break;

            case R.id.title_left: // 返回
                finish();
                break;
        }
    }

    private void requestDictionary() {
        getPresenter().requestDictionary(new BasePublicFundPresenter.PreSenterCallBack<String>() {
            @Override
            public void even(String result) {
                BankListOfJZSupport bankListOfJZSupport = new Gson().fromJson(result, BankListOfJZSupport.class);
                if (PublicFundContant.REQEUST_SUCCESS.equals(bankListOfJZSupport.getErrorCode())) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("datasets");
                        Log.i("SubbranchBankInfo", jsonArray.toString());
                        jsonArray = jsonArray.getJSONArray(0);
                        if (jsonArray != null && jsonArray.length() > 0) {
                            channlidDictionarys = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<DataDictionary>>() {
                            }.getType());
                        }
                        showBankView();
                    } catch (Exception e) {
                        loadingDialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    loadingDialog.dismiss();
                    MToast.makeText(BuyPublicFundActivity.this, bankListOfJZSupport.getErrorMessage(), Toast.LENGTH_LONG);
                }
            }

            @Override
            public void field(String errorCode, String errorMsg) {
                loadingDialog.dismiss();
            }
        });
    }

    /**
     * 获取信息
     *
     * @param fundCode "210013" "004373" 调试数据
     */

    LoadingDialog loadingDialog;

    private void requestData(String fundCode) {
        loadingDialog = LoadingDialog.getLoadingDialog(this, "加载中", false, false);
        requestDictionary();
        getPresenter().getData(fundCode, new BasePublicFundPresenter.PreSenterCallBack<String>() {
            @Override
            public void even(String o) {
                Log.e("申购信息", "" + o);
                bean = new Gson().fromJson(o, Bean.class);
                bean.setFundCode(fundCode);
                currectPayBank = bean.getBankCardInfoList().get(0);
                if(currectPayBank == null) return;
                showBankView();
            }

            @Override
            public void field(String errorCode, String errorMsg) {
                loadingDialog.dismiss();
                Log.e("申购信息", "" + errorMsg);
            }
        });

        loadingDialog.show();
    }

    /**
     * 显示支付银行
     */
    Map<String,String> dictionaryTable = null;
    private void showBankView() {
        if (bean == null || channlidDictionarys == null) return;
        if(loadingDialog != null) loadingDialog.dismiss();
        if (!BStrUtils.isEmpty(bean.getLimitOrderAmt()) && !"null".equals(bean.getLimitOrderAmt())) {
            buyInput.setHint("最低买入" + bean.getLimitOrderAmt() + unit);
        }

        if(dictionaryTable == null) {
            dictionaryTable = new HashMap<>();
            for(DataDictionary dataDictionary : channlidDictionarys){
                dictionaryTable.put(dataDictionary.getSubitem(),dataDictionary.getSubitemname());
            }
            for(BankCardInfo bankCardInfo : bean.getBankCardInfoList()){
                String bankName = dictionaryTable.get(bankCardInfo.getChannelid());
                if(!TextUtils.isEmpty(bankName)) bankCardInfo.setBankname(bankName);
            }

        }
        this.bankName.setText(currectPayBank.getBankname());
        String bankCoade = currectPayBank.getDepositacct();
        if (bankCoade.length() > 4) {
            bankTailCode.setText(bankCoade.substring(bankCoade.length() - 4));
        }

    }


    /**
     * 开始支付
     *
     * @param psw
     */
    private void starPay(String money, String psw) {
        LoadingDialog loadingDialog = LoadingDialog.getLoadingDialog(this, "正在支付", false, false);

        getPresenter().sure(bean, currectPayBank, money, psw, new BasePublicFundPresenter.PreSenterCallBack<String>() {
            @Override
            public void even(String result) {
                loadingDialog.dismiss();
                BankListOfJZSupport bankListOfJZSupport = new Gson().fromJson(result, BankListOfJZSupport.class);

                if (PublicFundContant.REQEUST_SUCCESS.equals(bankListOfJZSupport.getErrorCode())) {
                    NavigationUtils.gotoWebActivity(BuyPublicFundActivity.this, CwebNetConfig.publicFundBuyResult + "?amount=" + money, "申购成功", false);
                    finish();
                } else {
                    MToast.makeText(BuyPublicFundActivity.this, bankListOfJZSupport.getErrorMessage(), Toast.LENGTH_LONG);
                }

            }

            @Override
            public void field(String errorCode, String errorMsg) {
                loadingDialog.dismiss();
                Log.e("Test", " 申购异常 " + errorMsg);
                MToast.makeText(BuyPublicFundActivity.this, " 支付失败", Toast.LENGTH_LONG);
            }
        });
        loadingDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == PayFundBankSelectDialog.REQUESTCODE && resultCode == Activity.RESULT_OK) {
            //TODO 发起请求
            BankCardInfo bankCordInfo = (BankCardInfo) data.getExtras().get("bankCordInfo");
            if(bankCordInfo == null) return;

            bankCordInfo.setCustno(currectPayBank.getCustno());
            String bankName = dictionaryTable.get(bankCordInfo.getChannelid());
            if(!TextUtils.isEmpty(bankName)) bankCordInfo.setBankname(bankName);
            if(bean != null) bean.getBankCardInfoList().add(0,bankCordInfo);
            currectPayBank = bankCordInfo;
            showBankView();
        }
    }

    public static class Bean {
         /* {
                    "fundtype": "2",
                        "sharetype": " ",
                        "buyflag": "1",
                        "userBankCardInfo": {
                    "transactionaccountid": "Z001A00000249",
                            "moneyaccount": "199",
                            "depositacct": "6222020502022289222",
                            "status": "0",
                            "bankname": "工商银行",
                            "cardtelno": " ",
                            "custno": "189",
                            "paycenterid": "0330",
                            "authenticateflag": "1",
                            "branchcode": "370",
                            "isopenmobiletrade": "1",
                            "depositacctname": "能星辰",
                            "channelid": "Z001"
                },
                    "fundName": "金鹰货币B",
                        "tano": "21"
                }*/

        private String fundName; // 基金名字
        private String fundCode; // 基金号
        private String fundtype; // 基金类型
        private String sharetype; // 收费类型
        private String buyflag = "1";
        private String tano;// TA代码
        private String businesscode;
        private String rate; // 费率
        private String profitDate; // 收益日期
        private String limitOfDay; //银行卡每日限额
        private String limitOfSingle; //银行卡单笔限额
        private String limitOrderAmt; //最低买入
        private List<BankCardInfo> userBankCardInfo = new ArrayList<>();


        public String getFundName() {
            return fundName;
        }

        public void setFundName(String fundName) {
            this.fundName = fundName;
        }

        public String getFundCode() {
            return fundCode;
        }

        public void setFundCode(String fundCode) {
            this.fundCode = fundCode;
        }

        public String getFundtype() {
            return fundtype;
        }

        public void setFundtype(String fundtype) {
            this.fundtype = fundtype;
        }

        public String getSharetype() {
            return sharetype;
        }

        public void setSharetype(String sharetype) {
            this.sharetype = sharetype;
        }

        public String getBuyflag() {
            return buyflag;
        }

        public void setBuyflag(String buyflag) {
            this.buyflag = buyflag;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getProfitDate() {
            return profitDate;
        }

        public void setProfitDate(String profitDate) {
            this.profitDate = profitDate;
        }

        public String getLimitOfDay() {
            return limitOfDay;
        }

        public void setLimitOfDay(String limitOfDay) {
            this.limitOfDay = limitOfDay;
        }

        public String getLimitOfSingle() {
            return limitOfSingle;
        }

        public void setLimitOfSingle(String limitOfSingle) {
            this.limitOfSingle = limitOfSingle;
        }

        public String getTano() {
            return tano;
        }

        public void setTano(String tano) {
            this.tano = tano;
        }

        public String getBusinesscode() {
            return businesscode;
        }

        public void setBusinesscode(String businesscode) {
            this.businesscode = businesscode;
        }

        public List<BankCardInfo> getBankCardInfoList() {
            return userBankCardInfo;
        }

        public void setBankCardInfoList(List<BankCardInfo> bankCardInfoList) {
            this.userBankCardInfo = bankCardInfoList;
        }

        public String getLimitOrderAmt() {
            return limitOrderAmt;
        }

        public void setLimitOrderAmt(String limitOrderAmt) {
            this.limitOrderAmt = limitOrderAmt;
        }
    }

    public static class BankCardInfo implements Serializable {
        //  {"fundtype":"2","sharetype":" ","buyflag":"1","userBankCardInfo":{"transactionaccountid":"Z001A00000249","moneyaccount":"199","depositacct":"6222020502022289222","status":"0","bankname":"工商银行","cardtelno":" ","custno":"189","paycenterid":"0330","authenticateflag":"1","branchcode":"370","isopenmobiletrade":"1","depositacctname":"能星辰","channelid":"Z001"},"fundName":"金鹰货币B","tano":"21"}

         /* "moneyaccount": "199",
                "depositacct": "6222020502022289222",
                "status": "0",
                "bankname": "工商银行",
                "cardtelno": " ",
                "custno": "189",
                "paycenterid": "0330",
                "authenticateflag": "1",
                "branchcode": "370",
                "isopenmobiletrade": "1",
                "depositacctname": "能星辰",
                "channelid": "Z001"*/



       /*  "moneyaccount": "295",
                 "channelid": "Z004",
                 "paycenterid": "0330",
                 "depositacctname": "陈龙5",
                 "depositcard": " ",
                 "bankname": "中国建设银行股份有限公司阜阳鼓楼支行",
                 "depositprov": " ",
                 "depositcity": " ",
                 "depositacct": "6217000010093363177",
                 "transactionaccountid": "Z004A00000303"*/

        private String moneyaccount = ""; // /交易账户id（从银行卡列表信息中获取
        private String transactionaccountid = ""; // 账户号
        private String bankname = ""; // 名字
        private String depositacct = ""; // 卡号
        private String status;
        private String cardtelno;
        private String custno = ""; // 客户号
        private String paycenterid; // 所属网点
        private String authenticateflag;
        private String branchcode;
        private String isopenmobiletrade;
        private String depositacctname;
        private String channelid;  //

        public String getTransactionaccountid() {
            return transactionaccountid;
        }

        public void setTransactionaccountid(String transactionaccountid) {
            this.transactionaccountid = transactionaccountid;
        }

        public String getMoneyaccount() {
            return moneyaccount;
        }

        public void setMoneyaccount(String moneyaccount) {
            this.moneyaccount = moneyaccount;
        }

        public String getBankname() {
            return bankname;
        }

        public void setBankname(String bankname) {
            this.bankname = bankname;
        }

        public String getDepositacct() {
            return depositacct;
        }

        public void setDepositacct(String depositacct) {
            this.depositacct = depositacct;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCardtelno() {
            return cardtelno;
        }

        public void setCardtelno(String cardtelno) {
            this.cardtelno = cardtelno;
        }

        public String getCustno() {
            return custno;
        }

        public void setCustno(String custno) {
            this.custno = custno;
        }

        public String getPaycenterid() {
            return paycenterid;
        }

        public void setPaycenterid(String paycenterid) {
            this.paycenterid = paycenterid;
        }

        public String getAuthenticateflag() {
            return authenticateflag;
        }

        public void setAuthenticateflag(String authenticateflag) {
            this.authenticateflag = authenticateflag;
        }

        public String getBranchcode() {
            return branchcode;
        }

        public void setBranchcode(String branchcode) {
            this.branchcode = branchcode;
        }

        public String getIsopenmobiletrade() {
            return isopenmobiletrade;
        }

        public void setIsopenmobiletrade(String isopenmobiletrade) {
            this.isopenmobiletrade = isopenmobiletrade;
        }

        public String getDepositacctname() {
            return depositacctname;
        }

        public void setDepositacctname(String depositacctname) {
            this.depositacctname = depositacctname;
        }

        public String getChannelid() {
            return channelid;
        }

        public void setChannelid(String channelid) {
            this.channelid = channelid;
        }
    }
}
