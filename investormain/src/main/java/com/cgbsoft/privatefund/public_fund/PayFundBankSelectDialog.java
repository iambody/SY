package com.cgbsoft.privatefund.public_fund;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.widget.dialog.BaseDialog;
import com.cgbsoft.privatefund.R;

import java.util.List;

/**
 * Created by wangpeng on 18-3-5.
 */

public class PayFundBankSelectDialog extends BaseDialog {
    public static int REQUESTCODE = 10000;

    private List<BuyPublicFundActivity.BankCardInfo> bankCardInfos;
    private RecyclerView bankList;
    private SelectListener selectListener;
    private String currectBankCodeNum = "";
    public PayFundBankSelectDialog(Context context,String currectBankCodeNum,List<BuyPublicFundActivity.BankCardInfo> bankCardInfos, SelectListener selectListener) {
        super(context, R.style.dialog_alpha);
        this.bankCardInfos = bankCardInfos;
        this.selectListener = selectListener;
        this.currectBankCodeNum = currectBankCodeNum;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
        setContentView(R.layout.dialog_paybank_select);
        initView();
        bindViews();
    }


    private void initView() {
        bankList = (RecyclerView) findViewById(R.id.rl_bank_list);
    }

    private void bindViews() {
        bankList.setLayoutManager(new LinearLayoutManager(getContext()));
        bankList.setAdapter(new MyAdapter(currectBankCodeNum,bankCardInfos, new SelectListener() {
            @Override
            public void select(int index) {
                PayFundBankSelectDialog.this.selectListener.select(index);
                PayFundBankSelectDialog.this.dismiss();
            }
        }));


        findViewById(R.id.iv_dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayFundBankSelectDialog.this.dismiss();
            }
        });

        findViewById(R.id.ll_add_new_bankcord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectListener != null) selectListener.select(-2);
                PayFundBankSelectDialog.this.dismiss();
            }
        });
    }


    private static class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<BuyPublicFundActivity.BankCardInfo> list;
        private SelectListener selectListener;
        private String currectBankNum = "";
        public MyAdapter(String currectBankNum,List<BuyPublicFundActivity.BankCardInfo> list, SelectListener selectListener) {
            this.list = list;
            this.selectListener = selectListener;
            this.currectBankNum = currectBankNum;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paybank_list, parent, false);
            return new MyViewHolder(view, new SelectListener() {
                @Override
                public void select(int index) {
                    if (selectListener != null) MyAdapter.this.selectListener.select(index);
                }
            });
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bindData(list, position,currectBankNum);
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView bankIcon;
        public TextView bankName;
        public TextView bankLimit;
        public ImageView selectState;
        private SelectListener selectListener;
        private int index;

        public MyViewHolder(View itemView, SelectListener selectListener) {
            super(itemView);
            bankIcon = (ImageView) itemView.findViewById(R.id.iv_back_icon);
            bankName = (TextView) itemView.findViewById(R.id.tv_back_name);
            bankLimit = (TextView) itemView.findViewById(R.id.tv_bank_limit);
            selectState = (ImageView) itemView.findViewById(R.id.iv_select_state);

            this.selectListener = selectListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyViewHolder.this.selectListener != null)
                        MyViewHolder.this.selectListener.select(index);
                }
            });

        }


        public void bindData(List<BuyPublicFundActivity.BankCardInfo> selectListener, int postion, String curBankNum) {
            index = postion;
            BuyPublicFundActivity.BankCardInfo bankCardInfo = selectListener.get(postion);

            String bankCoade = bankCardInfo.getDepositacct();
            if(curBankNum.trim().equals(bankCoade.trim())){
                selectState.setVisibility(View.VISIBLE);
                selectState.setBackgroundResource(R.drawable.icon_paybank_selected);
            }else {
                selectState.setVisibility(View.GONE);
            }

            if (bankCoade.length() > 4) {
                bankCoade = bankCoade.substring(bankCoade.length() - 4);
            }
            bankName.setText(bankCardInfo.getBankShortName() + "(" + bankCoade + ")");
            bankLimit.setText(bankCardInfo.getBankLimit());
            bankIcon.setBackgroundResource(R.drawable.bank_icon);
            if("0".equals(bankCardInfo.getBankEnableStatus())){
                itemView.findViewById(R.id.tv_not_useable).setVisibility(View.VISIBLE);
            }else {
                itemView.findViewById(R.id.tv_not_useable).setVisibility(View.GONE);
            }
        }
    }


    public interface SelectListener {
        void select(int index);
    }

}
