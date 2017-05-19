package com.cgbsoft.lib.base.webview;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.R2;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.dm.Utils.helper.FileUtils;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.DownloadUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.ThreadUtils;
import com.cgbsoft.lib.widget.DefaultDialog;
import com.jhworks.library.ImageSelector;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

/**
 * 通用的WebView页面的基累，所有其他WevView的activity需要基础此Activity
 *
 * @author chenlong
 */
public class BaseWebViewActivity<T extends BasePresenterImpl> extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    public static final int SAVE_REQUST = 300;
    public static final int RELATIVE_ASSERT = 301;
    public static final int ASSERT_PROVE = 302;
    public static final int BACK_RESULT_CODE = 401;
    public static final int BACK_CAMERA_CODE = 402;
    public static final String SAVE_PARAM = "saveValue";
    public static final String BACK_PARAM = "backValue";
    String url = "";
    private String title;

    @BindView(R2.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R2.id.title_mid)
    protected TextView titleMid;

    @BindView(R2.id.title_right_btn)
    protected TextView rightTextBtn;

    @BindView(R2.id.webview)
    protected BaseWebview mWebview;

    @BindView(R2.id.menu_cloud)
    protected ImageView cloudImage;

    protected boolean hasEmailShare;

    protected boolean hasShowTitle;

    protected boolean hasRightShare;

    protected boolean hasRightSave;

    protected boolean hasPushMessage;

    protected boolean initPage;

    protected String pushMessageValue;

    protected MenuItem rightItem;

    protected boolean isLive;

    protected boolean isLookZhiBao;

    private Observable<Object> executeObservable;

    @Override
    protected int layoutID() {
        return R.layout.acitivity_userinfo;
    }

    @Override
    protected void before() {
        hasEmailShare = getIntent().getBooleanExtra(WebViewConstant.PAGE_SHARE_WITH_EMAIL, false);
        hasShowTitle = getIntent().getBooleanExtra(WebViewConstant.PAGE_SHOW_TITLE, true);
        hasRightShare = getIntent().getBooleanExtra(WebViewConstant.RIGHT_SHARE, false);
        hasPushMessage = getIntent().getBooleanExtra(WebViewConstant.PUSH_MESSAGE_COME_HERE, false);
        hasRightSave = getIntent().getBooleanExtra(WebViewConstant.RIGHT_SAVE, false);
        initPage = getIntent().getBooleanExtra(WebViewConstant.PAGE_INIT, false);
        pushMessageValue = getIntent().getStringExtra(WebViewConstant.push_message_value);
    }

    @Override
    protected void after() {
        super.after();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected void data() {
        titleMid.setText(title);
        mWebview.loadUrl(url);
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
    }

    /**
     * 获取注册rxbus的id, 如果子类需要注册一个rxbus必须重写注册方法
     * @return
     */
    protected String getRegeistRxBusId() {
        return "";
    }

    /**
     * 执行注册在此webview中事件的回调接口,根据objec对象转化成需要的数据，子类直接实现此方法即可
     * @param
     */
    protected void onEventRxBus(Object object) {}

    /**
     * 点击分享按钮操作，具体子类覆盖次方法，如果子类没有分享功能则不需要复写此方法
     */
    protected void pageShare() {}

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
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
        mWebview.setClick(result -> executeOverideUrlCallBack(result));

        // 装配url数据
        url = fullUrlPath(getIntent().getStringExtra(WebViewConstant.push_message_url));
        title = getIntent().getStringExtra(WebViewConstant.push_message_title);
        toolbar.setVisibility(hasShowTitle ? View.VISIBLE : View.GONE);
        rightTextBtn.setVisibility(hasRightShare || hasRightSave ? View.VISIBLE : View.GONE);

        if (initPage && !TextUtils.isEmpty(pushMessageValue)) {
            mWebview.postDelayed(() -> {
                String javascript = "javascript:Tools.init('" + pushMessageValue + "')";
                mWebview.loadUrl(javascript);
            }, 1000);
        }
    }

    @OnClick(R2.id.menu_cloud)
    void cloudMenuClick() {
        if (SPreference.getToCBean(this) != null && TextUtils.isEmpty(SPreference.getToCBean(this).getBandingAdviserId())) {
            NavigationUtils.startActivityByRouter(this, "investormain_bindvisiteactivity");
        } else {
            if (isLive  && !isLookZhiBao) {
                isLookZhiBao = true;
                //joinLive();
            } else {
                NavigationUtils.startActivityByRouter(this, "investormain_cloudmenuactivity", "product_detail", true);
            }
        }
    }

    @OnClick(R2.id.title_right_btn)
    void rightTextBtnClick() {
        if (hasRightSave) {
            String jascript = "javascript:Tools.save()";
            mWebview.loadUrl(jascript);
        } else if (hasRightShare) {
            pageShare();
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
        mWebview.loadUrl("javascript:refresh()");
        if (url.contains("apptie/detail.html")) {
            cloudImage.setVisibility(View.VISIBLE);
        }
//        if ("设置".equals(title) || url.contains("/calendar/index.html") || url.contains("invite_ordinary.html") || url.contains("set_det_gesture.html")) {
//
//        } else
        try {
            mWebview.getClass().getMethod("onResume").invoke(mWebview, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("BaseWebViewActivity", "onCreateOptionsMenu");
        if (hasRightShare || hasRightSave) {
//            getMenuInflater().inflate(R.menu.page_menu, menu);
//            rightItem = menu.findItem(R.id.firstBtn);
//            MenuItem secItem = menu.findItem(R.id.secondBtn);
//            rightItem.setTitle(hasRightShare ? R.string.umeng_socialize_share : R.string.save);
//            rightItem.setIcon(hasRightShare ? R.drawable.fenxiang_share_nor : R.drawable.shape_white);
//            secItem.setVisible(false);
            rightTextBtn.setText(hasRightShare ? R.string.umeng_socialize_share : R.string.save);
        }
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.firstBtn) {
            if (item.getTitle().equals(getString(R.string.umeng_socialize_share))) {
                pageShare();
            } else if(item.getTitle().equals(getString(R.string.save))) {
                String jascript = "javascript:Tools.save()";
                mWebview.loadUrl(jascript);
            }
        }
        return false;
    }
//	public void onEventMainThread(EventBusUpdateHeadImage event) {
//		String laun = "javascript:setHeadImage('" + event.getRemoteAddress() + "');";
//		mWebview.loadUrl(laun);
//	}
}
