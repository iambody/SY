package com.cgbsoft.privatefund.mvp.ui.center;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.bean.BindBankCardInfoBean;
import com.cgbsoft.privatefund.mvp.contract.center.BindBankCardInfoContract;
import com.cgbsoft.privatefund.mvp.presenter.center.BindBankCardInfoPresenterImpl;
import com.chenenyu.router.annotation.Route;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @auther chenlong
 */
@Route(RouteConfig.GOTO_BIND_BANK_CARD_ACTIVITY_INFO)
public class BindBankCardInfoActivity extends BaseActivity<BindBankCardInfoPresenterImpl> implements BindBankCardInfoContract.BindBankCardInfoView{

    @BindView(R.id.title_left)
    ImageView back;
    @BindView(R.id.title_mid)
    TextView titleTV;
    @BindView(R.id.list_view)
    ListView listView;

    private BindBankCardAdapter bindBankCardAdapter;
    private LoadingDialog mLoadingDialog;

    @OnClick(R.id.title_left)
    public void clickBack() {
        this.finish();
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_bank_card_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        back.setVisibility(View.VISIBLE);
        titleTV.setText(getResources().getString(R.string.public_fund_setting_bankcard_info));
        mLoadingDialog = LoadingDialog.getLoadingDialog(baseContext, "", false, false);
        initListView();
        getPresenter().requestBindBankCardInfo();
    }

    private void initListView() {
        bindBankCardAdapter = new BindBankCardAdapter();
        listView.setAdapter(bindBankCardAdapter);
    }

    @Override
    protected BindBankCardInfoPresenterImpl createPresenter() {
        return new BindBankCardInfoPresenterImpl(this, this);
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
    public void requestInfoSuccess(List<BindBankCardInfoBean> bindCardList) {
        hideLoadDialog();
        bindBankCardAdapter.addData(bindCardList);
    }

    @Override
    public void requestInfoFailure(String mssage) {
        hideLoadDialog();
        Toast.makeText(getApplicationContext(), mssage, Toast.LENGTH_SHORT).show();
    }

    private String hintLastBankCardNumber(String identifyNumber) {
        if (!TextUtils.isEmpty(identifyNumber) && identifyNumber.length() > 15) {
            String hintStr = identifyNumber.substring(0, identifyNumber.length() - 4);
            return addSpaceDivideNumber(identifyNumber.replace(hintStr, ViewUtils.productEncodyStr(hintStr)));
        }
        return identifyNumber;
    }

    private String addSpaceDivideNumber(String identifyNumber) {
      StringBuffer sb = new StringBuffer();
      if (!TextUtils.isEmpty(identifyNumber)) {
          char[] chars = identifyNumber.toCharArray();
          for (int i = 0; i < chars.length; i++) {
              if (i != 0 && i%4 == 0 && i != chars.length - 1) {
                  sb.append(" ");
              }
              sb.append(chars[i]);
          }
          return sb.toString();
      }
      return identifyNumber;
    }

    public class BindBankCardAdapter extends BaseAdapter {

        private List<BindBankCardInfoBean> data;

        public BindBankCardAdapter() {
            data = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addData(List<BindBankCardInfoBean> bindBankCardInfoBeans) {
            this.data.clear();
            if (!CollectionUtils.isEmpty(bindBankCardInfoBeans)) {
                this.data.addAll(bindBankCardInfoBeans);
            }
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            BindBankCardInfoBean bindBankCardInfoBean = data.get(position);
            ViewHolder holder;
            if (convertView == null) {
                view = LayoutInflater.from(BindBankCardInfoActivity.this).inflate(R.layout.bind_bank_card_list_item, parent, false);
                holder = new ViewHolder();
                holder.bank_name = (TextView) view.findViewById(R.id.bank_name);
                holder.bank_type = (TextView) view.findViewById(R.id.bank_type);
                holder.bank_number = (TextView) view.findViewById(R.id.bank_number);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            holder.bank_name.setText(bindBankCardInfoBean.getBankname());
            holder.bank_number.setText(hintLastBankCardNumber(bindBankCardInfoBean.getDepositacct()));
            return view;
        }
    }

    class ViewHolder{
        TextView bank_name;
        TextView bank_type;
        TextView bank_number;
    }
}
