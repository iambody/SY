package com.cgbsoft.privatefund.mvp.ui.center;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.bean.product.PublicFundInf;
import com.cgbsoft.privatefund.mvp.contract.center.PublicFundInfoContract;
import com.cgbsoft.privatefund.mvp.presenter.center.PublicFundInfoPresenterImpl;
import com.chenenyu.router.annotation.Route;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @auther chenlong
 */
@Route(RouteConfig.GOTO_PUBLIC_FUND_INFO_ACTIVITY)
public class PublicFundIdentifyInfoActivity extends BaseActivity<PublicFundInfoPresenterImpl> implements PublicFundInfoContract.PublicFundInfoView {

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
        titleTV.setText(getResources().getString(R.string.public_fund_account_info));

        PublicFundInf publicFundInf = AppManager.getPublicFundInf(getApplicationContext());
//        tv_public_fund_info_name.setText(publicFundInf.getCustFullName());
//        tv_public_fund_info_identify_number.setText(publicFundInf.getCertificateNo());

        BStrUtils.setTv(tv_public_fund_info_name, publicFundInf.getCustFullName());
        BStrUtils.setTv(tv_public_fund_info_identify_number, getStarString(publicFundInf.getCertificateNo(), 9));


//        mLoadingDialog = LoadingDialog.getLoadingDialog(baseContext, "", false, false);
//        getPresenter().requestPublicFundInfo();

    }


    private static String getStarString(String content, int begin, int end) {
        if (BStrUtils.isEmpty(content)) return "";
        if (begin >= content.length() || begin < 0) {
            return content;
        }
        if (end >= content.length() || end < 0) {
            return content;
        }
        if (begin >= end) {
            return content;
        }
        String starStr = "";
        for (int i = begin; i < end; i++) {
            starStr = starStr + "*";
        }
        return content.substring(0, begin) + starStr + content.substring(end, content.length());

    }

    private static String getStarString(String content, int length) {
        if (BStrUtils.isEmpty(content)) return "";

        if (length >= content.length() || length < 0) {
            return content;
        }
        int begin =  (content.length() - length) / 2+1;
        int end = begin + length;
        if (end >= content.length() || end < 0) {
            return content;
        }

        String starStr = "";
        for (int i = begin; i < end; i++) {
            starStr = starStr + "*";
        }
        return content.substring(0, begin) + starStr + content.substring(end, content.length());

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
    public void requestInfoSuccess(String[] info) {
        hideLoadDialog();
        if (info != null) {
            tv_public_fund_info_name.setText(info[0]);
            tv_public_fund_info_identify_number.setText(hintIndentifyNumber(info[1]));
        }
    }

    @Override
    public void requestInfoFailure(String mssage) {
        hideLoadDialog();
        Toast.makeText(getApplicationContext(), mssage, Toast.LENGTH_SHORT).show();
    }

    private String hintIndentifyNumber(String identifyNumber) {
        if (!TextUtils.isEmpty(identifyNumber) && identifyNumber.length() > 14) {
            String hintStr = identifyNumber.substring(5, identifyNumber.length() - 4);
            return identifyNumber.replace(hintStr, ViewUtils.productEncodyStr(hintStr));
        }
        return identifyNumber;
    }
}
