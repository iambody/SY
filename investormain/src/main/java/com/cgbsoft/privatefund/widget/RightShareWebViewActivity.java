package com.cgbsoft.privatefund.widget;

import com.cgbsoft.lib.TaskInfo;
import com.cgbsoft.lib.base.webview.BaseWebNetConfig;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.chenenyu.router.annotation.Route;

import java.net.URLDecoder;

import app.privatefund.com.share.bean.ShareCommonBean;
import app.privatefund.com.share.dialog.CommonShareDialog;

/**
 * @author chenlong
 */
@Route(RouteConfig.GOTO_RIGHT_SHARE_ACTIVITY)
public class RightShareWebViewActivity extends BaseWebViewActivity {

    CommonShareDialog commonShareDialog;

    @Override
    protected void before() {
        super.before();
        if(url.contains("discover")){//是资讯页面
            TaskInfo.complentTask("查看资讯");
        }
    }

    @Override
    protected void executeOverideUrlCallBack(String actionUrl) {
        if (actionUrl.contains(WebViewConstant.AppCallBack.TOC_SHARE)) {
            shareToc(actionUrl);
        }
    }

    private void shareToc(String actionUrl) {
        LogUtils.Log("mm", actionUrl);
        // sendCommand(’tocShare’,'proName','子标题',,'tocShareProductImg','/apptie/detail.html?schemeId='123456789'');
        String actionDecode = URLDecoder.decode(actionUrl);
        String[] split = actionDecode.split(":");
        String sharePYQtitle = "";

        String titles = split[2];
        String subTitle = split[3];
        String imageTitle = split[4];
        String link = split[5];
        if (split.length >= 7) {
            sharePYQtitle = split[6];
        }
        link = link.startsWith("/") ? BaseWebNetConfig.baseParentUrl + link.substring(0) : BaseWebNetConfig.baseParentUrl + link;
        if (null != commonShareDialog && commonShareDialog.isShowing()) return;
        ShareCommonBean shareCommonBean = new ShareCommonBean(titles, subTitle, link, "");
        commonShareDialog = new CommonShareDialog(baseContext, CommonShareDialog.Tag_Style_WxPyq, shareCommonBean, shareType -> {
            //分享微信朋友圈成功
            if(CommonShareDialog.SHARE_WXCIRCLE == shareType && url.contains("discover")){
                //自选页面分享朋友圈成功
                TaskInfo.complentTask("分享资讯");
                DataStatistApiParam.onStatisToCShareInfOnCircle(titles,title );
            }
        });
        commonShareDialog.show();
    }

    @Override
    protected void pageShare() {
        String javascript = "javascript:shareClick()";
        mWebview.loadUrl(javascript);
    }
}
