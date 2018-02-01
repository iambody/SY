package com.cgbsoft.privatefund.public_fund;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.privatefund.R;
import com.chenenyu.router.annotation.Route;

/**
 * Created by wangpeng on 18-1-29.
 */
@Route(RouteConfig.GOTO_PUBLIC_FUND_BUY)
public class BuyPublicFundActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG_FUND_CODE = "tag_fund_code";
    public static final String TAG_FUND_RISK_LEVEL = "tag_fund_risk_level";
    private Button buyConfirm;
    private ImageView bankIcon;
    private TextView bankName;
    private TextView bankTailCode;
    private EditText buyInput;

    private String fundName; // 基金名字
    private String fundCode; // 基金号
    private String rate; // 费率
    private String profitDate; // 收益日期
    private String limitOfDay; //银行卡每日限额
    private String limitOfSingle; //银行卡单笔限额
    private String unit = "元"; //银行卡单笔限额

    private PayPasswordDialog payPasswordDialog;

    @Override
    protected int layoutID() {
        return R.layout.activity_buy_publicfund;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
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
        Imageload.display(this.getApplicationContext(), "", bankIcon);

        buyConfirm.setOnClickListener(this);

    }


    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Confirm:
                String inputText = buyInput.getText().toString();
                if (BStrUtils.isEmpty(inputText)) {
                    Toast.makeText(this, "请输入买入基金的金额", Toast.LENGTH_LONG).show();
                    return;
                }
                if (payPasswordDialog == null) {
                    payPasswordDialog = new PayPasswordDialog(this, null, fundName, inputText + unit);
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
     * 开始支付
     *
     * @param psw
     */
    private void starPay(String psw) {

    }
}
