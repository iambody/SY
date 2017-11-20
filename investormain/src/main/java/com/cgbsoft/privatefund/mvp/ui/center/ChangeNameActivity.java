package com.cgbsoft.privatefund.mvp.ui.center;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.model.CredentialStateMedel;
import com.cgbsoft.privatefund.mvp.contract.center.PersonalInformationContract;
import com.cgbsoft.privatefund.mvp.presenter.center.PersonalInformationPresenterImpl;
import com.chenenyu.router.annotation.Route;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sunfei on 2017/7/12 0012.
 */
@Route(RouteConfig.GOTO_CHANGE_USERNAME_ACTIVITY)
public class ChangeNameActivity extends BaseActivity<PersonalInformationPresenterImpl> implements PersonalInformationContract.PersonalInformationView{

    @BindView(R.id.title_left)
    ImageView back;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.title_right_newc)
    TextView rightTV;
    @BindView(R.id.et_new_name)
    EditText newName;
    private UserInfoDataEntity.UserInfo userInfo;
    private LoadingDialog mLoadingDialog;

    @OnClick(R.id.title_left)
    public void clickBack() {
        this.finish();
    }
    @OnClick(R.id.iv_edit_text_clear)
    public void clearEditText(){
        newName.setText("");
    }
    @OnClick(R.id.title_right_newc)
    public void changeDone(){
        String newNameStr = newName.getText().toString();
        if (TextUtils.isEmpty(newNameStr)) {
            Toast.makeText(getApplicationContext(), "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        getPresenter().updateUserInfoToServer(newNameStr,null!=userInfo?userInfo.getSex():"",null!=userInfo?userInfo.getBirthday():"");
    }
    @Override
    protected int layoutID() {
        return R.layout.activity_changename;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        back.setVisibility(View.VISIBLE);
        titleTV.setText(getResources().getString(R.string.personal_information_title_changename));
        rightTV.setVisibility(View.VISIBLE);
        mLoadingDialog = LoadingDialog.getLoadingDialog(baseContext, "", false, false);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        userInfo = AppManager.getUserInfo(baseContext);
        rightTV.setEnabled(false);
        newName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();
                if (length > 0) {
                    rightTV.setEnabled(true);
                    rightTV.setTextColor(getResources().getColor(R.color.black));
                } else {
                    rightTV.setEnabled(false);
                    rightTV.setTextColor(getResources().getColor(R.color.color_6a6a6a));
                }
            }
        });
        String name = getIntent().getStringExtra("name");
        if (!TextUtils.isEmpty(name)) {
            newName.setText(name);
            newName.setSelection(name.length());
//            rightTV.setEnabled(true);
        }
    }

    @Override
    protected PersonalInformationPresenterImpl createPresenter() {
        return new PersonalInformationPresenterImpl(baseContext,this);
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
    public void updateSuccess() {
        if (null != userInfo) {
            String newUserName = newName.getText().toString();
            userInfo.setNickName(newUserName);
            AppInfStore.saveUserInfo(baseContext,userInfo);
            clearEditText();
            setResult(RESULT_OK);
            this.finish();
        }
    }

    @Override
    public void updateError(Throwable error) {
        Toast.makeText(getApplicationContext(),"更新失败",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadImgSuccess(String imgRemotePath) {

    }

    @Override
    public void uploadImgError(Throwable error) {

    }

    @Override
    public void verifyIndentitySuccess(boolean hasIndentity, boolean hasUpload, String indentityCode, String title, String credentialCode, String status, String statusCode) {

    }

    @Override
    public void verifyIndentityError(Throwable error) {

    }

    @Override
    public void verifyIndentityV3Success(CredentialStateMedel credentialStateMedel) {

    }

}
