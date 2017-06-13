package com.cgbsoft.privatefund.widget;

import com.cgbsoft.lib.base.webview.BaseWebNetConfig;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.chenenyu.router.annotation.Route;

import java.net.URLDecoder;

import app.privatefund.com.share.bean.ShareCommonBean;
import app.privatefund.com.share.dialog.CommonShareDialog;

/**
 * @author chenlong
 */
@Route("investornmain_invisterbasewebviewctivity")
public class InverstorBaseWebviewActivity extends BaseWebViewActivity {

    @Override
    protected void executeOverideUrlCallBack(String actionUrl) {
        if (actionUrl.contains(WebViewConstant.AppCallBack.INVITE_CUSTOM)) {
            String[] split = actionUrl.split(":");
            try {
                String title = URLDecoder.decode(split[5], "utf-8");
                String type = URLDecoder.decode(split[2], "utf-8");
                String content = URLDecoder.decode(split[4], "utf-8");
                String link = URLDecoder.decode(split[3], "utf-8");
                link = link.startsWith("/") ? BaseWebNetConfig.baseParentUrl + link.substring(0) : BaseWebNetConfig.baseParentUrl + link;
                ShareCommonBean commonShareBean = new ShareCommonBean();
                commonShareBean.setShareTitle(title);
                commonShareBean.setShareContent(content);
                commonShareBean.setShareUrl(link);
                if ("2".equals(type)) {
                    CommonShareDialog commonShareDialog = new CommonShareDialog(InverstorBaseWebviewActivity.this, CommonShareDialog.Tag_Style_WxPyq, commonShareBean, null);
                    commonShareDialog.show();
                } else {
                    CommonShareDialog commonShareDialog = new CommonShareDialog(this, CommonShareDialog.Tag_Style_WeiXin, commonShareBean, null);
                    commonShareDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
