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

    public PayFundBankSelectDialog(Context context, List<BuyPublicFundActivity.BankCardInfo> bankCardInfos, SelectListener selectListener) {
        super(context, R.style.dialog_alpha);
        this.bankCardInfos = bankCardInfos;
        this.selectListener = selectListener;
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
        bankList = findViewById(R.id.rl_bank_list);
    }

    private void bindViews() {
        bankList.setLayoutManager(new LinearLayoutManager(getContext()));
        bankList.setAdapter(new MyAdapter(bankCardInfos));

        findViewById(R.id.iv_dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectListener != null){
                    int select = ((MyAdapter)(bankList.getAdapter())).getSelectIndex();
                    if(select >= 0) selectListener.select(select);
                }
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
        private int lastSelectIndex = -1;

        public MyAdapter(List<BuyPublicFundActivity.BankCardInfo> list) {
            this.list = list;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paybank_list, parent, false);
            return new MyViewHolder(view, new SelectListener() {
                @Override
                public void select(int index) {
                    lastSelectIndex = index;
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bindData(list, position, lastSelectIndex);
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }


        public int getSelectIndex() {
            return lastSelectIndex;
        }
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView bankIcon;
        public TextView bankName;
        public ImageView selectState;
        private SelectListener selectListener;
        private int index;

        public MyViewHolder(View itemView, SelectListener selectListener) {
            super(itemView);
            bankIcon = itemView.findViewById(R.id.iv_back_icon);
            bankName = itemView.findViewById(R.id.tv_back_name);
            selectState = itemView.findViewById(R.id.iv_select_state);

            this.selectListener = selectListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyViewHolder.this.selectListener != null) MyViewHolder.this.selectListener.select(index);
                }
            });

        }


        public void bindData(List<BuyPublicFundActivity.BankCardInfo> selectListener, int postion, int selectIndex) {
            index = postion;
            BuyPublicFundActivity.BankCardInfo bankCardInfo = selectListener.get(postion);

            String bankCoade = bankCardInfo.getDepositacct();
            if (bankCoade.length() > 4) {
                bankCoade = bankCoade.substring(bankCoade.length() - 4);
            }
            bankName.setText(bankCardInfo.getBankname() + "(" + bankCoade + ")");
            bankIcon.setBackgroundResource(R.drawable.bank_icon);
            if (postion == selectIndex) {
                selectState.setVisibility(View.VISIBLE);
                selectState.setBackgroundResource(R.drawable.icon_paybank_selected);
            } else {
                selectState.setVisibility(View.GONE);
            }
        }
    }


    public interface SelectListener {
        void select(int index);
    }

}
