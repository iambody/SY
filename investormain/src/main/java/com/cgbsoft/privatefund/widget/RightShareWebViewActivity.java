package com.cgbsoft.privatefund.widget;

import com.cgbsoft.lib.base.webview.BaseWebNetConfig;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
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

        String title = split[2];
        String subTitle = split[3];
        String imageTitle = split[4];
        String link = split[5];
        if (split.length >= 7) {
            sharePYQtitle = split[6];
        }
        link = link.startsWith("/") ? BaseWebNetConfig.baseParentUrl + link.substring(0) : BaseWebNetConfig.baseParentUrl + link;
        if (null != commonShareDialog && commonShareDialog.isShowing()) return;
        ShareCommonBean shareCommonBean = new ShareCommonBean(title, subTitle, link, "");
        commonShareDialog = new CommonShareDialog(baseContext, CommonShareDialog.Tag_Style_WxPyq, shareCommonBean, null);
        commonShareDialog.show();
    }
}
