package app.mall.com.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.SpannableUtils;
import com.cgbsoft.lib.widget.MToast;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;
import com.iapppay.alpha.interfaces.callback.IPayResultCallback;
import com.iapppay.alpha.sdk.main.IAppPay;
import com.iapppay.alpha.sdk.main.IAppPayOrderUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.mall.com.PayConfig;
import app.mall.com.model.PayMethod;
import app.mall.com.model.RechargeConfigBean;
import app.mall.com.mvp.contract.PayContract;
import app.mall.com.mvp.presenter.PayPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qcloud.mall.R;
import qcloud.mall.R2;

@Route(RouteConfig.MALL_PAY)
public class PayActivity extends BaseActivity<PayPresenter> implements PayContract.View {

    @BindView(R2.id.pay_paynumber)
    TextView payPaynumber;
    private ImageView title_left;
    private TextView title_mid;
    private PayMethod payMethod;
    private String cpprivateinfo;

    @BindView(R2.id.pay_yundou_edit)
    EditText pay_yundou_edit;

    @BindView(R2.id.pay_yundou_zengsong_txt)
    TextView pay_yundou_zengsong_txt;

    @BindView(R2.id.pay_yundou_edit_bt)
    ImageView pay_yundou_edit_bt;

    @BindView(R2.id.recharge_up_bt1)
    TextView recharge_up_bt1;

    @BindView(R2.id.recharge_up_bt2)
    TextView recharge_up_bt2;

    @BindView(R2.id.recharge_up_bt3)
    TextView recharge_up_bt3;

    @BindView(R2.id.recharge_up_bt1_tag)
    TextView recharge_up_bt1_tag;

    @BindView(R2.id.recharge_up_bt2_tag)
    TextView recharge_up_bt2_tag;

    @BindView(R2.id.recharge_up_bt3_tag)
    TextView recharge_up_bt3_tag;

    @BindView(R2.id.chongzhi_down_lay_bt1)
    View recharge_down_lay_bt1;

    @BindView(R2.id.chongzhi_down_lay_bt2)
    View recharge_down_lay_bt2;

    @BindView(R2.id.chongzhi_down_lay_bt3)
    View recharge_down_lay_bt3;

    @BindView(R2.id.pay_yd)
    TextView pay_yundou_queding;

    @BindView(R2.id.yd_hint)
    LinearLayout ydHint;

    @BindView(R2.id.comment_leftright_lay_right)
    TextView comment_leftright_lay_right;

    @BindView(R2.id.pay_yundou_select_method)
    View pay_yundou_select_method;

    @BindView(R2.id.pay_method_txt)
    TextView pay_method_txt;

    @BindView(R2.id.pay_method_iv)
    ImageView pay_method_iv;

    @BindView(R2.id.title_mid)
    TextView titleMid;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    private int totleCount = 500;

    private List<TextView> up_bttag_txts = new ArrayList<>();

    private List<TextView> up_bt_txts = new ArrayList<>();
    private String cporderid;
    private String payConfig = "";

    private RechargeConfigBean rechargeConfigBean = new RechargeConfigBean();
    private Context context = PayActivity.this;


    @Override
    protected int layoutID() {
        if (AppManager.isInvestor(this)) {
            return R.layout.activity_recharge_c;
        } else {
            return R.layout.activity_pay;
        }
//        return R.layout.activity_pay;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        IAppPay.init(this, IAppPay.PORTRAIT, PayConfig.appid);
        getPresenter().getRechargeConfig();
        up_bt_txts.add(recharge_up_bt1);
        up_bt_txts.add(recharge_up_bt2);
        up_bt_txts.add(recharge_up_bt3);
        titleMid.setText(getResources().getString(R.string.ydRecharge));

        up_bttag_txts.add(recharge_up_bt1_tag);
        up_bttag_txts.add(recharge_up_bt2_tag);
        up_bttag_txts.add(recharge_up_bt3_tag);
        changePayNumber("301");
        pay_yundou_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int count = Integer.parseInt(s.toString());
                    if (AppManager.isInvestor(PayActivity.this)){
                        pay_yundou_queding.setBackgroundColor(0xffbf9b69);
                    }else {
                        ydHint.setVisibility(count < 500 ? View.VISIBLE : View.GONE);
                        pay_yundou_queding.setBackgroundColor(count < 500 ? 0xffbebebe : 0xffea1202);
                    }

                    if (rechargeConfigBean.getLevels() != null) {
                        if (count >= rechargeConfigBean.getLevels().get(2).getYdAmount()) {
                            if (rechargeConfigBean.getLevels().get(2).getDonationRatio() != 0.0) {
                                pay_yundou_zengsong_txt.setText("额外赠送" + BStrUtils.floatToint(context, (float) (count * rechargeConfigBean.getLevels().get(2).getDonationRatio())) + "云豆");
                                pay_yundou_zengsong_txt.setTextColor(0xffea1202);
                            }
                            totleCount = BStrUtils.floatToint(context, (float) (count * rechargeConfigBean.getLevels().get(2).getDonationRatio())) + count;
                        } else if (count >= rechargeConfigBean.getLevels().get(1).getYdAmount()) {
                            if (rechargeConfigBean.getLevels().get(1).getDonationRatio() != 0.0) {
                                pay_yundou_zengsong_txt.setText("额外赠送" + BStrUtils.floatToint(context, (float) (count * rechargeConfigBean.getLevels().get(1).getDonationRatio())) + "云豆");
                                pay_yundou_zengsong_txt.setTextColor(0xffea1202);
                            }
                            totleCount = BStrUtils.floatToint(context, (float) (count * rechargeConfigBean.getLevels().get(1).getDonationRatio())) + count;
                        } else if (count >= rechargeConfigBean.getLevels().get(0).getYdAmount()) {
                            if (rechargeConfigBean.getLevels().get(0).getDonationRatio() != 0.0) {
                                pay_yundou_zengsong_txt.setText("额外赠送" + BStrUtils.floatToint(context, (float) (count * rechargeConfigBean.getLevels().get(0).getDonationRatio())) + "云豆");
                                pay_yundou_zengsong_txt.setTextColor(0xffea1202);
                            }
                            totleCount = BStrUtils.floatToint(context, (float) (count * rechargeConfigBean.getLevels().get(0).getDonationRatio())) + count;
                        } else {
                            pay_yundou_zengsong_txt.setText("云豆");
                            pay_yundou_zengsong_txt.setTextColor(0xff000000);
                            totleCount = count;
                        }
                    }
                    CLickSelectbt(-1);
                    comment_leftright_lay_right.setText("¥" + BStrUtils.replacePoint(String.valueOf((float) count / (float) 10)) + "元");
                } catch (Exception e) {
                    comment_leftright_lay_right.setText("¥ " + 0 + "元");
                    pay_yundou_queding.setBackgroundColor(0xffbebebe);
                }
            }
        });

    }

    @Override
    protected PayPresenter createPresenter() {
        return new PayPresenter(this, this);
    }

    @Override
    public void getRechargeConfigSuc(String s) {
        rechargeConfigBean = new Gson().fromJson(s, RechargeConfigBean.class);
        freshData(rechargeConfigBean);
    }

    /**
     * 获取数据后需要刷新数据
     */
    private void freshData(RechargeConfigBean data) {
        //首先刷新上边三个按钮  此处 先判断size大小
        recharge_up_bt1.setText(String.format("%d", data.getLevels().get(0).getYdAmount()));
        recharge_up_bt2.setText(String.format("%d", data.getLevels().get(1).getYdAmount()));
        recharge_up_bt3.setText(String.format("%d", data.getLevels().get(2).getYdAmount()));

        recharge_up_bt1_tag.setText(String.format("%s", String.format("+%s%%", (int) (100 * data.getLevels().get(0).getDonationRatio()))));
        recharge_up_bt2_tag.setText(String.format("%s", String.format("+%s%%", (int) (100 * data.getLevels().get(1).getDonationRatio()))));
        recharge_up_bt3_tag.setText(String.format("%s", String.format("+%s%%", (int) (100 * data.getLevels().get(2).getDonationRatio()))));
        payConfig = SPreference.getString(context, "payConfig");
        if (TextUtils.isEmpty(payConfig)) {
            payMethod = rechargeConfigBean.getPayMethodList().get(0);
        } else {
            payMethod = new Gson().fromJson(payConfig, PayMethod.class);
        }
        pay_method_txt.setText(payMethod.getName());
        pay_method_iv.setImageResource(403 == payMethod.getTypeCode() ? R.drawable.pay_weixin_icon : 401 == payMethod.getTypeCode() ? R.drawable.pay_zhifubao_icon : R.drawable.pay_yinlian_icon);
        if (data.getLevels().get(0).getDonationRatio() == 0.0) {
            recharge_up_bt1_tag.setVisibility(View.INVISIBLE);
        }
        if (data.getLevels().get(1).getDonationRatio() == 0.0) {
            recharge_up_bt2_tag.setVisibility(View.INVISIBLE);
        }
        if (data.getLevels().get(2).getDonationRatio() == 0.0) {
            recharge_up_bt3_tag.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void checkRecharge(String s) {
        try {
            JSONObject ja = new JSONObject(s);
            String rechargeCode = ja.getString("rechargeCode");
            if (rechargeCode.equals("1")) {
                Intent intent = new Intent(PayActivity.this, RechargeResultActivity.class);
                intent.putExtra("rechargeResultState", true);
                intent.putExtra("rechargeCount", pay_yundou_edit.getText().toString());
                startActivity(intent);
                PayActivity.this.finish();
                Toast.makeText(PayActivity.this, "云豆充值成功", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(PayActivity.this, RechargeResultActivity.class);
                intent.putExtra("rechargeResultState", false);
                intent.putExtra("rechargeResult", "云豆充值失败");
                startActivity(intent);
                Toast.makeText(PayActivity.this, "云豆充值失败", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rechargeResult(String s) {
        int ydCount = Integer.parseInt(pay_yundou_edit.getText().toString());
        try {
            JSONObject ja = new JSONObject(s);
            cporderid = ja.getString("orderId");
            //关闭软键盘
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            /** 以下参数在文档中有详细介绍*/
            String appuserid = AppManager.getUserId(this);
            cpprivateinfo = "";
            int waresid_open_price = PayConfig.WARES_ID_2;        //开放价格 ===  请传入您后台配置的 商品编号  填错会报“4230 应用状态异常 ”
            String param = getTransdata(appuserid, cpprivateinfo, waresid_open_price, ydCount / 10.0f, cporderid);
            //启动收银台

            IAppPay.startPay(this, param, payMethod.getTypeCode(), mIPayResultCallback);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void after() {
        super.after();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 点击事件需要修改shape背景
     */
    private void CLickSelectbt(int Clickpostion) {

        if (Clickpostion == -1) {
            for (int i = 0; i < rechargeConfigBean.getLevels().size(); i++) {
                if (pay_yundou_edit.getText().toString().equals(String.format("%d", rechargeConfigBean.getLevels().get(i).getYdAmount()))) {
                    up_bt_txts.get(i).setTextColor(getResources().getColor(R.color.app_golden_click));
                    up_bt_txts.get(i).setBackgroundResource(R.drawable.shap_corner_text_red);
                } else {
                    up_bt_txts.get(i).setTextColor(getResources().getColor(R.color.app_golden));
                    up_bt_txts.get(i).setBackgroundResource(R.drawable.shape_corner_txt);
                }
            }
        } else {
            up_bt_txts.get(Clickpostion).setTextColor(getResources().getColor(R.color.app_golden_click));
            for (int i = 0; i < up_bt_txts.size(); i++) {
                up_bt_txts.get(i).setBackgroundResource(i == Clickpostion ? R.drawable.shap_corner_text_red : R.drawable.shape_corner_txt);

                if (i == Clickpostion) {
                    pay_yundou_edit.setText(String.format("%d", rechargeConfigBean.getLevels().get(Clickpostion).getYdAmount()));
                } else {
                    up_bt_txts.get(i).setTextColor(getResources().getColor(R.color.app_golden));
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (101 == requestCode && RESULT_OK == resultCode) {//选择完毕支付方式 进行回显
            payMethod = (PayMethod) data.getSerializableExtra("paymethod");
            pay_method_txt.setText(payMethod.getName());
            pay_method_iv.setImageResource(403 == payMethod.getTypeCode() ? R.drawable.pay_weixin_icon : 401 == payMethod.getTypeCode() ? R.drawable.pay_zhifubao_icon : R.drawable.pay_yinlian_icon);
        }
    }

    /**
     * 添加云豆
     *
     * @param count
     */
    private void addYDCount(int count) {
        if (TextUtils.isEmpty(pay_yundou_edit.getText().toString())) {
            pay_yundou_edit.setText(String.format("%d", count));
        } else {
            pay_yundou_edit.setText(String.format("%d", Integer.parseInt(pay_yundou_edit.getText().toString()) + count));
        }
    }


    @OnClick(R2.id.pay_yundou_select_method)
    public void selectPayMothed() {
        Intent i = new Intent(context, PayMethodActivity.class);
        i.putExtra("payList", (Serializable) rechargeConfigBean.getPayMethodList());
        startActivityForResult(i, 101);
    }

    @OnClick(R2.id.chongzhi_down_lay_bt3)
    public void selRechargeCount3() {
        btnKuaisuDatasite("+1000");
        addYDCount(1000);
    }

    @OnClick(R2.id.chongzhi_down_lay_bt2)
    public void selRechargeCount2() {
        btnKuaisuDatasite("+100");
        addYDCount(100);
    }

    @OnClick(R2.id.chongzhi_down_lay_bt1)
    public void selRechargeCount1() {
        btnKuaisuDatasite("+10");
        addYDCount(10);
    }

    @OnClick(R2.id.recharge_up_bt3)
    public void selRechargeCountUp3() {
        btnKuaisuDatasite(String.format("%d", rechargeConfigBean.getLevels().get(2).getYdAmount()));
        CLickSelectbt(2);
    }

    @OnClick(R2.id.recharge_up_bt2)
    public void selRechargeCountUp2() {
        btnKuaisuDatasite(String.format("%d", rechargeConfigBean.getLevels().get(1).getYdAmount()));
        CLickSelectbt(1);
    }

    @OnClick(R2.id.recharge_up_bt1)
    public void selRechargeCountUp1() {
        btnKuaisuDatasite(String.format("%d", rechargeConfigBean.getLevels().get(0).getYdAmount()));
        CLickSelectbt(0);
    }

    @OnClick(R2.id.pay_yd)
    public void Recharge() {
        payDataStaist();

        if (payMethod != null) {
            startPay(payMethod.getTypeCode());
        } else {
            new MToast(this).show("获取支付配置失败", Toast.LENGTH_LONG);
        }
    }


    /**
     * 支付回调
     */
    IPayResultCallback mIPayResultCallback = new IPayResultCallback() {

        //@Override
        public void onPayResult(int resultCode, String signvalue, String resultInfo) {
            // TODO Auto-generated method stub

            switch (resultCode) {
                case IAppPay.PAY_SUCCESS:

                    int signIndex = signvalue.indexOf("&sign=");
                    int signTypeIndex = signvalue.indexOf("&signtype=");
                    final String transData = URLDecoder.decode(signvalue.substring("transdata=".length(), signIndex));
                    String sign = URLDecoder.decode(signvalue.substring(signIndex + "&sign=".length(), signTypeIndex));
                    String signType = signvalue.substring(signTypeIndex + "&signtype=".length());

                    HashMap<String, Object> params = new HashMap<>();
                    params.put("transdata", transData);
                    params.put("sign", sign);
                    params.put("signtype", signType);
                    getPresenter().checkRechargeResult(params);
//                    boolean isPaySuccess = IAppPayOrderUtils.checkPayResult(signvalue, PayConfig.publicKey);
//                    if (isPaySuccess) {
//                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(PayActivity.this, "支付成功但验证签失败", Toast.LENGTH_LONG).show();
//                    }
                    break;
                default:
                    Toast.makeText(PayActivity.this,
                            (TextUtils.isEmpty(resultInfo) ? "未知错误" : resultInfo), Toast.LENGTH_LONG)
                            .show();
                    Intent intent = new Intent(PayActivity.this, RechargeResultActivity.class);
                    intent.putExtra("rechargeResultState", false);
                    intent.putExtra("rechargeResult", resultInfo);
                    startActivity(intent);
                    break;
            }
            Log.d("TAG", "requestCode:" + resultCode + ",signvalue:" + signvalue + ",resultInfo:" + resultInfo);
        }

    };

    /**
     * 发起支付
     */
    public void startPay(int payMethod) {
        int ydCount = Integer.parseInt(pay_yundou_edit.getText().toString());
        if (!AppManager.isInvestor(this)){
            if (ydCount < 500) {
                Toast.makeText(this, "充值金额不能小于500云豆", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        float iprice = 0;
        try {
            iprice = ydCount / 10.0f;
        } catch (Exception e) {
            Toast.makeText(this, "金额不合法", Toast.LENGTH_LONG).show();
            return;
        }

        ArrayMap<String, Object> params = new ArrayMap<>();
        params.put("rechargeAmount", BStrUtils.holdOnePoint(ydCount / 10.0f));
        params.put("ydAmount", totleCount);
        getPresenter().ydRecharge(params);
    }


    /**
     * 获取收银台参数
     */
    private String getTransdata(String appuserid, String cpprivateinfo, int waresid, float price, String cporderid) {
        //调用 IAppPayOrderUtils getTransdata() 获取支付参数
        IAppPayOrderUtils orderUtils = new IAppPayOrderUtils();
        orderUtils.setAppid(PayConfig.appid);
        orderUtils.setWaresid(waresid);
        orderUtils.setCporderid(cporderid);
        orderUtils.setAppuserid(appuserid);
        orderUtils.setPrice(price);//单位 元
        if (AppManager.isInvestor(this)) {
            orderUtils.setWaresname("云豆-安卓-C");//开放价格名称(用户可自定义，如果不传以后台配置为准)
        } else {
            orderUtils.setWaresname("云豆-安卓-B");//开放价格名称(用户可自定义，如果不传以后台配置为准)
        }
        orderUtils.setCpprivateinfo(cpprivateinfo);
        return orderUtils.getTransdata(PayConfig.privateKey);
    }


    /**
     * 埋点
     */
    private void payDataStaist() {
        if (AppManager.isInvestor(this)) {
            DataStatistApiParam.RechargeButton();
        } else {
            DataStatistApiParam.Pay_B_BtClick();
        }
    }

    private void btnKuaisuDatasite(String s) {
        if (AppManager.isInvestor(this)) {
            DataStatistApiParam.RechargeNum(s);
        } else {
            DataStatistApiParam.Pay_B_KuaiXuan(s);
        }
    }

    private void changePayNumber(String paynumber) {
        if (BStrUtils.isEmpty(paynumber)) return;
        String number = "¥" + paynumber;
        String numberFront = "支付金额：";
        String changeNumber = numberFront + number;
        payPaynumber.setText(SpannableUtils.setTextForeground(changeNumber, numberFront.length(), changeNumber.length()-1, R.color.app_golden));
    }

}
