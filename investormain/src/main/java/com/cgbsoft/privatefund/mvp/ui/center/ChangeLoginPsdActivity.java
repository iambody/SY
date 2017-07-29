package com.cgbsoft.privatefund.mvp.ui.center;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.MD5Utils;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.center.ChangePsdContract;
import com.cgbsoft.privatefund.mvp.presenter.center.ChangePsdPresenterImpl;
import com.chenenyu.router.annotation.Route;

import butterknife.BindView;
import butterknife.OnClick;

import static android.R.attr.password;

/**
 * Created by sunfei on 2017/6/29 0029.
 */
@Route(RouteConfig.GOTO_CHANGE_PSD_ACTIVITY)
public class ChangeLoginPsdActivity extends BaseActivity<ChangePsdPresenterImpl> implements ChangePsdContract.ChangePsdView {
    @BindView(R.id.title_left)
    protected ImageView back;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.et_psd_old)
    EditText oldPsd;
    @BindView(R.id.et_psd_new)
    EditText newPsd;
    @BindView(R.id.et_psd_confirm_new)
    EditText newConfirm;
    @BindView(R.id.til_psd_old)
    TextInputLayout tilOldPsd;
    private LoadingDialog mLoadingDialog;

    @Override
    protected int layoutID() {
        return R.layout.activity_changelogin_psd;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        back.setVisibility(View.VISIBLE);
        titleTV.setText(getResources().getString(R.string.setting_item_change_pwd));
        initView(savedInstanceState);
        DataStatistApiParam.changePsdPage();
    }

    private void initView(Bundle savedInstanceState) {
//        AppManager.getu
        mLoadingDialog = LoadingDialog.getLoadingDialog(baseContext, "", false, false);
    }

    @Override
    protected ChangePsdPresenterImpl createPresenter() {
        return new ChangePsdPresenterImpl(baseContext,this);
    }
    @OnClick(R.id.title_left)
    public void clickBack(){
        this.finish();
    }

    /**
     * 修改登录密码
     */
    @OnClick(R.id.bt_change_psd)
    public void submitChangeRequest(){
        String oldPsdStr = oldPsd.getText().toString();
        String newPsdStr = newPsd.getText().toString();
        String newConfirm = this.newConfirm.getText().toString();
        if (TextUtils.isEmpty(oldPsdStr)) {
            showToast("请输入原始密码");
            return;
        }
        if (TextUtils.isEmpty(newPsdStr)) {
            showToast("请输入新密码");
            return;
        }
        if (TextUtils.isEmpty(newConfirm)) {
            showToast("请确认新密码");
            return;
        }
        if (!newPsdStr.equals(newConfirm)) {
            showToast("二次新密码不一致,请确认新密码");
            return;
        }
        if (oldPsdStr.length() > 16 || oldPsdStr.length() < 6) {
            MToast.makeText(getApplicationContext(), getString(R.string.pwd_noright_str), Toast.LENGTH_SHORT);
            return;
        }
        if (newConfirm.length() > 16 || newConfirm.length() < 6) {
            MToast.makeText(getApplicationContext(), getString(R.string.pwd_noright_str), Toast.LENGTH_SHORT);
            return;
        }
        getPresenter().submitChangeRequest(AppManager.getUserId(baseContext), MD5Utils.getShortMD5(oldPsdStr),MD5Utils.getShortMD5(newConfirm));
        DataStatistApiParam.changePsdPageSubmit();
    }
    private void showToast(String content){
        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_SHORT).show();
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
    public void changePsdSuccess() {
        showToast("登录密码修改成功");
        this.finish();
    }

    @Override
    public void changePsdError(Throwable error) {
        showToast(error.getMessage());
    }
}
