package com.cgbsoft.lib.base.webview;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.R2;
import com.cgbsoft.lib.base.model.MallAddress;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.shake.ShakeListener;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.lib.utils.tools.TrackingDiscoveryDataStatistics;
import com.cgbsoft.lib.utils.tools.TrackingHealthDataStatistics;
import com.cgbsoft.lib.utils.ui.DialogUtils;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;
import com.cgbsoft.privatefund.bean.commui.WebRightTopViewConfigBean;
import com.chenenyu.router.annotation.Route;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import rx.Observable;

/**
 * 通用的WebView页面的基累，所有其他WevView的activity需要基础此Activity
 *
 * @author chenlong
 */
@Route(RouteConfig.GOTO_BASE_WEBVIEW)
public class BaseWebViewActivity<T extends BasePresenterImpl> extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    public static final int SAVE_REQUST = 300;
    public static final int RELATIVE_ASSERT = 301;
    public static final int ASSERT_PROVE = 302;
    public static final int BACK_RESULT_CODE = 401;
    public static final int BACK_CAMERA_CODE = 402;
    public static final String SAVE_PARAM = "saveValue";
    public static final String BACK_PARAM = "backValue";

    protected String url = "";
    protected String title;

    @BindView(R2.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R2.id.title_mid)
    protected TextView titleMid;

    @BindView(R2.id.webview)
    protected BaseWebview mWebview;

    @BindView(R2.id.divide_line)
    protected View mView;
//    @BindView(R2.id.baseweb_appbar)
//    AppBarLayout baseweb_appbar;

//    @BindView(R2.id.title_normal_new)
//    RelativeLayout titleRelativeLayout;


//    @BindView(R2.id.title_right)
//    TextView myTitleRightText;
//    @BindView(R2.id.baseweb_title_right_iv)
//    ImageView baseweb_title_right_iv;
//    @BindView(R2.id.baseweb_title_leftright_iv)
//    ImageView baseweb_title_leftright_iv;

    protected boolean hasEmailShare;

    protected boolean hasShowTitle;

    protected boolean hasRightShare;

    protected boolean hasRightSave;

    protected boolean rightMessageIcon;

    protected boolean rightRechargeShow;

    protected boolean hasPushMessage;

    protected boolean rightYundouRule;

    protected boolean rightMemberRule;

    protected boolean initPage;

    protected String pushMessageValue;

    protected boolean hasUnreadInfom;

    protected MenuItem rightItem;

    protected boolean isLive;

    protected boolean isLookZhiBao;

    private ShakeListener mShakeListener;

    private Observable<Object> executeObservable;
    private Observable<MallAddress> mallChoiceObservable;
    private Observable<String> mallDeleteObservable;
    private Observable<Boolean> unReadMessageObservable;
    private Observable<String> callBackObservable;
    private Observable<WebRightTopViewConfigBean> openWebConfigObservable;
    private boolean isH5ControlRight;
    private boolean isDivTitle;//是否需要html的形式显示
    private boolean isHideBar;//是否隐藏toolbar
    private boolean isHindBack;

    @Override
    protected int layoutID() {
        return R.layout.acitivity_userinfo;
    }


    @Override
    protected void before() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        hasEmailShare = getIntent().getBooleanExtra(WebViewConstant.PAGE_SHARE_WITH_EMAIL, false);
        hasShowTitle = getIntent().getBooleanExtra(WebViewConstant.PAGE_SHOW_TITLE, false);
//        hasRightShare = getIntent().getBooleanExtra(WebViewConstant.RIGHT_SHARE, true);
        hasPushMessage = getIntent().getBooleanExtra(WebViewConstant.PUSH_MESSAGE_COME_HERE, false);
        hasRightSave = getIntent().getBooleanExtra(WebViewConstant.RIGHT_SAVE, false);
        rightMessageIcon = getIntent().getBooleanExtra(WebViewConstant.right_message_index, false);
        rightRechargeShow = getIntent().getBooleanExtra(WebViewConstant.RIGHT_RECHARGE_HAS, false);
        rightYundouRule = getIntent().getBooleanExtra(WebViewConstant.RIGHT_YUNDOU_RULE_HAS, false);
        rightMemberRule = getIntent().getBooleanExtra(WebViewConstant.RIGHT_MEMBER_RULE_HAS, false);
        isDivTitle = getIntent().getBooleanExtra(WebViewConstant.push_message_title_isdiv, false);
        isHideBar = getIntent().getBooleanExtra(WebViewConstant.push_message_title_is_hidetoolbar, false);


        initPage = getIntent().getBooleanExtra(WebViewConstant.PAGE_INIT, false);
        if (getIntent().getExtras().containsKey(WebViewConstant.push_message_value))
            pushMessageValue = getIntent().getStringExtra(WebViewConstant.push_message_value);
        url = fullUrlPath(getIntent().getStringExtra(WebViewConstant.push_message_url));
        title = getIntent().getStringExtra(WebViewConstant.push_message_title);
    }

    /**
     * 根据次字方法来判断是否需要回调，默认是不需要回调
     *
     * @return
     */
    protected boolean getCallBack() {
        return false;
    }

    protected void data() {
        mWebview.setDownloadListener((s, s1, s2, s3, l) -> {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
//        mWebview.loadUrl(url);
        if (!TextUtils.isEmpty(getRegeistRxBusId())) {
            executeObservable = RxBus.get().register(getRegeistRxBusId(), Object.class);
            executeObservable.subscribe(new RxSubscriber<Object>() {
                @Override
                protected void onEvent(Object o) {
                    onEventRxBus(o);
                }

                @Override
                protected void onRxError(Throwable error) {
                }
            });
        }

        mallChoiceObservable = RxBus.get().register(RxConstant.MALL_CHOICE_ADDRESS, MallAddress.class);
        mallChoiceObservable.subscribe(new RxSubscriber<MallAddress>() {
            @Override
            protected void onEvent(MallAddress myAddress) {
                mWebview.loadUrl("javaScript:products.setAddress('" + myAddress.getId() + "','" + myAddress.getName() + "','" + myAddress.getPhone() + "','" + myAddress.getAddress() + "')");
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });
        mallDeleteObservable = RxBus.get().register(RxConstant.DELETE_ADDRESS, String.class);
        mallDeleteObservable.subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String addressId) {
                mWebview.loadUrl("javaScript:products.deleteAddressId('" + addressId + "')");
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });

        unReadMessageObservable = RxBus.get().register(RxConstant.UNREAD_MESSAGE_OBSERVABLE, Boolean.class);
        unReadMessageObservable.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean booleans) {
                System.out.println("--------booleans=" + booleans);
                hasUnreadInfom = booleans;
                if (rightMessageIcon) {
                    rightItem.setIcon(ContextCompat.getDrawable(BaseWebViewActivity.this, hasUnreadInfom ? R.drawable.select_news_new_black_red_point : R.drawable.select_webview_message_index));
                    rightItem.setVisible(rightMessageIcon);
                }
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });

        callBackObservable = RxBus.get().register(RxConstant.SWIPT_CODE_RESULT, String.class);
        callBackObservable.subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String url) {
                mWebview.loadUrl("javascript:setCallBack('" + url + "')");
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });

        if (rightMessageIcon) {
            System.out.println("----------webview=---rightMessageIcon" + rightMessageIcon);
            RxBus.get().post(RxConstant.REFRUSH_UNREADER_NUMBER_RESULT_OBSERVABLE, true);
        }


    }

    /**
     * 获取注册rxbus的id, 如果子类需要注册一个rxbus必须重写注册方法
     *
     * @return
     */
    protected String getRegeistRxBusId() {
        return "";
    }

    /**
     * 执行注册在此webview中事件的回调接口,根据objec对象转化成需要的数据，子类直接实现此方法即可
     *
     * @param
     */
    protected void onEventRxBus(Object object) {
    }

    /**
     * 点击分享按钮操作，具体子类覆盖次方法，如果子类没有分享功能则不需要复写此方法
     */
    protected void pageShare() {
        String javascript = "javascript:shareClick()";
        mWebview.loadUrl(javascript);
        if (!TextUtils.isEmpty(url) && url.contains("&goCustomFeedBack=0")) { // 健康项目详情页面埋点
            TrackingHealthDataStatistics.projectDetailRightShare(this);
        } else if (!TextUtils.isEmpty(url) && url.contains("information/details.html")) {
            TrackingDiscoveryDataStatistics.rightShare(this, title);
        }
    }

    /**
     * 执行具体业务方法，需要子类复写此回调方法，如果子类没有需要实现的业务回调则不需要复写此方法
     */
    protected void executeOverideUrlCallBack(String actionUrl) {
    }

    public String getTitleName() {
        return title;
    }

    @Override
    protected T createPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        // toolbar事件设置
        toolbar.setTitle("");
        titleMid.setText(title);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_back_black_24dp);
        toolbar.setNavigationOnClickListener((View v) -> {
            if (null != url && url.contains(CwebNetConfig.publicFundRiskUrl)) {
                publicRiskEvaluation();
                return;
            }

            if (!TextUtils.isEmpty(url) && url.contains("&goCustomFeedBack=0")) { // 健康项目详情页面埋点
                TrackingHealthDataStatistics.projectDetailLeftBack(this);
            }

            if (!TextUtils.isEmpty(url) && url.contains("information/details.html")) {
                TrackingDiscoveryDataStatistics.leftBack(this, title);
            }

            if (!TextUtils.isEmpty(url) && url.contains("health/free_consult.html")) { // 免费咨询页面返回
                TrackingHealthDataStatistics.freeConsultLeftBack(this);
            }

            mWebview.loadUrl("javascript:WebView.back(0)");
            finish();
        });
        mWebview.setClick(result -> executeOverideUrlCallBack(result));

        // 装配url数据
        toolbar.setVisibility(hasShowTitle ? View.GONE : View.VISIBLE);
        mView.setVisibility(hasShowTitle ? View.GONE : View.VISIBLE);
        if (initPage && !TextUtils.isEmpty(pushMessageValue)) {
            mWebview.postDelayed(() -> {
                String javascript = "javascript:Tools.init('" + pushMessageValue + "')";
                mWebview.loadUrl(javascript);
            }, 1000);
        }
        changeTitileStyle();
        initShakeInSetPage();
        mWebview.loadUrl(url);
    }

    private void changeTitileStyle() {
        if (title.equals("我的财富值")) {
            toolbar.setVisibility(View.GONE);
            RelativeLayout titleRelativeLayout = (RelativeLayout) findViewById(R.id.title_normal_new);
            titleRelativeLayout.setVisibility(View.VISIBLE);
            titleRelativeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ImageView imageView = (ImageView) findViewById(R.id.title_left);
            imageView.setImageResource(R.drawable.ic_back_black_24dp);
            imageView.setOnClickListener(v -> finish());
            TextView titleTextView = (TextView) findViewById(R.id.title_mid_empty);
            titleTextView.setText("我的财富值");
            titleTextView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            TextView rightText = (TextView) findViewById(R.id.title_right);
            rightText.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            rightText.setText("会员规则");
            rightText.setOnClickListener(v -> {
                Intent intent = new Intent(BaseWebViewActivity.this, BaseWebViewActivity.class);
                intent.putExtra(WebViewConstant.push_message_url, CwebNetConfig.memberRule);
                intent.putExtra(WebViewConstant.push_message_title, "会员规则");
                startActivity(intent);
            });
        }
        if (rightMemberRule) {
            toolbar.setVisibility(View.GONE);
            mView.setVisibility(View.GONE);
            RelativeLayout titleRelativeLayout = (RelativeLayout) findViewById(R.id.title_normal_new);
            titleRelativeLayout.setVisibility(View.VISIBLE);
            titleRelativeLayout.setBackgroundColor(Color.parseColor("#292B2A"));
            ImageView imageView = (ImageView) findViewById(R.id.title_left);
            imageView.setImageResource(R.drawable.ic_back_white_24dp);
            imageView.setOnClickListener(v -> finish());
            TextView titleTextView = (TextView) findViewById(R.id.title_mid_empty);
            titleTextView.setText("会员专区");
            titleTextView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            TextView myTitleRightText = (TextView) findViewById(R.id.title_right);
            myTitleRightText.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            myTitleRightText.setText("会员规则");
            myTitleRightText.setOnClickListener(v -> {
                Intent intent = new Intent(BaseWebViewActivity.this, BaseWebViewActivity.class);
                intent.putExtra(WebViewConstant.push_message_url, CwebNetConfig.memberRule);
                intent.putExtra(WebViewConstant.push_message_title, "会员规则");
                startActivity(intent);
            });
        }
        if (isHideBar) {//从openweb指令进来的
            toolbar.setVisibility(View.GONE);
//            mView.setVisibility(View.GONE);
//            myTitleRelativeLayout = (RelativeLayout) findViewById(R.id.title_normal_new);
            TextView titleTextView = (TextView) findViewById(R.id.title_mid_empty);
            if (isDivTitle) {
                titleTextView.setText(Html.fromHtml(title));
            } else {
                titleTextView.setText(title);
            }
            titleTextView.setTextColor(ContextCompat.getColor(this, android.R.color.black));

            RelativeLayout titleRelativeLayout = (RelativeLayout) findViewById(R.id.title_normal_new);
            titleRelativeLayout.setVisibility(View.VISIBLE);
            titleRelativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));

            ImageView myTitleLeftImageView = (ImageView) findViewById(R.id.title_left);
            myTitleLeftImageView.setImageResource(R.drawable.ic_back_black_24dp);
            myTitleLeftImageView.setOnClickListener(v -> finish());

//
            TextView myTitleRightText = (TextView) findViewById(R.id.title_right);
            myTitleRightText.setTextColor(ContextCompat.getColor(this, android.R.color.black));
//            baseweb_title_right_iv = (ImageView) findViewById(R.id.baseweb_title_right_iv);
//
        }
    }


    private ShakeListener.OnShakeListener onShakeListener = new ShakeListener.OnShakeListener() {
        @Override
        public void onShakeStart() {
        }

        @Override
        public void onShakeFinish() {
            DialogUtils.createSwitchBcDialog(BaseWebViewActivity.this).show();
        }
    };

    public void showPayItem() {
        rightRechargeShow = true;
        rightItem.setTitle("充值");
        rightItem.setVisible(true);
    }

    public void showShareButton() {
        if (rightItem != null) {
            rightItem.setVisible(true);
        }
    }

    public void showTitleRight(String rightStr) {
        isH5ControlRight = true;
        if (rightItem != null) {
            rightItem.setTitle(rightStr);
            rightItem.setVisible(true);
            rightItem.setIcon(null);
        }
    }

    public void modifyTitleName(String name) {
        titleMid.setText(name);
    }

    /**
     * @param url
     * @return
     */
    private String fullUrlPath(String url) {
        if (!url.contains("http")) {
            url = BaseWebNetConfig.baseParentUrl + url;
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra(WebViewConstant.Jump_Info_KeyWord))) {
            String keyWords = getIntent().getStringExtra(WebViewConstant.Jump_Info_KeyWord);
            if (url.contains("?")) {
                url += "&" + WebViewConstant.Jump_Info_KeyWord + "=" + keyWords + "";
            } else {
                url += "?" + WebViewConstant.Jump_Info_KeyWord + "=" + keyWords + "";
            }
        }
        return url;
    }

    @Override
    public void onBackPressed() {
        if (url.contains("rankList_share")) {
            mWebview.loadUrl("javascript:delectChart()");
        }
//        if (null != url && url.contains(CwebNetConfig.publicFundRiskUrl)) {
//            publicRiskEvaluation();
//            return;
//        }


        mWebview.loadUrl("javascript:WebView.back(1)");
        if ("风险评测".equals(title)) {
            backEvent();
            return;
        }

        if (isHindBack) {
            PromptManager.ShowCustomToast(baseContext, "请确认");
            return;
        }
        if (hasPushMessage) {
//			NavigationUtils.startMessageList(context);
        }

        if (url.contains("rankList_share")) {
            ThreadUtils.runOnMainThreadDelay(() -> BaseWebViewActivity.this.onBackPressed(), 1000);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 风险测评
     */
    private void publicRiskEvaluation() {
//        new DefaultDialog(BaseWebViewActivity.this, getString(R.string.public_risk_evaluation), "取消", "确定") {
//            @Override
//            public void left() {
//                this.dismiss();
//            }
//
//            @Override
//            public void right() {
//                finish();
//            }
//        }.show();
        finish();
    }

    private void backEvent() {
        new DefaultDialog(BaseWebViewActivity.this, getString(R.string.risk_comment_prmpt), "取消", "确定") {
            @Override
            public void left() {
                this.dismiss();
            }

            @Override
            public void right() {
                finish();
            }
        }.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebview.loadUrl("javascript:WebView.willDisappear()");
        MobclickAgent.onPause(this);
        if (!TextUtils.isEmpty(url) && url.endsWith("mine_active_list.html")) {
            MobclickAgent.onPageEnd(Constant.SXY_HDZX);
        }
        if (CwebNetConfig.mineYunDou.equals(url)) {
            MobclickAgent.onPageEnd(Constant.SXY_WDYD);
        }
        if (CwebNetConfig.mineCardCoupons.equals(url)) {
            MobclickAgent.onPageEnd(Constant.SXY_WDKJ);
        }
        if (CwebNetConfig.mineBestCard.equals(url)) {
            MobclickAgent.onPageEnd(Constant.SXY_WDHK);
        }
        if ((CwebNetConfig.mineGoodsOrder.concat("?labelType=0")).equals(url)) {
            MobclickAgent.onPageEnd(Constant.SXY_WDDD);
        }
        if (CwebNetConfig.mineHealthOrder.equals(url)) {
            MobclickAgent.onPageEnd(Constant.SXY_WDJK);
        }
        if (CwebNetConfig.common_problem.equals(url)) {
            MobclickAgent.onPageEnd(Constant.SXY_CJWT);
        }
        if (CwebNetConfig.healthValue.equals(url)) {
            MobclickAgent.onPageEnd(Constant.SXY_CFZ);
        }
        if (CwebNetConfig.investeCarlendar.equals(url)) {
            MobclickAgent.onPageEnd(Constant.SXY_TZRL);
        }

        mWebview.loadUrl("javascript:WebView.didDisappear()");
        try {
            mWebview.getClass().getMethod("onPause").invoke(mWebview, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (!TextUtils.isEmpty(url) && url.endsWith("mine_active_list.html")) {
            MobclickAgent.onPageStart(Constant.SXY_HDZX);
        }
        if (CwebNetConfig.mineYunDou.equals(url)) {
            MobclickAgent.onPageStart(Constant.SXY_WDYD);
        }
        if (CwebNetConfig.mineCardCoupons.equals(url)) {
            MobclickAgent.onPageStart(Constant.SXY_WDKJ);
        }
        if (CwebNetConfig.mineBestCard.equals(url)) {
            MobclickAgent.onPageStart(Constant.SXY_WDHK);
        }
        if ((CwebNetConfig.mineGoodsOrder.concat("?labelType=0")).equals(url)) {
            MobclickAgent.onPageStart(Constant.SXY_WDDD);
        }
        if (CwebNetConfig.mineHealthOrder.equals(url)) {
            MobclickAgent.onPageStart(Constant.SXY_WDJK);
        }
        if (CwebNetConfig.common_problem.equals(url)) {
            MobclickAgent.onPageStart(Constant.SXY_CJWT);
        }
        if (CwebNetConfig.healthValue.equals(url)) {
            MobclickAgent.onPageStart(Constant.SXY_CFZ);
        }
        if (CwebNetConfig.investeCarlendar.equals(url)) {
            MobclickAgent.onPageStart(Constant.SXY_TZRL);
        }
        try {
            mWebview.getClass().getMethod("onResume").invoke(mWebview, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mWebview.loadUrl("javascript:WebView.willAppear()");

        if (url.contains("life/order_detail.html")) {
            return;
        }

        mWebview.loadUrl("javascript:refresh()");
        mWebview.loadUrl("javascript:WebView.didAppear()");
    }

    private void initShakeInSetPage() {
        if (TextUtils.equals("设置", title)) {
            mShakeListener = new ShakeListener(this);
            mShakeListener.setOnShakeListener(onShakeListener);
            mShakeListener.register();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BACK_RESULT_CODE) { // 处理h5返回问题
            int index = data.getIntExtra(BACK_PARAM, -1);
            if (index != 0 && (url.contains("/apptie/detail.html") || url.contains("/calendar/index.html"))) { //
                return;
            }
            if (index == 0) {
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(BACK_PARAM, index - 1);
            setResult(BACK_RESULT_CODE, intent);
            finish();
        } else if (requestCode == SAVE_REQUST) {
            if (data == null) {
                return;
            }
            String value = data.getStringExtra(SAVE_PARAM);
            if (!TextUtils.isEmpty(value)) {
                String laun = "javascript:Tools.saveSuccess('" + value + "');";
                mWebview.loadUrl(laun);
            }
        } else if (requestCode == RELATIVE_ASSERT) {
            int status = Integer.valueOf(SPreference.getToCBean(BaseWebViewActivity.this).getStockAssetsStatus());
            String laun = "javascript:localUp(sUserAgg,'stockAssetsStatus'," + status + ",'toC')";
            mWebview.loadUrl(laun);
        } else if (requestCode == ASSERT_PROVE) {
            int status = Integer.valueOf(SPreference.getToCBean(BaseWebViewActivity.this).getStockAssetsStatus());
            String laun = "javascript:localUp(sUserAgg,'assetsCertificationStatus'," + status + ",'toC')";
            mWebview.loadUrl(laun);
        } else if (requestCode == Constant.REQUEST_IMAGE) {
        } else if (requestCode == BACK_CAMERA_CODE) {
            if (resultCode == RESULT_OK) {
//                ArrayList<String> mSelectPath = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
//                System.out.println("-----select1=" + mSelectPath.get(0));
//                String newTargetFile = FileUtils.compressFileToUpload(mSelectPath.get(0), true);
//                String paths = DownloadUtils.postObject(newTargetFile, Constant.UPLOAD_HEAD_TYPE);
//                FileUtils.deleteFile(newTargetFile);
            }
        }
    }

    @Override
    protected void onDestroy() {
//
        mWebview.clearAnimation();
        mWebview.removeAllViews();
        mWebview.destroy();
        super.onDestroy();
        if (executeObservable != null && !TextUtils.isEmpty(getRegeistRxBusId())) {
            RxBus.get().unregister(getRegeistRxBusId(), executeObservable);
        }
        if (mallChoiceObservable != null && !TextUtils.isEmpty(getRegeistRxBusId())) {
            RxBus.get().unregister(getRegeistRxBusId(), mallChoiceObservable);
        }

        if (unReadMessageObservable != null) {
            RxBus.get().unregister(RxConstant.UNREAD_MESSAGE_OBSERVABLE, unReadMessageObservable);
        }

        if (callBackObservable != null) {
            RxBus.get().unregister(RxConstant.SWIPT_CODE_RESULT, callBackObservable);
        }

        if (mShakeListener != null) {
            mShakeListener.unregister();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.page_menu, menu);
        rightItem = menu.findItem(R.id.firstBtn);
        MenuItem secItem = menu.findItem(R.id.secondBtn);
        secItem.setVisible(false);

        if ("设置".equals(title)) {
            rightItem.setTitle("设置");
            Drawable drawable = getResources().getDrawable(R.drawable.qiehuan);
            rightItem.setIcon(drawable);
        } else if ((title != null && title.contains("活动")) && rightRechargeShow) {
            rightItem.setTitle("充值");
            rightItem.setVisible(true);
        } else if (rightYundouRule) {
            rightItem.setTitle("使用规则");
            rightItem.setVisible(true);
        } else if (rightMemberRule) {
            rightItem.setTitle("会员规则");
            rightItem.setVisible(true);
        } else {
            rightItem.setIcon(ContextCompat.getDrawable(this, rightMessageIcon ? (hasUnreadInfom ? R.drawable.select_news_new_black_red_point : R.drawable.select_webview_message_index) : R.drawable.select_share_navigation));
            rightItem.setVisible(rightMessageIcon);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.firstBtn) {
            if (rightMessageIcon) {
                NavigationUtils.startActivityByRouter(this, RouteConfig.IM_MESSAGE_LIST_ACTIVITY);
            } else if (title.contains("活动") && rightRechargeShow) {
                NavigationUtils.startActivityByRouter(this, RouteConfig.MALL_PAY);
            } else if (rightYundouRule) {
                Intent intent = new Intent(this, BaseWebViewActivity.class);
                intent.putExtra(WebViewConstant.push_message_url, CwebNetConfig.yundouRule);
                intent.putExtra(WebViewConstant.push_message_title, "使用规则");
                startActivity(intent);
            } else if (rightMemberRule) {
                Intent intent = new Intent(this, BaseWebViewActivity.class);
                intent.putExtra(WebViewConstant.push_message_url, CwebNetConfig.memberRule);
                intent.putExtra(WebViewConstant.push_message_title, "会员规则");
                startActivity(intent);
            } else if (isH5ControlRight) {
                mWebview.loadUrl("javascript:titltRightClick()");
            } else {
                pageShare();
            }
        }

        return false;
    }

    protected String getV2String(String resultStr) {

        try {
            JSONObject obj = new JSONObject(resultStr);
            return obj.getString("result");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }


    public void setWebRightTopViewConfig(WebRightTopViewConfigBean webRightTopViewConfig) {
        TextView myTitleRightText = (TextView) findViewById(R.id.title_right);

        ImageView baseweb_title_right_iv = (ImageView) findViewById(R.id.baseweb_title_right_iv);
        switch (webRightTopViewConfig.getRightButtonType()) {
            case 1:
                if (null == myTitleRightText) break;
                myTitleRightText.setVisibility(View.VISIBLE);
                BStrUtils.setTv(myTitleRightText, webRightTopViewConfig.getRightButtons().get(0).getText());
                myTitleRightText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mWebview.loadUrl(String.format("javascript:%s()", webRightTopViewConfig.getRightButtons().get(0).getEvent()));
                    }
                });
                break;
            case 2:
                if (null == myTitleRightText || null == baseweb_title_right_iv) break;
                myTitleRightText.setVisibility(View.GONE);
                baseweb_title_right_iv.setVisibility(View.VISIBLE);
                if (null != webRightTopViewConfig.getRightButtons() && webRightTopViewConfig.getRightButtons().size() > 0) {//只有一个时候
                    Imageload.display(baseContext, webRightTopViewConfig.getRightButtons().get(0).getIcon(), baseweb_title_right_iv);
                    baseweb_title_right_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mWebview.loadUrl(String.format("javascript:%s(%s)", webRightTopViewConfig.getRightButtons().get(0).getEvent()));
                        }
                    });
                }
                if (null != webRightTopViewConfig.getRightButtons() && 2 == webRightTopViewConfig.getRightButtons().size()) {//有两个时候

                }
                break;
        }
        isHindBack = false;
        if (webRightTopViewConfig.isHideReturnButton()) {
            findViewById(R.id.title_left).setVisibility(View.GONE);
            isHindBack = true;
        }

    }

    /**
     * 隐藏toolbar返回键
     */
    public void hideBackIv() {

//        findViewById(R.id.title_left).setVisibility(View.GONE);
//        if (null != toolbar)
//            toolbar.setNavigationIcon(R.color.transparent);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}


