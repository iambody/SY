package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.widget.RoundImageView;
import com.cgbsoft.lib.widget.RoundProgressbar;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.model.MineModel;
import com.cgbsoft.privatefund.mvp.contract.home.MineContract;
import com.cgbsoft.privatefund.mvp.presenter.home.MinePresenter;

import butterknife.BindView;

/**
 * @author chenlong
 *
 * 我的fragment
 */
public class MineFragment extends BaseFragment<MinePresenter> implements MineContract.View {

    @BindView(R.id.account_info_name)
    TextView textViewName;

    @BindView(R.id.account_info_image_id)
    RoundImageView roundImageView;

    @BindView(R.id.mine_caifu_value)
    TextView textViewCaifu;

    @BindView(R.id.mine_yundou_id)
    TextView textViewYundou;

    @BindView(R.id.mine_private_banker_id)
    TextView textViewPrivateBanker;

    @BindView(R.id.mine_account_info_qiandao_ll)
    LinearLayout linearLayoutQiandao;

    @BindView(R.id.mine_account_info_activity_ll)
    LinearLayout linearLayoutActivity;

    @BindView(R.id.mine_account_info_ticket_ll)
    LinearLayout linearLayoutTicket;

    @BindView(R.id.mine_account_info_cards_ll)
    LinearLayout linearLayoutCard;

    @BindView(R.id.account_bank_assert_total_text)
    TextView textViewAssertTotalText;

    @BindView(R.id.account_bank_assert_total_value)
    TextView textViewAssertTotalValue;

    @BindView(R.id.account_bank_assert_guquan_text)
    TextView textViewGuquanText;

    @BindView(R.id.account_bank_assert_guquan_value)
    TextView textViewGuquanValue;

    @BindView(R.id.account_bank_assert_zhaiquan_text)
    TextView textViewzhaiquanText;

    @BindView(R.id.account_bank_assert_zhaiquan_value)
    TextView textViewzhaiquanValue;

    @BindView(R.id.account_bank_on_bug_ll)
    LinearLayout linearLayoutBankNoData;

    @BindView(R.id.account_bank_had_bug_ll)
    LinearLayout linearLayoutBankHadData;

    @BindView(R.id.roundProgressBar)
    RoundProgressbar roundProgressbar;

    @Override
    protected int layoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        textViewName.setText(AppManager.getUserInfo(getActivity()).getUserName());
        Imageload.display(getActivity(), AppManager.getUserInfo(getActivity()).getHeadImageUrl(), roundImageView);
    }

    private void initMineInfo(MineModel mineModel) {
        if (mineModel != null) {
            linearLayoutBankNoData.setVisibility(mineModel.getBank() == null ? View.VISIBLE : View.GONE );
            linearLayoutBankHadData.setVisibility(mineModel.getBank() == null ? View.GONE : View.VISIBLE);
            roundProgressbar.setProgress(40);
//            textViewAssertTotalText.setText(String.format(getString(R.string.account_bank_cunxun_assert), mineModel.getBank().getDurationUnit()));
            textViewAssertTotalValue.setText(mineModel.getBank().getDurationAmt());
//            textViewGuquanText.setText(String.format(getString(R.string.account_bank_guquan_assert), mineModel.getBank().getEquityUnit(), mineModel.getBank().getEquityRatio()));
            textViewGuquanValue.setText(mineModel.getBank().getEquityAmt());
//            textViewzhaiquanText.setText(String.format(getString(R.string.account_bank_zhaiquan_assert), mineModel.getBank().getDebtUnit(), mineModel.getBank().getDebtRatio()));
            textViewzhaiquanValue.setText(mineModel.getBank().getDebtAmt());
        }
    }

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter(getActivity(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void requestDataSuccess(MineModel mineModel) {

    }

    @Override
    public void requestDataFailure(String errMsg) {

    }
}

