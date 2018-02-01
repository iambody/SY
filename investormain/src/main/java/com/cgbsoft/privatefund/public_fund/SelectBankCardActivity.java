package com.cgbsoft.privatefund.public_fund;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangpeng on 18-1-30.
 */

public class SelectBankCardActivity extends BaseActivity {
    public final static String BANK_NAME = "bankname";
    public final static String CHANNEL_ID = "channelid";
    public final static String CHANNEL_NAME = "channelname";

    private RecyclerView bankList;
    private List<BankListOfJZSupport.BankOfJZSupport> bankOfJZSupportList = new ArrayList<>();

    @Override
    protected int layoutID() {
        return R.layout.activity_selcet_bankcard;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.tv_title)).setText("选择所支持的银行");
        bankList = (RecyclerView) findViewById(R.id.rv_bank_list);

        bindView();
    }


    /**
     * 绑定view的数据与监听
     */
    private void bindView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        bankList.setLayoutManager(linearLayoutManager);
        bankList.setAdapter(new SelectBankAdapter(bankOfJZSupportList, new SelectBankAdapter.SelectBankCardLinsterer() {
            @Override
            public void seclecBackCard(BankListOfJZSupport.BankOfJZSupport bankOfJZSupport) {
                       getIntent().putExtra(BANK_NAME,"");
                       getIntent().putExtra(CHANNEL_ID,bankOfJZSupport.getChannelid());
                       getIntent().putExtra(CHANNEL_NAME,bankOfJZSupport.getFullname());
                       setResult(Activity.RESULT_OK,getIntent());
                       finish();
            }
        }));

        bindBankCardData();
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
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
        Map<String,Object> parms = new HashMap();
            parms.put("trantype", "530335");
            parms.put("custno", "175");
            parms.put("planflag", "");

      //  NetWork.post("/api/v2/kz/proxy", RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), jsonObject.toString()), String.class)

        ApiClient.getPublicFundFormProxy(parms).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                Log.e("test","   金证 所支持的银行卡 ："+s);
               /* String result = JsonUtils.gerSringFromJsonStr(s, "result");
                BankListOfJZSupport bankListOfJZSupport = new Gson().fromJson(result, BankListOfJZSupport.class);
                if ("0000".equals(bankListOfJZSupport.getErrorCode())) { //成功
                    bankOfJZSupportList.addAll(bankListOfJZSupport.getDatasets());
                    bankList.getAdapter().notifyDataSetChanged();
                } else if ("PPPP".equals(bankListOfJZSupport.getErrorCode())) {// 处理中
                    Toast.makeText(SelectBankCardActivity.this, "服务器正在处理中", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SelectBankCardActivity.this, bankListOfJZSupport.getErrorMessage(), Toast.LENGTH_LONG).show();
                }*/
            }

            @Override
            protected void onRxError(Throwable error) {
                Log.e("绑定银行卡", error.toString());
            }
        });
    }

    static class SelectBankAdapter extends RecyclerView.Adapter<SelectBankAdapter.SelectBankViewHolder> {
        private List<BankListOfJZSupport.BankOfJZSupport> bankCardList;
        private SelectBankCardLinsterer linsterer;

        public SelectBankAdapter(List<BankListOfJZSupport.BankOfJZSupport> bankCardList, SelectBankCardLinsterer linsterer) {
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
            private BankListOfJZSupport.BankOfJZSupport bankOfJZSupport;

            public SelectBankViewHolder(View itemView, final SelectBankCardLinsterer linsterer) {
                super(itemView);
                bankName = (TextView)itemView.findViewById(R.id.tv_bank_name);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (linsterer != null) linsterer.seclecBackCard(bankOfJZSupport);
                    }
                });
            }

            public void bindView(BankListOfJZSupport.BankOfJZSupport bankOfJZSupport) {
                this.bankOfJZSupport = bankOfJZSupport;
                bankName.setText(bankOfJZSupport.getName());
            }

        }


        interface SelectBankCardLinsterer {
            void seclecBackCard(BankListOfJZSupport.BankOfJZSupport bankOfJZSupport);
        }

    }

}
