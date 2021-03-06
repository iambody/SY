package com.cgbsoft.privatefund.mvp.ui.center;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.bean.BindBankCardInfoBean;
import com.cgbsoft.privatefund.bean.DataDictionary;
import com.cgbsoft.privatefund.mvp.contract.center.BindBankCardInfoContract;
import com.cgbsoft.privatefund.mvp.presenter.center.BindBankCardInfoPresenterImpl;
import com.cgbsoft.privatefund.public_fund.BindingBankCardOfPublicFundActivity;
import com.cgbsoft.privatefund.public_fund.PayFundBankSelectDialog;
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
    SwipeMenuListView listView;

    private BindBankCardAdapter bindBankCardAdapter;
    private LoadingDialog mLoadingDialog;
    private List<DataDictionary> dataDictionaryList;
    private int deleteIndex;
    private static final int CONTENT = 0;
    private static final int BUTTON = 1;

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
//        getPresenter().requsetSubbranchBankInfo();
        getPresenter().requestBindBankCardInfo();
    }

    private void initListView() {
        bindBankCardAdapter = new BindBankCardAdapter();
        listView.setAdapter(bindBankCardAdapter);

        /*  注释侧滑删除逻辑
        SwipeMenuCreator creator = menu -> {
            if (menu.getViewType() == CONTENT) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setWidth(DimensionPixelUtil.dp2px(BindBankCardInfoActivity.this, 60));
                deleteItem.setIcon(R.drawable.icon_unbind_card_info);
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener((position, menu, index) -> {
          if (index == 0) {
              if (bindBankCardAdapter.getCount() == 2) {
                  Toast.makeText(getApplicationContext(), "至少绑定一张银行卡！", Toast.LENGTH_SHORT).show();
                  return false;
              }
              BindBankCardInfoBean bindBankCardInfoBean = (BindBankCardInfoBean)bindBankCardAdapter.getItem(position);
              String simpleBankName = findNameByChannelId(bindBankCardInfoBean.getChannelid());
              PayPasswordDialog payPasswordDialog = new PayPasswordDialog(BindBankCardInfoActivity.this, "请输入您的交易密码", simpleBankName, hintLastBankCardNumber(bindBankCardInfoBean.getDepositacct()));
              payPasswordDialog.setmPassWordInputListener(psw -> {
                  deleteIndex = position;
                  payPasswordDialog.dismiss();
                  PublicFundInf publicFundInf = AppManager.getPublicFundInf(BindBankCardInfoActivity.this);
                  String custno = publicFundInf.getCustno();
                  getPresenter().unBindUserCard(bindBankCardInfoBean.getChannelid(), custno, bindBankCardInfoBean.getDepositacct(), psw);
              });
              payPasswordDialog.setWidtherDialog(true);
              payPasswordDialog.show();
          }
            return false;
        });
        */
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

    private void gotoAddBindBankCard() {
        Intent intent = new Intent(this,BindingBankCardOfPublicFundActivity.class);
        intent.putExtra(BindingBankCardOfPublicFundActivity.STYLE,1);
        startActivityForResult(intent, PayFundBankSelectDialog.REQUESTCODE);
    }

    @Override
    public void requestInfoFailure(String mssage) {
        hideLoadDialog();
        Toast.makeText(getApplicationContext(), mssage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unBindCardSuccess() {
        hideLoadDialog();
        if (bindBankCardAdapter != null) {
            bindBankCardAdapter.removeData(deleteIndex);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PayFundBankSelectDialog.REQUESTCODE &&
                resultCode == Activity.RESULT_OK) {
            showLoadDialog();
            getPresenter().requestBindBankCardInfo();
        }
    }


    @Override
    public void unBindCardFailure(String errorMsg) {
        hideLoadDialog();
        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void requestSubbranchBankSuccess(List<DataDictionary> dataList) {
        dataDictionaryList = dataList;
        getPresenter().requestBindBankCardInfo();
    }

    @Override
    public void requestSubbranckBankFailure(String errorMsg) {
        hideLoadDialog();
        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
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
                this.data.add(new BindBankCardInfoBean());
            }
            notifyDataSetChanged();
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (data.size() - 1 == position) {
                return BUTTON;
            }
            return CONTENT;
        }

        public void removeData(int postion) {
            if (!CollectionUtils.isEmpty(data)) {
                data.remove(postion);
                notifyDataSetChanged();
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            BindBankCardInfoBean bindBankCardInfoBean = data.get(position);
            if (getItemViewType(position) == CONTENT)  {
                ViewHolder holder;
                if (convertView == null) {
                    view = LayoutInflater.from(BindBankCardInfoActivity.this).inflate(R.layout.bind_bank_card_list_item, parent, false);
                    holder = new ViewHolder();
                    holder.bank_name = (TextView) view.findViewById(R.id.bank_name);
                    holder.bank_type = (TextView) view.findViewById(R.id.bank_type);
                    holder.bank_number = (TextView) view.findViewById(R.id.bank_number);
                    holder.bankIcon = (ImageView) view.findViewById(R.id.bank_icon);
                    holder.relativeLayout = (RelativeLayout) view.findViewById(R.id.bank_icon_background);
                    view.setTag(holder);
                } else {
                    view = convertView;
                    holder = (ViewHolder) view.getTag();
                }
                holder.bank_name.setText(bindBankCardInfoBean.getBankShortName());
                holder.bank_number.setText(hintLastBankCardNumber(bindBankCardInfoBean.getDepositAcct()));
                Imageload.display(BindBankCardInfoActivity.this, bindBankCardInfoBean.getIcon(), holder.bankIcon);
                Imageload.displayListenr(BindBankCardInfoActivity.this, bindBankCardInfoBean.getBackground(), holder.relativeLayout);
            } else if (getItemViewType(position) == BUTTON) {
                ButtonViewHolder holder;
                if (convertView == null) {
                    view = LayoutInflater.from(BindBankCardInfoActivity.this).inflate(R.layout.list_item_add_bindcard, parent, false);
                    holder = new ButtonViewHolder();
                    holder.addBindCard = (TextView) view.findViewById(R.id.tv_add_bind_card);
                    holder.addBindCard.setOnClickListener(v -> {
                        gotoAddBindBankCard();
                    });
                    view.setTag(holder);
                } else {
                    view = convertView;
                }
            }
            return view;
        }
    }

    class ViewHolder{
        TextView bank_name;
        TextView bank_type;
        TextView bank_number;
        ImageView bankIcon;
        RelativeLayout relativeLayout;
    }

    class ButtonViewHolder {
        TextView addBindCard;
    }
}
