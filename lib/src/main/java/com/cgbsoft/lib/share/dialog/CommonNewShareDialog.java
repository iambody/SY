package com.cgbsoft.lib.share.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.base.model.UserInfoDataEntity;
import com.cgbsoft.lib.share.bean.ShareCommonBean;
import com.cgbsoft.lib.share.bean.ShareViewBean;
import com.cgbsoft.lib.utils.tools.TrackingDataManger;
import com.cgbsoft.lib.widget.FlowLayoutView;
import com.cgbsoft.lib.widget.RoundImageView;
import com.cgbsoft.lib.widget.dialog.BaseDialog;

import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

/**
 * desc  新的分享dialog样式
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/12/13-11:28
 */
public class CommonNewShareDialog extends BaseDialog {
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
    private String urlNoCode, urlCode;


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

    private CommonShareDialog.CommentShareListener commentShareListener;
    private UserInfoDataEntity.UserInfo userInfo;

    public CommonNewShareDialog(Context context, int theme) {
        super(context, theme);
    }

    public CommonNewShareDialog(Context context) {
        super(context);
    }
    public CommonNewShareDialog(Context context, int tag_Style, ShareCommonBean commonShareBean, CommonShareDialog.CommentShareListener commentShareListener) {
        super(context, R.style.share_comment_style);
        dcontext = context;
        Tag_Style = tag_Style;
        this.commonShareBean = commonShareBean;
        this.commentShareListener = commentShareListener;
        ShareSDK.initSDK(dcontext);
        userInfo = AppManager.getUserInfo(dcontext);

        try {
            TrackingDataManger.shareIn(dcontext, commonShareBean.getShareTitle());
        } catch (Exception e) {
        }
    }
}
