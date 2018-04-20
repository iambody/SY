package com.cgbsoft.privatefund.public_fund;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.dialog.WheelDialogAddress;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangpeng on 18-1-29.
 * <p>
 * 绑定公募基金银行列表
 */
@Route(RouteConfig.GOTO_PUBLIC_FUND_BIND_BANK_CARD)
public class BindingBankCardOfPublicFundActivity extends BaseActivity<BindingBankCardOfPublicFundPresenter> implements View.OnClickListener {
    public static final String TAG_PARAMETER = "tag_parameter";
    public static final int ADD_BANK = 1;
    public static final String STYLE = "Style";
    public static final String TITLE = "title";

    public final static int SELECT_BANK = 100;
    public final static int SELECT_BRANCH_BANK = 101;

    private TextView mPayBankName; // 用于银行名字
    private TextView mBankBranchName; // 用于支付的支行银行名字
    private EditText mPankcardCode; // 银行卡号
    private EditText mPhoneCode; // 手机号
    private EditText mVerificationCode; // 验证码
    private Button getVerificationCode; // 获取验证码
    private CheckBox public_bindcard_cb_ar;//授权协议
    private BindingBankCardBean bindingBankCardBean;
    private LinearLayout public_bindcard_cb_ar_lay;
    private TextView mAddressBank;
    private String cityName;

    private int style;// 1 为赠卡风格 其它数字为开户流程风格
    private boolean isCheckBoxSel = true;

    @Override
    protected int layoutID() {
        return R.layout.activity_binding_bankcard;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        // 获取从H5传递过来的数据
     /*   "custno":"193",
                "certificatetype":"0",
                "certificateno":"110102200001018457",
                "depositacctname":"杨静",
                "authenticateflag":"1",
                "depositname":"杨静",
                "depositcity":" ",
                "depositprov":" ",
                "operorg":"9999",
                "tpasswd":"123456"*/

        mPayBankName = (TextView) findViewById(R.id.tv_pay_bank_name);
        mBankBranchName = (TextView) findViewById(R.id.tv_bank_branch);
        mPankcardCode = (EditText) findViewById(R.id.ev_bankcard_code);
        mPhoneCode = (EditText) findViewById(R.id.ev_phone_number);
        mVerificationCode = (EditText) findViewById(R.id.ev_verification_code_input);
        getVerificationCode = (Button) findViewById(R.id.bt_get_verification_code);
        mAddressBank = (TextView) findViewById(R.id.actv_bank_city);

        public_bindcard_cb_ar = (CheckBox) findViewById(R.id.public_bindcard_cb_ar);
        public_bindcard_cb_ar_lay = (LinearLayout) findViewById(R.id.public_bindcard_cb_ar_lay);


        style = getIntent().getIntExtra(STYLE, 0);
        String data = getIntent().getStringExtra(TAG_PARAMETER);
        if (style == 1) data = AppInfStore.getPublicFundInfo(this.getApplicationContext());
        if (!BStrUtils.isEmpty(data)) {
            bindingBankCardBean = new Gson().fromJson(data, BindingBankCardBean.class);
        }

        if (style == 1) {
            findViewById(R.id.rl_step_flow).setVisibility(View.GONE);
            View view = findViewById(R.id.rl_phonenum_root);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            lp.setMargins(0, DimensionPixelUtil.dip2px(this, 10), 0, 0);

            findViewById(R.id.rl_cusno_name).setVisibility(View.VISIBLE);
            TextView textView = (TextView) findViewById(R.id.tv_cusno_name);
            textView.setText(bindingBankCardBean.getDepositAcctName());
            ((ViewGroup) mPankcardCode.getParent()).getChildAt(2).setVisibility(View.GONE);
            public_bindcard_cb_ar.setOnCheckedChangeListener((buttonView, isChecked) -> isCheckBoxSel = isChecked);
//            public_bindcard_cb_ar_lay.setVerticalGravity(View.GONE);
        } else {
            findViewById(R.id.rl_cusno_name).setVisibility(View.GONE);
            ((ViewGroup) mPankcardCode.getParent()).getChildAt(2).setVisibility(View.VISIBLE);


        }

        bindView();
    }

    @Override
    protected BindingBankCardOfPublicFundPresenter createPresenter() {
        return new BindingBankCardOfPublicFundPresenter(this, null);
    }

    /**
     * 绑定View数据与监听
     */
    private void bindView() {
        // 该表标题
        if (TextUtils.isEmpty(getIntent().getStringExtra(TITLE))) {
            ((TextView) findViewById(R.id.title_mid)).setText("公募基金开户");
        } else {
            ((TextView) findViewById(R.id.title_mid)).setText(getIntent().getStringExtra(TITLE));
        }

        if (style == ADD_BANK) {
            ((TextView) findViewById(R.id.bt_Confirm)).setText("完成");
        } else {
            ((TextView) findViewById(R.id.bt_Confirm)).setText("去风险测评");
        }

        // 获取验证码按钮
        findViewById(R.id.bt_get_verification_code).setOnClickListener(this);
        // 确认购买
        findViewById(R.id.bt_Confirm).setOnClickListener(this);

        // 选择银行
        findViewById(R.id.rl_select_bankcard).setOnClickListener(this);

        // 选择支行
        mBankBranchName.setOnClickListener(this);
        findViewById(R.id.public_bindcard_tv_ar_proto).setOnClickListener(this);
        // 返回键
        findViewById(R.id.title_left).setVisibility(View.VISIBLE);
        findViewById(R.id.title_left).setOnClickListener(this);


        // 上次退出页面时,验证码的时间点
        if (SPreference.getString(BindingBankCardOfPublicFundActivity.this, LAST_VERIFICATION_TIME) != null) {
            long lastTime = Long.valueOf(SPreference.getString(BindingBankCardOfPublicFundActivity.this, LAST_VERIFICATION_TIME));
            int time = (int) (System.currentTimeMillis() - lastTime);
            if (time > 1000 && time < 60 * 1000) {
                sendVerificationCode(getVerificationCode, time / 1000, false);
            }
        }

        mAddressBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != bindingBankCardBean && null != bindingBankCardBean.getBanknameid()) {
                    showAddressSelector();
                } else {
                    MToast.makeText(BindingBankCardOfPublicFundActivity.this, "请先选择银行卡类型", Toast.LENGTH_LONG);
                }
            }
        });

        mPankcardCode.addTextChangedListener(mTextWatcher);

        mPankcardCode.addTextChangedListener(new TextWatcher() {
            private boolean isRun = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isRun) {
                    isRun = false;
                    return;
                }
                isRun = true;
                String d = "";
                String newStr = s.toString();
                newStr = newStr.replace(" ", "");
                int index = 0;
                while ((index + 4) < newStr.length()) {
                    d += (newStr.substring(index, index + 4) + " ");
                    index += 4;
                }
                d += (newStr.substring(index, newStr.length()));
                int i = mPankcardCode.getSelectionStart();
                mPankcardCode.setText(d);
                try {
                    if (i % 5 == 0 && before == 0) {
                        if (i + 1 <= d.length()) {
                            mPankcardCode.setSelection(i + 1);
                        } else {
                            mPankcardCode.setSelection(d.length());
                        }
                    } else if ((before == 1 || before == 0) && i < d.length()) {
                        mPankcardCode.setSelection(i);
                    } else {
                        mPankcardCode.setSelection(d.length());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                 /*  int lastSpaceIndex = s.toString().lastIndexOf(" ");
                   if(lastSpaceIndex < s.length() && s.length() -1 - lastSpaceIndex == 4 ){
                       mPankcardCode.setText(s+" ");
                   }else {
                       mPankcardCode.setText(s);
                   }*/
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mPhoneCode.addTextChangedListener(mTextWatcher);
      /*  mPhoneCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(s.length() > 11){
                    mPhoneCode.setText(s.subSequence(0,11));
                    if(mPhoneCode.getSelectionStart() > 11){
                        mPhoneCode.setSelection(11);
                    }
                }else {
                    mPhoneCode.setText(s);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {//
            }
        });*/
        mVerificationCode.addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {//
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {//
        }

        @Override
        public void afterTextChanged(Editable s) {
            requestChangConfirmColor();
        }
    };

    /**
     * 地址选择
     */
    private void showAddressSelector() {
        List<Map<String, Object>> parentList = null;
        try {
            StringBuilder sb = new StringBuilder();
            InputStream open = getResources().getAssets().open("city.json");
            BufferedReader bis = new BufferedReader(new InputStreamReader(open));
            String line = "";
            while ((line = bis.readLine()) != null) {
                sb.append(line);
            }
            String sbs = sb.toString();
            parentList = new Gson().fromJson(sbs, new TypeToken<List<Map<String, Object>>>() {
            }.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        WheelDialogAddress dialogAddress = new WheelDialogAddress(BindingBankCardOfPublicFundActivity.this);
        dialogAddress.setList(parentList);
        dialogAddress.setTitle("选择地区");
        dialogAddress.setConfirmCallback(new WheelDialogAddress.ConfirmListenerInteface() {

            @Override
            public void confirm(Map<String, Object> map) {
                if (map != null) {
                    String province = (String) map.get("province");
                    List<Map<String, Object>> cityList = (List<Map<String, Object>>) map.get("city");
                    String childPositionStr = (String) map.get("child_position");
                    String grandSonPositionStr = (String) map.get("grandson_position");
                    int childPositionInt = Integer.parseInt(childPositionStr);
                    int grandSonPositionInt = Integer.parseInt(grandSonPositionStr);
                    Map<String, Object> cityObj = cityList.get(childPositionInt);
                    cityName = (String) cityObj.get("n");
                    List<Map<String, Object>> districtList = (List<Map<String, Object>>) cityObj.get("areas");
                    Map<String, Object> districtObj = districtList.get(grandSonPositionInt);
                    String districtName = (String) districtObj.get("s");

                    String addressBank = province.concat(cityName).concat(districtName);
                    if (!addressBank.equals(mAddressBank.getText())) {
                        mBankBranchName.setText("");
                        mBankBranchName.setHint(mBankBranchName.getHint());
                    }
                    mAddressBank.setText(province.concat(cityName).concat(districtName));
                    //  loadBranchbankData(cityName);

                }
            }
        });
        dialogAddress.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_get_verification_code: // 获取验证码
                sendVerificationCode(getVerificationCode, 60, true);
                break;

            case R.id.bt_Confirm: // 确认绑定
                finshBanding();
                break;

            case R.id.rl_select_bankcard: //选择银行卡
                startActivityForResult(new Intent(this, SelectBankCardActivity.class), SELECT_BANK);
                break;

            case R.id.tv_bank_branch:// 选址支行
                if (bindingBankCardBean == null || BStrUtils.isEmpty(bindingBankCardBean.getBanknameid())) {
                    MToast.makeText(this, "请先选择银行", Toast.LENGTH_LONG);
                    return;
                }

                if (TextUtils.isEmpty(mAddressBank.getText())) {
                    MToast.makeText(this, "请先选择开户行地址", Toast.LENGTH_LONG);
                    return;
                }
                Intent intent = new Intent(this, SelectBranchBankActivity.class);
                intent.putExtra(SelectBranchBankActivity.CITY_NAME, cityName);
                intent.putExtra(SelectBranchBankActivity.BANK_NAME_ID, bindingBankCardBean.getBanknameid());
                startActivityForResult(intent, SELECT_BRANCH_BANK);
                break;
            case R.id.title_left:// 返回键
                finish();
                break;
            case R.id.public_bindcard_tv_ar_proto://银行转账授权协议
                NavigationUtils.gotoNavWebActivity(baseContext, CwebNetConfig.bankTransferState, "申购说明");
                break;
            default: //
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 接收银行信息
        if (SELECT_BANK == requestCode && data != null) {
            mPayBankName.setText(data.getStringExtra(SelectBankCardActivity.CHANNEL_NAME));
            bindingBankCardBean.setChannelid(data.getStringExtra(SelectBankCardActivity.CHANNEL_ID));
            bindingBankCardBean.setBanknameid(data.getStringExtra(SelectBankCardActivity.BANK_NAME_ID));
            if (mAddressBank != null && !TextUtils.isEmpty(mAddressBank.getText())) {
                mAddressBank.setText("");
                mAddressBank.setHint("请选择开户行地址");
                mBankBranchName.setText("");
                mBankBranchName.setHint(mBankBranchName.getHint());
            }
        }

        // 接收支行信息
        if (data != null && requestCode == SELECT_BRANCH_BANK) {
            bindingBankCardBean.setBankname(data.getStringExtra(SelectBranchBankActivity.CHANNEL_NAME));
            bindingBankCardBean.setChannelname(data.getStringExtra(SelectBranchBankActivity.CHANNEL_NAME));
            bindingBankCardBean.setParatype(data.getStringExtra(SelectBranchBankActivity.PARATYPE));
            mBankBranchName.setText(bindingBankCardBean.getChannelname());
            requestChangConfirmColor();
        }

    }

    /**
     * 改变确定按钮颜色
     */
    private void requestChangConfirmColor() {
        boolean isEnabled = true;
        //支行名字
        if (TextUtils.isEmpty(mBankBranchName.getText())) {
            isEnabled = false;
        }
        // 银行卡号
        String bankcard = mPankcardCode.getText() != null ? mPankcardCode.getText().toString().replace(" ", "") : "";
        if (TextUtils.isEmpty(bankcard)) {
            isEnabled = false;
        }
        //
        if (TextUtils.isEmpty(mPhoneCode.getText())) {
            isEnabled = false;
        }

        if (TextUtils.isEmpty(mVerificationCode.getText())) {
            isEnabled = false;
        }

        findViewById(R.id.bt_Confirm).setEnabled(isEnabled);
        if (isEnabled) {
            findViewById(R.id.bt_Confirm).setBackgroundResource(R.drawable.public_fund_conrner_golden);
        } else {
            findViewById(R.id.bt_Confirm).setBackgroundResource(R.drawable.public_fund_conrner_gray);
        }
    }


    /**
     * 发送验证码
     */
    private final static int TIME = R.id.bt_get_verification_code;
    private final static String LAST_VERIFICATION_TIME = "last_verification_time";

    /**
     * 获取验证码
     *
     * @param v
     * @param maxTime 最大秒数 默认60
     */
    public void sendVerificationCode(final View v, int maxTime, boolean isCheck) {
        // 发起验证码请求
        String phoneCode = mPhoneCode.getText().toString().trim();
        if (isCheck && BStrUtils.isEmpty(phoneCode)) {
            MToast.makeText(this, "手机号不能为空", Toast.LENGTH_LONG);
            return;
        }


        String bankCode = mPankcardCode.getText() != null ? mPankcardCode.getText().toString().replace(" ", "") : "";
        if (isCheck && BStrUtils.isEmpty(bankCode)) {
            MToast.makeText(this, "银行号不能为空", Toast.LENGTH_LONG);
            return;
        }

        if (!isCheck) return;
        lastBankCode = bankCode;
        if (TextUtils.isEmpty(bindingBankCardBean.getChannelid())) {
            MToast.makeText(BindingBankCardOfPublicFundActivity.this, "请先填写银行信息", Toast.LENGTH_LONG);
            return;
        }
        v.setEnabled(false);
        v.setBackgroundResource(R.drawable.public_fund_conrner_gray);
        getPresenter().getVerificationCodeFormServer(bindingBankCardBean.getChannelid(), phoneCode, bankCode, new BasePublicFundPresenter.PreSenterCallBack<String>() {
            @Override
            public void even(String s) {

                try {
                    String code = new JSONObject(s).getString("code");
                    String msg = new JSONObject(s).getString("msg");
                    if (PublicFundContant.REQEUST_SUCCESS.equals(code)) {
                        // 发送成功
                        MToast.makeText(BindingBankCardOfPublicFundActivity.this, "验证码发送成功", Toast.LENGTH_LONG).show();
                        // 开启倒计时
                        startCountdown(v, maxTime);
                    } else if (PublicFundContant.REQEUSTING.equals(code)) {
                        MToast.makeText(BindingBankCardOfPublicFundActivity.this, "处理中", Toast.LENGTH_LONG).show();
                    } else {
                        MToast.makeText(BindingBankCardOfPublicFundActivity.this, msg, Toast.LENGTH_LONG).show();
                        // 关闭倒计时
                        closeConutDown(getVerificationCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    MToast.makeText(BindingBankCardOfPublicFundActivity.this, "发送验证码失败", Toast.LENGTH_LONG);
                    // 关闭倒计时
                    closeConutDown(getVerificationCode);
                }
             /*       BankListOfJZSupport bankListOfJZSupport = new Gson().fromJson(s, BankListOfJZSupport.class);
                    if (bankListOfJZSupport != null) {
                        String code = bankListOfJZSupport.getErrorCode();
                        if (PublicFundContant.REQEUST_SUCCESS.equals(code)) {
                            // 发送成功
                            MToast.makeText(BindingBankCardOfPublicFundActivity.this, "验证码发送成功", Toast.LENGTH_LONG);
                        } else if (PublicFundContant.REQEUSTING.equals(code)) {
                            MToast.makeText(BindingBankCardOfPublicFundActivity.this, "处理中", Toast.LENGTH_LONG);
                        } else {
                            MToast.makeText(BindingBankCardOfPublicFundActivity.this, bankListOfJZSupport.getErrorMessage(), Toast.LENGTH_LONG);
                            getVerificationCode.setTag(TIME, null);
                            timer.cancel();
                        }
                    }*/

            }

            @Override
            public void field(String errorCode, String errorMsg) {
                // 关闭倒计时
                closeConutDown(getVerificationCode);
            }
        });


    }


    private void closeConutDown(Button v) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                v.setText("获取验证码");
                v.setEnabled(true);
                v.setBackgroundResource(R.drawable.public_fund_conrner_golden);
            }
        });
        Timer timer = (Timer) v.getTag();
        if (timer == null) return; // 当timer为空时，说明验证码倒计时还有没有启动
        timer.cancel();
        v.setTag(TIME, null);
    }

    /**
     * 开启倒计时
     */
    private void startCountdown(View v, int maxTime) {

        if (v.getTag(TIME) == null || (Integer) v.getTag(TIME) == 0) {
            v.setTag(TIME, maxTime);
            final Timer timer = new Timer();
            v.setTag(timer);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    int time = (int) v.getTag(TIME);
                    if (time > 1) {
                        v.setTag(TIME, --time);
                        final int finalTime = time;
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                getVerificationCode.setText(finalTime + "s");
                            }
                        });
                    } else {
                        closeConutDown(getVerificationCode);
                    }
                }
            }, 1000, 1000);
        }
    }

    /**
     * 完成绑定
     */

    private String lastBankCode = "";

    private void finshBanding() {
        String phoneCode = mPhoneCode.getText().toString();
        if (BStrUtils.isEmpty(phoneCode)) {
            MToast.makeText(this, "手机号不能为空", Toast.LENGTH_LONG);
            return;
        }

        String verificationCode = mVerificationCode.getText().toString();
        if (BStrUtils.isEmpty(verificationCode)) {
            MToast.makeText(this, "验证码不能为空", Toast.LENGTH_LONG);
            return;
        }

        String addressBank = mAddressBank.getText().toString();
        if (BStrUtils.isEmpty(addressBank)) {
            MToast.makeText(this, "请输入支行地址", Toast.LENGTH_LONG);
            return;
        }
        String bankBranchName = mBankBranchName.getText().toString();
        if (BStrUtils.isEmpty(bankBranchName)) {
            MToast.makeText(this, "请输入支行名", Toast.LENGTH_LONG);
            return;
        }

        String payBankName = mPayBankName.getText().toString();
        if (BStrUtils.isEmpty(payBankName)) {
            MToast.makeText(this, "请选择银行", Toast.LENGTH_LONG);
            return;
        }
        if (!isCheckBoxSel) {//银行授权协议
            MToast.makeText(this, "请同意银行授权协议", Toast.LENGTH_LONG);
            return;
        }
        String bankCode = mPankcardCode.getText() != null ? mPankcardCode.getText().toString().replace(" ", "") : "";
        if (BStrUtils.isEmpty(bankCode)) {
            MToast.makeText(this, "银行号不能为空", Toast.LENGTH_LONG);
            return;
        }

        //　防止没有发送验证码就点击绑定和获取验证之后又修改了银行卡号之后绑定银行卡
        if (!lastBankCode.equals(bankCode)) {
            MToast.makeText(this, "请先点击获取验证码", Toast.LENGTH_LONG);
            return;
        }

        LoadingDialog loadingDialog = LoadingDialog.getLoadingDialog(this, "正在绑定", false, false);
        loadingDialog.show();
        getPresenter().sureBind(bindingBankCardBean, payBankName, bankCode, phoneCode, verificationCode, new BasePublicFundPresenter.PreSenterCallBack<String>() {
            @Override
            public void even(String s) {
                loadingDialog.dismiss();

                Log.i("绑定成功", " 银行卡信息：-->" + s);
                if (style == ADD_BANK) {
                    Gson gson = new Gson();
                    BuyPublicFundActivity.BankCardInfo bankCordInfo = gson.fromJson(s, BuyPublicFundActivity.BankCardInfo.class);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bankCordInfo", bankCordInfo);
                    getIntent().putExtras(bundle);
                    BindingBankCardOfPublicFundActivity.this.setResult(Activity.RESULT_OK, getIntent());
                } else {
                    // 去风险测评页面
                    UiSkipUtils.gotoPublicFundRisk(BindingBankCardOfPublicFundActivity.this);
                    RxBus.get().post(RxConstant.REFRESH_PUBLIC_FUND_INFO, 10);
                }
                MToast.makeText(BindingBankCardOfPublicFundActivity.this, "绑定成功", Toast.LENGTH_LONG);
                finish();

                      /*    String datasets = "";
                String code = "";
                String message = "";
                try {
                    JSONObject result = new JSONObject(s);
                    code = result.getString("errorCode");
                    message = result.getString("errorMessage");
                    datasets = result.getJSONArray("datasets").getString(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

             /*   if (PublicFundContant.REQEUST_SUCCESS.equals(code)) { // 成功
                    if (style == 1) {
                        Gson gson = new Gson();
                        BuyPublicFundActivity.BankCardInfo bankCordInfo = gson.fromJson(datasets, BuyPublicFundActivity.BankCardInfo.class);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("bankCordInfo", bankCordInfo);
                        getIntent().putExtras(bundle);
                        BindingBankCardOfPublicFundActivity.this.setResult(Activity.RESULT_OK, getIntent());
                    } else {
                        // 去风险测评页面
                        UiSkipUtils.gotoPublicFundRisk(BindingBankCardOfPublicFundActivity.this);
                        RxBus.get().post(RxConstant.REFRESH_PUBLIC_FUND_INFO, 10);
                    }
                    MToast.makeText(BindingBankCardOfPublicFundActivity.this, "绑定成功", Toast.LENGTH_LONG);
                    finish();
                } else if (PublicFundContant.REQEUSTING.equals(code)) {
                    MToast.makeText(BindingBankCardOfPublicFundActivity.this, "处理中", Toast.LENGTH_LONG);
                } else if ("1106".equals(code)) {
                    MToast.makeText(BindingBankCardOfPublicFundActivity.this, "请输入正确的验证码", Toast.LENGTH_LONG);
                } else {
                    MToast.makeText(BindingBankCardOfPublicFundActivity.this, message, Toast.LENGTH_LONG);
                }*/
            }

            @Override
            public void field(String errorCode, String errorMsg) {
                if (!"500".equals(errorCode))
                    MToast.makeText(BindingBankCardOfPublicFundActivity.this, "绑定失败", Toast.LENGTH_LONG);
                Log.e("绑定页面", " 网络错误 " + errorMsg);
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getVerificationCode.getTag(TIME) != null && (Integer) getVerificationCode.getTag(TIME) > 1) {
            SPreference.putString(BindingBankCardOfPublicFundActivity.this, LAST_VERIFICATION_TIME, "" + System.currentTimeMillis());
            getVerificationCode.setTag(TIME, null);
            Timer timer = (Timer) getVerificationCode.getTag();
            timer.cancel();
        }
    }

}
