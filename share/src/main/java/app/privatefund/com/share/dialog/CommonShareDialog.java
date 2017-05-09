package app.privatefund.com.share.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.utils.tools.ViewHolders;
import com.cgbsoft.lib.widget.FlowLayoutView;
import com.cgbsoft.privatefund.bean.share.CommonShareBean;

import java.util.HashMap;

import app.privatefund.com.share.R;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * desc
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/3/31-18:26
 */

public class CommonShareDialog extends Dialog implements PlatformActionListener {
    /**
     * 上下文
     */
    private Context Dcontext;
    /**
     * 控制window层
     */
    private Window Dwindows;
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
    public static final int Tag_Style_Vido_WeiXin = 3;
    /**
     * 根据不同的Tag进行处理
     */
    private int Tag_Style;


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
    private ImageView comment_share_card_iv;
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
    private Boolean IsCardSelect = true;

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
    private String SMSNoCode, SMSCode;
    private String EmailNoCode, EmailCode;
    /**
     * 是否符合
     */
    private boolean IsCanShare;


    /**
     * 需要的分享bean
     *
     * @param
     */
    private CommonShareBean commonShareBean;


    /**
     * 所有可能需要用到的view=>联系人，微信，朋友圈，邮件，短信，复制链接
     * Type=Tag_Style_Six   标识上边的全部由
     * Type=Tag_Style_Five  联系人，微信， 邮件，短信，复制链接
     * Type=Tag_Style_One   微信
     */
    private View ViewLinkman, ViewWeChat, ViewMoment, ViewEmails, ViewSmS, ViewCopyLink;

    /**
     * 初始化bt标题的数组联系人，微信，朋友圈，邮件，短信，复制链接
     */
    private int[] BtNamesLs = {R.string.lianxiren, R.string.weixin, R.string.pengyouquan, R.string.youjian, R.string.duanxin, R.string.fuzhilianjie};
    /**
     * 初始化bt的图标 联系人，微信，朋友圈，邮件，短信，复制链接
     */
    private int[] BtResourceLs = {R.drawable.tuandui_share, R.drawable.wexin_share, R.drawable.share_moents, R.drawable.e_mail_share, R.drawable.message_share, R.drawable.fuzhilianjie_share};

    /**
     * 基础view
     */
    private View BaseView;


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

    public CommonShareDialog(Context dcontext, int tag_Style, CommonShareBean commonShareBean, CommentShareListener commentShareListener) {
        super(dcontext, R.style.share_comment_style);
        Dcontext = dcontext;
        Tag_Style = tag_Style;
        this.commonShareBean = commonShareBean;
        this.commentShareListener = commentShareListener;
        ShareSDK.initSDK(Dcontext);
//     根据需求进行判断   IsCanShare=!BStrUtils.isEmpty(UserInfManger.GetRealName())&&UserInfManger.IsAdviser(MyContext);
//        IUrl(commonShareBean);
    }

    //配置url 进行选中未选择的替换
    private void IUrl(CommonShareBean BBshare) {
        //分享微信
        UrlNoCode = BBshare.getUrl();
//      根据需求进行判断  UrlCode = BBshare.getUrl() + UserInfManger.GetCardUrlParms(true);
        //分享短信
        if (!BStrUtils.isEmpty(BBshare.getDuanxinText())) {
            SMSNoCode = BBshare.getDuanxinText();
            SMSCode = BBshare.getDuanxinText().replace(UrlNoCode, UrlCode);
        }
        //分享邮件
        if (!BStrUtils.isEmpty(BBshare.getYoujianText())) {
            EmailNoCode = BBshare.getYoujianText();
            EmailCode = BBshare.getYoujianText().replace(UrlNoCode, UrlCode);
        }
        if (IsCanShare) //如果有真实姓名&&是理财师 直接所有url设置为 带card url的
            SetBeanCardUrl(true);


    }

    /**
     * 根据选择状态进行处理 分享bean的url或者短信url的配置
     */
    public void SetBeanCardUrl(boolean IsAddCard) {
        commonShareBean.setUrl(IsAddCard ? UrlCode : UrlNoCode);
        commonShareBean.setDuanxinText(IsAddCard ? SMSCode : SMSNoCode);
        commonShareBean.setYoujianText(IsAddCard ? EmailCode : EmailNoCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseView = ViewHolders.ToView(Dcontext, R.layout.share_dialog_common_share);
        setContentView(BaseView);
        InitBase();
    }

    private void InitBase() {
        IBase();
    }

    private void IBase() {
        //配置信息
        WindowManager.LayoutParams wparams = getWindow().getAttributes();
        wparams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(wparams);
        //开始初始化
        Dwindows = getWindow();
        Dwindows.setWindowAnimations(R.style.share_comment_anims_style);
    }

    /**
     * 当关闭时候要关闭分享资源
     */
    private void CloseShareSdk() {
        ShareSDK.stopSDK(Dcontext);
    }

    /**
     * 微信分享的初始化
     */
    private void WeChatShare(CommonShareBean WxShareData) {
        if (!Utils.isWeixinAvilible(Dcontext)) {//没有安装微信
            PromptManager.ShowCustomToast(Dcontext, Dcontext.getResources().getString(R.string.pleaseinstanllweixin));
            return;
        }
        platform_wx = ShareSDK.getPlatform(Wechat.NAME);
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(WxShareData.getTitle());
        sp.setText(WxShareData.getContent());
        sp.setUrl(WxShareData.getUrl());
        sp.setImageData(null);
        sp.setImageUrl(WxShareData.getImageUrl());
        sp.setImagePath(null);

        platform_wx.setPlatformActionListener(this); // 设置分享事件回调
        // 执行分享
        platform_wx.share(sp);
    }

    /**
     * 朋友圈分享
     */
    private void WxCircleShare(CommonShareBean WxShareData) {
        if (!Utils.isWeixinAvilible(Dcontext)) {//没有安装微信
            PromptManager.ShowCustomToast(Dcontext, Dcontext.getResources().getString(R.string.pleaseinstanllweixin));
            return;
        }
        platform_circle = ShareSDK.getPlatform(WechatMoments.NAME);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(WxShareData.getTitle());
        sp.setText(WxShareData.getContent());
        sp.setUrl(WxShareData.getUrl());
        sp.setImageData(null);
        sp.setImageUrl(WxShareData.getImageUrl());
        sp.setImagePath(null);
        platform_circle.setPlatformActionListener(this); // 设置分享事件回调
        // 执行分享
        platform_circle.share(sp);
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

    }

    /**
     * 微信分享取消
     *
     * @param platform
     * @param i
     */
    @Override
    public void onCancel(Platform platform, int i) {

    }

    @Override
    public void dismiss() {
        super.dismiss();

        try {
            CloseShareSdk();
        } catch (Exception e) {
        }
    }

    /**
     * 预留的回调接口哦
     */
    public interface CommentShareListener {
        void onclick();
    }
}
