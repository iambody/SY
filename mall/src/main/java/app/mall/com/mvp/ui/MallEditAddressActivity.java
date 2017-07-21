package app.mall.com.mvp.ui;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.dialog.WheelDialogAddress;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.ui.DialogUtils;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import app.mall.com.model.MallAddressBean;
import app.mall.com.mvp.contract.MallContract;
import app.mall.com.mvp.presenter.MallPresenter;
import butterknife.BindView;
import butterknife.OnClick;
import qcloud.mall.R;
import qcloud.mall.R2;

import static android.view.View.GONE;

/**
 * 商城编辑地址页面
 */
@Route("mall_address")
public class MallEditAddressActivity extends BaseActivity<MallPresenter> implements MallContract.View, TextWatcher, Toolbar.OnMenuItemClickListener {

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

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    @BindView(R2.id.normal_layout_edit)
    LinearLayout normal_layout_edit;

    @BindView(R2.id.normal_layout_add)
    LinearLayout normal_layout_add;

    @BindView(R2.id.set_normal_address)
    TextView set_normal_address;

    @BindView(R2.id.mall_address_area)
    EditText mall_address_area;

    private boolean isAddressNormal;

    @Override
    protected int layoutID() {
        return R.layout.activity_mall_edit_address;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addressBean = (MallAddressBean) getIntent().getSerializableExtra("addressBean");
        if (addressBean != null) {
            et_recever_name.setText(addressBean.getShopping_name());
            et_recever_address.setText(addressBean.getAddress());
            et_recever_phone.setText(addressBean.getPhone());
            btn_address_save.setEnabled(true);
            mall_address_area.setText(addressBean.getRegionAddress());
            if (addressBean.getDefault_flag().equals("1")){
                normal_layout_add.setVisibility(GONE);
                isAddressNormal = true;
            }else {
                normal_layout_edit.setVisibility(GONE);
                isAddressNormal = false;
            }
        } else {
            btn_address_save.setEnabled(false);
        }

        et_recever_name.addTextChangedListener(this);
        et_recever_address.addTextChangedListener(this);
        et_recever_phone.addTextChangedListener(this);
        mall_address_area.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    showAddressDialog();
                }
            }
        });
        mall_address_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressDialog();
            }
        });
        if (addressBean != null) {
            titleMid.setText("编辑地址");
            btn_address_save.setText("保存并使用");
        } else {
            titleMid.setText("添加新地址");
            btn_address_save.setText("添加新地址");
            normal_layout_edit.setVisibility(GONE);
        }

        if (isAddressNormal){
            set_normal_address.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.mall_check_address,0,0,0);
        }else {
            set_normal_address.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.mall_normal_address,0,0,0);
        }
        normal_layout_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddressNormal){
                    isAddressNormal = false;
                    set_normal_address.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.mall_normal_address,0,0,0);
                }else {
                    set_normal_address.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.mall_check_address,0,0,0);
                    isAddressNormal = true;
                }
            }
        });

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
        String area = mall_address_area.getText().toString().trim();
        if (addressBean != null) { //如果有传入值不是新增
            addressBean.setAddress(address);
            addressBean.setPhone(phone);
            addressBean.setShopping_name(name);
            addressBean.setRegionAddress(area);
            if (isAddressNormal){
                addressBean.setDefault_flag("1");
            }else {
                addressBean.setDefault_flag("0");
            }
            getPresenter().saveMallAddress(addressBean);

        } else {
            String defaultAddress = "0";
            if (isAddressNormal){
                defaultAddress = "1";
            }else {
                defaultAddress = "0";
            }
            getPresenter().addMallAddress(
                    new MallAddressBean(
                            name,
                            address,
                            phone,
                            defaultAddress,
                            "",
                            area)
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
        RxBus.get().post(RxConstant.DELETE_ADDRESS,id);
        finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (addressBean != null) {
            getMenuInflater().inflate(com.cgbsoft.lib.R.menu.page_menu, menu);
            MenuItem firstItem = menu.findItem(com.cgbsoft.lib.R.id.firstBtn);
            MenuItem secItem = menu.findItem(com.cgbsoft.lib.R.id.secondBtn);
            firstItem.setTitle("删除");
            secItem.setVisible(false);
            return super.onCreateOptionsMenu(menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (addressBean != null) {
            getPresenter().deleteMallAddress(addressBean.getId());
        }
        return false;
    }
    public void showAddressDialog() {
        List<Map<String, Object>> parentList=null;
        try {
            StringBuilder sb = new StringBuilder();
            InputStream open = getResources().getAssets().open("address.json");
            BufferedReader bis = new BufferedReader(new InputStreamReader(open));
            String line = "";
            while ((line = bis.readLine()) != null) {
                sb.append(line);
            }
            String sbs = sb.toString();
            parentList = new Gson().fromJson(sbs, new TypeToken<List<Map<String, Object>>>() {
            }.getType());
        } catch (IOException e) {
            LogUtils.Log("aaa", "printStackTrace===");
            e.printStackTrace();
        }

        WheelDialogAddress dialogAddress = new WheelDialogAddress(this);
        dialogAddress.setList(parentList);
        dialogAddress.setTitle("选择地区");
        dialogAddress.setConfirmCallback(new WheelDialogAddress.ConfirmListenerInteface() {

            @Override
            public void confirm(Map<String, Object> map) {
                if (map != null) {
                    Iterator<Map.Entry<String, Object>> iterators = map.entrySet().iterator();
                    Map.Entry<String, Object> entry = iterators.next();
                    String key = entry.getKey();
                    if (!TextUtils.isEmpty(key) && key.equals("sub") && iterators.hasNext()) {
                        entry = iterators.next();
                    }
                    String nameChinese = entry.getKey();
                    String nameEnglish = (String) entry.getValue();
                    List<Map<String, Object>> child = (List<Map<String, Object>>) map.get("sub");
                    String childNameChinese = "";
                    String childNameEnglish = "";
                    String districtName = "";
                    String districtNum = "";
                    if (null != child) {
                        String childPositionStr = (String) map.get("child_position");
                        String grandSonPositionStr = (String) map.get("grandson_position");
                        int childPositionInt = Integer.parseInt(childPositionStr);
                        int grandSonPositionInt = Integer.parseInt(grandSonPositionStr);
                        Map<String, Object> childMap = child.get(childPositionInt);
                        Iterator<Map.Entry<String, Object>> iteratorsChild = childMap.entrySet().iterator();
                        Map.Entry<String, Object> childEntry = iteratorsChild.next();
                        String childKey = childEntry.getKey();
                        if (!TextUtils.isEmpty(childKey) && childKey.equals("sub") && iteratorsChild.hasNext()) {
                            childEntry = iteratorsChild.next();
                        }
                        childNameChinese = childEntry.getKey();
                        childNameEnglish = (String) childEntry.getValue();
                        List<Map<String, Object>> grandson = (List<Map<String, Object>>) childMap.get("sub");
                        if (null != grandson) {
                            Map<String, Object> grandSonMap = grandson.get(grandSonPositionInt);
                            Map.Entry<String, Object> grandSonEntry = grandSonMap.entrySet().iterator().next();
                            districtName = grandSonEntry.getKey();
                            districtNum = (String) grandSonEntry.getValue();
                        }
                    }
                    mall_address_area.setText(nameChinese.concat(childNameChinese).concat(districtName));
                }
            }
        });
        dialogAddress.show();
    }
}
