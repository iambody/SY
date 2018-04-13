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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.Utils;
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

    public PayFundBankSelectDialog(Context context, String currectBankCodeNum, List<BuyPublicFundActivity.BankCardInfo> bankCardInfos, SelectListener selectListener) {
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
        lp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;
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
        bankList.setAdapter(new MyAdapter(currectBankCodeNum, bankCardInfos, new SelectListener() {
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

      /*  findViewById(R.id.ll_add_new_bankcord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectListener != null) selectListener.select(-2);
                PayFundBankSelectDialog.this.dismiss();
            }
        });*/
    }


    private static class MyAdapter extends RecyclerView.Adapter {
        private List<BuyPublicFundActivity.BankCardInfo> list;
        private SelectListener selectListener;
        private String currectBankNum = "";

        private static int FOOT = 2;

        public MyAdapter(String currectBankNum, List<BuyPublicFundActivity.BankCardInfo> list, SelectListener selectListener) {
            this.list = list;
            this.selectListener = selectListener;
            this.currectBankNum = currectBankNum;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == FOOT)
                return FootViewHolder.creat(parent.getContext(), MyAdapter.this.selectListener);

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paybank_list, parent, false);
            return new MyViewHolder(view, new SelectListener() {
                @Override
                public void select(int index) {
                    if ("0".equals(list.get(index).getBankEnableStatus())) return;
                    //   MToast.makeText(parent.getContext(), parent.getContext().getString(R.string.public_fund_bank_not_useable), Toast.LENGTH_LONG).show();
                    if (selectListener != null) MyAdapter.this.selectListener.select(index);
                }
            });
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) != FOOT)
                ((MyViewHolder) holder).bindData(list, position, currectBankNum);
        }

        @Override
        public int getItemCount() {
            return list == null ? 1 : list.size() + 1;
        }


        @Override
        public int getItemViewType(int position) {
            return (position == getItemCount() - 1) ? FOOT : -FOOT;
        }
    }


    private static class FootViewHolder extends RecyclerView.ViewHolder {

        public static FootViewHolder creat(Context context, SelectListener selectListener) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.convertDipOrPx(context, 70)));
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.convertDipOrPx(context, 26), Utils.convertDipOrPx(context, 19));
            layoutParams.setMargins(Utils.convertDipOrPx(context, 15), 0, 0, 0);
            ImageView icon = new ImageView(context);
            icon.setBackgroundResource(R.drawable.bank_icon);
            linearLayout.addView(icon, layoutParams);

            LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textLayoutParams.setMargins(Utils.convertDipOrPx(context, 10), 0, 0, 0);
            TextView text = new TextView(context);
            text.setText("使用新卡支付");
            text.setTextColor(context.getResources().getColor(R.color.black));
            text.setTextSize(16);
            linearLayout.addView(text, textLayoutParams);
            return new FootViewHolder(linearLayout, selectListener);
        }

        public FootViewHolder(View itemView, final SelectListener selectListener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectListener != null)
                        selectListener.select(-2);
                }
            });
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
            Imageload.display(bankIcon.getContext(), bankCardInfo.getIcon(), this.bankIcon, R.drawable.bank_icon, R.drawable.bank_icon);
            String bankCoade = bankCardInfo.getDepositAcct();
            if (curBankNum.trim().equals(bankCoade.trim())) {
                selectState.setVisibility(View.VISIBLE);
                selectState.setBackgroundResource(R.drawable.icon_paybank_selected);
            } else {
                selectState.setVisibility(View.GONE);
            }

            if (bankCoade.length() > 4) {
                bankCoade = bankCoade.substring(bankCoade.length() - 4);
            }
            bankName.setText(bankCardInfo.getBankShortName() + "　尾号(" + bankCoade + ")");
            bankLimit.setText(bankCardInfo.getBankLimit());
            if ("0".equals(bankCardInfo.getBankEnableStatus())) {
                bankLimit.setVisibility(View.GONE);
                itemView.findViewById(R.id.tv_not_useable).setVisibility(View.VISIBLE);
            } else {
                bankLimit.setVisibility(View.VISIBLE);
                itemView.findViewById(R.id.tv_not_useable).setVisibility(View.GONE);
            }
        }
    }


    public interface SelectListener {
        void select(int index);
    }

}
