package app.product.com.mvp.ui;

import android.content.Intent;

import com.cgbsoft.lib.TaskInfo;
import com.cgbsoft.lib.base.webview.BaseWebNetConfig;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.chenenyu.router.annotation.Route;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import app.privatefund.com.share.bean.ShareCommonBean;
import app.privatefund.com.share.dialog.CommonShareDialog;
import app.product.com.mvc.ui.PdfActivity;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/20-18:53
 */
@Route(RouteConfig.GOTOPRODUCTDETAIL)
public class ProductDetailActivity extends BaseWebViewActivity {
    private CommonShareDialog commonShareDialog;
    private ShareCommonBean shareCommonBean;

    @Override
    protected void data() {
        super.data();
        TaskInfo.complentTask("查看产品");
    }

    @Override
    protected void executeOverideUrlCallBack(String actionUrl) {
        if (actionUrl.contains(WebViewConstant.AppCallBack.TOC_SHARE)) {
            shareToC(actionUrl);
        }
        if (actionUrl.contains(WebViewConstant.AppCallBack.TOC_PDF)) {
            PromptManager.ShowCustomToast(baseContext, "开始展示pdf");
            showPdf(actionUrl);
        }
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
            if (split.length >= 7) {
                sharePYQtitle = split[6];
            }
            link = link.startsWith("/") ? " https://app.simuyun.com/app5.0" + link : " https://app.simuyun.com/app5.0" + "/" + link;

            String shareType = link.contains("apptie/detail.html") ? "chanpin" : link.contains("discover/details.html") ? "zixun" : "";
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
//d8-app.simuyun.com/app5.0/pdfjs/web/viewer.html?file=
        try {
            actionDecode = URLDecoder.decode(action, "utf-8");
            String[] split = actionDecode.split(":");
            String url = split[2] + ":" + split[3];
            String title = split[4];

            String pdfurl = BaseWebNetConfig.pdfUrlToC + url;
//            ProductNavigationUtils.startProductPdf(baseContext, pdfurl, title);
            startActivity(new Intent(baseContext, PdfActivity.class).putExtra("pdfurl", url).putExtra("pdftitle",title));
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

    }
}
