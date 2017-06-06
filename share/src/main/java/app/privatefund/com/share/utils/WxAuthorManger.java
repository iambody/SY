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

public class WxAuthorManger {
    /**
     * 单例模式
     */

    private static WxAuthorManger wxAuthorManger = null;
    /**
     * 上下文
     */
    private static Context wxContext;
    /**
     * 供给外部的回调
     *
     */
    private static AuthorUtilsResultListenr authorUtilsResultListenr;

    /**
     * 授权成功
     */
    public  static final int   WxAuthorOk=1;
    /**
     * 授权成功
     */
    public  static final int  WxAuthorCANCLE=3;
    /**
     * 授权成功
     */
    public  static final int  WxAuthorERROR=0;

    private WxAuthorManger() {
    }


    //静态工厂方法
    public static WxAuthorManger getInstance(Context wxContextx, AuthorUtilsResultListenr authorUtilsResultListenrss) {
        if (wxAuthorManger == null) {
            wxAuthorManger = new WxAuthorManger(wxContextx );
        }
         authorUtilsResultListenr=authorUtilsResultListenrss;
        return wxAuthorManger;
    }

    /**
     * 初始化
     *
     * @param wxContextx
     * @param
     */
    private WxAuthorManger(Context wxContextx ) {
        this.wxContext = wxContextx;
    }

    /**
     * 可以溢出sdk依赖
     */
    public static void unbinAuthor() {
        ShareSDK.stopSDK();


    }

   static Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int auth_statu = msg.what;

            switch (auth_statu) {
                case WxAuthorOk:
                    Platform platform = (Platform) msg.obj;
                    authorUtilsResultListenr.getAuthorResult(WxAuthorOk, platform);
//                    PromptManager.ShowCustomToast(wxContext,"获取的名字"+platform.getDb().getUserName());


//                    final String nickname = platform.getDb().getUserName();
//                    final String userid = platform.getDb().getUserId();
//                    String usericon = platform.getDb().getUserIcon();
//                    String platformName = platform.getName();
//                    // Toast.makeText(
//                    // BaseContext,
//                    // String.format("昵称%s;   id是%s;   名字是%s", nickname,
//                    // userid, platformName), 10 * 1000).show();

                    break;
                case WxAuthorERROR://错误
                    Platform errorplatform = (Platform) msg.obj;
                    Toast.makeText(wxContext, "微信验证失败", Toast.LENGTH_SHORT).show();
                    authorUtilsResultListenr.getAuthorResult(WxAuthorERROR, errorplatform);

                    break;
                case WxAuthorCANCLE://取消
                    Platform Cancleplatform = (Platform) msg.obj;
                    Toast.makeText(wxContext, "微信验证取消", Toast.LENGTH_SHORT).show();
                    authorUtilsResultListenr.getAuthorResult(WxAuthorCANCLE, null);
                default:
                    break;
            }
            return;

        }
    };

    /**
     * 授权完成后会有回调接口  会暴露状态
     */

    public static  void startAuth() {

        Platform weixinplatform = ShareSDK.getPlatform( Wechat.NAME);// Wechat.NAME);
        weixinplatform.removeAccount(true);
//        ShareSDK.removeCookieOnAuthorize(true);// 清理cookie
        weixinplatform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int arg0, HashMap<String, Object> hashMap) {
                Message message = new Message();
                message.obj = platform;
                message.what = WxAuthorOk;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

                Message message = new Message();
                message.obj = platform;

                message.what =WxAuthorERROR;
                mHandler.sendMessage(message);
            }

            @Override
            public void onCancel(Platform platform, int arg0) {
                Message message = new Message();
                message.obj = platform;
                message.what = WxAuthorCANCLE;
                mHandler.sendMessage(message);
            }
        });
//        weixinplatform.SSOSetting(false);
        weixinplatform.showUser(null);// 获得用户数据

    }

    /**
     * 暴露的回调接口
     */
    public  interface AuthorUtilsResultListenr {
          void getAuthorResult(int type, Platform platform);
    }
}
