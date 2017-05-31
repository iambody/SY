package app.privatefund.com.vido.mvp.ui.video;

import com.cgbsoft.lib.TaskInfo;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.LogUtils;
import com.chenenyu.router.annotation.Route;

import java.net.URLDecoder;

import app.privatefund.com.share.bean.ShareCommonBean;
import app.privatefund.com.share.dialog.CommonShareDialog;
import app.privatefund.com.vido.R;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/24-22:01
 */
@Route(RouteConfig.GOTO_VIDEO_INFORMATIOON)
public class InformationDetailActivity extends BaseWebViewActivity {

    CommonShareDialog commonShareDialog;

    @Override
    protected int layoutID() {
        return R.layout.activity_information_detail;
    }

    @Override
    protected void data() {
        super.data();
        TaskInfo.complentTask("查看资讯");
    }

    @Override
    protected void executeOverideUrlCallBack(String actionUrl) {
        if (actionUrl.contains(WebViewConstant.AppCallBack.TOC_SHARE)) {
            //分享
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
        String baseWebsite = "https://app.simuyun.com/app5.0";
        link = link.startsWith("/") ? baseWebsite + link : baseWebsite + "/" + link;


        String shareType = link.contains("apptie/detail.html") ? "chanpin" : link.contains("discover/details.html") ? "zixun" : "";
//            https://app.simuyun.com/app5.0/apptie/detail.html?schemeId=04c9dff066ab41499e2a189052ca6d94&type=1&share=1
//            https://app.simuyun.com/app5.0/discover/details.html?id=cf2e0d629cf143c0b3e5a6f0b2415ded&category=4&share=1
        if (null != commonShareDialog) commonShareDialog = null;
        ShareCommonBean shareCommonBean = new ShareCommonBean(title, subTitle, link, "");
        commonShareDialog = new CommonShareDialog(baseContext, CommonShareDialog.Tag_Style_WxPyq, shareCommonBean, new CommonShareDialog.CommentShareListener() {
            @Override
            public void completShare() {
                TaskInfo.complentTask("分享资讯");
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
