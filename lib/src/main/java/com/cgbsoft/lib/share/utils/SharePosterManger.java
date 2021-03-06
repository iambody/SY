package com.cgbsoft.lib.share.utils;

import android.content.Context;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.Utils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * desc 专门分享海报的manger
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/26-16:23
 */
public class SharePosterManger implements PlatformActionListener {

    //微信分享的标识
    public static final int WXSHARE = 101;
    //朋友圈分享的标识
    public static final int CIRCLESHARE = 102;

    private static SharePosterManger shareManger;

    private Context wxContext;
    /**
     * 授权成功
     */
    public static final int WxAuthorOk = 1;
    /**
     * 授权成功
     */
    public static final int WxAuthorCANCLE = 3;
    /**
     * 授权成功
     */
    public static final int WxAuthorERROR = 0;

    /**
     * 暴露接口
     */

    private static SharePosterManger.ShareResultListenr shareResultListenr;

    //获取的分享bean
    private static String sharePostPath;

    /**
     * 朋友圈分享对象
     */
    private Platform platform_circle;

    /**
     * 微信好友分享对象
     */
    private Platform platform_wx;

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (null != shareResultListenr)
            shareResultListenr.completShare();
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        if (null != shareResultListenr)
            shareResultListenr.errorShare();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        if (null != shareResultListenr)
            shareResultListenr.cancelShare();
    }

    /**
     * 暴露的回调接口
     */
    public interface ShareResultListenr {
        //分享成功
        void completShare();

        void errorShare();

        void cancelShare();
    }

    private SharePosterManger() {
    }

    //静态工厂方法
    public static SharePosterManger getInstance(Context wxContextx, String posterPath, SharePosterManger.ShareResultListenr shareResultListenrss) {
        if (shareManger == null) {
            shareManger = new SharePosterManger(wxContextx);
        }
        shareResultListenr = shareResultListenrss;
        sharePostPath = posterPath;
        return shareManger;
    }

    /**
     * 初始化
     *
     * @param wxContextx
     * @param
     */
    private SharePosterManger(Context wxContextx) {
        this.wxContext = wxContextx;
//        Mob.initSDK(wxContext);
    }


    /**
     * 手动分享代码
     */
    public void goShareWx(int Wxtype) {
        switch (Wxtype) {
            case WXSHARE://微信分享
                WeChatShare(sharePostPath);
                break;
            case CIRCLESHARE://朋友圈分享
                WxCircleShare(sharePostPath);
                break;
        }
    }

    /**
     * 微信分享的初始化
     */
    private void WeChatShare(String posterPath) {
        if (!Utils.isWeixinAvilible(wxContext)) {//没有安装微信
            PromptManager.ShowCustomToast(wxContext, wxContext.getResources().getString(R.string.pleaseinstanllweixin));
            return;
        }
        platform_wx = ShareSDK.getPlatform(Wechat.NAME);
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);// 一定要设置分享属性
        sp.setImagePath(posterPath);

        platform_wx.setPlatformActionListener(this); // 设置分享事件回调
        // 执行分享
        platform_wx.share(sp);
    }

    /**
     * 朋友圈分享
     */
    private void WxCircleShare(String posterPath) {
        if (!Utils.isWeixinAvilible(wxContext)) {//没有安装微信
            PromptManager.ShowCustomToast(wxContext, wxContext.getResources().getString(R.string.pleaseinstanllweixin));
            return;
        }
        platform_circle = ShareSDK.getPlatform(WechatMoments.NAME);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);// 一定要设置分享属性
        sp.setImagePath(posterPath);

        platform_circle.setPlatformActionListener(this); // 设置分享事件回调
        // 执行分享
        platform_circle.share(sp);
    }

    /**
     * 手动注销
     */
    public static void unbindShare() {
//        ShareSDK.stopSDK();
    }

}
