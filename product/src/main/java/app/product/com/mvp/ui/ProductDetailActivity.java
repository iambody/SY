package app.product.com.mvp.ui;

import com.cgbsoft.lib.TaskInfo;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.chenenyu.router.annotation.Route;

import java.net.URLDecoder;

import app.privatefund.com.share.bean.ShareCommonBean;
import app.privatefund.com.share.dialog.CommonShareDialog;

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
}
