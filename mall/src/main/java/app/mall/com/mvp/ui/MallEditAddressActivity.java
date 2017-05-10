package app.mall.com.mvp.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;

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
public class MallEditAddressActivity extends BaseActivity<MallPresenter> implements MallContract.View {

    //地址
    @BindView(R2.id.mall_receiving_address)
    EditText et_recever_address;
    //电话
    @BindView(R2.id.mall_receiving_phone)
    EditText et_recever_phone;
    //姓名
    @BindView(R2.id.mall_receiving_name)
    EditText et_recever_name;
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
    }

    @OnClick(R2.id.mall_address_save_msg)
    void save() {
        String address = et_recever_address.getText().toString().trim();
        String phone = et_recever_phone.getText().toString().trim();
        String name = et_recever_name.getText().toString().trim();
        if (addressBean != null) { //如果有传入值不是新增
            addressBean.setAddress(address);
            addressBean.setPhone(phone);
            addressBean.setShopping_name(name);
            getPresenter().saveMallAddress(addressBean);
        } else {
            getPresenter().addMaddAddress(
                    new MallAddressBean(
                            name,
                            address,
                            phone,
                            1,
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

}
