package com.cgbsoft.privatefund.public_fund;

import android.app.Activity;
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
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.TrackingDataManger;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.bean.DataDictionary;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangpeng on 18-1-29.
 */
@Route(RouteConfig.GOTO_PUBLIC_FUND_BUY)
public class BuyPublicFundActivity extends BaseActivity<BuyPublicFundPresenter> implements View.OnClickListener {
    public static final String TAG_FUND_CODE = "tag_fund_code";
    public static final String TAG_FUND_NAME = "tag_fund_name";
    public static final String TAG_FUND_Type = "tag_fund_type";
    public static final String TAG_FUND_RISK_LEVEL = "tag_fund_risk_level";
    public static final String YINGTAI_QIANBAO = "210012";
    private Button buyConfirm;
    private ImageView bankIcon;
    private TextView bankName;
    private TextView bankTailCode;
    private EditText buyInput;

    private String fundRiskLevel;// 风险级别

    private String fundCode; // 基金号
    private String fundName; // 基金名字
    private String fundType; // 基金类型
    private String unit = "元"; //银行卡单笔限额

    private boolean isPublicFund = true; // 是公募基金还是盈泰钱包

    private Bean bean;
    private List<DataDictionary> channlidDictionarys;
    private BankCardInfo currectPayBank;
    private TextView bankLimit;

    @Override
    protected int layoutID() {
        return R.layout.activity_buy_publicfund;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        fundCode = getIntent().getStringExtra(TAG_FUND_CODE);
        fundName = getIntent().getStringExtra(TAG_FUND_NAME);
        fundType = getIntent().getStringExtra(TAG_FUND_Type);
        if (YINGTAI_QIANBAO.equals(fundCode.trim())) isPublicFund = false;
        fundRiskLevel = getIntent().getStringExtra(TAG_FUND_RISK_LEVEL);

        buyInput = (EditText) findViewById(R.id.ev_buy_money_input);
        bankIcon = (ImageView) findViewById(R.id.iv_bank_icon);
        bankName = (TextView) findViewById(R.id.tv_bank_name);
        bankTailCode = (TextView) findViewById(R.id.tv_bank_tailcode);
        bankLimit = (TextView) findViewById(R.id.tv_bank_limit);
        buyConfirm = (Button) findViewById(R.id.bt_Confirm);


        bindView();
    }

    /**
     * 绑定View的监听与数据
     */
    private void bindView() {
        // 该表标题
        if (isPublicFund) {
            ((TextView) findViewById(R.id.title_mid)).setText("立即购买");
            buyConfirm.setText("确认购买");
        } else {
            ((TextView) findViewById(R.id.title_mid)).setText("盈泰钱包");
            buyConfirm.setText("确认转入");
        }

        if (!isPublicFund) {
            findViewById(R.id.ll_fundinfo).setVisibility(View.GONE);
        } else {
            ((TextView) findViewById(R.id.tv_fundname)).setText(fundName);
            ((TextView) findViewById(R.id.tv_fundcode)).setText(fundCode);
        }

        // 返回键
        findViewById(R.id.title_left).setVisibility(View.VISIBLE);
        findViewById(R.id.title_left).setOnClickListener(this);
        findViewById(R.id.rl_bank_card).setOnClickListener(this);

        buyInput.setHint("请输入金额");
        buyInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 设置输入框提示文本
                String limitAmt = bean != null ? bean.getLimitOrderAmt().trim() :"0";// 最少购买限额
                if (TextUtils.isEmpty(s.toString().trim()) || new BigDecimal(s.toString().trim()).compareTo(new BigDecimal(limitAmt)) < 0) {
                    buyConfirm.setBackgroundResource(R.drawable.public_fund_conrner_gray);
                } else {
                    buyConfirm.setBackgroundResource(R.drawable.public_fund_conrner_golden);
                }
            }
        });

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


                if (new BigDecimal(money).compareTo(BigDecimal.valueOf(10000)) >= 0) {
                    money = new BigDecimal(money).divide(BigDecimal.valueOf(10000)).doubleValue() + "";
                    unit = "万元";
                } else {
                    unit = "元";
                }

                String finalMoney = money;
                PayPasswordDialog payPasswordDialog = new PayPasswordDialog(this, null, bean.getFundName(), money + unit);
                payPasswordDialog.setmPassWordInputListener(new PayPasswordDialog.PassWordInputListener() {
                    @Override
                    public void onInputFinish(String psw) {
                        starPay(finalMoney, psw);
                        payPasswordDialog.dismiss();
                    }
                });
                payPasswordDialog.show();

                break;
            case R.id.rl_bank_card: // 用于支付的银行卡
                if (currectPayBank == null || bean == null || bean.getUserBankCardInfo() == null || bean.getUserBankCardInfo().size() < 0)
                    return;
                new PayFundBankSelectDialog(this, currectPayBank.getDepositAcct(), bean.getUserBankCardInfo(), new PayFundBankSelectDialog.SelectListener() {
                    @Override
                    public void select(int index) {
                        Log.e(this.getClass().getSimpleName(), "选择银行卡" + index);
                        if (index == -2) {
                            Activity activity = BuyPublicFundActivity.this;
                            Intent intent = new Intent(activity, BindingBankCardOfPublicFundActivity.class);
                            intent.putExtra(BindingBankCardOfPublicFundActivity.STYLE, 1);
                            intent.putExtra(BindingBankCardOfPublicFundActivity.TITLE, "使用新卡支付");
                            activity.startActivityForResult(intent, PayFundBankSelectDialog.REQUESTCODE);
                        } else if (index >= 0) {
                            BankCardInfo bankCardInfo = bean.getUserBankCardInfo().get(index);
                            if (bankCardInfo == null) return;
                            currectPayBank = bankCardInfo;
                            showBankView();
                        }
                    }
                }).show();

                break;

            case R.id.title_left: // 返回
                finish();
                break;
        }
    }

    /**
     * 请求银行名字的字典
     *
     */
/*    private void requestDictionary() {
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
    }*/

    /**
     * 获取信息
     *
     * @param fundCode "210013" "004373" 调试数据
     */

    LoadingDialog loadingDialog;

    private void requestData(String fundCode) {
        loadingDialog = LoadingDialog.getLoadingDialog(this, "加载中", false, false);
        //  requestDictionary();
        getPresenter().getData(fundCode, new BasePublicFundPresenter.PreSenterCallBack<String>() {
            @Override
            public void even(String o) {
                Log.e("申购信息", "" + o);
                loadingDialog.dismiss();
                bean = new Gson().fromJson(o, Bean.class);
                bean.setFundCode(fundCode);
                if (bean.getUserBankCardInfo().size() > 0) {
                    currectPayBank = bean.getUserBankCardInfo().get(0);
                }

                // 设置输入框提示文本
                String limitAmt = bean != null ? bean.getLimitOrderAmt().trim() : "";// 最少购买限额
                if (!BStrUtils.isEmpty(limitAmt) && !"null".equals(limitAmt)) {
                    if (new BigDecimal(limitAmt).compareTo(BigDecimal.valueOf(10000)) >= 0) {
                        buyInput.setHint("最低购买金额" + new BigDecimal(limitAmt).divide(BigDecimal.valueOf(10000)).doubleValue() + "万元");
                    } else {
                        buyInput.setHint("最低购买金额" + limitAmt + "元");
                    }
                }

                if (currectPayBank == null) return;
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
    Map<String, String> dictionaryTable = null;

    private void showBankView() {
        findViewById(R.id.iv_direct).setBackgroundResource(R.drawable.direct_right);
        Imageload.display(BuyPublicFundActivity.this, currectPayBank.getIcon(), this.bankIcon, R.drawable.bank_icon, R.drawable.bank_icon);
        this.bankName.setText(currectPayBank.getBankShortName());
        String bankCoade = currectPayBank.getDepositAcct();
        if (bankCoade.length() > 4) {
            bankTailCode.setText("尾号 "+bankCoade.substring(bankCoade.length() - 4));
        }

        if ("0".equals(currectPayBank.getBankEnableStatus())) {
            this.bankLimit.setText(getString(R.string.public_fund_bank_not_useable));
        } else {
            this.bankLimit.setText(currectPayBank.getBankLimit());
        }
    }


    /**
     * 开始支付
     *
     * @param psw
     */
    private void starPay(final String money, String psw) {
        LoadingDialog loadingDialog = LoadingDialog.getLoadingDialog(this, "正在支付", false, false);
        final String formatMoney = new DecimalFormat("0.00").format(new BigDecimal(money));

        if ("0".equals(currectPayBank.getBankEnableStatus())) {
            MToast.makeText(BuyPublicFundActivity.this, "当前银行卡因渠道变更暂无法进行支付", Toast.LENGTH_LONG).show();
            return;
        }
        getPresenter().sure(bean, currectPayBank, money, psw, new BasePublicFundPresenter.PreSenterCallBack<String>() {
            @Override
            public void even(String result) {
                loadingDialog.dismiss();

                String redeemReFundDate = "";
                if (!TextUtils.isEmpty(result)) {
                    try {
                        redeemReFundDate = new JSONObject(result).getString("redeemReFundDate");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                TrackingDataManger.buyPublicFund(BuyPublicFundActivity.this, BuyPublicFundActivity.this.fundName);
                if (isPublicFund) {
                    UiSkipUtils.gotoNewFundResult(BuyPublicFundActivity.this, 1, fundType, formatMoney, redeemReFundDate);
                } else {
                    NavigationUtils.gotoWebActivity(BuyPublicFundActivity.this, CwebNetConfig.publicFundBuyResult + "?amount=" + formatMoney, "申购成功", false);
                }
                finish();

            /*
                try {
                    String code = new JSONObject(result).getString("code");
                    String message = new JSONObject(result).getString("message");
                    if (PublicFundContant.REQEUST_SUCCESS.equals(code)) {
                        TrackingDataManger.buyPublicFund(BuyPublicFundActivity.this,BuyPublicFundActivity.this.fundName);
                        if(isPublicFund){
                            UiSkipUtils.gotoNewFundResult(BuyPublicFundActivity.this,2,fundType,money);
                        }else {
                            NavigationUtils.gotoWebActivity(BuyPublicFundActivity.this, CwebNetConfig.publicFundBuyResult + "?amount=" + money, "申购成功", false);
                        }
                        finish();
                    } else {
                        MToast.makeText(BuyPublicFundActivity.this, message, Toast.LENGTH_LONG);
                    }

                } catch (JSONException e) {
                    MToast.makeText(BuyPublicFundActivity.this, "申购失败", Toast.LENGTH_LONG);
                    e.printStackTrace();
                }*/
/*

                BankListOfJZSupport bankListOfJZSupport = new Gson().fromJson(result, BankListOfJZSupport.class);

                if (PublicFundContant.REQEUST_SUCCESS.equals(bankListOfJZSupport.getErrorCode())) {
                    TrackingDataManger.buyPublicFund(BuyPublicFundActivity.this,BuyPublicFundActivity.this.fundName);
                    if(isPublicFund){
                        UiSkipUtils.gotoNewFundResult(BuyPublicFundActivity.this,2,fundType,money);
                    }else {
                        NavigationUtils.gotoWebActivity(BuyPublicFundActivity.this, CwebNetConfig.publicFundBuyResult + "?amount=" + money, "申购成功", false);
                    }
                    finish();
                } else {
                    MToast.makeText(BuyPublicFundActivity.this, bankListOfJZSupport.getErrorMessage(), Toast.LENGTH_LONG);
                }
*/

            }

            @Override
            public void field(String errorCode, String errorMsg) {
                loadingDialog.dismiss();
                Log.e("Test", " 申购异常 " + errorMsg);
                //  MToast.makeText(BuyPublicFundActivity.this, " 支付失败", Toast.LENGTH_LONG);
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
            if (bankCordInfo == null) return;

            bankCordInfo.setCustNo(currectPayBank.getCustNo());
            String bankName = dictionaryTable.get(bankCordInfo.getChannelId());
            if (!TextUtils.isEmpty(bankName)) bankCordInfo.setBankName(bankName);
            if (bean != null) bean.getUserBankCardInfo().add(0, bankCordInfo);
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

        /**
         * {
         * "businessCode":"22",
         * "fundType":"2",
         * "userBankCardInfo":[
         * <p>
         * ],
         * "fundName":"金鹰货币A",
         * "taNo":"21",
         * "shareType":" ",
         * "limitOrderAmt":"1",
         * "buyFlag":"1"
         * }
         */

        private String fundName; // 基金名字
        private String fundCode; // 基金号
        private String fundType; // 基金类型
        private String shareType; // 收费类型
        private String buyFlag = "1";
        private String taNo;// TA代码
        private String businessCode;
        /*  private String rate; // 费率
          private String profitDate; // 收益日期
          private String limitOfDay; //银行卡每日限额
          private String limitOfSingle; //银行卡单笔限额*/
        private String limitOrderAmt; //最低买入
        private String serialNo; // 流水号
        private List<BankCardInfo> userBankCardInfo = new ArrayList<>();


        public String getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(String serialNo) {
            this.serialNo = serialNo;
        }

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

        public String getFundType() {
            return fundType;
        }

        public void setFundType(String fundType) {
            this.fundType = fundType;
        }

        public String getShareType() {
            return shareType;
        }

        public void setShareType(String shareType) {
            this.shareType = shareType;
        }

        public String getBuyFlag() {
            return buyFlag;
        }

        public void setBuyFlag(String buyFlag) {
            this.buyFlag = buyFlag;
        }

        public String getTaNo() {
            return taNo;
        }

        public void setTaNo(String taNo) {
            this.taNo = taNo;
        }

        public String getBusinessCode() {
            return businessCode;
        }

        public void setBusinessCode(String businessCode) {
            this.businessCode = businessCode;
        }

        public String getLimitOrderAmt() {
            return limitOrderAmt;
        }

        public void setLimitOrderAmt(String limitOrderAmt) {
            this.limitOrderAmt = limitOrderAmt;
        }

        public List<BankCardInfo> getUserBankCardInfo() {
            return userBankCardInfo;
        }

        public void setUserBankCardInfo(List<BankCardInfo> userBankCardInfo) {
            this.userBankCardInfo = userBankCardInfo;
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


        /* "   authenticateflag = 1;\\\\ 验证码\n"+
                 "                bankname = \"\\U4e2d\\U56fd\\U5efa\\U8bbe\\U94f6\\U884c\\U5317\\U4eac\\U4e0a\\U5730\\U652f\\U884c\";\\\\ 银行名称\n"+
                 "                branchcode = 370; // 托管网点\n"+
                 "                cardtelno = 18500152424; //绑卡对应手机号\n"+
                 "                channelid = Z004;// 银行代码\n"+
                 "                custno = 266; //客户号\n"+
                 "                depositacct = 6217000010076615759;// 银行卡号\n"+
                 "                depositacctname = \"\\U674e\\U6c38\\U5f3a\";//投资人全称\n"+
                 "                isopenmobiletrade = 1; //\n"+
                 "                moneyaccount = 341;// 资金账户\n"+
                 "                paycenterid = 0330; //支付渠道代码[110079]\n"+
                 "                status = 0;\n"+
                 "                transactionaccountid = Z004A00000349; //交易账户"
                 */


        /**
         * {
         * "authenticateFlag":"1",
         * "background":"http://upload.simuyun.com/publicfund/bankicon/HXBANK-bg3x.png",
         * "bankEnableStatus":"1",
         * "bankLimit":"",
         * "bankName":"华夏银行",
         * "bankShortName":"华夏银行",
         * "branchCode":"370",
         * "cardTelNo":" ",
         * "channelId":"Z017",
         * "custNo":"225",
         * "depositAcct":"6226311810148946",
         * "depositAcctName":"刘聪为",
         * "icon":"http://upload.simuyun.com/publicfund/bankicon/HXBANK3x.png",
         * "isOpenMobileTrade":"0",
         * "moneyAccount":"217",
         * "paycenterId":"0330",
         * "status":0,
         * "transactionAccountId":"Z017A00000267"
         * }
         */
        private String moneyaccount = ""; // /交易账户id（从银行卡列表信息中获取
        private String transactionAccountId = ""; // 账户号
        private String bankName = ""; // 名字
        private String depositAcct = ""; // 卡号
        private String status;
        private String cardTelNo;
        private String custNo = ""; // 客户号
        private String paycenterId; // 所属网点
        private String authenticateFlag;
        private String branchCode;
        private String isOpenMobileTrade;
        private String depositAcctName;
        private String bankShortName = "银行";  // 银行简称
        private String bankLimit;  // 银行卡限额
        private String bankEnableStatus;  // 银行卡可用状态　0不可用，１可用
        private String balFund = ""; //　
        private String balFundMode1 = ""; //　
        private String background = ""; //　
        private String icon = ""; //　
        private String channelId;  // 支付网点号

        private String availBalMode1 = ""; //　申请基金的份额(H５传过来)
        private String fullName;  //　渠道名字
        private String bankNameId;  //银行Id

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getBankNameId() {
            return bankNameId;
        }

        public void setBankNameId(String bankNameId) {
            this.bankNameId = bankNameId;
        }


        public String getMoneyaccount() {
            return moneyaccount;
        }

        public void setMoneyaccount(String moneyaccount) {
            this.moneyaccount = moneyaccount;
        }


        public String getTransactionAccountId() {
            return transactionAccountId;
        }

        public void setTransactionAccountId(String transactionAccountId) {
            this.transactionAccountId = transactionAccountId;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getDepositAcct() {
            return depositAcct;
        }

        public void setDepositAcct(String depositAcct) {
            this.depositAcct = depositAcct;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCardTelNo() {
            return cardTelNo;
        }

        public void setCardTelNo(String cardTelNo) {
            this.cardTelNo = cardTelNo;
        }

        public String getCustNo() {
            return custNo;
        }

        public void setCustNo(String custNo) {
            this.custNo = custNo;
        }

        public String getPaycenterId() {
            return paycenterId;
        }

        public void setPaycenterId(String paycenterId) {
            this.paycenterId = paycenterId;
        }

        public String getAuthenticateFlag() {
            return authenticateFlag;
        }

        public void setAuthenticateFlag(String authenticateFlag) {
            this.authenticateFlag = authenticateFlag;
        }

        public String getBranchCode() {
            return branchCode;
        }

        public void setBranchCode(String branchCode) {
            this.branchCode = branchCode;
        }

        public String getIsOpenMobileTrade() {
            return isOpenMobileTrade;
        }

        public void setIsOpenMobileTrade(String isOpenMobileTrade) {
            this.isOpenMobileTrade = isOpenMobileTrade;
        }

        public String getDepositAcctName() {
            return depositAcctName;
        }

        public void setDepositAcctName(String depositAcctName) {
            this.depositAcctName = depositAcctName;
        }

        public String getBankShortName() {
            return bankShortName;
        }

        public void setBankShortName(String bankShortName) {
            this.bankShortName = bankShortName;
        }

        public String getBankLimit() {
            return bankLimit;
        }

        public void setBankLimit(String bankLimit) {
            this.bankLimit = bankLimit;
        }

        public String getBankEnableStatus() {
            return bankEnableStatus;
        }

        public void setBankEnableStatus(String bankEnableStatus) {
            this.bankEnableStatus = bankEnableStatus;
        }

        public String getBalFund() {
            return balFund;
        }

        public void setBalFund(String balFund) {
            this.balFund = balFund;
        }

        public String getBalFundMode1() {
            return balFundMode1;
        }

        public void setBalFundMode1(String balFundMode1) {
            this.balFundMode1 = balFundMode1;
        }

        public String getBackground() {
            return background;
        }

        public void setBackground(String background) {
            this.background = background;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getAvailBalMode1() {
            return availBalMode1;
        }

        public void setAvailBalMode1(String availBalMode1) {
            this.availBalMode1 = availBalMode1;
        }
    }
}
