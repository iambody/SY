package com.cgbsoft.lib.share.dialog;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.share.bean.ShareCommonBean;
import com.cgbsoft.lib.share.bean.ShareViewBean;
import com.cgbsoft.lib.share.utils.ShareUtils;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.utils.poster.ElevenPoster;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.TrackingDataManger;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.utils.tools.ViewHolders;
import com.cgbsoft.lib.widget.FlowLayoutView;
import com.cgbsoft.lib.widget.RoundImageView;
import com.mob.MobSDK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


public class CommonShareDialog extends Dialog implements PlatformActionListener, View.OnClickListener {

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
    private int share_Type;
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
     * 根据不同的Tag进行处理
     */
    private int tag_Style;


    /**
     * 没有用ButterKnife 所以下边会有一坨
     */

    private ImageView comment_share_dismiss_bt;
    /**
     * 平台认证的tag图标
     */
    private ImageView commont_share_card_auth_tag;

    /**
     * 选择按钮默认选中
     */
    private ImageView common_card_select_bt;
    /**
     * card名片的iv
     */
    private RoundImageView comment_share_card_iv;
    /**
     * card名片的名字
     */
    private TextView comment_share_card_name;
    /**
     * card的地址
     */
    private TextView comment_share_card_address;
    /**
     * card的等级地址
     */
    private TextView commont_share_card_level;
    /**
     * 默认进来是被选中状态
     */
    private Boolean isCardSelect = true;

    /**
     * 名片布局
     */
    private RelativeLayout comment_share_up_lay;
    /**
     * 流view
     */
    private FlowLayoutView comment_share_flowlayout;
    /**
     * 保存下分享的不带名片的url 和 带名片的url
     */
    private String UrlNoCode, UrlCode;


    /**
     * 有的短信和邮件需要后边仅仅的添加一个parms
     */
//    private String SMSNoCode, SMSCode;
//    private String EmailNoCode, EmailCode;
    /**
     * 是否符合
     */
    private boolean isCanShare;


    /**
     * 需要的分享bean
     *
     * @param
     */
    private ShareCommonBean commonShareBean;

    /**
     * 所有可能需要用到的view=>联系人，微信，朋友圈，邮件，短信，复制链接
     * Type=Tag_Style_Six   标识上边的全部由
     * Type=Tag_Style_Five  联系人，微信， 邮件，短信，复制链接
     * Type=Tag_Style_One   微信
     */
//    private View ViewLinkman, ViewWeChat, ViewMoment, ViewEmails, ViewSmS, ViewCopyLink;

    /**
     * 初始化bt标题的数组联系人，微信，朋友圈，邮件，短信，复制链接
     */
    private int[] btNamesLs = {R.string.lianxiren, R.string.weixin, R.string.pengyouquan, R.string.youjian, R.string.duanxin, R.string.fuzhilianjie};
    /**
     * 初始化bt的图标 联系人，微信，朋友圈，邮件，短信，复制链接
     */
    private int[] btResourceLs = {R.drawable.tuandui_share, R.drawable.wexin_share, R.drawable.share_moents, R.drawable.e_mail_share, R.drawable.message_share, R.drawable.fuzhilianjie_share};
    /**
     * 保存集合
     */
    private List<ShareViewBean> viewLs;
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

    private CommentShareListener commentShareListener;
    private UserInfoDataEntity.UserInfo userInfo;

    public CommonShareDialog(Context context, int tagStyle, ShareCommonBean commonShareBean, CommentShareListener commentShareListener) {
        super(context, R.style.share_comment_style);
        dcontext = context;
        tag_Style = tagStyle;
        this.commonShareBean = commonShareBean;
        this.commentShareListener = commentShareListener;
//        ShareSDK.initSDK(dcontext);
        MobSDK.init(dcontext);
        userInfo = AppManager.getUserInfo(dcontext);
//     根据需求分享理财是名片
        isCanShare = !BStrUtils.isEmpty(userInfo.realName) && userInfo.isAdvisers.endsWith("y") && AppManager.isAdViser(dcontext);
        iUrl(commonShareBean);
        try {
            TrackingDataManger.shareIn(dcontext, commonShareBean.getShareTitle());
        } catch (Exception e) {
        }
    }

    //配置url 进行选中未选择的替换
    private void iUrl(ShareCommonBean BBshare) {
        //分享微信
        UrlNoCode = BBshare.getShareUrl();
//      根据需求进行判断  UrlCode = BBshare.getUrl() + UserInfManger.GetCardUrlParms(true);
        //分享短信
//        if (!BStrUtils.isEmpty(BBshare.getDuanxinText())) {
//            SMSNoCode = BBshare.getDuanxinText();
//            SMSCode = BBshare.getDuanxinText().replace(UrlNoCode, UrlCode);
//        }
        //分享邮件
//        if (!BStrUtils.isEmpty(BBshare.getYoujianText())) {
//            EmailNoCode = BBshare.getYoujianText();
//            EmailCode = BBshare.getYoujianText().replace(UrlNoCode, UrlCode);
//        }
        if (isCanShare) //如果有真实姓名&&是理财师 直接所有url设置为 带card url的
            setBeanCardUrl(true);

    }

    /**
     * 根据选择状态进行处理 分享bean的url或者短信url的配置
     */
    public void setBeanCardUrl(boolean IsAddCard) {
        commonShareBean.setShareUrl(IsAddCard ? UrlCode : UrlNoCode);
//        commonShareBean.setDuanxinText(IsAddCard ? SMSCode : SMSNoCode);
//        commonShareBean.setYoujianText(IsAddCard ? EmailCode : EmailNoCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseView = ViewHolders.ToView(dcontext, R.layout.share_dialog_common_share);
        setContentView(baseView);
        init();
    }

    private void init() {
        initBase();
        initIView();
        initConfigView();
    }

    private void initConfigView() {
        for (ShareViewBean h : viewLs) {
            setAnimation(h.getShareView());
            comment_share_flowlayout.addView(h.getShareView());
            h.getShareView().setOnClickListener(new MyShareClick(h.getPostion()));
        }
        setAnimation(comment_share_dismiss_bt);
        SetDownToUpAnimation(comment_share_up_lay);
    }

    private void initIView() {
        comment_share_card_name = ViewHolders.get(baseView, R.id.comment_share_card_name);
        comment_share_card_address = ViewHolders.get(baseView, R.id.comment_share_card_address);
        commont_share_card_level = ViewHolders.get(baseView, R.id.commont_share_card_level);
        comment_share_card_iv = (RoundImageView) baseView.findViewById(R.id.comment_share_card_iv);
        common_card_select_bt = ViewHolders.get(baseView, R.id.common_card_select_bt);
        comment_share_up_lay = ViewHolders.get(baseView, R.id.comment_share_up_lay);
        comment_share_flowlayout = (FlowLayoutView) baseView.findViewById(R.id.comment_share_flowlayout);
        comment_share_dismiss_bt = ViewHolders.get(baseView, R.id.comment_share_dismiss_bt);
        commont_share_card_auth_tag = ViewHolders.get(baseView, R.id.commont_share_card_auth_tag);
        comment_share_dismiss_bt.setOnClickListener(CommonShareDialog.this);
        common_card_select_bt.setOnClickListener(this);
//        commont_share_card_auth_tag.setVisibility(UserInfManger.IsPlatformAuth() ? View.VISIBLE : View.INVISIBLE);
        comment_share_up_lay.setVisibility(isCanShare ? View.VISIBLE : View.INVISIBLE);

//        comment_share_card_address.setVisibility(BStrUtils.isEmpty(UserInfManger.GetWorkCity()) ? View.INVISIBLE : View.VISIBLE);
//        commont_share_card_level.setVisibility(BStrUtils.isEmpty(UserInfManger.GetLevelStr()) ? View.INVISIBLE : View.VISIBLE);
//        //平台认证
////
//        UiHelper.SetTxt(commont_share_card_level, UserInfManger.GetLevelStr());
//        UiHelper.SetTxt(comment_share_card_address, UserInfManger.GetWorkCity());
//        if (!BStrUtils.isEmpty(UserInfManger.GetRealName()))
//            UiHelper.SetTxt(comment_share_card_name, String.format("%s经理", UserInfManger.GetRealName().charAt(0)));


        setViews(tag_Style);
    }

    private void initBase() {
        //配置信息
        WindowManager.LayoutParams wparams = getWindow().getAttributes();
        wparams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(wparams);
        //开始初始化
        dwindows = getWindow();
        dwindows.setWindowAnimations(R.style.share_comment_anims_style);
    }

    /**
     * 根据条数进行处理
     *
     * @param TypeNumbs
     */

    private void setViews(int TypeNumbs) {
        viewLs = new ArrayList<>();
        switch (TypeNumbs) {
            case Tag_Style_Inf_ZiXun://联系人 微信 朋友圈 down：邮件 短信 复制链接
                viewLs.add(new ShareViewBean(0, getViewApPotion(0)));
                viewLs.add(new ShareViewBean(1, getViewApPotion(1)));
                viewLs.add(new ShareViewBean(2, getViewApPotion(2)));
                viewLs.add(new ShareViewBean(3, getViewApPotion(3)));
                viewLs.add(new ShareViewBean(4, getViewApPotion(4)));
                viewLs.add(new ShareViewBean(5, getViewApPotion(5)));
                break;

            case Tag_Style_FundDetail://联系人 微信  邮件 短信 复制链接
                viewLs.add(new ShareViewBean(0, getViewApPotion(0)));
                viewLs.add(new ShareViewBean(1, getViewApPotion(1)));


                viewLs.add(new ShareViewBean(4, getViewApPotion(4)));
                viewLs.add(new ShareViewBean(5, getViewApPotion(5)));
                viewLs.add(new ShareViewBean(3, getViewApPotion(3)));
                break;

            case Tag_Style_WeiXin://微信
                viewLs.add(new ShareViewBean(1, getViewApPotion(1)));
                break;
            case Tag_Style_WxPyq://微信 朋友圈
                viewLs.add(new ShareViewBean(1, getViewApPotion(1)));
                viewLs.add(new ShareViewBean(2, getViewApPotion(2)));
                break;

        }


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.comment_share_dismiss_bt) {
            CommonShareDialog.this.dismiss();
            if (null != commentShareListener) {
                commentShareListener.cancleShare();
            }
        }
    }


    class MyShareClick implements View.OnClickListener {
        private int PostionType;

        public MyShareClick(int postionType) {
            PostionType = postionType;
        }

        @Override
        public void onClick(View v) {
            switch (PostionType) {
                case 0://联系人
                    break;
                case 1://微信
                    share_Type = SHARE_WX;
                    weChatShare(commonShareBean);

                    break;
                case 2://朋友圈
                    share_Type = SHARE_WXCIRCLE;
                    wxCircleShare(commonShareBean);
//                    WxCircrImg(commonShareBean);
                    try {
                        TrackingDataManger.shareClickCricle(dcontext);
                    } catch (Exception e) {
                    }
                    break;
                case 3://邮件
                    break;
                case 4://短信
                    break;
                case 5://复制链接
                    break;
            }
            try {
                TrackingDataManger.shareClick(dcontext,1==PostionType?"微信":"朋友圈" );
            } catch (Exception e) {
            }
            CommonShareDialog.this.dismiss();
        }

    }


    //只需要出入第几个 就直接初始化一个view
    private View getViewApPotion(int Postion) {
        int Wdith = ShareUtils.GetWidhAndHeight(dcontext, 1);
        int NeedWdith = Wdith / 3;
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                NeedWdith, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, DimensionPixelUtil.dip2px(dcontext, 10), 0, DimensionPixelUtil.dip2px(dcontext, 10));
        LinearLayout tv = (LinearLayout) LayoutInflater.from(dcontext).inflate(
                R.layout.view_commont_bt, comment_share_flowlayout, false);
        tv.setLayoutParams(lp);
        ImageView Iv = ViewHolders.get(tv, R.id.view_comment_share_iv);
        TextView Textview = ViewHolders.get(tv, R.id.view_comment_share_txt);
        Iv.setImageResource(btResourceLs[Postion]);
        ShareUtils.setTxt(Textview, dcontext.getResources().getString(btNamesLs[Postion]));

        return tv;
    }

    /**
     * 当关闭时候要关闭分享资源
     */
    private void closeShareSdk() {
//        ShareSDK.stopSDK(dcontext);
    }

    /**
     * 微信分享的初始化
     */
    private void weChatShare(ShareCommonBean WxShareData) {
        if (!Utils.isWeixinAvilible(dcontext)) {//没有安装微信
            PromptManager.ShowCustomToast(dcontext, dcontext.getResources().getString(R.string.pleaseinstanllweixin));
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
     * 分享海报
     *
     * @param WxShareData
     */
    private void wxCircrImg(ShareCommonBean WxShareData) {
        platform_circle = ShareSDK.getPlatform(WechatMoments.NAME);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setImagePath(getPostPath());
        platform_circle.setPlatformActionListener(this); // 设置分享事件回调
        // 执行分享
        platform_circle.share(sp);
    }

    /**
     * 生成view的对于图片地址
     */
    private String getPostPath() {
//        View postView = LayoutInflater.from(dcontext).inflate(R.layout.testscrooll, null);
//        return  ElevenPoster.getViewPath(postView, "sss");
        ScrollView postView = (ScrollView) LayoutInflater.from(dcontext).inflate(R.layout.testscrooll, null);
        ElevenPoster.resetViewSize((Activity) dcontext, postView);
        return ElevenPoster.getScrollViewPath(postView, "sss");


    }

    /**
     * 微信分享成功
     *
     * @param platform
     * @param i
     * @param hashMap
     */
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (null != commentShareListener) commentShareListener.completShare(share_Type);
    }

    /**
     * 微信分享失败
     *
     * @param platform
     * @param i
     * @param throwable
     */
    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        if (null != commentShareListener) {
            commentShareListener.cancleShare();
        }
    }

    /**
     * 开启时名片的动画的set
     *
     * @param VV
     */
    private void SetDownToUpAnimation(RelativeLayout VV) {
        AnimatorSet animationSet = new AnimatorSet();
        ObjectAnimator Translate = ObjectAnimator.ofFloat(VV, "translationY", -1000f, 0f);
        ObjectAnimator Alpha = ObjectAnimator.ofFloat(VV, "alpha", 0f, 0.4f, 06f, 0.7f, 0.9f, 1f);
        Alpha.setDuration(1000);
        animationSet.playSequentially(Translate);
        animationSet.setDuration(1000);
        animationSet.start();
    }

    /**
     * 当dialog现实时候开启几个按钮的动画set
     *
     * @param VV
     */
    private void setAnimation(View VV) {
        AnimatorSet animationSet = new AnimatorSet();
        ObjectAnimator Translate = ObjectAnimator.ofFloat(VV, "translationY", 1000f, 0f);
        ObjectAnimator Alpha = ObjectAnimator.ofFloat(VV, "alpha", 0f, 0.4f, 06f, 0.7f, 0.9f, 1f);
        Alpha.setDuration(1000);
        animationSet.playSequentially(Translate);
        animationSet.setDuration(1000);
        animationSet.start();
    }


    /**
     * 当关闭时候开始消失动画的set
     *
     * @param VV
     */
    private void setDisissAnimation(View VV) {
        AnimatorSet animationSet = new AnimatorSet();
        ObjectAnimator Translate = ObjectAnimator.ofFloat(VV, "translationY", 1000f, 0f);
        ObjectAnimator Alpha = ObjectAnimator.ofFloat(VV, "alpha", 0f, 0.4f, 06f, 0.7f, 0.9f, 1f);
        Alpha.setDuration(1000);
        animationSet.playSequentially(Translate);
        animationSet.setDuration(1000);
        animationSet.start();
    }

    /**
     * 微信分享取消
     *
     * @param platform
     * @param i
     */
    @Override
    public void onCancel(Platform platform, int i) {
        if (null != commentShareListener) {
            commentShareListener.cancleShare();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (null != commentShareListener) {
//            commentShareListener.cancleShare();
        }
        try {
            closeShareSdk();
        } catch (Exception e) {
        }
        TrackingDataManger.shareClose(dcontext);
    }

    /**
     * 预留的回调接口哦
     * -1时候表示弹框消失了
     */
    public interface CommentShareListener {
        //分享成功
        void completShare(int shareType);

        void cancleShare();
    }
}
