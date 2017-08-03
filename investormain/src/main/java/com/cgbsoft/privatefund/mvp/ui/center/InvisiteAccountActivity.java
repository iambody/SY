package com.cgbsoft.privatefund.mvp.ui.center;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.lib.widget.ClearEditText;
import com.cgbsoft.lib.widget.OnWheelChangedListener;
import com.cgbsoft.lib.widget.WheelAdapter;
import com.cgbsoft.lib.widget.WheelView;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.contract.home.InvisiteAccountContract;
import com.cgbsoft.privatefund.mvp.presenter.home.InvisiteAccountPresenter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenlong
 */
public class InvisiteAccountActivity extends BaseActivity<InvisiteAccountPresenter> implements InvisiteAccountContract.View {

    @BindView(R.id.title_left)
    ImageView backImage;

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

    @BindView(R.id.wheelview)
    WheelView wheelView;

    @BindView(R.id.gunlun_ll)
    LinearLayout linearLayout;

    private String accountType;
    private boolean loading;

    private List<String> mList;

    @Override
    protected int layoutID() {
        return R.layout.activity_invisite_account;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        isBindAdviser = !TextUtils.isEmpty(AppManager.getUserInfo(this).getToC().bandingAdviserId);
        accountType = AppManager.getUserInfo(this).getToC().getCustomerIdType();
        titleMid.setText(getResources().getString(R.string.datum_manage_account));
        titleRight.setText(R.string.rc_confirm);
        backImage.setVisibility(View.VISIBLE);
        mList =  Arrays.asList(getResources().getStringArray(R.array.select_identify));
        initView();
    }

    private void initView() {
        if (!TextUtils.isEmpty(accountType)) {
            realName.setEnabled(false);
            certifyType.setEnabled(false);
            certifyNumber.setEnabled(false);
        }
        realName.setText(AppManager.getUserInfo(this).getToC().getCustomerName());
        certifyType.setText(AppManager.getUserInfo(this).getToC().getCustomerIdType());
        certifyNumber.setText(AppManager.getUserInfo(this).getToC().getCustomerIdNumber());
        titleRight.setVisibility(!TextUtils.isEmpty(accountType) ? View.GONE : View.VISIBLE);
        invisiteCertifyPrompt.setVisibility(!TextUtils.isEmpty(accountType) ? View.GONE : View.VISIBLE);
        linearLayout.setVisibility(!TextUtils.isEmpty(accountType) ? View.GONE : View.VISIBLE);
        realName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ViewUtils.showInputMethod(realName);
                linearLayout.setVisibility(View.GONE);
            }
        });
        certifyType.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ViewUtils.hideInputMethod(certifyType);
            }
            linearLayout.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
        });
        certifyNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ViewUtils.showInputMethod(realName);
                linearLayout.setVisibility(View.GONE);
            }
        });
        wheelView.addChangingListener((wheel, oldValue, newValue) -> {
            certifyType.setText(mList.get(newValue));
        });
        wheelView.setAdapter(new MyAdapter());
    }

    class MyAdapter implements WheelAdapter {

        @Override
        public int getItemsCount() {
            return mList.size();
        }

        @Override
        public String getItem(int index) {
           return mList.get(index);
        }

        @Override
        public int getMaximumLength() {
            return mList.size();
        }
    }

    @Override
    protected InvisiteAccountPresenter createPresenter() {
        return new InvisiteAccountPresenter(this, this);
    }

    @OnClick(R.id.title_right)
    void commitInvisiteAccount() {
        commitAccount();
    }

    private void commitAccount() {
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
        RxBus.get().post(RxConstant.REFRUSH_USER_INFO_OBSERVABLE, true);
        finish();
    }

    @Override
    public void commitFailure(String errorMsg) {
        loading = false;
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.title_left)
    public void clickBack(){
        this.finish();
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

        if ("身份证".equals(certifyType.getText().toString())) {
            String value = certifyNumber.getText().toString();
            if (!Utils.checkIdentityCode(value)) {
                Toast.makeText(this, String.format(getString(R.string.invisit_account_input_right_prompt), getString(R.string.certify_number)), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
