package com.cgbsoft.privatefund.public_fund;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;

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

    public final static int SELECT_BANK = 100;

    private TextView mPayBankName; // 用于支付的银行名字
    private EditText mPankcardCode; // 银行卡号
    private EditText mPhoneCode; // 手机号
    private EditText mVerificationCode; // 验证码
    private Button getVerificationCode; // 获取验证码


    private BindingBankCardBean bindingBankCardBean;
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

        String data = getIntent().getStringExtra(TAG_PARAMETER);
        // 如果data不为说明是从h5跳转过来，否者从原生页面跳转过来  (目前逻辑全部是不为空的 原生和h5都是有数据的@wyk)
        if(!BStrUtils.isEmpty(data)){
            bindingBankCardBean =  new Gson().fromJson(data,BindingBankCardBean.class);
        }else {
            bindingBankCardBean = new BindingBankCardBean();
            bindingBankCardBean.setCustno(AppManager.getPublicFundInf(this).getCustno());
            bindingBankCardBean.setCertificateno(AppManager.getPublicFundInf(this).getCertificateno());
            bindingBankCardBean.setCertificatetype(AppManager.getPublicFundInf(this).getCertificatetype());
            bindingBankCardBean.setDepositacctname(AppManager.getPublicFundInf(this).getDepositacctname());
            bindingBankCardBean.setDepositname(AppManager.getPublicFundInf(this).getDepositacctname());
            //tod


        }
        mPayBankName = (TextView) findViewById(R.id.tv_pay_bank_name);
        mPankcardCode = (EditText) findViewById(R.id.ev_bankcard_code);
        mPhoneCode = (EditText) findViewById(R.id.ev_phone_number);
        mVerificationCode = (EditText) findViewById(R.id.ev_verification_code_input);
        getVerificationCode = (Button) findViewById(R.id.bt_get_verification_code);

        bindView();
    }

    @Override
    protected BindingBankCardOfPublicFundPresenter createPresenter() {
        return new BindingBankCardOfPublicFundPresenter(this,null);
    }

    /**
     * 绑定View数据与监听
     */
    private void bindView() {
        // 该表标题
        ((TextView) findViewById(R.id.title_mid)).setText("绑定银行卡");
        // 获取验证码按钮
        findViewById(R.id.bt_get_verification_code).setOnClickListener(this);
        // 确认购买
        findViewById(R.id.bt_Confirm).setOnClickListener(this);

        // 选择银行
        findViewById(R.id.rl_select_bankcard).setOnClickListener(this);

        // 返回键
        findViewById(R.id.title_left).setVisibility(View.VISIBLE);
        findViewById(R.id.title_left).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_get_verification_code: // 获取验证码
                sendVerificationCode(getVerificationCode,60);
                break;

            case R.id.bt_Confirm: // 确认绑定
                finshBanding();
                break;

            case R.id.rl_select_bankcard: //选择银行卡
                startActivityForResult(new Intent(this, SelectBankCardActivity.class), SELECT_BANK);
                break;

            case R.id.title_left:// 返回键
                finish();
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (SELECT_BANK == requestCode) {
            mPayBankName.setText(data.getStringExtra(SelectBankCardActivity.CHANNEL_NAME));
            bindingBankCardBean.setChannelid(data.getStringExtra(SelectBankCardActivity.CHANNEL_ID));
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
    public void sendVerificationCode(final View v,int maxTime) {
        if (v.getTag(TIME) == null || (Integer)v.getTag(TIME) == 0) {
            v.setTag(TIME,maxTime);
           final Timer timer =  new Timer();
            v.setTag(timer);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    int time = (int) v.getTag(TIME);
                    if(time > 1){
                        v.setTag(TIME,--time);
                        final int finalTime = time;
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                getVerificationCode.setText(finalTime +"");
                            }
                        });
                    }else {
                        timer.cancel();
                        v.setTag(TIME,null);
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                getVerificationCode.setText("获取验证码");
                            }
                        });

                    }
                }
            }, 1000, 1000);

            // 发起验证码请求
            String phoneCode =  mPhoneCode.getText().toString();
            if(BStrUtils.isEmpty(phoneCode)){
                MToast.makeText(this,"手机号不能为空",Toast.LENGTH_LONG);
                return;
            }
            String bankCode = mPankcardCode.getText().toString();
            if(BStrUtils.isEmpty(bankCode)){
                MToast.makeText(this,"银行号不能为空",Toast.LENGTH_LONG);
                return;
            }
            getPresenter().getVerificationCodeFormServer(bindingBankCardBean,phoneCode,bankCode, new BasePublicFundPresenter.PreSenterCallBack<String>() {
                @Override
                public void even(String s) {

                    BankListOfJZSupport bankListOfJZSupport = new Gson().fromJson(s,BankListOfJZSupport.class);
                    if(bankListOfJZSupport != null){
                        String code = bankListOfJZSupport.getErrorCode();
                        if(PublicFundContant.REQEUST_SUCCESS.equals(code)){
                            // 发送成功
                            MToast.makeText(BindingBankCardOfPublicFundActivity.this,"验证码发送成功", Toast.LENGTH_LONG);
                        }else if(PublicFundContant.REQEUSTING.equals(code)){
                            MToast.makeText(BindingBankCardOfPublicFundActivity.this,"处理中", Toast.LENGTH_LONG);
                        }else{
                            MToast.makeText(BindingBankCardOfPublicFundActivity.this,bankListOfJZSupport.getErrorMessage(), Toast.LENGTH_LONG);
                            getVerificationCode.setTag(TIME,null);
                            timer.cancel();
                        }
                    }

                }

                @Override
                public void field(String errorCode, String errorMsg) {
                    getVerificationCode.setTag(TIME,null);
                    timer.cancel();
                }
            });
        }


    }

    /**
     *  完成绑定
     */
    private void finshBanding(){
        String phoneCode =  mPhoneCode.getText().toString();
        if(BStrUtils.isEmpty(phoneCode)){
            MToast.makeText(this,"手机号不能为空",Toast.LENGTH_LONG);
            return;
        }

        String verificationCode = mVerificationCode.getText().toString();
        if(BStrUtils.isEmpty(verificationCode)){
            MToast.makeText(this,"验证码不能为空",Toast.LENGTH_LONG);
            return;
        }

        String payBankName = mPayBankName.getText().toString();
        if(BStrUtils.isEmpty(payBankName)){
            MToast.makeText(this,"请选择银行",Toast.LENGTH_LONG);
            return;
        }

        String bankCode = mPankcardCode.getText().toString();
        if(BStrUtils.isEmpty(bankCode)){
            MToast.makeText(this,"银行号不能为空",Toast.LENGTH_LONG);
            return;
        }

        getPresenter().sureBind(bindingBankCardBean,payBankName,bankCode,phoneCode,verificationCode, new BasePublicFundPresenter.PreSenterCallBack<String>() {
            @Override
            public void even(String s) {
                BankListOfJZSupport bankListOfJZSupport = new Gson().fromJson(s,BankListOfJZSupport.class);
                if(bankListOfJZSupport != null){
                    String code = bankListOfJZSupport.getErrorCode();
                    if(PublicFundContant.REQEUST_SUCCESS.equals(code)){ // 成功

                        MToast.makeText(BindingBankCardOfPublicFundActivity.this,"绑定成功", Toast.LENGTH_LONG);
                        // 去风险测评页面
                        UiSkipUtils.gotoPublicFundRisk(BindingBankCardOfPublicFundActivity.this);
                        RxBus.get().post(RxConstant.REFRESH_PUBLIC_FUND_INFO, 10);
                        finish();
                    }else if(PublicFundContant.REQEUSTING.equals(code)){
                        MToast.makeText(BindingBankCardOfPublicFundActivity.this,"处理中", Toast.LENGTH_LONG);
                    }else{
                        MToast.makeText(BindingBankCardOfPublicFundActivity.this,bankListOfJZSupport.getErrorMessage(), Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void field(String errorCode, String errorMsg) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 上次退出页面时,验证码的时间点
        if(SPreference.getString(BindingBankCardOfPublicFundActivity.this,LAST_VERIFICATION_TIME) != null){
            long lastTime = Long.valueOf(SPreference.getString(BindingBankCardOfPublicFundActivity.this,LAST_VERIFICATION_TIME));
            int time = (int) (System.currentTimeMillis() - lastTime);
            if(time > 1000 && time < 60*1000){
                sendVerificationCode(getVerificationCode,time/1000);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(getVerificationCode.getTag(TIME)!=null && (Integer)getVerificationCode.getTag(TIME) > 1){
            SPreference.putString(BindingBankCardOfPublicFundActivity.this,LAST_VERIFICATION_TIME,""+System.currentTimeMillis());
            getVerificationCode.setTag(TIME,null);
            Timer timer = (Timer) getVerificationCode.getTag();
            timer.cancel();
        }
    }
}
