package app.product.com.mvp.ui;

import android.content.Intent;

import com.cgbsoft.lib.TaskInfo;
import com.cgbsoft.lib.base.model.bean.ProductlsBean;
import com.cgbsoft.lib.base.webview.BaseWebNetConfig;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import app.privatefund.com.share.bean.ShareCommonBean;
import app.privatefund.com.share.dialog.CommonShareDialog;
import app.product.com.mvc.ui.PdfActivity;
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

    @Override
    protected int layoutID() {
        initParams();
        return super.layoutID();
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
    }

    @Override
    protected void executeOverideUrlCallBack(String actionUrl) {
        if (actionUrl.contains(WebViewConstant.AppCallBack.TOC_SHARE)) {
            shareToC(actionUrl);
        }
        if (actionUrl.contains(WebViewConstant.AppCallBack.TOC_PDF)) {
            showPdf(actionUrl);
        }
    }

    @Override
    protected boolean getCallBack() {
        return true;
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

            link = link.startsWith("/") ? " https://app.simuyun.com/app5.0" + link : " https://app.simuyun.com/app5.0" + "/" + link;

            shareCommonBean = new ShareCommonBean(title, subTitle, link, "");
            commonShareDialog = new CommonShareDialog(baseContext, CommonShareDialog.Tag_Style_WxPyq, shareCommonBean, new CommonShareDialog.CommentShareListener() {

                @Override
                public void completShare() {
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
