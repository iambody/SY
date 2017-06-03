package app.mall.com.mvp.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.model.MallAddress;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.widget.dialog.MenuDialog;
import com.cgbsoft.lib.widget.recycler.RecyclerControl;
import com.chenenyu.router.annotation.Route;

import java.util.ArrayList;

import app.mall.com.model.MallAddressBean;
import app.mall.com.mvp.adapter.MallListAdapter;
import app.mall.com.mvp.contract.MallContract;
import app.mall.com.mvp.listener.MallAddressListeber;
import app.mall.com.mvp.presenter.MallPresenter;
import butterknife.BindFloat;
import butterknife.BindView;
import butterknife.OnClick;
import qcloud.mall.R;
import qcloud.mall.R2;

@Route("mall_choice_address")
public class MallAddressListActivity extends BaseActivity<MallPresenter> implements MallContract.View, MallAddressListeber {

    @BindView(R2.id.mall_address_list)
    RecyclerView rcv_mall_address_list;
    @BindView(R2.id.title_mid)
    TextView titleMid;

    @BindView(R2.id.mall_new_address)
    Button mall_new_address;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    private MallListAdapter mallListAdapter;
    private ArrayList<MallAddressBean> mallAddressBeans;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected int layoutID() {
        return R.layout.activity_mall_address_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(com.cgbsoft.lib.R.drawable.ic_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getPresenter().getMallAddressList();
        titleMid.setText("收货地址");
        mallListAdapter = new MallListAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_mall_address_list.setLayoutManager(linearLayoutManager);
        rcv_mall_address_list.setAdapter(mallListAdapter);
    }

    @Override
    protected void after() {
        super.after();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
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
    protected void onRestart() {
        super.onRestart();
        getPresenter().getMallAddressList();
    }

    @Override
    public void getMallAddressLitSuc(ArrayList<MallAddressBean> list) {
        mallAddressBeans = list;
        if (mallListAdapter.getList() != null && mallListAdapter.getList().size() > 0) {
            mallListAdapter.deleteAllData();
        }
        mallListAdapter.refAllData(list);
        mallListAdapter.notifyDataSetChanged();

    }

    @Override
    public void deleteSuc(String id) {
        for (int i = 0; i < mallAddressBeans.size(); i++) {
            if (mallAddressBeans.get(i).getId().equals(id)) {
                mallListAdapter.delOne(i);
                mallAddressBeans.remove(i);
            }
        }
        mallListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setDefaultSuc(String id) {
        for (int i = 0; i < mallAddressBeans.size(); i++) {
            if (mallAddressBeans.get(i).getId().equals(id)) {
                mallAddressBeans.get(i).setDefault_flag("1");
            } else {
                mallAddressBeans.get(i).setDefault_flag("0");
            }
        }
        mallListAdapter.notifyDataSetChanged();
    }

    @Override
    public void addAddressSuc(MallAddressBean mallAddressBean) {

    }

    @Override
    public void onItemClick(int position, LinearLayout linear) {
        linear.setBackgroundColor(0xffd0d0d0);
        MallAddressBean model = mallAddressBeans.get(position);
        RxBus.get().post(RxConstant.MALL_CHOICE_ADDRESS, new MallAddress(model.getPhone(), model.getId(), model.getShopping_name(), model.getAddress()));
        mallAddressBeans.get(position);
        this.finish();
    }

    @OnClick(R2.id.mall_new_address)
    public void newAddress() {
        Intent intent = new Intent(this, MallEditAddressActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(final int position, LinearLayout linear) {
        linear.setBackgroundColor(0xffd0d0d0);

        new MenuDialog(this, new String[]{"设置默认", "编辑", "删除"}) {

            @Override
            public void onCheck(String menuStr) {
                switch (menuStr) {
                    case "设置默认":
                        mallListAdapter.notifyDataSetChanged();
                        this.dismiss();
                        getPresenter().setDefaultAddress(mallAddressBeans.get(position).getId());
                        break;
                    case "编辑":
                        //修改地址
                        mallListAdapter.notifyDataSetChanged();
                        this.dismiss();
                        Intent intent = new Intent(MallAddressListActivity.this, MallEditAddressActivity.class);
                        intent.putExtra("addressBean", mallAddressBeans.get(position));
                        startActivity(intent);
                        break;
                    case "删除":
                        mallListAdapter.notifyDataSetChanged();
                        this.dismiss();
                        getPresenter().deleteMallAddress(mallAddressBeans.get(position).getId());
                        break;
                    default:
                        this.dismiss();
                        break;
                }
            }
        }.show();

    }


    @Override
    public void onErrorClickListener() {

    }
}
