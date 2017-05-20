package app.mall.com.mvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
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
import qcloud.mall.R;
import qcloud.mall.R2;

@Route("mall_choice_address")
public class MallAddressListActivity extends BaseActivity<MallPresenter> implements MallContract.View, MallAddressListeber {

    @BindView(R2.id.mall_address_list)
    RecyclerView rcv_mall_address_list;
    @BindView(R2.id.title_mid)
    TextView titleMid;
    private MallListAdapter mallListAdapter;
    private ArrayList<MallAddressBean> mallAddressBeans;
    private LinearLayoutManager linearLayoutManager;


    @Override
    protected int layoutID() {
        return R.layout.activity_mall_address_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        getPresenter().getMallAddressList();
        titleMid.setText("收货地址");
        mallListAdapter = new MallListAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_mall_address_list.setLayoutManager(linearLayoutManager);
        rcv_mall_address_list.setAdapter(mallListAdapter);

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
        mallAddressBeans = list;
        mallListAdapter.refAllData(list);
        mallListAdapter.notifyDataSetChanged();

    }

    @Override
    public void deleteSuc(String id) {
        for (int i = 0; i < mallAddressBeans.size(); i++) {
            if (mallAddressBeans.get(i).getId().equals(id)) {
                mallAddressBeans.remove(i);
            }
        }
        mallListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setDefaultSuc(String id) {
        for (int i = 0; i < mallAddressBeans.size(); i++) {
            if (mallAddressBeans.get(i).getId().equals(id)) {
                mallAddressBeans.get(i).setDefault_flag(1);
            } else {
                mallAddressBeans.get(i).setDefault_flag(0);
            }
        }
        mallListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position, LinearLayout linear) {
        linear.setBackgroundColor(0xffd0d0d0);
        //TODO 给h5传值
        mallAddressBeans.get(position);
        this.finish();
    }

    @Override
    public void onItemLongClick(final int position, LinearLayout linear) {
        linear.setBackgroundColor(0xffd0d0d0);

        new MenuDialog(this, new String[]{"设置默认", "编辑", "删除"}) {

            @Override
            public void onCheck(String menuStr) {
                switch (menuStr) {
                    case "设置默认":
                        getPresenter().setDefaultAddress(mallAddressBeans.get(position).getId());
                        this.dismiss();
                        break;
                    case "编辑":
                        //修改地址
                        Intent intent = new Intent(MallAddressListActivity.this, MallEditAddressActivity.class);
                        intent.putExtra("addressBean", mallAddressBeans.get(position));
                        startActivity(intent);
                        this.dismiss();
                        break;
                    case "删除":
                        getPresenter().deleteMallAddress(mallAddressBeans.get(position).getId());
                        this.dismiss();
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
