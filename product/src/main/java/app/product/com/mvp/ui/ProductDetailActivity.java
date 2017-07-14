package app.product.com.mvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.cgbsoft.lib.TaskInfo;
import com.cgbsoft.lib.base.model.bean.ProductlsBean;
import com.cgbsoft.lib.base.webview.BaseWebNetConfig;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import app.privatefund.com.share.bean.ShareCommonBean;
import app.privatefund.com.share.dialog.CommonShareDialog;
import app.product.com.R;
import app.product.com.mvc.ui.PdfActivity;
import rx.Observable;
import rx.Observer;
import rx.subscriptions.CompositeSubscription;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/20-18:53
 */
@Route(RouteConfig.GOTOPRODUCTDETAIL)
public class ProductDetailActivity extends BaseWebViewActivity {
    //main产品列表传递进来的
    public static final String PRODUCTID_TAG = "productid";

    private CompositeSubscription mCompositeSubscription;

    //需要的分享dialog
    private CommonShareDialog commonShareDialog;
    //需要的分享bean
    private ShareCommonBean shareCommonBean;
    // 需要的产品产品详情数据bean
    private ProductlsBean productlsBean;
    //
    private String productSchemeId;

    private Observable<Boolean> liveObserver;
    private boolean hasLive;

    @Override
    protected int layoutID() {
        initParams();
        return R.layout.acitivity_product_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (url.contains("apptie/detail.html")) {
            findViewById(R.id.cloud_menu_imagevew).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.cloud_menu_imagevew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SPreference.getToCBean(ProductDetailActivity.this) != null && TextUtils.isEmpty(SPreference.getToCBean(ProductDetailActivity.this).getBandingAdviserId())) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(WebViewConstant.push_message_url, CwebNetConfig.noBindUserInfo);
                    hashMap.put(WebViewConstant.push_message_title, "填写信息");
                    NavigationUtils.startActivityByRouter(ProductDetailActivity.this, RouteConfig.GOTO_BASE_WEBVIEW, hashMap);
                } else {
//                    if (isLive && !isLookZhiBao) {
//                        isLookZhiBao = true;
//                        //joinLive();
//                    } else {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("product_detail", true);
                    hashMap.put("hasLive", hasLive);
                    NavigationUtils.startActivityByRouter(ProductDetailActivity.this, RouteConfig.GOTO_CLOUD_MENU_ACTIVITY, hashMap, com.cgbsoft.lib.R.anim.home_fade_in, com.cgbsoft.lib.R.anim.home_fade_out);
//                    }
                }
                DataStatistApiParam.onStatisToCProductDetailMenu();
            }
        });
        liveObserver = RxBus.get().register(RxConstant.ZHIBO_STATUES, Boolean.class);
        liveObserver.subscribe(new RxSubscriber<Boolean>() {
            @Override
            protected void onEvent(Boolean aBoolean) {
                hasLive = aBoolean;
            }

            @Override
            protected void onRxError(Throwable error) {
            }
        });
    }

    /**
     * 获取intent里面传递的数据
     */
    private void initParams() {
        if (null != getIntent() && getIntent().getExtras().containsKey(PRODUCTID_TAG)) {
            productSchemeId = getIntent().getStringExtra(PRODUCTID_TAG);
        }
//    获取到就开始请求网络
        mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(ApiClient.getProductDetail(productSchemeId).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                productlsBean = new Gson().fromJson(getV2String(s), ProductlsBean.class);
                LogUtils.Log("s", "s");

            }

            @Override
            protected void onRxError(Throwable error) {
                LogUtils.Log("s", error.toString());
            }
        }));
    }

    @Override
    protected void data() {
        super.data();
        TaskInfo.complentTask("查看产品");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mCompositeSubscription && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
        mCompositeSubscription = null;
        TaskInfo.unbind();
        //返回按钮的埋点
//        DataStatistApiParam.onStatisToCProductDetailBack();
        if (liveObserver != null) {
            RxBus.get().unregister(RxConstant.ZHIBO_STATUES, liveObserver);
        }

    }

    @Override
    protected void executeOverideUrlCallBack(String actionUrl) {
        if (actionUrl.contains(WebViewConstant.AppCallBack.TOC_SHARE)) {
            shareToC(actionUrl);
        }
        if (actionUrl.contains(WebViewConstant.AppCallBack.TOC_PDF)) {
            showPdf(actionUrl);
        }
        if (actionUrl.contains(WebViewConstant.AppCallBack.TOC_PRODUCT_TOUTIAO)) {
            gotoZiXun(actionUrl);
        }
    }

    /**
     * 跳转到资讯
     *
     * @param actionUrl
     */
    private void gotoZiXun(String actionUrl) {
        String[] split = actionUrl.split(":");
        try {
            String infoId = URLDecoder.decode(split[2], "utf-8");
            String category = URLDecoder.decode(split[3], "utf-8");
            String title = URLDecoder.decode(split[4], "utf-8");
            String Url = BaseWebNetConfig.detailToZiXun + infoId + "&category=" + category;
            Router.build(RouteConfig.GOTO_VIDEO_INFORMATIOON)
                    .with(WebViewConstant.push_message_url, Url)
                    .with(WebViewConstant.push_message_title, title)
                    .with(WebViewConstant.PAGE_SHOW_TITLE, false)
                    .with(WebViewConstant.RIGHT_SHARE, true)
                    .go(baseContext);
        } catch (Exception e) {

        }


//        Domain.foundNews + newsBean.getInfoId() + "&category=" + newsBean.getCategory();
    }

    /**
     * 分享产品
     *
     * @param action
     */
    private void shareToC(String action) {
        String actionDecode = URLDecoder.decode(action);
        String[] split = actionDecode.split(":");
        String sharePYQtitle = "";
        try {
            String title = split[2];
            String subTitle = split[3];
            String imageTitle = split[4];
            String link = split[5];

            link = link.startsWith("/") ? BaseWebNetConfig.baseParentUrl + link : BaseWebNetConfig.baseParentUrl + "/" + link;

            shareCommonBean = new ShareCommonBean(title, subTitle, link, "");
            commonShareDialog = new CommonShareDialog(baseContext, CommonShareDialog.Tag_Style_WxPyq, shareCommonBean, new CommonShareDialog.CommentShareListener() {

                @Override
                public void completShare(int shareType) {
                    TaskInfo.complentTask("分享产品");

                }
            });
            commonShareDialog.show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 在webview里面展示pdf
     */
    private void showPdf(String action) {
        String actionDecode = null;
        try {
            actionDecode = URLDecoder.decode(action, "utf-8");
            String[] split = actionDecode.split(":");
            String url = split[2] + ":" + split[3];
            String title = split[4];
            String pdfurl = BaseWebNetConfig.pdfUrlToC + url;
//            ProductNavigationUtils.startProductPdf(baseContext, pdfurl, title);
            startActivity(new Intent(baseContext, PdfActivity.class).putExtra("pdfurl", url).putExtra("pdftitle", title).putExtra("productbean", productlsBean));
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

    }

}
