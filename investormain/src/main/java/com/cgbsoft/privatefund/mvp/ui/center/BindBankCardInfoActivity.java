package com.cgbsoft.privatefund.mvp.ui.center;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bumptech.glide.Glide;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.bean.BindBankCardInfoBean;
import com.cgbsoft.privatefund.bean.DataDictionary;
import com.cgbsoft.privatefund.bean.product.PublicFundInf;
import com.cgbsoft.privatefund.bean.product.PublishFundRecommendBean;
import com.cgbsoft.privatefund.mvp.contract.center.BindBankCardInfoContract;
import com.cgbsoft.privatefund.mvp.presenter.center.BindBankCardInfoPresenterImpl;
import com.cgbsoft.privatefund.public_fund.PayPasswordDialog;
import com.chenenyu.router.annotation.Route;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imageloader.core.ImageLoader;

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
        getPresenter().requsetSubbranchBankInfo();
    }

    private void initListView() {
        bindBankCardAdapter = new BindBankCardAdapter();
        listView.setAdapter(bindBankCardAdapter);
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
                  Toast.makeText(getApplicationContext(), "至少保留一张银行卡！", Toast.LENGTH_SHORT).show();
                  return false;
              }
              BindBankCardInfoBean bindBankCardInfoBean = (BindBankCardInfoBean)bindBankCardAdapter.getItem(position);
              String simpleBankName = findNameByChannelId(bindBankCardInfoBean.getChannelid());
              PayPasswordDialog payPasswordDialog = new PayPasswordDialog(BindBankCardInfoActivity.this, "请输入你的交易密码", simpleBankName, hintLastBankCardNumber(bindBankCardInfoBean.getDepositacct()));
              payPasswordDialog.setmPassWordInputListener(psw -> {
                  deleteIndex = position;
                  payPasswordDialog.dismiss();
                  PublicFundInf publicFundInf = AppManager.getPublicFundInf(BindBankCardInfoActivity.this);
                  String custno = publicFundInf.getCustno();
                  getPresenter().unBindUserCard(bindBankCardInfoBean.getChannelid(), custno, bindBankCardInfoBean.getDepositacct(), psw);
              });
              payPasswordDialog.show();
              payPasswordDialog.hindSummaryAndMoney();
          }
            return false;
        });
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
        PublicFundInf publicFundInf1 = AppManager.getPublicFundInf(this);
        PublishFundRecommendBean publishFundRecommendBean = AppManager.getPubliFundRecommend(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("trantype", "bgAddCard");
            jsonObject.put("custno", publicFundInf1.getCustno());
            jsonObject.put("authenticateflag", "1");
            jsonObject.put("certificateno", publishFundRecommendBean.getCertificateno());
            jsonObject.put("certificatetype", publishFundRecommendBean.getCertificatetype());
            jsonObject.put("depositacct", publishFundRecommendBean.getDepositacct());
            jsonObject.put("depositacctname", publishFundRecommendBean.getDepositacctname());
            jsonObject.put("depositname", publishFundRecommendBean.getDepositacctname());
            jsonObject.put("depositcity", "");
            jsonObject.put("depositprov", "");
            jsonObject.put("operorg", "9999");
            jsonObject.put("tpasswd", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("tag_parameter", jsonObject.toString());
        NavigationUtils.startActivityByRouter(this, RouteConfig.GOTO_PUBLIC_FUND_BIND_BANK_CARD, map);
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
                String simpleBankName = findNameByChannelId(bindBankCardInfoBean.getChannelid());
                holder.bank_name.setText(TextUtils.isEmpty(simpleBankName) ? bindBankCardInfoBean.getBankname(): simpleBankName);
                holder.bank_number.setText(hintLastBankCardNumber(bindBankCardInfoBean.getDepositacct()));
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

    private String findNameByChannelId(String channelId) {
        if (!CollectionUtils.isEmpty(dataDictionaryList)) {
            for (DataDictionary dataDictionary : dataDictionaryList) {
                if (TextUtils.equals(channelId, dataDictionary.getSubitem())) {
                    return dataDictionary.getSubitemname();
                }
            }
        }
        return "";
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
