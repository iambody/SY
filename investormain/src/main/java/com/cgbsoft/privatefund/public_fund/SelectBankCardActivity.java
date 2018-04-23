package com.cgbsoft.privatefund.public_fund;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangpeng on 18-1-30.
 */

public class SelectBankCardActivity extends BaseActivity<BindingBankCardOfPublicFundPresenter> {
    public final static String BANK_NAME_ID = "banknameid";
    public final static String CHANNEL_ID = "channelid";
    public final static String CHANNEL_NAME = "channelname";
    public final static String CHANNEL_IV = "channelbankiv";
    public final static String CHANNEL_DEC = "channelbankdes";


    private RecyclerView bankList;
    private List<BuyPublicFundActivity.BankCardInfo> bankOfJZSupportList = new ArrayList<>();

    @Override
    protected int layoutID() {
        return R.layout.activity_selcet_bankcard;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        bankList = (RecyclerView) findViewById(R.id.rv_bank_list);
        bindView();
    }


    /**
     * 绑定view的数据与监听
     */
    private void bindView() {
        // 该表标题
        ((TextView) findViewById(R.id.title_mid)).setText("请选择银行卡");
        // 返回键
        findViewById(R.id.title_left).setVisibility(View.VISIBLE);
        findViewById(R.id.title_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        bankList.setLayoutManager(linearLayoutManager);
//        bankList.setAdapter(new SelectBankAdapter(baseContext,bankOfJZSupportList, new SelectBankAdapter.SelectBankCardLinsterer() {
//            @Override
//            public void seclecBackCard(BankListOfJZSupport.BankOfJZSupport bankOfJZSupport) {
//                getIntent().putExtra(BANK_NAME_ID, bankOfJZSupport.getBanknameid());
//                getIntent().putExtra(CHANNEL_ID, bankOfJZSupport.getChannelid());
//                getIntent().putExtra(CHANNEL_NAME, bankOfJZSupport.getFullname());
//                setResult(Activity.RESULT_OK, getIntent());
//                finish();
//            }
//        }));
        findViewById(R.id.fund_select_bank_tips_del_iv).setOnClickListener((view) -> findViewById(R.id.fund_select_bank_tips_lay).setVisibility(View.GONE));
        bindBankCardData();
    }

    @Override
    protected BindingBankCardOfPublicFundPresenter createPresenter() {
        return new BindingBankCardOfPublicFundPresenter(this, null);
    }


    /**
     * 绑定银行卡名字
     * <p>
     * {
     * trantype: '530335',
     * custno: '175',  //客户号（H5调取app指令的时候会传入）
     * planflag: ''  //留空即可
     * }
     */
    private void bindBankCardData() {
        LoadingDialog loadingDialog = LoadingDialog.getLoadingDialog(this, "加载中", false, false);
        getPresenter().getBinidedBankList(AppManager.getPublicFundInf(SelectBankCardActivity.this).getCustNo(), new BasePublicFundPresenter.PreSenterCallBack<String>() {
            @Override
            public void even(String result) {
                loadingDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(result);
                    String tip = object.getString("tip");
                    String bankLsStr = object.getString("bankList");
                    BStrUtils.setTv((TextView) findViewById(R.id.fund_select_bank_tips), tip);
                    List<BuyPublicFundActivity.BankCardInfo> bankListOfJZSupports = new Gson().fromJson(bankLsStr, new TypeToken<List<BuyPublicFundActivity.BankCardInfo>>() {
                    }.getType());
                    bankOfJZSupportList.addAll(bankListOfJZSupports);
//                    bankList.getAdapter().notifyDataSetChanged();
                    bankList.setAdapter(new SelectBankAdapter(baseContext, bankOfJZSupportList, new SelectBankAdapter.SelectBankCardLinsterer() {
                        @Override
                        public void seclecBackCard(BuyPublicFundActivity.BankCardInfo bankOfJZSupport) {
                            getIntent().putExtra(BANK_NAME_ID, bankOfJZSupport.getBankNameId());
                            getIntent().putExtra(CHANNEL_ID, bankOfJZSupport.getChannelId());
                            getIntent().putExtra(CHANNEL_NAME, bankOfJZSupport.getFullName());
                            getIntent().putExtra(CHANNEL_IV, bankOfJZSupport.getIcon());
                            getIntent().putExtra(CHANNEL_DEC, bankOfJZSupport.getBankLimit());
                            setResult(Activity.RESULT_OK, getIntent());
                            finish();
                        }
                    }));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
              /*  if (PublicFundContant.REQEUST_SUCCESS.equals(bankListOfJZSupport.getErrorCode())) { //成功
                } else if (PublicFundContant.REQEUSTING.equals(bankListOfJZSupport.getErrorCode())) {// 处理中
                    Toast.makeText(SelectBankCardActivity.this, "服务器正在处理中", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SelectBankCardActivity.this, bankListOfJZSupport.getErrorMessage(), Toast.LENGTH_LONG).show();
                }*/
            }

            @Override
            public void field(String errorCode, String errorMsg) {
                loadingDialog.dismiss();
                Log.e("SellPublicFundActivity", " " + errorMsg);
            }
        });
        loadingDialog.show();
    }

    static class SelectBankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<BuyPublicFundActivity.BankCardInfo> bankCardList;
        private SelectBankCardLinsterer linsterer;
        //Header和Footer,以及对应的Type  for 防止改需求 把head相关的也预备

        private View mHeaderView;
        private View mFooterView;
        private int TYPE_HEADER = -1;
        private int TYPE_FOOTER = -2;
        private Context context;

        //一些辅助的方法
        public int getHeaderCount() {
            return isHasHeader() ? 1 : 0;
        }

        private boolean isHasHeader() {
            return mHeaderView != null;
        }

        private boolean isHasFooter() {
            return mFooterView != null;
        }


        public SelectBankAdapter(Context context, List<BuyPublicFundActivity.BankCardInfo> bankCardList, SelectBankCardLinsterer linsterer) {
            this.bankCardList = bankCardList;
            this.linsterer = linsterer;
            this.context = context;
            mFooterView = LayoutInflater.from(context).inflate(R.layout.item_publicfund_banckls_foot, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mFooterView.setLayoutParams(params);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (TYPE_FOOTER == viewType) {
                return new SelectBankFootViewHolder(mFooterView);
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_bankcard, parent, false);
                return new SelectBankViewHolder(context, view, linsterer);
            }
        }

        @Override
        public int getItemViewType(int position) {
            //pos超出
            if (isHasFooter() && position == getItemCount() - 1)
                return TYPE_FOOTER;

            return super.getItemViewType(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position != getItemCount() - 1) {
                ((SelectBankViewHolder) holder).bindView(bankCardList.get(position));

            }
        }

        @Override
        public int getItemCount() {
            if (null == bankCardList) return 0;


            int pos = bankCardList.size();
//            if (isHasHeader())
//                pos++;
            if (isHasFooter())
                pos++;

            return pos;
        }


        static class SelectBankViewHolder extends RecyclerView.ViewHolder {
            private TextView bankName, item_public_fund_bankls_notes;
            private ImageView item_public_fund_bankls_iv;
            private Context context;
            private SelectBankCardLinsterer linsterer;
            private BuyPublicFundActivity.BankCardInfo bankOfJZSupport;

            public SelectBankViewHolder(Context contexts, View itemView, final SelectBankCardLinsterer linsterer) {
                super(itemView);
                context = contexts;
                bankName = (TextView) itemView.findViewById(R.id.tv_bank_name);
                item_public_fund_bankls_notes = (TextView) itemView.findViewById(R.id.item_public_fund_bankls_notes);
                item_public_fund_bankls_iv = (ImageView) itemView.findViewById(R.id.item_public_fund_bankls_iv);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (linsterer != null) linsterer.seclecBackCard(bankOfJZSupport);
                    }
                });
            }

            public void bindView(BuyPublicFundActivity.BankCardInfo bankOfJZSupport) {
                this.bankOfJZSupport = bankOfJZSupport;
                bankName.setText(bankOfJZSupport.getBankShortName());
                BStrUtils.setTv(item_public_fund_bankls_notes, bankOfJZSupport.getBankLimit());
                Imageload.display(context instanceof Activity ? context.getApplicationContext() : context, bankOfJZSupport.getIcon(), item_public_fund_bankls_iv);
            }

        }

        static class SelectBankFootViewHolder extends RecyclerView.ViewHolder {
            public SelectBankFootViewHolder(View itemView) {
                super(itemView);
            }
        }

        interface SelectBankCardLinsterer {
            void seclecBackCard(BuyPublicFundActivity.BankCardInfo bankOfJZSupport);
        }

    }

}
