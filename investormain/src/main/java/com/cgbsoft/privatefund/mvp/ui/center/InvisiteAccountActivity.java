package com.cgbsoft.privatefund.mvp.ui.center;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.widget.ClearEditText;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.home.InvisiteAccountContract;
import com.cgbsoft.privatefund.mvp.presenter.home.InvisiteAccountPresenter;
import com.cgbsoft.privatefund.mvp.ui.home.AssetProveActivity;
import com.cgbsoft.privatefund.mvp.ui.home.RelativeAssetActivity;
import com.cgbsoft.privatefund.mvp.ui.home.RiskEvaluationActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenlong
 */
public class InvisiteAccountActivity extends BaseActivity<InvisiteAccountPresenter> implements InvisiteAccountContract.View {

    @BindView(R.id.title_mid)
    TextView titleMid;

    @BindView(R.id.title_right)
    TextView titleRight;

    @BindView(R.id.invisite_realname)
    ClearEditText realName;

    @BindView(R.id.invisite_certifiy_type)
    ClearEditText certifyType;

    @BindView(R.id.invisite_certifiy_number)
    ClearEditText certifyNumber;

    @BindView(R.id.invisite_certifiy_prompt)
    TextView invisiteCertifyPrompt;

    private boolean isBindAdviser;
    private boolean loading;

    @Override
    protected int layoutID() {
        return R.layout.activity_invisite_account;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        isBindAdviser = !TextUtils.isEmpty(AppManager.getUserInfo(this).getToC().bandingAdviserId);
        titleMid.setText(getResources().getString(R.string.datum_manage_account));
        titleRight.setText(R.string.rc_confirm);
        initView();
    }

    private void initView() {
        if (isBindAdviser) {
            realName.setEnabled(false);
            certifyType.setEnabled(false);
            certifyNumber.setEnabled(false);
            realName.setText(AppManager.getUserInfo(this).getToC().getCustomerName());
            certifyType.setText(AppManager.getUserInfo(this).getToC().getCustomerIdType());
            certifyNumber.setText(AppManager.getUserInfo(this).getToC().getCustomerPhone());
        }
        titleRight.setVisibility(isBindAdviser ? View.GONE : View.VISIBLE);
        invisiteCertifyPrompt.setVisibility(isBindAdviser ? View.GONE : View.VISIBLE);
    }

    @Override
    protected InvisiteAccountPresenter createPresenter() {
        return new InvisiteAccountPresenter(this, this);
    }

    @OnClick(R.id.title_right)
    void commitInvisiteAccount() {
        if (loading) {
            return;
        }
        if (validateEditView()) {
            String name = realName.getText().toString();
            String type = certifyType.getText().toString();
            String typeValue = certifyNumber.getText().toString();
            getPresenter().commitInvisiteAccount(AppManager.getUserId(this), name, type, typeValue);
            loading = true;
        }
    }

    @Override
    public void commitSuccess() {
        loading = false;
        Toast.makeText(this,"投资账号提交成功！", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void commitFailure() {
        loading = false;
        Toast.makeText(this,"投资账号提交失败！", Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.title_left)
    public void clickBack(){
        this.finish();
    }

    @OnClick(R.id.datum_manage_risk)
    public void gotoRiskComment(){
        NavigationUtils.startActivity(this, RiskEvaluationActivity.class);
    }

    @OnClick(R.id.datum_manage_asset_report)
    public void gotoAssetReport(){
        String url = CwebNetConfig.assetReport;
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.push_message_url, url);
        hashMap.put(WebViewConstant.push_message_title, getString(R.string.mine_assert_report));
        NavigationUtils.startActivity(this, BaseWebViewActivity.class, hashMap);
    }

    @OnClick(R.id.datum_manage_asset_certify)
    public void gotoAssetCertify(){
        NavigationUtils.startActivity(this, AssetProveActivity.class);
    }
    @OnClick(R.id.datum_manage_relative_asset)
    public void gotoRelativeAsset(){
        NavigationUtils.startActivity(this, RelativeAssetActivity.class);
    }

    private boolean validateEditView() {
        if (TextUtils.isEmpty(realName.getText().toString())) {
            Toast.makeText(this, String.format(getString(R.string.invisit_account_input_prompt), getString(R.string.real_name)), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(certifyType.getText().toString())) {
            Toast.makeText(this, String.format(getString(R.string.invisit_account_input_prompt), getString(R.string.centify_type)), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(certifyNumber.getText().toString())) {
            Toast.makeText(this, String.format(getString(R.string.invisit_account_input_prompt), getString(R.string.certify_number)), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
