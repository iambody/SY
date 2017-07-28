package com.cgbsoft.lib.base.webview;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.shake.ShakeListener;
import com.cgbsoft.lib.utils.tools.DownloadUtils;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.lib.utils.ui.DialogUtils;
import com.cgbsoft.lib.widget.dialog.DefaultDialog;
import com.chenenyu.router.annotation.Route;
import com.jhworks.library.ImageSelector;
import com.tencent.smtt.sdk.DownloadListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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

    protected MenuItem rightItem;

    protected boolean isLive;

    protected boolean isLookZhiBao;

    private ShakeListener mShakeListener;

    private Observable<Object> executeObservable;
    private Observable<MallAddress> mallChoiceObservable;
    private Observable<String> mallDeleteObservable;

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

    @Override
    protected void data() {
        mWebview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
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
    }

    /**
     * 执行具体业务方法，需要子类复写此回调方法，如果子类没有需要实现的业务回调则不需要复写此方法
     */
    protected void executeOverideUrlCallBack(String actionUrl) {
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
        toolbar.setNavigationOnClickListener(v -> finish());
        mWebview.setClick(result -> executeOverideUrlCallBack(result));

        // 装配url数据
        toolbar.setVisibility(hasShowTitle ? View.GONE : View.VISIBLE);
        if (initPage && !TextUtils.isEmpty(pushMessageValue)) {
            mWebview.postDelayed(() -> {
                String javascript = "javascript:Tools.init('" + pushMessageValue + "')";
                mWebview.loadUrl(javascript);
            }, 1000);
        }
        initShakeInSetPage();
        mWebview.loadUrl(url);
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

    public void showPayItem(){
        rightRechargeShow = true;
        rightItem.setTitle("充值");
        rightItem.setVisible(true);
    }

    public void showShareButton() {
        if (rightItem != null) {
            rightItem.setVisible(true);
        }
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
        if ("风险评测".equals(title)) {
            backEvent();
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
        try {
            mWebview.getClass().getMethod("onResume").invoke(mWebview, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        mWebview.loadUrl(url);
        if (url.contains("life/order_detail.html")) {
            return;
        }
        LogUtils.Log("JavaScriptObjectToc", "ss");
        mWebview.loadUrl("javascript:refresh()");

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
            Intent intent = new Intent();
            intent.putExtra(BACK_PARAM, index);
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
                ArrayList<String> mSelectPath = data.getStringArrayListExtra(ImageSelector.EXTRA_RESULT);
                System.out.println("-----select1=" + mSelectPath.get(0));
                String newTargetFile = FileUtils.compressFileToUpload(mSelectPath.get(0), true);
                String paths = DownloadUtils.postObject(newTargetFile, Constant.UPLOAD_HEAD_TYPE);
                FileUtils.deleteFile(newTargetFile);
            }
        }
    }

    @Override
    protected void onDestroy() {
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
        }else {
            rightItem.setIcon(ContextCompat.getDrawable(this, rightMessageIcon ? R.drawable.select_happy_life_toolbar_right : R.drawable.select_share_navigation));
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
            } else{
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
//	public void onEventMainThread(EventBusUpdateHeadImage event) {
//		String laun = "javascript:setHeadImage('" + event.getRemoteAddress() + "');";
//		mWebview.loadUrl(laun);
//	}
}
