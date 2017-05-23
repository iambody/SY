package app.mall.com.mvp.ui;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.chenenyu.router.annotation.Route;

import java.util.ArrayList;

import app.mall.com.model.MallAddressBean;
import app.mall.com.mvp.contract.MallContract;
import app.mall.com.mvp.presenter.MallPresenter;
import butterknife.BindView;
import butterknife.OnClick;
import qcloud.mall.R;
import qcloud.mall.R2;

/**
 * 商城编辑地址页面
 */
@Route("mall_address")
public class MallEditAddressActivity extends BaseActivity<MallPresenter> implements MallContract.View, TextWatcher {

    //地址
    @BindView(R2.id.mall_receiving_address)
    EditText et_recever_address;
    //电话
    @BindView(R2.id.mall_receiving_phone)
    EditText et_recever_phone;
    //姓名
    @BindView(R2.id.mall_receiving_name)
    EditText et_recever_name;
    @BindView(R2.id.title_mid)
    TextView titleMid;
    //保存
    @BindView(R2.id.mall_address_save_msg)
    Button btn_address_save;
    private MallAddressBean addressBean;

    @Override
    protected int layoutID() {
        return R.layout.activity_mall_edit_address;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        addressBean = (MallAddressBean) getIntent().getSerializableExtra("addressBean");
        if (addressBean != null) {
            et_recever_name.setText(addressBean.getShopping_name());
            et_recever_address.setText(addressBean.getAddress());
            et_recever_phone.setText(addressBean.getPhone());
            btn_address_save.setEnabled(true);
        } else {
            btn_address_save.setEnabled(false);
        }

        et_recever_name.addTextChangedListener(this);
        et_recever_address.addTextChangedListener(this);
        et_recever_phone.addTextChangedListener(this);
        if (addressBean != null) {
            titleMid.setText("编辑收货地址");
        }else{
            titleMid.setText("新增收货地址");
        }
    }

    @Override
    protected void after() {
        super.after();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @OnClick(R2.id.mall_address_save_msg)
    public void save() {
        String address = et_recever_address.getText().toString().trim();
        String phone = et_recever_phone.getText().toString().trim();
        String name = et_recever_name.getText().toString().trim();
        if (addressBean != null) { //如果有传入值不是新增
            addressBean.setAddress(address);
            addressBean.setPhone(phone);
            addressBean.setShopping_name(name);
            getPresenter().saveMallAddress(addressBean);

        } else {
            getPresenter().addMallAddress(
                    new MallAddressBean(
                            name,
                            address,
                            phone,
                            "1",
                            "")
            );
        }
    }

    @Override
    protected MallPresenter createPresenter() {
        return new MallPresenter(this, this);
    }


    @Override
    public void saveAddressSucc(MallAddressBean model) {
        this.finish();
    }

    @Override
    public void saveAddressErr(String s) {

    }

    @Override
    public void getMallAddressLitSuc(ArrayList<MallAddressBean> list) {

    }

    @Override
    public void deleteSuc(String id) {

    }

    @Override
    public void setDefaultSuc(String id) {

    }

    @Override
    public void addAddressSuc(MallAddressBean mallAddressBean) {
        this.finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(et_recever_name.getText()) || TextUtils.isEmpty(et_recever_phone.getText()) || TextUtils.isEmpty(et_recever_address.getText())) {
            btn_address_save.setEnabled(false);
        } else {
            btn_address_save.setEnabled(true);
        }
    }
}
