package app.privatefund.com.share.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * desc主要是信息捕捉
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/3/31-18:32
 */

public class WxAuthorUtils {
    //单例模式
    private static WxAuthorUtils wxAuthorUtils = null;
    //上下文
    private static Context wxContext;

    /**
     * 供给外部的回调
     *
     * @param wxContext
     */
    private AuthorUtilsResultListenr authorUtilsResultListenr;

    private WxAuthorUtils() {
    }

    ;

    //静态工厂方法
    public static WxAuthorUtils getInstance(Context wxContextx, AuthorUtilsResultListenr authorUtilsResultListenrs) {
        if (wxAuthorUtils == null) {
            wxAuthorUtils = new WxAuthorUtils(wxContextx, authorUtilsResultListenrs);
        }
        return wxAuthorUtils;
    }

    /**
     * 初始化
     *
     * @param wxContextx
     * @param authorUtilsResultListenrs
     */
    private WxAuthorUtils(Context wxContextx, AuthorUtilsResultListenr authorUtilsResultListenrs) {
        this.wxContext = wxContextx;
        this.authorUtilsResultListenr = authorUtilsResultListenrs;
        ShareSDK.initSDK(wxContext);
    }

    /**
     * 可以溢出sdk依赖
     */
    public static void unbinAuthor() {
        ShareSDK.stopSDK();


    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int auth_statu = msg.what;
            Platform platform = (Platform) msg.obj;
            switch (auth_statu) {
                case 1:
                    authorUtilsResultListenr.getAuthorResult(1, platform);
//
//                    final String nickname = platform.getDb().getUserName();
//                    final String userid = platform.getDb().getUserId();
//                    String usericon = platform.getDb().getUserIcon();
//                    String platformName = platform.getName();
//                    // Toast.makeText(
//                    // BaseContext,
//                    // String.format("昵称%s;   id是%s;   名字是%s", nickname,
//                    // userid, platformName), 10 * 1000).show();

                    break;
                case 0:
                    Toast.makeText(wxContext, "微信验证失败", Toast.LENGTH_SHORT).show();
                    authorUtilsResultListenr.getAuthorResult(0, platform);

                    break;
                case 3:
                    Toast.makeText(wxContext, "微信验证取消", Toast.LENGTH_SHORT).show();
                    authorUtilsResultListenr.getAuthorResult(3, platform);
                default:
                    break;
            }
            return;

        }
    };

    /**
     * 授权完成后会有回调接口  会暴露状态
     */

    private void WxAuth() {
        Platform weixinplatform = ShareSDK.getPlatform(wxContext, Wechat.NAME);// Wechat.NAME);
        weixinplatform.removeAccount(true);
        weixinplatform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int arg0, HashMap<String, Object> hashMap) {
                Message message = new Message();
                message.obj = arg0;

                message.what = 1;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

                Message message = new Message();
                message.obj = throwable.toString();

                message.what = 0;
                mHandler.sendMessage(message);
            }

            @Override
            public void onCancel(Platform platform, int arg0) {
                Message message = new Message();
                message.obj = arg0;
                message.what = 3;
                mHandler.sendMessage(message);
            }
        });
        weixinplatform.showUser(null);// 获得用户数据

    }

    /**
     * 暴露的回调接口
     */
    public interface AuthorUtilsResultListenr {
        public void getAuthorResult(int type, Platform platform);
    }
}
