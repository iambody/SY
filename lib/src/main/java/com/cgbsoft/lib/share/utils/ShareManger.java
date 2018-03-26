package com.cgbsoft.lib.share.utils;

import android.content.Context;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.share.bean.ShareCommonBean;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.Utils;
import com.mob.MobSDK;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/6-16:25
 */
public class ShareManger implements PlatformActionListener {
    //微信分享的标识
    public static final int WXSHARE = 101;
    //朋友圈分享的标识
    public static final int CIRCLESHARE = 102;
    //微信文本分享
    public static final int WXTXT = 103;
    //小程序
    public static final int WXMINIPROGRAM = 104;
    //只分享图片
    public static final int IMAGESHARE = 105;
    //朋友圈文本分享
    public static final int CIRCLETXT = 106;
    //朋友圈分享视频
    public static final int CIRCLEVIDEO = 107;
    //朋友圈音频分享
    public static final int CIRCLMUSICE = 108;

    private static ShareManger shareManger;
    private Context wxContext;

    /**
     * 暴露接口
     */

    private static ShareResultListenr shareResultListenr;

    //获取的分享bean
    private static ShareCommonBean shareCommonBean;

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

    private ShareManger() {
    }

    //静态工厂方法
    public static ShareManger getInstance(Context wxContextx, ShareCommonBean shareCommonBeans, ShareResultListenr shareResultListenrss) {
        if (shareManger == null) {
            shareManger = new ShareManger(wxContextx);
        }
        shareResultListenr = shareResultListenrss;
        shareCommonBean = shareCommonBeans;
        return shareManger;
    }

    /**
     * 初始化
     *
     * @param wxContextx
     * @param
     */
    private ShareManger(Context wxContextx) {
        this.wxContext = wxContextx;
        MobSDK.init(wxContext);
    }


    /**
     * 手动分享代码
     */
    public void goShareWx(int wxtype) {
        switch (wxtype) {
            case WXSHARE://微信分享
                weChatShare(shareCommonBean);
                break;
            case CIRCLESHARE://朋友圈分享
                wxCircleShare(shareCommonBean);
                break;
            case WXMINIPROGRAM://小程序
                wxMiniprogram();
            case IMAGESHARE://分享图片
                wxImageShare(shareCommonBean);
                break;
            case CIRCLETXT://f分享文本到朋友圈
                circleXtxt(shareCommonBean);
                break;
            case WXTXT://分享文本给好友
                wxTxtShare(shareCommonBean);
                break;
            case CIRCLEVIDEO://分享视频到朋友圈
                circleVideoShare(shareCommonBean);
                break;
            case CIRCLMUSICE://分享音频到
                circleMusicShare(shareCommonBean);
                break;
        }
    }

    /**
     * 音频
     * 分享朋友圈的
     */
    private void circleMusicShare(ShareCommonBean shareCommonBean) {
//        if (!Utils.isWeixinAvilible(wxContext)) {//没有安装微信
//            PromptManager.ShowCustomToast(wxContext, wxContext.getResources().getString(R.string.pleaseinstanllweixin));
//            return;
//        }
//        platform_wx = ShareSDK.getPlatform(WechatMoments.NAME);
//        Wechat.ShareParams sp = new Wechat.ShareParams();
//        sp.setShareType(Platform.SHARE_MUSIC);// 一定要设置分享属性
//        sp.setTitle(shareCommonBean.getShareTitle());
//        sp.setText(BStrUtils.isEmpty(shareCommonBean.getShareContent()) ? shareCommonBean.getShareTitle() : shareCommonBean.getShareContent());
//        sp.setUrl(shareCommonBean.getShareUrl());
//        sp.setImageData(null);
//        sp.setImageUrl(BStrUtils.isEmpty(shareCommonBean.getShareNetLog()) ? Constant.SHARE_LOG : shareCommonBean.getShareNetLog());
//
//        sp.setImagePath(null);
//
//        platform_wx.setPlatformActionListener(this); // 设置分享事件回调
//        // 执行分享
//        platform_wx.share(sp);

    }

    /**
     * @param shareCommonBean
     */
    private void circleVideoShare(ShareCommonBean shareCommonBean) {
        if (!Utils.isWeixinAvilible(wxContext)) {//没有安装微信
            PromptManager.ShowCustomToast(wxContext, wxContext.getResources().getString(R.string.pleaseinstanllweixin));
            return;
        }
        platform_wx = ShareSDK.getPlatform(WechatMoments.NAME);
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_VIDEO);// 一定要设置分享属性
        sp.setTitle(shareCommonBean.getShareTitle());
        sp.setText(BStrUtils.isEmpty(shareCommonBean.getShareContent()) ? shareCommonBean.getShareTitle() : shareCommonBean.getShareContent());
        sp.setUrl(shareCommonBean.getShareUrl());
        sp.setImageData(null);
        sp.setImageUrl(BStrUtils.isEmpty(shareCommonBean.getShareNetLog()) ? Constant.SHARE_LOG : shareCommonBean.getShareNetLog());

        sp.setImagePath(null);

        platform_wx.setPlatformActionListener(this); // 设置分享事件回调
        // 执行分享
        platform_wx.share(sp);

    }

    /**
     * 好友分享文本
     *
     * @param shareCommonBean
     */
    private void wxTxtShare(ShareCommonBean shareCommonBean) {
        platform_wx = ShareSDK.getPlatform(Wechat.NAME);
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setText(shareCommonBean.getShareContent());
        sp.setShareType(Platform.SHARE_TEXT);
        platform_wx.setPlatformActionListener(this);
        platform_wx.share(sp);
    }

    /**
     * 朋友圈分享文本
     */
    private void circleXtxt(ShareCommonBean WxShareData) {
        platform_circle = ShareSDK.getPlatform(WechatMoments.NAME);
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setText(WxShareData.getShareContent());
        sp.setShareType(Platform.SHARE_TEXT);
        platform_circle.setPlatformActionListener(this);
        platform_circle.share(sp);
    }

    /**
     * 分享单张图片
     */
    private void wxImageShare(ShareCommonBean commonBean) {
        platform_wx = ShareSDK.getPlatform(Wechat.NAME);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
//        sp.setImagePath(WxShareData);
        sp.setTitle("s");
        sp.setText("ss");
//        sp.setImageData(shareCommonBean.getPoster());
        platform_wx.setPlatformActionListener(this); // 设置分享事件回调
        // 执行分享
        platform_wx.share(sp);
    }

    private void wxMiniprogram() {
        if (!Utils.isWeixinAvilible(wxContext)) {//没有安装微信
            PromptManager.ShowCustomToast(wxContext, wxContext.getResources().getString(R.string.pleaseinstanllweixin));
            return;
        }

        HashMap<String, Object
                > map = new HashMap<>();
        map.put("Id", "4");
        map.put("SortId", "4");
        map.put("AppId", "wx1622f3ec2d611b59");
        map.put("AppSecret", "81eb3bce61b825bab4d95bbade40153c");
        map.put("userName", "gh_da3da530187d");
        map.put("ShareByAppClient", "true");
        map.put("path", "pages/detailI/detailI?id=30ed55786ff041ba92c083ce2f086b2f&category=6");
        map.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(Wechat.NAME, map);

        Platform platform_wx = ShareSDK.getPlatform(Wechat.NAME);
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setText("测试");
        shareParams.setTitle("测试的呀");
        shareParams.setImageUrl(Constant.SHARE_LOG);
        shareParams.setUrl("www.baidu.com");
        shareParams.setSiteUrl("www.baidu.com");
        shareParams.setShareType(Platform.SHARE_WXMINIPROGRAM);


        platform_wx.setPlatformActionListener(this);
        platform_wx.share(shareParams);


    }

    /**
     * 微信分享的初始化
     */
    private void weChatShare(ShareCommonBean WxShareData) {
        if (!Utils.isWeixinAvilible(wxContext)) {//没有安装微信
            PromptManager.ShowCustomToast(wxContext, wxContext.getResources().getString(R.string.pleaseinstanllweixin));
            return;
        }
        platform_wx = ShareSDK.getPlatform(Wechat.NAME);
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
        sp.setTitle(WxShareData.getShareTitle());
        sp.setText(BStrUtils.isEmpty(WxShareData.getShareContent()) ? WxShareData.getShareTitle() : WxShareData.getShareContent());
        sp.setUrl(WxShareData.getShareUrl());
        sp.setImageData(null);
        sp.setImageUrl(BStrUtils.isEmpty(WxShareData.getShareNetLog()) ? Constant.SHARE_LOG : WxShareData.getShareNetLog());

        sp.setImagePath(null);

        platform_wx.setPlatformActionListener(this); // 设置分享事件回调
        // 执行分享
        platform_wx.share(sp);
    }

    /**
     * 朋友圈分享
     */
    private void wxCircleShare(ShareCommonBean WxShareData) {
        if (!Utils.isWeixinAvilible(wxContext)) {//没有安装微信
            PromptManager.ShowCustomToast(wxContext, wxContext.getResources().getString(R.string.pleaseinstanllweixin));
            return;
        }
        platform_circle = ShareSDK.getPlatform(WechatMoments.NAME);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(WxShareData.getShareTitle());
        sp.setText(WxShareData.getShareContent());
        sp.setUrl(WxShareData.getShareUrl());
        sp.setImageData(null);
        sp.setImageUrl(BStrUtils.isEmpty(WxShareData.getShareNetLog()) ? Constant.SHARE_LOG : WxShareData.getShareNetLog());
        sp.setImagePath(null);
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
