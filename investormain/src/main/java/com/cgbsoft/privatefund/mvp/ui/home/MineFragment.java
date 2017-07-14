package com.cgbsoft.privatefund.mvp.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.mvp.model.video.VideoInfoModel;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.lib.widget.RoundImageView;
import com.cgbsoft.lib.widget.RoundProgressbar;
import com.cgbsoft.privatefund.InitApplication;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.model.MineModel;
import com.cgbsoft.privatefund.mvp.contract.home.MineContract;
import com.cgbsoft.privatefund.mvp.presenter.home.MinePresenter;
import com.cgbsoft.privatefund.mvp.ui.center.DatumManageActivity;
import com.cgbsoft.privatefund.mvp.ui.center.SettingActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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

    @BindView(R.id.user_leaguar_level)
    TextView userLeaguarLevel;

    @BindView(R.id.user_leaguar_update_desc)
    TextView userLeaguarUpdateDesc;

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

    @BindView(R.id.account_health_had_bug_ll)
    LinearLayout health_had_data_ll;

    @BindView(R.id.account_health_on_bug_ll)
    LinearLayout health_had_no_data_ll;

    @BindView(R.id.account_order_send_text)
    TextView account_order_send_text;

    @BindView(R.id.account_order_receive_text)
    TextView account_order_receive_text;

    @BindView(R.id.account_order_finished_text)
    TextView account_order_finished_text;

    @BindView(R.id.account_order_sale_text)
    TextView account_order_sale_text;

    @BindView(R.id.account_order_all_text)
    TextView account_order_all_text;

    @BindView(R.id.tab_layout)
    XTabLayout xTabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private DaoUtils daoUtils;

    private String[] videos;

    private MineModel mineModel;

    private static final long DEALAY = 100;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            handler.postDelayed(runnable, DEALAY);
        }
    };

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
        daoUtils = new DaoUtils(getActivity(), DaoUtils.W_VIDEO);
        initVideoView();
        getPresenter().getMineData();
    }

    @OnClick(R.id.mine_title_set_id)
    void gotoSetActivity() {
        NavigationUtils.startActivity(getActivity(), SettingActivity.class);
    }

    @OnClick(R.id.mine_title_message_id)
    void gotoMessagectivity() {
        NavigationUtils.startActivityByRouter(getActivity(), RouteConfig.IM_MESSAGE_LIST_ACTIVITY);
    }

    @OnClick(R.id.account_info_name)
    void gotoPersonInfoNamectivity() {
        NavigationUtils.startActivityByRouter(getActivity(), RouteConfig.GOTOC_PERSONAL_INFORMATION_ACTIVITY);
    }

    @OnClick(R.id.account_info_image_id)
    void gotoPersonInfoHeaderctivity() {
        NavigationUtils.startActivityByRouter(getActivity(), RouteConfig.GOTOC_PERSONAL_INFORMATION_ACTIVITY);
    }

    @OnClick(R.id.account_info_level_ll)
    void gotoPersonInfoctivity() {
        NavigationUtils.startActivityByRouter(getActivity(), RouteConfig.GOTOC_PERSONAL_INFORMATION_ACTIVITY);
    }

    @OnClick(R.id.account_info_caifu_value_ll)
    void gotoWealthctivity() {
        String url = CwebNetConfig.memeberArea;
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.push_message_url, url);
        hashMap.put(WebViewConstant.push_message_title, getString(R.string.mine_members));
        NavigationUtils.startActivity(getActivity(), BaseWebViewActivity.class, hashMap);
    }

     private Runnable runnable = () -> {
         int currentProgress = roundProgressbar.getProgress();
         if (currentProgress > 40) {
             return;
         }
         roundProgressbar.setProgress(currentProgress + 5);
         handler.sendMessage(Message.obtain());
    };

    @OnClick(R.id.account_info_yundou_value_ll)
    void gotoYundouctivity() {
        // 云豆
    }

    @OnClick(R.id.account_info_private_bank_value_ll)
    void gotoPrivateBanktivity() {
        // 私行
        String realName = AppManager.getUserInfo(getActivity()).getToC().getAdviserRealName();
        if (TextUtils.isEmpty(realName)) {
            String url = CwebNetConfig.selectAdviser;
            HashMap<String ,String> hashMap = new HashMap<>();
            hashMap.put(WebViewConstant.push_message_url, url);
            hashMap.put(WebViewConstant.push_message_title, getString(R.string.select_bind_advise));
            NavigationUtils.startActivity(getActivity(), BaseWebViewActivity.class, hashMap);
        } else {
            // 去私行
        }
    }

    @OnClick(R.id.mine_account_info_qiandao_ll)
    void gotoQiandaoActivity() {
        String url = CwebNetConfig.signInPage;
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.push_message_url, url);
        hashMap.put(WebViewConstant.push_message_title, getString(R.string.mine_signin));
        NavigationUtils.startActivity(getActivity(), BaseWebViewActivity.class, hashMap);
    }

    @OnClick(R.id.mine_account_info_activity_ll)
    void gotoMineActiviteActivity() {
        NavigationUtils.startActivity(getActivity(), MineActiviesActivity.class);
    }

    @OnClick(R.id.mine_account_info_ticket_ll)
    void gotoCouponsActivity() {
        String url = CwebNetConfig.mineCardCoupons;
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.push_message_url, url);
        hashMap.put(WebViewConstant.push_message_title, getString(R.string.mine_card_coupons));
        NavigationUtils.startActivity(getActivity(), BaseWebViewActivity.class, hashMap);
    }

    @OnClick(R.id.mine_account_info_cards_ll)
    void gotoBestCardActivity() {
        String url = CwebNetConfig.mineBestCard;
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.push_message_url, url);
        hashMap.put(WebViewConstant.push_message_title, getString(R.string.mine_best_card));
        NavigationUtils.startActivity(getActivity(), BaseWebViewActivity.class, hashMap);
    }

    @OnClick(R.id.account_bank_go_look_product)
    void gotoLookProductActivity() {
        // 去看产品
        RxBus.get().post(RxConstant.INVERSTOR_MAIN_PAGE, 1);
    }

    @OnClick(R.id.account_bank_go_relative_assert)
    void gotoRelativeAssetActivity() {
        NavigationUtils.startActivity(getActivity(), RelativeAssetActivity.class);
    }

    @OnClick(R.id.mine_bank_asset_match_ll)
    void gotoAssetMatchActivity() {
        String url = CwebNetConfig.mineAssertOrder;
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.push_message_url, url.concat("?labelType="));
        hashMap.put(WebViewConstant.push_message_title, getString(R.string.mine_assert_order));
        NavigationUtils.startActivity(getActivity(), BaseWebViewActivity.class, hashMap);
    }

    @OnClick(R.id.mine_bank_invistor_carlendar_ll)
    void gotoInvestorCarlendarActivity() {
        String url = CwebNetConfig.investeCarlendar;
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.push_message_url, url);
        hashMap.put(WebViewConstant.push_message_title, getString(R.string.mine_investor_carlendar));
        NavigationUtils.startActivity(getActivity(), BaseWebViewActivity.class, hashMap);
    }

    @OnClick(R.id.mine_bank_datum_manager_ll)
    void gotoDatumCarlendarActivity() {
        NavigationUtils.startActivity(getActivity(), DatumManageActivity.class);
    }

    @OnClick(R.id.account_order_goto_receive_address)
    void gotoManagerAddressActivity() {
        // 管理收货地址
    }

    @OnClick(R.id.account_order_send_ll)
    void gotoWaitSendHuoActivity() {
        String url = CwebNetConfig.mineGoodsOrder.concat("?labelType=1");
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.push_message_url, url);
        hashMap.put(WebViewConstant.push_message_title, getString(R.string.mine_order));
        NavigationUtils.startActivity(getActivity(), BaseWebViewActivity.class, hashMap);
    }

    @OnClick(R.id.account_order_receive_ll)
    void gotoWaitReceiveHuoActivity() {
        String url = CwebNetConfig.mineGoodsOrder.concat("?labelType=2");
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.push_message_url, url);
        hashMap.put(WebViewConstant.push_message_title, getString(R.string.mine_order));
        NavigationUtils.startActivity(getActivity(), BaseWebViewActivity.class, hashMap);
    }

    @OnClick(R.id.account_order_finished_ll)
    void gotoFinishedActivity() {
        String url = CwebNetConfig.mineGoodsOrder.concat("?labelType=3");
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.push_message_url, url);
        hashMap.put(WebViewConstant.push_message_title, getString(R.string.mine_order));
        NavigationUtils.startActivity(getActivity(), BaseWebViewActivity.class, hashMap);
    }

    @OnClick(R.id.account_order_sale_ll)
    void gotoAfterSaleActivity() {
        String url = CwebNetConfig.mineGoodsOrder.concat("?labelType=4");
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.push_message_url, url);
        hashMap.put(WebViewConstant.push_message_title, getString(R.string.mine_order));
        NavigationUtils.startActivity(getActivity(), BaseWebViewActivity.class, hashMap);
    }

    @OnClick(R.id.account_order_all_ll)
    void gotoOrderAllctivity() {
        String url = CwebNetConfig.mineGoodsOrder.concat("?labelType=0");
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.push_message_url, url);
        hashMap.put(WebViewConstant.push_message_title, getString(R.string.mine_order));
        NavigationUtils.startActivity(getActivity(), BaseWebViewActivity.class, hashMap);
    }

    @OnClick(R.id.health_all_title_ll)
    void gotoHealthAllctivity() {
        String url = CwebNetConfig.mineHealthOrder;
        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.push_message_url, url);
        hashMap.put(WebViewConstant.push_message_title, getString(R.string.mine_health_list));
        NavigationUtils.startActivity(getActivity(), BaseWebViewActivity.class, hashMap);
    }

    private void initMineInfo(MineModel mineModel) {
        if (mineModel != null) {
            this.mineModel = mineModel;
            handler.postDelayed(runnable, DEALAY);
            initUserInfo(mineModel);
            initPrivateBank(mineModel);
            initOrderView(mineModel);
            initHealthView(mineModel);
        }
    }

    private void initUserInfo(MineModel mineModel) {
        if (mineModel.getMyInfo() != null) {
            MineModel.MineUserInfo mineUserInfo = mineModel.getMyInfo();
            textViewName.setText(mineUserInfo.getNickName());
            Imageload.display(getActivity(), mineUserInfo.getHeadImageUrl(), roundImageView, R.drawable.logo, R.drawable.logo);
            userLeaguarLevel.setText(mineUserInfo.getMemberLevel());
            userLeaguarUpdateDesc.setText(mineUserInfo.getMemberBalance());
            textViewCaifu.setText(mineUserInfo.getMemberValue());
            textViewYundou.setText(mineUserInfo.getYdTotal());
            textViewPrivateBanker.setText(TextUtils.isEmpty(mineUserInfo.getAdviserName()) ? "无" : mineUserInfo.getAdviserName());
        }
    }

    private void initPrivateBank(MineModel mineModel) {
        linearLayoutBankNoData.setVisibility(mineModel.getBank() == null ? View.VISIBLE : View.GONE );
        linearLayoutBankHadData.setVisibility(mineModel.getBank() == null ? View.GONE : View.VISIBLE);
        if (mineModel.getBank() != null) {
            MineModel.PrivateBank privateBank = mineModel.getBank();
            textViewAssertTotalText.setText(String.format(getString(R.string.account_bank_cunxun_assert), privateBank.getDurationUnit()));
            textViewAssertTotalValue.setText(mineModel.getBank().getDurationAmt());
            textViewGuquanValue.setText(mineModel.getBank().getEquityAmt());
            textViewzhaiquanValue.setText(mineModel.getBank().getDebtAmt());
            textViewGuquanText.setText(String.format(getString(R.string.account_bank_guquan_assert), privateBank.getEquityUnit(), TextUtils.isEmpty(privateBank.getEquityRatio()) ? "0%" : privateBank.getEquityRatio().concat("%")));
            textViewzhaiquanText.setText(String.format(getString(R.string.account_bank_zhaiquan_assert), privateBank.getDebtUnit(), TextUtils.isEmpty(privateBank.getDebtRatio()) ? "0%" : privateBank.getDebtRatio().concat("%")));
        }
    }

    private void initOrderView(MineModel mineModel) {
        if (!CollectionUtils.isEmpty(mineModel.getMallOrder())) {
            for (MineModel.Orders orders : mineModel.mallOrder) {
                TextView current = null;
                switch (orders.getGoodsStatusCode()) {
                    case "1":
                        current = account_order_send_text;
                        break;
                    case "2":
                        current = account_order_receive_text;
                        break;
                    case "3":
                        current = account_order_finished_text;
                        break;
                    case "4":
                        current = account_order_sale_text;
                        break;
                    case "0":
                        current = account_order_all_text;
                        break;
                }
                if (orders.getCount() > 0) {
                    ViewUtils.createTopRightBadgerView(getActivity(), current, orders.getCount());
                }
            }
        }
    }

    private void initHealthView(MineModel mineModel) {
        if (mineModel != null && mineModel.getHealthy() != null) {
//            health_had_data_ll.setVisibility(CollectionUtils.isEmpty(mineModel.getHealthy().getContent()) ? View.GONE : View.VISIBLE);
//            health_had_no_data_ll.setVisibility(CollectionUtils.isEmpty(mineModel.getHealthy().getContent()) ? View.VISIBLE : View.GONE);
            if (!CollectionUtils.isEmpty(mineModel.getHealthy().getContent())) {
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
                    health_had_data_ll.addView(LayoutInflater.from(getActivity()).inflate(R.layout.acitivity_divide_online, health_had_data_ll));
                }
            }
        }
    }

    private void initVideoView() {
        videos = InitApplication.getContext().getResources().getStringArray(R.array.mine_video_tag_text);
        List<VideoInfoModel> playlList = daoUtils.getAllVideoInfoHistory();
        List<VideoInfoModel> downlList = daoUtils.getAllVideoInfo();
        Log.i("MineFragment", "playlist=" + + playlList.size() + "-----downlList=" + downlList.size());
        for (String name : videos) {
            XTabLayout.Tab tab = xTabLayout.newTab();
            tab.setText(name);
            xTabLayout.addTab(tab);
        }
        viewPager.setOffscreenPageLimit(2);
        List<Fragment> list = new ArrayList<>();
        setFragmentParams(playlList, list, true);
        setFragmentParams(downlList, list, false);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                if (object instanceof View) {
                    container.removeView((View) object);
                } else if (object instanceof Fragment) {
                    getChildFragmentManager().beginTransaction().detach((Fragment) object);
                }
            }
        });
        xTabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < xTabLayout.getTabCount(); i++) {
            xTabLayout.getTabAt(i).setText(videos[i]);
        }
    }

    private void setFragmentParams(List<VideoInfoModel> valuesList, List<Fragment> fragmentList, boolean isPlay) {
        HorizontalScrollFragment scrollFragment= new HorizontalScrollFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(HorizontalScrollFragment.GET_VIDEO_PARAMS, valuesList == null ? new ArrayList<>() : (ArrayList)valuesList);
        bundle.putBoolean(HorizontalScrollFragment.IS_VIDEO_PLAY_PARAMS, isPlay);
        scrollFragment.setArguments(bundle);
        fragmentList.add(scrollFragment);
    }
}

