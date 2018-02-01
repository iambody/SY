package com.cgbsoft.privatefund.mvp.ui.center;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.center.BindBankCardInfoContract;
import com.cgbsoft.privatefund.mvp.presenter.center.BindBankCardInfoPresenterImpl;
import com.chenenyu.router.annotation.Route;

import app.product.com.utils.ViewUtil;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @auther chenlong
 */
@Route(RouteConfig.GOTO_BIND_BANK_CARD_ACTIVITY)
public class BindBankCardInfoActivity extends BaseActivity<BindBankCardInfoPresenterImpl> implements BindBankCardInfoContract.BindBankCardInfoView{

    @BindView(R.id.title_left)
    ImageView back;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.bank_name)
    TextView bank_name;
    @BindView(R.id.bank_type)
    TextView bank_type;
    @BindView(R.id.bank_number)
    TextView bank_number;

    private LoadingDialog mLoadingDialog;

    @OnClick(R.id.title_left)
    public void clickBack() {
        this.finish();
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_bank_card_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        back.setVisibility(View.VISIBLE);
        titleTV.setText(getResources().getString(R.string.public_fund_setting_bankcard_info));
        mLoadingDialog = LoadingDialog.getLoadingDialog(baseContext, "", false, false);
        getPresenter().requestBindBankCardInfo();
    }

    @Override
    protected BindBankCardInfoPresenterImpl createPresenter() {
        return new BindBankCardInfoPresenterImpl(this, this);
    }

    @Override
    public void showLoadDialog() {
        mLoadingDialog.show();
    }

    @Override
    public void hideLoadDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void requestInfoSuccess(String[] info) {
        hideLoadDialog();
        bank_name.setText(info[0]);
        bank_number.setText(hintLastBankCardNumber(info[1]));
        bank_type.setText(hintLastBankCardNumber(info[2]));
    }

    @Override
    public void requestInfoFailure(String mssage) {
        Toast.makeText(getApplicationContext(), mssage, Toast.LENGTH_SHORT).show();
    }

    private String hintLastBankCardNumber(String identifyNumber) {
        if (!TextUtils.isEmpty(identifyNumber) && identifyNumber.length() > 15) {
            String hintStr = identifyNumber.substring(0, identifyNumber.length() - 4);
            return addSpaceDivideNumber(identifyNumber.replace(hintStr, ViewUtils.productEncodyStr(hintStr)));
        }
        return identifyNumber;
    }

    private String addSpaceDivideNumber(String identifyNumber) {
      StringBuffer sb = new StringBuffer();
      if (!TextUtils.isEmpty(identifyNumber)) {
          char[] chars = identifyNumber.toCharArray();
          for (int i = 0; i < chars.length; i++) {
              sb.append(chars[i]);
              if (i != 0 && i%4 == 0 && i != chars.length - 1) {
                  sb.append(" ");
              }
          }
          return sb.toString();
      }
      return identifyNumber;
    }
}
