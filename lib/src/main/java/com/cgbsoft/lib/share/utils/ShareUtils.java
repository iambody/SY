package com.cgbsoft.lib.share.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/20-21:11
 */
public class ShareUtils {
    /**
     * 获取屏幕宽高
     * Type 1返回 宽度 2返回高度
     */
    public static int GetWidhAndHeight(Context PcoContext, int Type) {
        WindowManager manager = (WindowManager) PcoContext.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        int width2 = dm.widthPixels;
        int height2 = dm.heightPixels;
        return 1 == Type ? width2 : height2;
    }

    /**
     * Textview放置文本
     */
    public static void setTxt(TextView T, String Str) {
        T.setText(nullToStr(Str));
    }

    /**
     * 判断如果
     **/
    public static String nullToStr(String str) {

        if (null == str || "".equals(str)) {
            return "--";
        }
        return str;
    }

//    public void test(Context context) {
//        ShareCommonBean shareCommonBean = new ShareCommonBean("分享title", "分享内容", "分享链接", "此为分享的左侧log图片的网络url（以后每个分享都要根据场景显示不同的log;eg：某些节日需要展示该节日风格的log）,如果有就添加如果没有就传null");
//        ShareManger manger = ShareManger.getInstance(context, shareCommonBean, new ShareManger.ShareResultListenr() {
//            @Override
//            public void completShare() {//分享成功
//
//            }
//
//            @Override
//            public void errorShare() {//分享失败
//
//            }
//
//            @Override
//            public void cancelShare() {//分享取消
//
//            }
//        });
//        manger.goShareWx(ShareManger.CIRCLESHARE);//朋友圈分享
//        manger.goShareWx(ShareManger.WXSHARE);//微信 分享
//        manger.unbindShare();
//
//    }
}
