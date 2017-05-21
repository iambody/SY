package app.product.com.mvp.ui;

import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.utils.tools.PromptManager;

import java.net.URLDecoder;

import app.privatefund.com.share.bean.ShareCommonBean;
import app.privatefund.com.share.dialog.CommonShareDialog;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/20-18:53
 */
public class ProductDetailActivity extends BaseWebViewActivity {
    private CommonShareDialog commonShareDialog;
    private ShareCommonBean shareCommonBean;

    @Override
    protected void executeOverideUrlCallBack(String actionUrl) {
        PromptManager.ShowCustomToast(baseContext, actionUrl);
        shareToC(actionUrl);
    }

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
            shareCommonBean = new ShareCommonBean(title, subTitle, link, app.privatefund.com.share.R.drawable.logo);
            commonShareDialog = new CommonShareDialog(baseContext, CommonShareDialog.Tag_Style_WxPyq, shareCommonBean, new CommonShareDialog.CommentShareListener() {
                @Override
                public void onclick() {

                }
            });
            commonShareDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
