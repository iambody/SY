package app.privatefund.com.vido.mvp.ui.video;

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

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard();
    }

    private void shareToc(String actionUrl) {
        LogUtils.Log("mm", actionUrl);
        // sendCommand(’tocShare’,'proName','子标题',,'tocShareProductImg','/apptie/detail.html?schemeId='123456789'');
        String actionDecode = URLDecoder.decode(actionUrl);
        String[] split = actionDecode.split(":");
        String sharePYQtitle = "";

        String mytitle = split[2];
        String subTitle = split[3];
        String imageTitle = split[4];
        String link = split[5];
        if (split.length >= 7) {
            sharePYQtitle = split[6];
        }
        link = link.startsWith("/") ? BaseWebNetConfig.baseParentUrl + link.substring(0) : BaseWebNetConfig.baseParentUrl + link;
        String shareType = link.contains("apptie/detail.html") ? "chanpin" : link.contains("discover/details.html") ? "zixun" : "";
        if (null != commonShareDialog && commonShareDialog.isShowing()) return;
        ShareCommonBean shareCommonBean = new ShareCommonBean(mytitle, subTitle, link, "");
        commonShareDialog = new CommonShareDialog(baseContext, CommonShareDialog.Tag_Style_WxPyq, shareCommonBean, new CommonShareDialog.CommentShareListener() {
            @Override
            public void completShare(int shareType) {
                TaskInfo.complentTask("分享资讯");
                //分享微信朋友圈成功
                if (CommonShareDialog.SHARE_WXCIRCLE == shareType)
                    DataStatistApiParam.onStatisToCShareInfOnCircle(mytitle, title);
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
