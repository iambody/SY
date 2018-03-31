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

import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.widget.dialog.BaseDialog;
import com.cgbsoft.privatefund.R;

import java.util.List;

/**
 * Created by wangpeng on 18-3-28.
 */

public class SellFundBankSelectDialog extends BaseDialog {

    private List<BuyPublicFundActivity.BankCardInfo> bankCardInfos;
    private RecyclerView bankList;
    private PayFundBankSelectDialog.SelectListener selectListener;
    private String currectBankCodeNum = "";
    private boolean isFund;

    public SellFundBankSelectDialog(Context context, String currectBankCodeNum, List<BuyPublicFundActivity.BankCardInfo> bankCardInfos, boolean isFund, PayFundBankSelectDialog.SelectListener selectListener) {
        super(context, R.style.dialog_alpha);
        this.bankCardInfos = bankCardInfos;
        this.isFund = isFund;
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
        lp.softInputMode =  WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
        getWindow().setAttributes(lp);
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
        bankList.setAdapter(new SellFundBankSelectDialog.MyAdapter(currectBankCodeNum, bankCardInfos,this.isFund,new PayFundBankSelectDialog.SelectListener() {
            @Override
            public void select(int index) {
                SellFundBankSelectDialog.this.selectListener.select(index);
                SellFundBankSelectDialog.this.dismiss();
            }
        }));

        findViewById(R.id.iv_dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellFundBankSelectDialog.this.dismiss();
            }
        });
    }

    private static class MyAdapter extends RecyclerView.Adapter<SellFundBankSelectDialog.MyViewHolder> {
        private List<BuyPublicFundActivity.BankCardInfo> list;
        private PayFundBankSelectDialog.SelectListener selectListener;
        private String currectBankNum = "";
        private boolean isFund;

        public MyAdapter(String currectBankNum, List<BuyPublicFundActivity.BankCardInfo> list,boolean isFund, PayFundBankSelectDialog.SelectListener selectListener) {
            this.list = list;
            this.selectListener = selectListener;
            this.currectBankNum = currectBankNum;
            this.isFund = isFund;
        }


        @Override
        public SellFundBankSelectDialog.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paybank_list, parent, false);
            return new SellFundBankSelectDialog.MyViewHolder(view, new PayFundBankSelectDialog.SelectListener() {
                @Override
                public void select(int index) {
                    if (selectListener != null)
                        SellFundBankSelectDialog.MyAdapter.this.selectListener.select(index);
                }
            });
        }


        @Override
        public void onBindViewHolder(SellFundBankSelectDialog.MyViewHolder holder, int position) {
            holder.bindData(list, position, currectBankNum,isFund);
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
        private PayFundBankSelectDialog.SelectListener selectListener;
        private int index;

        public MyViewHolder(View itemView, PayFundBankSelectDialog.SelectListener selectListener) {
            super(itemView);
            bankIcon = (ImageView) itemView.findViewById(R.id.iv_back_icon);
            bankName = (TextView) itemView.findViewById(R.id.tv_back_name);
            bankLimit = (TextView) itemView.findViewById(R.id.tv_bank_limit);
            selectState = (ImageView) itemView.findViewById(R.id.iv_select_state);

            this.selectListener = selectListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SellFundBankSelectDialog.MyViewHolder.this.selectListener != null)
                        SellFundBankSelectDialog.MyViewHolder.this.selectListener.select(index);
                }
            });

        }


        public void bindData(List<BuyPublicFundActivity.BankCardInfo> selectListener, int postion, String curBankNum,boolean isFund) {
            index = postion;
            BuyPublicFundActivity.BankCardInfo bankCardInfo = selectListener.get(postion);

            String bankCoade = bankCardInfo.getDepositacct();
            if (curBankNum.trim().equals(bankCoade.trim())) {
                selectState.setVisibility(View.VISIBLE);
                selectState.setBackgroundResource(R.drawable.icon_paybank_selected);
            } else {
                selectState.setVisibility(View.GONE);
            }

            if (bankCoade.length() > 4) {
                bankCoade = bankCoade.substring(bankCoade.length() - 4);
            }
            bankName.setText(bankCardInfo.getBankShortName() + "(" + bankCoade + ")");
            if(isFund){
                this.bankLimit.setText("可卖出份额"+bankCardInfo.getAvailbalMode1()+"份");
            }else {
                this.bankLimit.setText("可体现金额"+bankCardInfo.getAvailbalMode1()+"元");
            }
            Imageload.display(bankIcon.getContext(),bankCardInfo.getIcon(),bankIcon,R.drawable.bank_icon,R.drawable.bank_icon);
            //bankIcon.setBackgroundResource(R.drawable.bank_icon);
            itemView.findViewById(R.id.tv_not_useable).setVisibility(View.GONE);
        }
    }
}
