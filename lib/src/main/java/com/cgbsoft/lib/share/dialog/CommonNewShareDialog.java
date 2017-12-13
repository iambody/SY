package com.cgbsoft.lib.share.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.share.bean.ShareCommonBean;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.TrackingDataManger;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.utils.tools.ViewHolders;
import com.cgbsoft.lib.widget.dialog.BaseDialog;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * desc  新的分享dialog样式
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/12/13-11:28
 */
public class CommonNewShareDialog extends BaseDialog implements PlatformActionListener, View.OnClickListener {
    /**
     * 微信分享标识
     */
    public static final int SHARE_WX = 101;
    /**
     * 微信朋友圈分享标识
     */
    public static final int SHARE_WXCIRCLE = 102;

    /**
     * 记载分享标识
     */
    private int tagStyle;
    /**
     * 上下文
     */
    private Context dcontext;
    /**
     * 控制window层
     */
    private Window dwindows;
    /**
     * 该种模式是消息资讯 up:联系人 微信 朋友圈 down：邮件 短信 复制链接
     */
    public static final int Tag_Style_Inf_ZiXun = 1;
    /**
     * 该种模式是 up:联系人 微信  邮件 短信 复制链接
     */
    public static final int Tag_Style_FundDetail = 2;
    /**
     * 该种模式是 up:  微信
     */
    public static final int Tag_Style_WeiXin = 3;
    /**
     * 是微信朋友圈
     */
    public static final int Tag_Style_WxPyq = 4;


    /**
     * 需要的分享bean
     *
     * @param
     */
    private ShareCommonBean commonShareBean;

    /**
     * 基础view
     */
    private View baseView;

    /**
     * 朋友圈分享对象
     */
    private Platform platform_circle;

    /**
     * 微信好友分享对象
     */
    private Platform platform_wx;

    /**
     * 预留的回调接口进行动态的需求
     */

    private ShareListener commentShareListener;
    private UserInfoDataEntity.UserInfo userInfo;


    public CommonNewShareDialog(Context context, int tag_Style, ShareCommonBean commonShareBean, ShareListener commentShareListener) {
        super(context, R.style.share_new_comment_style);
        dcontext = context;
        tagStyle = tag_Style;
        this.commonShareBean = commonShareBean;
        this.commentShareListener = commentShareListener;
        ShareSDK.initSDK(dcontext);
        userInfo = AppManager.getUserInfo(dcontext);
        try {
            TrackingDataManger.shareIn(dcontext, commonShareBean.getShareTitle());
        } catch (Exception e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseView = ViewHolders.ToView(dcontext, R.layout.dialog_common_new_share);
        setContentView(baseView);
        init();
    }

    private void init() {
        initBase();
    }

    private TextView sahre_wx_bt, sahre_circle_bt, sahre_cancle_bt;

    private void initBase() {
        //配置信息
        WindowManager.LayoutParams wparams = getWindow().getAttributes();
        wparams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(wparams);
        sahre_wx_bt = ViewHolders.get(baseView, R.id.sahre_wx_bt);
        sahre_circle_bt = ViewHolders.get(baseView, R.id.sahre_circle_bt);
        sahre_cancle_bt = ViewHolders.get(baseView, R.id.sahre_cancle_bt);
        sahre_wx_bt.setOnClickListener(this);
        sahre_circle_bt.setOnClickListener(this);
        sahre_cancle_bt.setOnClickListener(this);

        switch (tagStyle) {
            case SHARE_WX:
                sahre_circle_bt.setVisibility(View.GONE);
                break;
            case SHARE_WXCIRCLE:
                sahre_circle_bt.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 微信分享的初始化
     */
    private void weChatShare(ShareCommonBean WxShareData) {
        if (!Utils.isWeixinAvilible(dcontext)) {//没有安装微信
            PromptManager.ShowCustomToast(dcontext, dcontext.getResources().getString(R.string.pleaseinstanllweixin));
            return;
        }
        if (null == WxShareData) {
            PromptManager.ShowCustomToast(dcontext, dcontext.getResources().getString(R.string.data_error));
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
        if (!Utils.isWeixinAvilible(dcontext)) {//没有安装微信
            PromptManager.ShowCustomToast(dcontext, dcontext.getResources().getString(R.string.pleaseinstanllweixin));
            return;
        }
        if (null == WxShareData) {
            PromptManager.ShowCustomToast(dcontext, dcontext.getResources().getString(R.string.data_error));
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


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (null != commentShareListener) commentShareListener.completShare(tagStyle);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        if (null != commentShareListener) {
            commentShareListener.cancleShare();
        }
    }

    @Override
    public void onCancel(Platform platform, int i) {
        if (null != commentShareListener) {
            commentShareListener.cancleShare();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        try {
            closeShareSdk();
            TrackingDataManger.shareClose(dcontext);
        } catch (Exception e) {
        }
    }

    /**
     * 当关闭时候要关闭分享资源
     */
    private void closeShareSdk() {
        ShareSDK.stopSDK(dcontext);
    }

    @Override
    public void onClick(View v) {
        if (R.id.sahre_wx_bt == v.getId()) {//微信
            weChatShare(commonShareBean);
        } else if (R.id.sahre_circle_bt == v.getId()) {//朋友圈
            wxCircleShare(commonShareBean);
        } else if (R.id.sahre_cancle_bt == v.getId()) {//取消
            CommonNewShareDialog.this.dismiss();
        }
    }

    public interface ShareListener {
        //分享成功
        void completShare(int shareType);

        void cancleShare();
    }
}
