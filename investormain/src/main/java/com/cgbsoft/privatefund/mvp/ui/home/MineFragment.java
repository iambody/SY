package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.view.View;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.privatefund.R;

/**
 * @author chenlong
 *
 * 我的fragment
 */
public class MineFragment extends BaseFragment {

//    @BindView(R.id.mine_caifu_value)
//    TextView textViewCaifu;
//
//    @BindView(R.id.mine_yundou_id)
//    TextView textViewYundou;
//
//    @BindView(R.id.mine_private_banker_id)
//    TextView textViewPrivateBanker;
//
//    @BindView(R.id.mine_account_info_qiandao_ll)
//    LinearLayout linearLayoutQiandao;
//
//    @BindView(R.id.mine_account_info_activity_ll)
//    LinearLayout linearLayoutActivity;
//
//    @BindView(R.id.mine_account_info_ticket_ll)
//    LinearLayout linearLayoutTicket;
//
//    @BindView(R.id.mine_account_info_cards_ll)
//    LinearLayout linearLayoutCard;
//
//    @BindView(R.id.account_bank_assert_total_value)
//    TextView textViewAssertTotal;
//
//    @BindView(R.id.account_bank_assert_zhaiquan_text)
//    TextView textViewGuquan;
//
//    @BindView(R.id.account_bank_assert_zhaiquan_value)
//    TextView textViewGuquanValue;
//
//    @BindView(R.id.account_bank_assert_zhaiquan_texta)
//    TextView textViewzhaiquanText;
//
//    @BindView(R.id.account_bank_assert_zhaiquan_valuea)
//    TextView textViewzhaiquanValue;

    @Override
    protected int layoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {}

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

