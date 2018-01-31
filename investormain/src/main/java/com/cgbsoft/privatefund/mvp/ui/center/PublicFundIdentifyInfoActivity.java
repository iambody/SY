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
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.center.PublicFundInfoContract;
import com.cgbsoft.privatefund.mvp.presenter.center.PublicFundInfoPresenterImpl;
import com.chenenyu.router.annotation.Route;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @auther chenlong
 */
@Route(RouteConfig.GOTO_PUBLIC_FUND_INFO_ACTIVITY)
public class PublicFundIdentifyInfoActivity extends BaseActivity<PublicFundInfoPresenterImpl> implements PublicFundInfoContract.PublicFundInfoView{

    @BindView(R.id.title_left)
    ImageView back;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.tv_public_fund_info_name)
    TextView tv_public_fund_info_name;
    @BindView(R.id.tv_public_fund_info_identify_number)
    TextView tv_public_fund_info_identify_number;

    private LoadingDialog mLoadingDialog;

    @OnClick(R.id.title_left)
    public void clickBack() {
        this.finish();
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_public_fund_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        back.setVisibility(View.VISIBLE);
        titleTV.setText(getResources().getString(R.string.setting_title));
        mLoadingDialog = LoadingDialog.getLoadingDialog(baseContext, "", false, false);
        getPresenter().requestPublicFundInfo();
    }

    @Override
    protected PublicFundInfoPresenterImpl createPresenter() {
        return new PublicFundInfoPresenterImpl(this, this);
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
    public void requestInfoSuccess(String info) {
        hideLoadDialog();
        Log.i("public_fund_info=", info);
        tv_public_fund_info_name.setText("XINM");
        tv_public_fund_info_identify_number.setText(hintIndentifyNumber("JIEHANGJ"));
    }

    @Override
    public void requestInfoFailure(String mssage) {
        Toast.makeText(getApplicationContext(), mssage, Toast.LENGTH_SHORT).show();
    }

    private String hintIndentifyNumber(String identifyNumber) {
        if (!TextUtils.isEmpty(identifyNumber) && identifyNumber.length() > 14) {
            String hintStr = identifyNumber.substring(6, identifyNumber.length() - 4);
            return identifyNumber.replace(hintStr, "*");
        }
        return identifyNumber;
    }
}
