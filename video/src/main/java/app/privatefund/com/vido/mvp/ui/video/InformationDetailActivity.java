package app.privatefund.com.vido.mvp.ui.video;

import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.tools.PromptManager;

import app.privatefund.com.vido.R;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/24-22:01
 */
public class InformationDetailActivity extends BaseWebViewActivity {


    @Override
    protected int layoutID() {
        return R.layout.activity_information_detail;
    }

    @Override
    protected void executeOverideUrlCallBack(String actionUrl) {
        if (actionUrl.contains(WebViewConstant.AppCallBack.TOC_SHARE)) {
       //分享
            PromptManager.ShowCustomToast(baseContext,actionUrl);
        }
    }

    @Override
    protected void pageShare() {
        String javascript = "javascript:shareClick()";
        mWebview.loadUrl(javascript);
    }
}
