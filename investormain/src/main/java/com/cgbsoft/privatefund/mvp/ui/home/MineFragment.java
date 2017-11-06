package com.cgbsoft.privatefund.mvp.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidkun.xtablayout.XTabLayout;
import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.listener.listener.GestureManager;
import com.cgbsoft.lib.mvp.model.video.VideoInfoModel;
import com.cgbsoft.lib.utils.cache.CacheManager;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.constant.VideoStatus;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.net.NetConfig;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.ViewUtils;
import com.cgbsoft.lib.widget.RoundImageView;
import com.cgbsoft.lib.widget.RoundProgressbar;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;
import com.cgbsoft.privatefund.InitApplication;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.model.CredentialStateMedel;
import com.cgbsoft.privatefund.model.MineModel;
import com.cgbsoft.privatefund.mvp.contract.home.MineContract;
import com.cgbsoft.privatefund.mvp.presenter.home.MinePresenter;
import com.cgbsoft.privatefund.mvp.ui.center.CardCollectActivity;
import com.cgbsoft.privatefund.mvp.ui.center.DatumManageActivity;
import com.cgbsoft.privatefund.mvp.ui.center.SelectIndentityActivity;
import com.cgbsoft.privatefund.mvp.ui.center.SettingActivity;
import com.cgbsoft.privatefund.mvp.ui.center.UploadIndentityCradActivity;
import com.cgbsoft.privatefund.utils.UnreadInfoNumber;
import com.cgbsoft.privatefund.widget.CustomViewPage;
import com.cgbsoft.privatefund.widget.RightShareWebViewActivity;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;
import com.readystatesoftware.viewbadger.BadgeView;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.mall.com.mvp.ui.MallAddressListActivity;
import app.privatefund.com.vido.mvp.ui.video.model.VideoDownloadListModel;
import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongContext;
import me.grantland.widget.AutofitTextView;
import rx.Observable;

/**
 * @author chenlong
 *         <p>
 *         我的fragment
 */
public class MineFragment extends BaseFragment<MinePresenter> implements MineContract.View, HorizontalScrollFragment.ChangeHeightListener {

    @BindView(R.id.account_info_name)
    TextView textViewName;

    @BindView(R.id.mine_title_message_id)
    ImageView imageViewMessagIcon;

    @BindView(R.id.show_current_select_address)
    TextView textViewCurrentAddress;

    @BindView(R.id.account_info_image_id)
    RoundImageView roundImageView;

    @BindView(R.id.user_leaguar_level)
    TextView userLeaguarLevel;

    @BindView(R.id.user_leaguar_update_desc)
    TextView userLeaguarUpdateDesc;

    @BindView(R.id.mine_caifu_value)
    AutofitTextView textViewCaifu;

    @BindView(R.id.mine_yundou_id)
    AutofitTextView textViewYundou;

    @BindView(R.id.mine_private_banker_id)
    AutofitTextView textViewPrivateBanker;

    @BindView(R.id.mine_account_info_qiandao_ll)
    LinearLayout linearLayoutQiandao;

    @BindView(R.id.mine_account_info_activity_ll)
    LinearLayout linearLayoutActivity;

    @BindView(R.id.mine_account_info_ticket_ll)
    LinearLayout linearLayoutTicket;

    @BindView(R.id.mine_account_info_cards_ll)
    LinearLayout linearLayoutCard;

    @BindView(R.id.account_bank_hide_assert)
    TextView textViewShowAssert;

    @BindView(R.id.account_bank_go_relative_assert)
    TextView noRelativeAssert;

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

    @BindView(R.id.private_bank_bottom_buttons)
    LinearLayout privateBackBottomButtons;

    @BindView(R.id.viewpager)
    CustomViewPage viewPager;

    private DaoUtils daoUtils;
    private String[] videos;
    private MineModel mineModel;
    private boolean showAssert;
    private boolean isLoading;
    private static final long DEALAY = 500;
    private static final int WAIT_CHECK = 1;
    private static final int CHECK_PAST = 2;
    private static final int CHECK_FAILURE = 3;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 10;
    private Observable<Boolean> swtichAssetObservable;
    private Observable<String> switchGroupObservable;
    private List<HorizontalScrollFragment> videoList;
    private CredentialStateMedel credentialStateMedel;

    private BadgeView waitSender;
    private BadgeView waitReceiver;
    private UnreadInfoNumber unreadInfoNumber;

    public static final String LEVER_NAME = "lever_name_value";
    public boolean isClickBack;
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            float currentProgress = (float) (roundProgressbar.getProgress());
////         float guQuanValue = Float.parseFloat(mineModel.getBank().getEquityRatio());
//            float zhaiQuanValue = Float.parseFloat(mineModel.getBank().getDebtRatio());
//            if (currentProgress >= zhaiQuanValue) {
//                return;
//            }
//            roundProgressbar.setProgress((int)currentProgress + 1);
//        }
//    };

    @Override
    protected void before() {
        super.before();
        showAssert = AppManager.isShowAssert(getActivity());
        showSelectAddress();
    }

    private void showSelectAddress() {
        try {
            ApplicationInfo appInfo = RongContext.getInstance().getPackageManager().getApplicationInfo(RongContext.getInstance().getPackageName(), PackageManager.GET_META_DATA);
            String msg = appInfo.metaData.getString("RONG_CLOUD_APP_KEY");
            if (("tdrvipksrbgn5".equals(msg))) {
                textViewCurrentAddress.setVisibility(View.VISIBLE);
                textViewCurrentAddress.setText("你当前的地址是：".concat(NetConfig.SERVER_ADD));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int layoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter(getActivity(), this);
    }

    @Override
    public void requestDataSuccess(MineModel mineModel) {
        isLoading = false;
        initMineInfo(mineModel);
    }

    @Override
    public void requestDataFailure(String errMsg) {
        isLoading = false;
    }

    @Override
    public void verifyIndentitySuccess(String identity, String hasIdCard, String title, String credentialCode, String status, String statusCode, String customerName, String credentialNumber, String credentialTitle, String existStatus, String credentialCodeExist) {
//        this.identity = identity;
//        this.hasIdCard = hasIdCard;
//        this.title = title;
//        this.credentialCode = "45".equals(existStatus) ? credentialCodeExist : credentialCode;
//        this.status = status;
//        this.statusCode = statusCode;
//        this.customerName = customerName;
//        this.credentialNumber = credentialNumber;
//        this.credentialTitle = credentialTitle;
//        this.existStatus = existStatus;
//        if (TextUtils.isEmpty(statusCode)) {
//            noRelativeAssert.setText(getResources().getString(R.string.account_bank_no_relative_assert));
//        } else if (!TextUtils.isEmpty(statusCode) && "50".equals(statusCode)) {
//            noRelativeAssert.setVisibility(View.GONE);
//        } else {
//            noRelativeAssert.setText(String.format(getString(R.string.account_bank_no_relative_assert_with_status_new), status));
////            noRelativeAssert.setText(getString(R.string.account_bank_no_relative_assert));
//        }
//
//        if (isClickBack) {
//            isClickBack = false;
//            if ("45".equals(existStatus)) {
//                replenishCards();
//            } else {
//                if (!TextUtils.isEmpty(identity)) {
//                    if ("1001".equals(identity) && "0".equals(hasIdCard)) {//去上传证件照
//                        Intent intent = new Intent(getActivity(), UploadIndentityCradActivity.class);
//                        intent.putExtra("credentialCode", credentialCode);
//                        intent.putExtra("indentityCode", identity);
//                        intent.putExtra("title", title);
//                        startActivity(intent);
//                    } else {//去证件列表
//                        Intent intent = new Intent(getActivity(), CardCollectActivity.class);
//                        intent.putExtra("indentityCode", identity);
//                        startActivity(intent);
//                    }
//                } else {//无身份
//                    Intent intent = new Intent(getActivity(), SelectIndentityActivity.class);
//                    startActivity(intent);
//                }
//            }
//        }
    }

    @Override
    public void verifyIndentityError(Throwable e) {
        if (isClickBack) {
            isClickBack = false;
            Toast.makeText(getActivity().getApplicationContext(), "服务器忙,请稍后再试!", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * @param credentialStateMedel 获取身份状态信息
     *                             <p>
     *                             "credentialDetailId": 8630,
     *                             "credentialCode": "100101",
     *                             "customerType": "10",
     *                             "credentialState": "50",
     *                             "customerImageState": "0",
     *                             "idCardStateName": "已通过",
     *                             "customerIdentity": "1001",
     *                             "credentialTypeName": "身份证",
     *                             "customerLivingbodyState": "0",
     *                             "credentialStateName": "已通过",
     *                             "idCardState": "50"
     */
    @Override
    public void verifyIndentitySuccessV3(CredentialStateMedel credentialStateMedel) {
        String stateCode;
        String stateName;
        this.credentialStateMedel = credentialStateMedel;
        if ("1001".equals(credentialStateMedel.getCustomerIdentity())) {
            stateCode = credentialStateMedel.getIdCardState();
            stateName = credentialStateMedel.getIdCardStateName();
            if ("30".equals(credentialStateMedel.getIdCardState())) {
                privateBackBottomButtons.setVisibility(View.GONE);
            }
        } else {
            stateCode = credentialStateMedel.getCredentialState();
            stateName = credentialStateMedel.getCredentialStateName();
            if ("30".equals(credentialStateMedel.getCredentialState())) {
                privateBackBottomButtons.setVisibility(View.GONE);
            }
        }

        if (TextUtils.isEmpty(stateCode)) {
            noRelativeAssert.setText(getResources().getString(R.string.account_bank_no_relative_assert));
        } else if (!TextUtils.isEmpty(stateCode) && "50".equals(stateCode)) {
            noRelativeAssert.setVisibility(View.GONE);
        } else {
            noRelativeAssert.setText(String.format(getString(R.string.account_bank_no_relative_assert_with_status_new), stateName));
//            noRelativeAssert.setText(getString(R.string.account_bank_no_relative_assert));
        }
        if (TextUtils.isEmpty(credentialStateMedel.getCustomerIdentity())) {
            privateBackBottomButtons.setVisibility(View.GONE);
        }

        if (isClickBack) {
            isClickBack = false;
            //判断是否是大陆居民
            if (credentialStateMedel.getCustomerIdentity().equals("1001")) {
                // 判断是否是身份证
                if (credentialStateMedel.getCredentialState().equals("100101")) {
                    /**
                     *     判断状态
                     * 5： 新用户未上传；
                     * 10：新用户审核中；
                     * 30：新用户已驳回；
                     * 45：存量用户已有证件号码未上传证件照；
                     * 50：新用户已通过；
                     * 70：新用户已过期；
                     */
                    switch (credentialStateMedel.getIdCardState()) {
                        case "5":
                            Intent intent = new Intent(getActivity(), SelectIndentityActivity.class);
                            startActivity(intent);
                            break;
                        case "10":
                            jumpGuidePage();
                            break;
                        case "30":
                            jumpGuidePage();
                            break;
                        case "45":
                            jumpGuidePage();
                            break;
                        case "50":
                            if ("0".equals(credentialStateMedel.getCustomerLivingbodyState())) {
                                jumpGuidePage();
                            } else {
                                Intent intent1 = new Intent(getActivity(), CardCollectActivity.class);
                                intent1.putExtra("indentityCode", credentialStateMedel.getCustomerIdentity());
                                startActivity(intent1);
                            }
                            break;
                        case "70":
                            jumpGuidePage();
                            break;
                        default:
                            jumpGuidePage();
                            break;
                    }
                } else {   //非身份证
                    Intent intent1 = new Intent(getActivity(), CardCollectActivity.class);
                    intent1.putExtra("indentityCode", credentialStateMedel.getCustomerIdentity());
                    startActivity(intent1);
                }
            } else {
                Intent intent1 = new Intent(getActivity(), CardCollectActivity.class);
                intent1.putExtra("indentityCode", credentialStateMedel.getCustomerIdentity());
                startActivity(intent1);
            }
        }

//        if (isClickBack) {
//            isClickBack = false;
//            if ("45".equals(stateCode)) {
//                replenishCards();
//            } else {
//                if (!TextUtils.isEmpty(identity)) {
//                    if ("1001".equals(identity) && "0".equals(hasIdCard)) {//去上传证件照
//                        Intent intent = new Intent(getActivity(), UploadIndentityCradActivity.class);
//                        intent.putExtra("credentialCode", credentialCode);
//                        intent.putExtra("indentityCode", identity);
//                        intent.putExtra("title", title);
//                        startActivity(intent);
//                    } else {//去证件列表
//                        Intent intent = new Intent(getActivity(), CardCollectActivity.class);
//                        intent.putExtra("indentityCode", identity);
//                        startActivity(intent);
//                    }
//                } else {//无身份
//                    Intent intent = new Intent(getActivity(), SelectIndentityActivity.class);
//                    startActivity(intent);
//                }
//            }
//        }


    }


    private void initObserver() {
        swtichAssetObservable = RxBus.get().register(RxConstant.SWITCH_ASSERT_SHOW, Boolean.class);
        swtichAssetObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                AppInfStore.saveShowAssetStatus(getActivity(), aBoolean);
                showAssert = aBoolean;
                if (aBoolean) {
                    showAssert();
                } else {
                    hideAssert();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });

        switchGroupObservable = RxBus.get().register(RxConstant.SWITCH_GROUP_SHOW, String.class);
        switchGroupObservable.subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String valuse) {
//                AppInfStore.saveShowAssetStatus(getActivity(), true);
                switch (valuse) {
                    case GestureManager.ASSERT_GROUP:
                        if (credentialStateMedel != null) {
//                        toAssertMatchActivit();
                            if (null == credentialStateMedel.getCredentialState()) {
                                isClickBack = true;
//                            getPresenter().verifyIndentity();
                                getPresenter().verifyIndentityV3();
                            } else {
                                isClickBack = false;
                                //90：存量已有证件号已上传证件照待审核
                                if ("45".equals(credentialStateMedel.getCredentialState()) || "45".equals(credentialStateMedel.getIdCardState())) {//存量用户已有证件号码未上传证件照；
                                    jumpGuidePage();
                                } else {
                                    toAssertMatchActivit();
                                }
                            }
                        }
                        break;
                    case GestureManager.INVISTE_CARLENDAR:
                        if (credentialStateMedel != null) {
//                        toInvestorCarlendarActivity();
                            if (null == credentialStateMedel.getCredentialState()) {
                                isClickBack = true;
//                            getPresenter().verifyIndentity();
                                getPresenter().verifyIndentityV3();
                            } else {
                                isClickBack = false;
                                //90：存量已有证件号已上传证件照待审核
                                if ("45".equals(credentialStateMedel.getCredentialState()) || "45".equals(credentialStateMedel.getIdCardState())) {//存量用户已有证件号码未上传证件照；
                                    replenishCards();
                                } else {
                                    toInvestorCarlendarActivity();
                                }
                            }
                        }
                        break;
                    case GestureManager.DATUM_MANAGER:
                        Intent intent1 = new Intent(getActivity(), DatumManageActivity.class);
                        intent1.putExtra("credentialStateMedel",credentialStateMedel);
                        startActivity(intent1);
                        break;
                    case GestureManager.CENTIFY_DIR:
                        RxBus.get().post(RxConstant.GOTO_SWITCH_CENTIFY_DIR, true);
                        break;
                    case GestureManager.RELATIVE_ASSERT:
                        if (credentialStateMedel != null) {
//                        getPresenter().verifyIndentity();
                            if (null == credentialStateMedel.getCredentialState()) {
                                isClickBack = true;
//                            getPresenter().verifyIndentity();
                                getPresenter().verifyIndentityV3();
                            } else {
                                isClickBack = false;
                                if (!TextUtils.isEmpty(credentialStateMedel.getCustomerIdentity())) {
                                    if ("1001".equals(credentialStateMedel.getCustomerIdentity()) ) {//去上传证件照
                                        jumpGuidePage();
                                    } else {//去证件列表
                                        Intent intent = new Intent(getActivity(), CardCollectActivity.class);
                                        intent.putExtra("indentityCode", credentialStateMedel.getCustomerIdentity());
                                        startActivity(intent);
                                    }
                                } else {//无身份
                                    Intent intent = new Intent(getActivity(), SelectIndentityActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                        break;
                    case GestureManager.RELATIVE_ASSERT_IN_DATDMANAGE:
                        RxBus.get().post(RxConstant.GOTO_SWITCH_RELATIVE_ASSERT_IN_DATAMANAGE, true);
                        break;
                }
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });
    }

    private void showAssert() {
        if (mineModel == null) {
            return;
        }
        textViewShowAssert.setText(R.string.account_bank_hide_assert);
        MineModel.PrivateBank privateBank = mineModel.getBank();
        textViewAssertTotalText.setText(String.format(getString(R.string.account_bank_cunxun_assert), privateBank.getDurationUnit()));
        textViewAssertTotalValue.setText(mineModel.getBank().getDurationAmt());
        textViewGuquanValue.setText(mineModel.getBank().getEquityAmt());
        textViewzhaiquanValue.setText(mineModel.getBank().getDebtAmt());
        textViewGuquanText.setText(String.format(getString(R.string.account_bank_guquan_assert), privateBank.getEquityUnit(), TextUtils.isEmpty(privateBank.getEquityRatio()) ? "0%" : privateBank.getEquityRatio().concat("%")));
        textViewzhaiquanText.setText(String.format(getString(R.string.account_bank_zhaiquan_assert), privateBank.getDebtUnit(), TextUtils.isEmpty(privateBank.getDebtRatio()) ? "0%" : privateBank.getDebtRatio().concat("%")));
    }

    private void hideAssert() {
        if (mineModel == null) {
            return;
        }
        MineModel.PrivateBank privateBank = mineModel.getBank();
        textViewShowAssert.setText(R.string.account_bank_show_assert);
        ViewUtils.textViewFormatPasswordType(textViewAssertTotalValue);
        ViewUtils.textViewFormatPasswordType(textViewGuquanValue);
        ViewUtils.textViewFormatPasswordType(textViewzhaiquanValue);
        textViewGuquanText.setText(String.format(getString(R.string.account_bank_guquan_assert), privateBank.getEquityUnit(), ViewUtils.PASSWROD_TYPE_START_FOUR));
        textViewzhaiquanText.setText(String.format(getString(R.string.account_bank_zhaiquan_assert), privateBank.getDebtUnit(), ViewUtils.PASSWROD_TYPE_START_FOUR));
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        daoUtils = new DaoUtils(getActivity(), DaoUtils.W_VIDEO);
        initObserver();
        unreadInfoNumber = new UnreadInfoNumber(getActivity(), imageViewMessagIcon, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constant.SXY_WODE);
        if (isLoading) {
            return;
        }
//        initRelativeStatus();
        isLoading = true;
        initVideoView();
        getPresenter().getMineData();
        getPresenter().verifyIndentityV3();
        if (unreadInfoNumber != null) {
            unreadInfoNumber.initUnreadInfoAndPosition();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constant.SXY_WODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    NavigationUtils.startDialgTelephone(getActivity(), getString(R.string.custom_server_telephone_number));
                } else {
                    Toast.makeText(getActivity(), "请开启用户拨打电话权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void initRelativeStatus() {
        String valuse = "";
        if (WAIT_CHECK == Integer.valueOf(SPreference.getToCBean(getActivity()).getStockAssetsStatus())) {
            valuse = getString(R.string.relative_asset_doing);
        } else if (CHECK_PAST == Integer.valueOf(SPreference.getToCBean(getActivity()).getStockAssetsStatus())) {
            valuse = getString(R.string.relative_asset_past);
        } else if (CHECK_FAILURE == Integer.valueOf(SPreference.getToCBean(getActivity()).getStockAssetsStatus())) {
            valuse = getString(R.string.relative_asset_error);
        }
        noRelativeAssert.setVisibility(CHECK_PAST == Integer.valueOf(SPreference.getToCBean(getActivity()).getStockAssetsStatus()) ? View.INVISIBLE : View.VISIBLE);
        if (!TextUtils.isEmpty(valuse)) {
            noRelativeAssert.setText(String.format(getString(R.string.account_bank_no_relative_assert_with_status), valuse));
        } else {
            noRelativeAssert.setText(getString(R.string.account_bank_no_relative_assert));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
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
        NavigationUtils.startActivityByRouter(getActivity(), RouteConfig.GOTOC_PERSONAL_INFORMATION_ACTIVITY, LEVER_NAME, (mineModel != null && mineModel.getMyInfo() != null) ? mineModel.getMyInfo().getMemberLevel() : "");
    }

    @OnClick(R.id.account_info_image_id)
    void gotoPersonInfoHeaderctivity() {
        NavigationUtils.startActivityByRouter(getActivity(), RouteConfig.GOTOC_PERSONAL_INFORMATION_ACTIVITY, LEVER_NAME, (mineModel != null && mineModel.getMyInfo() != null) ? mineModel.getMyInfo().getMemberLevel() : "");
    }

    @OnClick(R.id.account_info_level_ll)
    void gotoPersonInfoctivity() {
        gotoMemberArea();
    }

    @OnClick(R.id.user_leaguar_update_desc)
    void gotoLeaguarActivity() {
        gotoMemberArea();
    }

    @OnClick(R.id.account_info_caifu_value_ll)
    void gotoWealthctivity() {
        boolean isBind = AppManager.isBindAdviser(baseActivity);
        String url = isBind ? CwebNetConfig.healthValue : CwebNetConfig.memeberArea;
        Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url);
        intent.putExtra(WebViewConstant.push_message_title, isBind ? getString(R.string.account_info_caifu_value) : getString(R.string.mine_members));
        if (!isBind) {
            intent.putExtra(WebViewConstant.RIGHT_MEMBER_RULE_HAS, true);
        }
        startActivity(intent);
    }

    private void gotoMemberArea() {
        String url = CwebNetConfig.memeberArea;
        Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url);
        intent.putExtra(WebViewConstant.push_message_title, getString(R.string.mine_members));
        intent.putExtra(WebViewConstant.RIGHT_MEMBER_RULE_HAS, true);
        startActivity(intent);
    }

//     private Runnable runnable = () -> {
//         Message.obtain(handler).sendToTarget();
//         try {
//             Thread.sleep(50);
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }
//     };

    @OnClick(R.id.account_info_yundou_value_ll)
    void gotoYundouctivity() {
        String mineYunDou = CwebNetConfig.mineYunDou;
        Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, mineYunDou);
        intent.putExtra(WebViewConstant.push_message_title, getString(R.string.account_info_mine_yundou));
        intent.putExtra(WebViewConstant.RIGHT_YUNDOU_RULE_HAS, true);
        startActivity(intent);
    }

    @OnClick(R.id.account_info_private_bank_value_ll)
    void gotoPrivateBanktivity() {
        Intent intent = new Intent(getActivity(), RightShareWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, AppManager.isBindAdviser(baseActivity) ? CwebNetConfig.BindchiceAdiser : CwebNetConfig.choiceAdviser);
        intent.putExtra(WebViewConstant.push_message_title, AppManager.isBindAdviser(baseActivity) ? getString(R.string.mine_private_bank) : getString(R.string.private_bank_jia));
        intent.putExtra(WebViewConstant.PAGE_SHOW_TITLE, false);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.mine_account_info_qiandao_ll)
    void gotoQiandaoActivity() {
        String url = CwebNetConfig.signInPage;
        Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url);
        intent.putExtra(WebViewConstant.push_message_title, getString(R.string.mine_signin));
        startActivity(intent);
    }

    @OnClick(R.id.mine_account_info_activity_ll)
    void gotoMineActiviteActivity() {
        NavigationUtils.startActivity(getActivity(), MineActiviesActivity.class);
        DataStatistApiParam.operateMineActivityClick();
    }

    @OnClick(R.id.mine_account_info_ticket_ll)
    void gotoCouponsActivity() {
        String url = CwebNetConfig.mineCardCoupons;
        Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url);
        intent.putExtra(WebViewConstant.push_message_title, getString(R.string.mine_card_coupons));
        intent.putExtra(WebViewConstant.right_message_index, true);
        startActivity(intent);
        DataStatistApiParam.operateMineCardQuanClick();
    }

    @OnClick(R.id.mine_account_info_cards_ll)
    void gotoBestCardActivity() {
        String url = CwebNetConfig.mineBestCard;
        Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url);
        intent.putExtra(WebViewConstant.push_message_title, getString(R.string.mine_best_card));
        intent.putExtra(WebViewConstant.right_message_index, true);
        startActivity(intent);
    }

    @OnClick(R.id.account_bank_hide_assert)
    void switchAssetNumber() {
        intercepterAssertGesturePassword();
    }

    private void intercepterAssertGesturePassword() {
        if (this.mineModel == null) {
            return;
        }
        if (showAssert) {
            hideAssert();
            showAssert = false;
            AppInfStore.saveShowAssetStatus(getActivity(), false);
        } else {
            GestureManager.showAssertGestureManager(getActivity());
        }
    }

    @OnClick(R.id.account_bank_go_look_product)
    void gotoLookProductActivity() {
        NavigationUtils.jumpNativePage(getActivity(), WebViewConstant.Navigation.PRODUCT_PAGE);
    }

    @OnClick(R.id.account_bank_go_relative_assert)
    void gotoRelativeAssetActivity() {
        if (null!=credentialStateMedel) {
            if (showAssert) {
                isClickBack = true;

                if (null == credentialStateMedel.getCredentialState()) {
                    isClickBack = true;
                    getPresenter().verifyIndentityV3();
                } else {
                    isClickBack = false;
                    if (!TextUtils.isEmpty(credentialStateMedel.getCustomerIdentity())) {
                        if ("1001".equals(credentialStateMedel.getCustomerIdentity())) {//去上传证件照
                            jumpGuidePage();
                        } else {//去证件列表
                            Intent intent = new Intent(getActivity(), CardCollectActivity.class);
                            intent.putExtra("indentityCode", credentialStateMedel.getCustomerIdentity());
                            startActivity(intent);
                        }
                    } else {//无身份
                        Intent intent = new Intent(getActivity(), SelectIndentityActivity.class);
                        startActivity(intent);
                    }
                }
            } else {
                isClickBack = false;
                GestureManager.showGroupGestureManage(getActivity(), GestureManager.RELATIVE_ASSERT);
            }
        }
//        NavigationUtils.startActivity(getActivity(), RelativeAssetActivity.class);
    }

//    @OnClick(R.id.mine_bank_asset_match_ll)
//    void gotoAssetMatchActivity() {
//        if (showAssert) {
//            toAssertMatchActivit();
//        } else {
//            GestureManager.showGroupGestureManage(getActivity(), GestureManager.ASSERT_GROUP);
//        }
//    }

    /**
     * 点击资产饼状图
     */
    @OnClick({R.id.account_bank_had_bug_ll, R.id.mine_bank_asset_match_ll})
    void clickAssetPieChart() {
        if (showAssert) {
            if (null == credentialStateMedel.getCredentialState()) {
                isClickBack = true;
                getPresenter().verifyIndentityV3();
            } else {
                isClickBack = false;
                //90：存量已有证件号已上传证件照待审核
                if ("0".equals(credentialStateMedel.getCustomerLivingbodyState())||"0".equals(credentialStateMedel.getCustomerImageState())) {//存量用户已有证件号码未上传证件照；
                    jumpGuidePage();
                } else {
                    toAssertMatchActivit();
                }
            }
        } else {
            GestureManager.showGroupGestureManage(getActivity(), GestureManager.ASSERT_GROUP);
        }
    }

    /**
     * 跳转到补充证件页面
     */
    private void replenishCards() {
        Intent intent = new Intent(getActivity(), UploadIndentityCradActivity.class);
        intent.putExtra("credentialStateMedel", credentialStateMedel);
        startActivity(intent);
    }

    /**
     * 跳转到引导页面
     */
    private void jumpGuidePage() {
        Intent intent = new Intent(getActivity(), CrenditralGuideActivity.class);
        intent.putExtra("credentialStateMedel", credentialStateMedel);
        startActivity(intent);
    }

    @OnClick(R.id.mine_bank_invistor_carlendar_ll)
    void gotoInvestorCarlendarActivity() {
        if (credentialStateMedel!=null) {
            if (showAssert) {
//            toInvestorCarlendarActivity();
                if (null == credentialStateMedel.getCredentialState()) {
                    isClickBack = true;
                    getPresenter().verifyIndentityV3();
                } else {
                    isClickBack = false;
                    //90：存量已有证件号已上传证件照待审核
                    if ("45".equals(credentialStateMedel.getIdCardState())) {//存量用户已有证件号码未上传证件照；
                        replenishCards();
                    } else {
                        toInvestorCarlendarActivity();
                    }
                }
            } else {
                GestureManager.showGroupGestureManage(getActivity(), GestureManager.INVISTE_CARLENDAR);
            }
        }
    }

    @OnClick(R.id.mine_bank_datum_manager_ll)
    void gotoDatumCarlendarActivity() {
        if (showAssert) {
            Intent intent1 = new Intent(getActivity(), DatumManageActivity.class);
            intent1.putExtra("credentialStateMedel",credentialStateMedel);
            startActivity(intent1);
        } else {
            GestureManager.showGroupGestureManage(getActivity(), GestureManager.DATUM_MANAGER);
        }
    }

    private void toAssertMatchActivit() {
        String url = CwebNetConfig.mineAssertOrder;
        Intent intent = new Intent(getActivity(), RightShareWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url.concat("?labelType="));
        intent.putExtra(WebViewConstant.push_message_title, getString(R.string.account_bank_asset_zuhe));
        intent.putExtra(WebViewConstant.right_message_index, true);
        startActivity(intent);
    }

    private void toInvestorCarlendarActivity() {
        String url = CwebNetConfig.investeCarlendar;
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(WebViewConstant.push_message_url, url);
        hashMap.put(WebViewConstant.push_message_title, getString(R.string.mine_investor_carlendar));
        NavigationUtils.startActivity(getActivity(), BaseWebViewActivity.class, hashMap);
    }

    @OnClick(R.id.account_order_goto_receive_address)
    void gotoManagerAddressActivity() {
        NavigationUtils.startActivity(getActivity(), MallAddressListActivity.class);
    }

    @OnClick(R.id.account_order_send_ll)
    void gotoWaitSendHuoActivity() {
        String url = CwebNetConfig.mineGoodsOrder.concat("?labelType=1");
        Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url);
        intent.putExtra(WebViewConstant.push_message_title, getString(R.string.mine_order));
        intent.putExtra(WebViewConstant.right_message_index, true);
        startActivity(intent);
        DataStatistApiParam.operateWaitSendClick();
    }

    @OnClick(R.id.account_order_receive_ll)
    void gotoWaitReceiveHuoActivity() {
        String url = CwebNetConfig.mineGoodsOrder.concat("?labelType=2");
        Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url);
        intent.putExtra(WebViewConstant.push_message_title, getString(R.string.mine_order));
        intent.putExtra(WebViewConstant.right_message_index, true);
        startActivity(intent);
        DataStatistApiParam.operateWaitReceiveClick();
    }

    @OnClick(R.id.account_order_finished_ll)
    void gotoFinishedActivity() {
        String url = CwebNetConfig.mineGoodsOrder.concat("?labelType=3");
        Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url);
        intent.putExtra(WebViewConstant.push_message_title, getString(R.string.mine_order));
        intent.putExtra(WebViewConstant.right_message_index, true);
        startActivity(intent);
    }

    @OnClick(R.id.account_order_sale_ll)
    void gotoAfterSaleActivity() {
//        String url = CwebNetConfig.mineGoodsOrder.concat("?labelType=4");
//        HashMap<String ,String> hashMap = new HashMap<>();
//        hashMap.put(WebViewConstant.push_message_url, url);
//        hashMap.put(WebViewConstant.push_message_title, getString(R.string.mine_order));
//        NavigationUtils.startActivity(getActivity(), BaseWebViewActivity.class, hashMap);
        new DefaultDialog(getActivity(), getString(R.string.account_order_server_dialog_prompt), getString(R.string.account_order_now_call), getString(R.string.cancel_str)) {
            @Override
            public void left() {
                dismiss();
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                } else {
                    NavigationUtils.startDialgTelephone(getActivity(), getString(R.string.custom_server_telephone_number));
                }
            }

            @Override
            public void right() {
                dismiss();
            }
        }.show();
    }

    @OnClick(R.id.account_order_all_ll)
    void gotoOrderAllctivity() {
        String url = CwebNetConfig.mineGoodsOrder.concat("?labelType=0");
        Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url);
        intent.putExtra(WebViewConstant.push_message_title, getString(R.string.mine_order));
        intent.putExtra(WebViewConstant.right_message_index, true);
        startActivity(intent);
        DataStatistApiParam.operateMineOrderAllClick();
    }

    @OnClick(R.id.account_health_to_look_server)
    void gotoHealthSeverctivity() {
        NavigationUtils.jumpNativePage(getActivity(), WebViewConstant.Navigation.HEALTH_CHECK_PAGE);
    }

    @OnClick(R.id.health_all_title_ll)
    void gotoHealthAllctivity() {
        String url = CwebNetConfig.mineHealthOrder;
        Intent intent = new Intent(getActivity(), BaseWebViewActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url);
        intent.putExtra(WebViewConstant.push_message_title, getString(R.string.mine_health_list));
        intent.putExtra(WebViewConstant.right_message_index, true);
        startActivity(intent);
        DataStatistApiParam.operateMineHealthClick();
    }

    private void initMineInfo(MineModel mineModel) {
        if (mineModel != null) {
            this.mineModel = mineModel;
            initUserInfo(mineModel);
            initPrivateBank(mineModel);
            initOrderView(mineModel);
            initHealthView(mineModel);
//            new Thread(runnable).start();
        }
    }

    private void initUserInfo(MineModel mineModel) {
        if (mineModel.getMyInfo() != null) {
            MineModel.MineUserInfo mineUserInfo = mineModel.getMyInfo();
            textViewName.setText(mineUserInfo.getNickName());
            Imageload.display(getActivity(), mineUserInfo.getHeadImageUrl(), roundImageView, R.drawable.logo, null);
            userLeaguarLevel.setText(TextUtils.isEmpty(mineUserInfo.getMemberLevel()) ? "无" : mineUserInfo.getMemberLevel());
            userLeaguarUpdateDesc.setText(mineUserInfo.getMemberBalance());
            userLeaguarUpdateDesc.setVisibility(TextUtils.isEmpty(mineUserInfo.getMemberLevel()) ? View.INVISIBLE : View.VISIBLE);
            textViewCaifu.setText(mineUserInfo.getMemberValue());
            textViewYundou.setText(mineUserInfo.getYdTotal());
            textViewPrivateBanker.setText(TextUtils.isEmpty(mineUserInfo.getAdviserName()) ? "无" : mineUserInfo.getAdviserName());
        }
    }

    private void initPrivateBank(MineModel mineModel) {
        boolean isNullPrivateBank = isNullPrivateBank(mineModel);
        linearLayoutBankNoData.setVisibility(isNullPrivateBank ? View.VISIBLE : View.GONE);
        linearLayoutBankHadData.setVisibility(isNullPrivateBank ? View.GONE : View.VISIBLE);
        textViewShowAssert.setVisibility(isNullPrivateBank ? View.GONE : View.VISIBLE);
        if (!TextUtils.isEmpty(mineModel.getBank().getDebtRatio())) {
            float zhaiQuanValue = Float.parseFloat(mineModel.getBank().getDebtRatio());
            roundProgressbar.setProgress((int) zhaiQuanValue);
        }

        if (showAssert) {
            showAssert();
        } else {
            hideAssert();
        }
    }

    private boolean isNullPrivateBank(MineModel mineModel) {
        if (mineModel.getBank() == null ||
                ((TextUtils.isEmpty(mineModel.getBank().getDebtAmt()) || "0".equals(mineModel.getBank().getDebtAmt())) &&
                        (TextUtils.isEmpty(mineModel.getBank().getEquityAmt()) || "0".equals(mineModel.getBank().getEquityAmt())))) {
            return true;
        }
        return false;
    }

    private void initOrderView(MineModel mineModel) {
        if (!CollectionUtils.isEmpty(mineModel.getMallOrder())) {
            for (MineModel.Orders orders : mineModel.mallOrder) {
                if ("1".equals(orders.getGoodsStatusCode())) {
                    if (orders.getCount() > 0) {
                        if (waitSender == null) {
                            waitSender = ViewUtils.createLeftTopRedPoint(getActivity(), account_order_send_text, orders.getCount());
                        } else {
                            waitSender.setText(String.valueOf(orders.getCount() > 99 ? 99 : orders.getCount()));
                            waitSender.invalidate();
                        }
                    } else if (waitSender != null) {
                        waitSender.hide();
                    }
                } else if ("2".equals(orders.getGoodsStatusCode())) {
                    if (orders.getCount() > 0) {
                        if (waitReceiver == null) {
                            waitReceiver = ViewUtils.createLeftTopRedPoint(getActivity(), account_order_receive_text, orders.getCount());
                        } else {
                            waitReceiver.setText(String.valueOf(orders.getCount() > 99 ? 99 : orders.getCount()));
                            waitReceiver.invalidate();
                        }
                    } else if (waitReceiver != null) {
                        waitReceiver.hide();
                    }
                }

            }
        }
    }

    private void initHealthView(MineModel mineModel) {
        if (mineModel != null && mineModel.getHealthy() != null) {
            health_had_data_ll.setVisibility(CollectionUtils.isEmpty(mineModel.getHealthy().getContent()) ? View.GONE : View.VISIBLE);
            health_had_no_data_ll.setVisibility(CollectionUtils.isEmpty(mineModel.getHealthy().getContent()) ? View.VISIBLE : View.GONE);
            if (!CollectionUtils.isEmpty(mineModel.getHealthy().getContent())) {
                createHealthItem(mineModel.getHealthy().getContent());
            }
        }
    }

    private void createHealthItem(List<MineModel.HealthItem> list) {
        if (!CollectionUtils.isEmpty(list)) {
            health_had_data_ll.removeAllViews();
            for (int i = 0; i < list.size(); i++) {
                MineModel.HealthItem healthItem = list.get(i);
                TextView textView = new TextView(getActivity());
                textView.setPadding(DimensionPixelUtil.dip2px(getActivity(), 15), 0, 0, 0);
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                textView.setSingleLine(true);
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setBackgroundResource(R.drawable.selector_bg_btn_white);
                textView.setHeight(DimensionPixelUtil.dip2px(getActivity(), 60));
                textView.setText(getString(R.string.account_health_zixun_server_title).concat(healthItem.getTitle()));
                textView.setTextColor(Color.parseColor("#5a5a5a"));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                textView.setOnClickListener(v -> {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(WebViewConstant.push_message_url, healthItem.getUrl());
//                    hashMap.put(WebViewConstant.push_message_title, healthItem.getTitle());
                    hashMap.put(WebViewConstant.push_message_title, getString(R.string.mine_zhuanti_detail));
                    NavigationUtils.startActivityByRouter(getActivity(), RouteConfig.GOTO_RIGHT_SHARE_ACTIVITY, hashMap);
                });
                health_had_data_ll.addView(textView);
                if (i != list.size() - 1) {
                    View lineView = LayoutInflater.from(getActivity()).inflate(R.layout.acitivity_divide_online, null);
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    lineView.setLayoutParams(layoutParams);
                    health_had_data_ll.addView(lineView);
                }
            }
        }
    }

    //*******************************************
    DownloadManager downloadManager;

    private List<VideoInfoModel> getdownls() {

        DownloadManager downloadManager = DownloadService.getDownloadManager();
        downloadManager.getThreadPool().setCorePoolSize(1);
        downloadManager.setTargetFolder(CacheManager.getCachePath(baseActivity, CacheManager.VIDEO));

        List<VideoInfoModel> list = daoUtils.getAllVideoInfo();
        if (list != null)

        {
            List<VideoDownloadListModel> dataList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                VideoInfoModel model = list.get(i);
                DownloadInfo info = downloadManager.getDownloadInfo(model.videoId);
                if (info != null && info.getState() == DownloadManager.FINISH && model.status != VideoStatus.FINISH) {
                    model.status = VideoStatus.FINISH;
                    daoUtils.saveOrUpdateVideoInfo(model);
                }
                if (model.status != VideoStatus.NONE)
                    dataList.add(createModel(model));
            }
        }
        return null;
    }


    private VideoDownloadListModel createModel(VideoInfoModel videoInfoModel) {
        VideoDownloadListModel model = new VideoDownloadListModel();
        model.type = VideoDownloadListModel.LIST;
        model.videoCoverUrl = videoInfoModel.videoCoverUrl;
        model.videoId = videoInfoModel.videoId;
        model.videoTitle = videoInfoModel.videoName;
        model.progressStr = getDownloadedFileSize(videoInfoModel);
        model.status = videoInfoModel.status;
        model.downloadTime = videoInfoModel.downloadTime;
        model.downloadtype = videoInfoModel.downloadtype;
        model.localPath = videoInfoModel.localVideoPath;
        model.max = 100;
        model.progress = (int) (videoInfoModel.percent * 100);

        if (videoInfoModel.downloadtype == VideoStatus.SD) {//标清
            model.videoUrl = videoInfoModel.sdUrl;
        } else if (videoInfoModel.downloadtype == VideoStatus.HD) {//高清
            model.videoUrl = videoInfoModel.hdUrl;
        }
        if (model.status != VideoStatus.FINISH) {
            DownloadInfo info = downloadManager.getDownloadInfo(model.videoId);
            if (info != null) {
                if (info.getState() == DownloadManager.DOWNLOADING) {
                    model.status = VideoStatus.DOWNLOADING;
                } else if (info.getState() == DownloadManager.WAITING) {
                    model.status = VideoStatus.WAIT;
                }
            }
        }
        return model;
    }

    public String getDownloadedFileSize(VideoInfoModel model) {
        DecimalFormat df = new DecimalFormat("#0.0");
        return df.format(model.size / 1024 / 1024) +
                "M/" +
                df.format(model.size / 1024 / 1024 * model.percent) +
                "M";
    }


//    public List<VideoInfoModel>getDownloadls(){
//        List<VideoInfoModel> list = daoUtils.getAllVideoInfo();
//        if (list != null) {
//            List<VideoDownloadListModel> dataList = new ArrayList<>();
//            for (int i = 0; i < list.size(); i++) {
//                VideoInfoModel model = list.get(i);
//                DownloadInfo info = getDownloadManager().getDownloadInfo(model.videoId);
//                if (info != null && info.getState() == DownloadManager.FINISH && model.status != VideoStatus.FINISH) {
//                    model.status = VideoStatus.FINISH;
//                    saveOrUpdateVideoInfo(model);
//                }
////                if (model.status != VideoStatus.NONE)
//                dataList.add(createModel(model));
//            }
//            getView().getLocalListSucc(dataList, isRef);
//        } else
//            getView().getLocalListFail(isRef);
//
//    }

    //********************************************************
    private void initVideoView() {
        videos = InitApplication.getContext().getResources().getStringArray(R.array.mine_video_tag_text);
        List<VideoInfoModel> playlList = daoUtils.getAllVideoInfoHistory();
        List<VideoInfoModel> downlList = daoUtils.getDownLoadVideoInfo();


        Log.i("MineFragment", "playlist=" + +playlList.size() + "-----downlList=" + downlList.size());
        if (videoList == null) {
            for (String name : videos) {
                XTabLayout.Tab tab = xTabLayout.newTab();
                xTabLayout.addTab(tab);
            }
            viewPager.setOffscreenPageLimit(2);
            videoList = new ArrayList<>();
            setFragmentParams(playlList, videoList, true);
            setFragmentParams(downlList, videoList, false);
            viewPager.resetHeight(0);
            initViewPage();
            xTabLayout.setupWithViewPager(viewPager);
        } else {
            videoList.get(0).refrushData(playlList);
            videoList.get(1).refrushData(downlList);
        }
    }

    private void initViewPage() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPager.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return videoList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return videoList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                if (object instanceof View) {
                    container.removeView((View) object);
                } else if (object instanceof Fragment) {
                    getChildFragmentManager().beginTransaction().detach((Fragment) object);
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return videos[position];
            }
        });
    }

    private HorizontalScrollFragment setFragmentParams(List<VideoInfoModel> valuesList, List<HorizontalScrollFragment> fragmentList, boolean isPlay) {
        HorizontalScrollFragment scrollFragment = new HorizontalScrollFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(HorizontalScrollFragment.GET_VIDEO_PARAMS, valuesList == null ? new ArrayList<>() : (ArrayList) valuesList);
        bundle.putBoolean(HorizontalScrollFragment.IS_VIDEO_PLAY_PARAMS, isPlay);
        scrollFragment.setArguments(bundle);
        fragmentList.add(scrollFragment);
        return scrollFragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (swtichAssetObservable != null) {
            RxBus.get().unregister(RxConstant.SWITCH_ASSERT_SHOW, swtichAssetObservable);
        }
        if (switchGroupObservable != null) {
            RxBus.get().unregister(RxConstant.SWITCH_GROUP_SHOW, switchGroupObservable);
        }

        if (unreadInfoNumber != null) {
            unreadInfoNumber.onDestroy();
        }
    }

    @Override
    public void changeData(int position, int height) {
        if (viewPager != null) {
            viewPager.addHeight(position, height);
        }
    }
}

