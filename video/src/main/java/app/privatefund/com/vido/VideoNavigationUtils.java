package app.privatefund.com.vido;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.cgbsoft.lib.base.webview.WebViewConstant;

import app.privatefund.com.vido.mvp.ui.video.InformationDetailActivity;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/24-22:23
 */
public class VideoNavigationUtils {
    public static void startInfomationDetailActivity(Context context, String url, String productName, int requestCode) {

        Intent intent = new Intent(context, InformationDetailActivity.class);
        intent.putExtra(WebViewConstant.push_message_url, url);
        intent.putExtra(WebViewConstant.push_message_title, productName);
        intent.putExtra(WebViewConstant.PAGE_SHOW_TITLE, true);
        intent.putExtra(WebViewConstant.RIGHT_SHARE, true);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }
}
