package com.cgbsoft.privatefund.widget;

import com.cgbsoft.lib.TaskInfo;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.chenenyu.router.annotation.Route;

/**
 * @author chenlong
 */

@Route(RouteConfig.GOTO_BASE_WITHSHARE_WEBVIEW)
public class WebviewWithShareActivity extends BaseWebViewActivity {

//    CommonShareDialog commonShareDialog;

    @Override
    protected int layoutID() {
        return app.privatefund.com.vido.R.layout.activity_information_detail;
    }

    @Override
    protected void data() {
        super.data();
        TaskInfo.complentTask("查看资讯");
    }

//    @Override
//    protected void executeOverideUrlCallBack(String actionUrl) {
//        if (actionUrl.contains(WebViewConstant.AppCallBack.TOC_SHARE)) {
//            //分享
//            shareToc(actionUrl);
//        }
//    }

//    private void shareToc(String actionUrl) {
//        LogUtils.Log("mm", actionUrl);
//        // sendCommand(’tocShare’,'proName','子标题',,'tocShareProductImg','/apptie/detail.html?schemeId='123456789'');
//        String actionDecode = URLDecoder.decode(actionUrl);
//        String[] split = actionDecode.split(":");
//        String sharePYQtitle = "";
//
//        String title = split[2];
//        String subTitle = split[3];
//        String imageTitle = split[4];
//        String link = split[5];
//        if (split.length >= 7) {
//            sharePYQtitle = split[6];
//        }
//        link = link.startsWith("/") ? BaseWebNetConfig.baseParentUrl + link.substring(0) : BaseWebNetConfig.baseParentUrl + link;
//        if (null != commonShareDialog && commonShareDialog.isShowing()) return;
//        ShareCommonBean shareCommonBean = new ShareCommonBean(title, subTitle, link, "");
//        commonShareDialog = new CommonShareDialog(baseContext, CommonShareDialog.Tag_Style_WxPyq, shareCommonBean, new CommonShareDialog.CommentShareListener() {
//            @Override
//            public void completShare(int shareType) {
//                TaskInfo.complentTask("分享资讯");
//            }
//        });
//        commonShareDialog.show();
//    }

//    @Override
//    protected void pageShare() {
//        String javascript = "javascript:shareClick()";
//        mWebview.loadUrl(javascript);
//    }
}

