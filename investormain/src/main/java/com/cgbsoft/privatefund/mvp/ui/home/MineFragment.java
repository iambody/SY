package com.cgbsoft.privatefund.mvp.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.lib.widget.RoundImageView;
import com.cgbsoft.lib.widget.RoundProgressbar;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.model.MineModel;
import com.cgbsoft.privatefund.mvp.contract.home.MineContract;
import com.cgbsoft.privatefund.mvp.presenter.home.MinePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenlong
 *
 * 我的fragment
 */
public class MineFragment extends BaseFragment<MinePresenter> implements MineContract.View {

    @BindView(R.id.mine_title_set_id)
    ImageView titleImageLeft;

    @BindView(R.id.mine_title_message_id)
    ImageView titleImageRight;

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

    @BindView(R.id.health_all_title_ll)
    LinearLayout health_title_all_ll;

    @BindView(R.id.account_health_had_bug_ll)
    LinearLayout health_had_data_ll;

    @BindView(R.id.account_health_on_bug_ll)
    LinearLayout health_had_no_data_ll;

    @BindView(R.id.account_order_send_ll)
    LinearLayout order_waith_send_ll;

    @BindView(R.id.account_order_receive_ll)
    LinearLayout order_waith_receive_ll;

    @BindView(R.id.account_order_finished_ll)
    LinearLayout order_waith_finished_ll;

    @BindView(R.id.account_order_sale_ll)
    LinearLayout order_waith_after_sale_ll;

    @BindView(R.id.account_order_all_ll)
    LinearLayout order_waith_all_ll;

    @BindView(R.id.account_order_receive_text)
    TextView account_order_receive_text;

    @Override
    protected int layoutID() {
        return R.layout.fragment_mine;
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
        initMineInfo(mineModel);
    }

    @Override
    public void requestDataFailure(String errMsg) {

    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        textViewName.setText(AppManager.getUserInfo(getActivity()).getUserName());
        Imageload.display(getActivity(), AppManager.getUserInfo(getActivity()).getHeadImageUrl(), roundImageView);
        if (AppManager.getUserInfo(getActivity()).getToC() != null) {
            textViewCaifu.setText(AppManager.getUserInfo(getActivity()).getToC().getWealth());
            textViewYundou.setText(String.valueOf(AppManager.getUserInfo(getActivity()).getToC().getMyPoint()));
            textViewPrivateBanker.setText(AppManager.getUserInfo(getActivity()).getToC().getAdviserRealName());
        }
        getPresenter().getMineData();
    }

    @OnClick(R.id.mine_title_set_id)
    void gotoSetActivity() {
        // TODO 设置
    }

    @OnClick(R.id.mine_title_message_id)
    void gotoMessagectivity() {
        NavigationUtils.startActivityByRouter(getActivity(), RouteConfig.IM_MESSAGE_LIST_ACTIVITY);
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
            initOrderView(mineModel.getMallOrder());
            initHealthView(mineModel);
        }
    }

    private void initOrderView(List<MineModel.Orders> mallOrder) {
        if (!CollectionUtils.isEmpty(mallOrder)) {
            for (MineModel.Orders orders : mallOrder) {
                LinearLayout current = null;
                switch (orders.getGoodsStatusCode()) {
                    case "1":
                        current = order_waith_send_ll;
                        break;
                    case "2":
                        current = order_waith_receive_ll;
                        break;
                    case "3":
                        current = order_waith_finished_ll;
                        break;
                    case "4":
                        current = order_waith_after_sale_ll;
                        break;
                    case "0":
                        current = order_waith_all_ll;
                        break;
                }
//                ViewUtils.createTopRightBadgerView(getActivity(), current, orders.getCount());
            }
            ViewUtils.createTopRightBadgerView(getActivity(), account_order_receive_text, 9);
        }
    }

    private void initHealthView(MineModel mineModel) {
        if (mineModel != null && mineModel.getHealthy() != null) {
            health_had_data_ll.setVisibility(CollectionUtils.isEmpty(mineModel.getHealthy().getContent()) ? View.GONE : View.VISIBLE);
            health_had_no_data_ll.setVisibility(CollectionUtils.isEmpty(mineModel.getHealthy().getContent()) ? View.VISIBLE : View.GONE);
            if (!CollectionUtils.isEmpty(mineModel.getHealthy().getContent())) {
                health_title_all_ll.setOnClickListener(v -> NavigationUtils.startActivityByRouter(getActivity(), RouteConfig.GOTO_BASE_WEBVIEW, WebViewConstant.push_message_url, mineModel.getHealthy().getAllHealthy()));
                createHealthItem(mineModel.getHealthy().getContent());
            }
        }
    }

    private void createHealthItem(List<MineModel.HealthItem> list) {
        if (!CollectionUtils.isEmpty(list)) {
            for (int i= 0; i < list.size(); i++) {
                MineModel.HealthItem healthItem  = list.get(i);
                TextView textView = new TextView(getActivity());
                textView.setGravity(Gravity.CENTER);
                textView.setHeight(DimensionPixelUtil.dip2px(getActivity(), 60));
                textView.setText(healthItem.getTitle());
                textView.setTextColor(Color.parseColor("#5a5a5a"));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                textView.setOnClickListener(v -> NavigationUtils.startActivityByRouter(getActivity(), RouteConfig.GOTO_BASE_WEBVIEW, WebViewConstant.push_message_url, healthItem.getUrl()));
                health_had_data_ll.addView(textView);
                if (i != list.size() -1) {
                    health_had_data_ll.addView(LayoutInflater.from(getActivity()).inflate(R.layout.acitivity_divide_online, null));
                }
            }
        }
    }
}

