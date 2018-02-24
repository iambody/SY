package com.cgbsoft.privatefund.public_fund;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.widget.ClearEditText;
import com.cgbsoft.lib.widget.dialog.LoadingDialog;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.public_fund.passworddiglog.BankBranchBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangpeng on 18-2-22.
 */

public class SelectBranchBankActivity extends BaseActivity<BindingBankCardOfPublicFundPresenter> {
    public final static String BANK_NAME_ID= "banknameid";
    public final static String CITY_NAME = "cityName";

    public final static String CHANNEL_NAME = "Channelname";
    public final static String PARATYPE = "Paratype";

    private RecyclerView bankList;

    private String bankNameId = "";
    private String cityName = "";
    private ArrayList<BankBranchBean> originBankListBranchs;
    private ArrayList<BankBranchBean> bankListBranchs =  new ArrayList<>();

    private ClearEditText searchTitle;

    @Override
    protected int layoutID() {
        return R.layout.activity_select_branchbank;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        searchTitle = findViewById(R.id.search_title_ed);
        searchTitle.setHint("请输入支行关键字");

        bankList = (RecyclerView) findViewById(R.id.rv_bank_list);
        bankNameId = getIntent().getStringExtra(BANK_NAME_ID);
        cityName = getIntent().getStringExtra(CITY_NAME);
        bindView();
    }


    /**
     * 绑定view的数据与监听
     */
    private void bindView() {
        // 返回键
        findViewById(R.id.search_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        bankList.setLayoutManager(linearLayoutManager);
        bankList.setAdapter(new SelectBankAdapter(bankListBranchs, new SelectBankAdapter.SelectBankCardLinsterer() {
            @Override
            public void seclecBranchBankCard(BankBranchBean bankBranchBean) {
                getIntent().putExtra(PARATYPE,bankBranchBean.getParatype());
                getIntent().putExtra(CHANNEL_NAME,bankBranchBean.getParavalue());
                setResult(Activity.RESULT_OK,getIntent());
                finish();
            }
        }));

        final int[] lastTextLength = {0};
        searchTitle.setTextChangedListener(new ClearEditText.TextChangedListener() {
            @Override
            public void onTextChanged(String value) {
                bankListBranchs.clear();
                if(BStrUtils.isEmpty(value)){
                    bankListBranchs.addAll(originBankListBranchs);
                }else {
                    for(BankBranchBean bankBranchBean:originBankListBranchs){
                        if(BStrUtils.NullToStr1(bankBranchBean.getParavalue()).contains(value)) bankListBranchs.add(bankBranchBean);
                    }
                }
                bankList.getAdapter().notifyDataSetChanged();

              /*  int curTextLegth = BStrUtils.NullToStr1(value).length();
                // 只要在删除或者输入字符超过2个字时才搜索
                if (curTextLegth < lastTextLength[0] || value.length() > 0) {

                }
                lastTextLength[0] = curTextLegth;*/
            }
        });
        searchTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String str = v.getText().toString();
                    if (TextUtils.isEmpty(str)) {
                        return false;
                    }
                    return false;
                }
                return false;
            }
        });

        loadBranchbankData();
    }

    @Override
    protected BindingBankCardOfPublicFundPresenter createPresenter() {
        return new BindingBankCardOfPublicFundPresenter(this,null);
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


    private void loadBranchbankData() {
        LoadingDialog loadingDialog = LoadingDialog.getLoadingDialog(this,"加载中",false,false);
        getPresenter().getBranchBankInfo(cityName.concat("市"), BStrUtils.nullToEmpty(bankNameId), new BasePublicFundPresenter.PreSenterCallBack<String>() {
            @Override
            public void even(String result) {
                loadingDialog.dismiss();
                BankListOfJZSupport bankListOfJZSupport = new Gson().fromJson(result, BankListOfJZSupport.class);
                if (bankListOfJZSupport != null) {
                    String code = bankListOfJZSupport.getErrorCode();
                    if (PublicFundContant.REQEUST_SUCCESS.equals(code)) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray datasets = jsonObject.getJSONArray("datasets").getJSONArray(0);
                            Gson gson = new Gson();
                            originBankListBranchs = gson.fromJson(datasets.toString(), new TypeToken<ArrayList<BankBranchBean>>(){}.getType());
                            bankListBranchs.clear();
                            bankListBranchs.addAll(originBankListBranchs);
                            RecyclerView.Adapter adapter =  bankList.getAdapter();
                            adapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else if (PublicFundContant.REQEUSTING.equals(bankListOfJZSupport.getErrorCode())) {// 处理中
                    Toast.makeText(SelectBranchBankActivity.this, "服务器正在处理中", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SelectBranchBankActivity.this, bankListOfJZSupport.getErrorMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void field(String errorCode, String errorMsg) {
                loadingDialog.dismiss();
                Log.e(this.getClass().getSimpleName()," "+errorMsg);
            }
        });
        loadingDialog.show();
    }

   /*
    private void bindBankCardData() {
        getPresenter().getBinidedBankList(AppManager.getPublicFundInf(SelectBranchBankActivity.this).getCustno(),new BasePublicFundPresenter.PreSenterCallBack<String>() {
            @Override
            public void even(String result) {
                loadingDialog.dismiss();
                BankListOfJZSupport bankListOfJZSupport = new Gson().fromJson(result,BankListOfJZSupport.class);
                if (PublicFundContant.REQEUST_SUCCESS.equals(bankListOfJZSupport.getErrorCode())) { //成功
                    bankOfJZSupportList.addAll(bankListOfJZSupport.getDatasets());
                    bankList.getAdapter().notifyDataSetChanged();
                } else if (PublicFundContant.REQEUSTING.equals(bankListOfJZSupport.getErrorCode())) {// 处理中
                    Toast.makeText(SelectBranchBankActivity.this, "服务器正在处理中", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SelectBranchBankActivity.this, bankListOfJZSupport.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void field(String errorCode, String errorMsg) {
                loadingDialog.dismiss();
                Log.e(this.getClass().getSimpleName()," "+errorMsg);
            }
        });
        loadingDialog.show();
    }
*/
    static class SelectBankAdapter extends RecyclerView.Adapter<SelectBankAdapter.SelectBankViewHolder> {
        private List<BankBranchBean> bankCardList;
        private SelectBankCardLinsterer linsterer;

        public SelectBankAdapter(List<BankBranchBean> bankCardList, SelectBankCardLinsterer linsterer) {
            this.bankCardList = bankCardList;
            this.linsterer = linsterer;
        }

        @Override
        public SelectBankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_bankcard, parent, false);
            return new SelectBankViewHolder(view, linsterer);
        }

        @Override
        public void onBindViewHolder(SelectBankViewHolder holder, int position) {
            holder.bindView(bankCardList.get(position));
        }

        @Override
        public int getItemCount() {
            return bankCardList == null ? 0 : bankCardList.size();
        }


        static class SelectBankViewHolder extends RecyclerView.ViewHolder {
            private TextView bankName;
            private SelectBankCardLinsterer linsterer;
            private BankBranchBean bankBranchBean;

            public SelectBankViewHolder(View itemView, final SelectBankCardLinsterer linsterer) {
                super(itemView);
                bankName = (TextView)itemView.findViewById(R.id.tv_bank_name);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (linsterer != null) linsterer.seclecBranchBankCard(bankBranchBean);
                    }
                });
            }

            public void bindView(BankBranchBean bankBranchBean) {
                this.bankBranchBean = bankBranchBean;
                bankName.setText(bankBranchBean.getParavalue());
            }

        }


        interface SelectBankCardLinsterer {
            void seclecBranchBankCard(BankBranchBean bankBranchBean);
        }

    }

}
