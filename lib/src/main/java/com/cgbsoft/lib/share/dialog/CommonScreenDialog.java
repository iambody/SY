package com.cgbsoft.lib.share.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.lib.utils.tools.ViewHolders;

import java.io.File;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/10/27-10:45
 */
public class CommonScreenDialog extends Dialog implements PlatformActionListener, View.OnClickListener {
    /**
     * 上下文
     */
    private Context dcontext;
    /**
     * 基础view
     */
    private View BaseView;
    /**
     * 控制window层
     */
    private Window dwindows;
    /**
     * 朋友圈分享对象
     */
    private Platform platform_circle;

    /**
     * 微信好友分享对象
     */
    private Platform platform_wx;

    //图片地址
    private Bitmap imagLocalPath;
    //展示的截图
    private ImageView dialog_screen_iv;
    //取消的按钮
    private ImageView dialog_screen_cancle_iv;
    //微信分享
    private ImageView dialog_screen_wx_iv;
    //朋友圈分享
    private ImageView dialog_screen_pyq_iv;

    /**
     * 预留的回调接口进行动态的需求
     */

    private CommentScreenListener commentScreenListener;

    public CommonScreenDialog(@NonNull Context context, Bitmap imagPath, CommentScreenListener screenListener) {
        super(context, R.style.share_comment_style);
        this.dcontext = context;
        this.imagLocalPath = imagPath;
        this.commentScreenListener = screenListener;
//        ShareSDK.initSDK(dcontext);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseView = ViewHolders.ToView(dcontext, R.layout.share_dialog_common_screen);
        setContentView(BaseView);
        init();
    }

    private void init() {
        //配置信息
        WindowManager.LayoutParams wparams = getWindow().getAttributes();
        wparams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        wparams.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(wparams);
        //开始初始化
        dwindows = getWindow();
        dwindows.setWindowAnimations(R.style.share_comment_anims_style);


        dialog_screen_iv = ViewHolders.get(BaseView, R.id.dialog_screen_iv);
        dialog_screen_cancle_iv = ViewHolders.get(BaseView, R.id.dialog_screen_cancle_iv);
        dialog_screen_wx_iv = ViewHolders.get(BaseView, R.id.dialog_screen_wx_iv);
        dialog_screen_pyq_iv = ViewHolders.get(BaseView, R.id.dialog_screen_pyq_iv);
        dialog_screen_cancle_iv.setOnClickListener(this);
        dialog_screen_wx_iv.setOnClickListener(this);
        dialog_screen_pyq_iv.setOnClickListener(this);
//        dialog_screen_iv.setImageBitmap(getDiskBitmap(imagLocalPath));
        dialog_screen_iv.setImageBitmap(imagLocalPath);
    }

    @Override
    public void onClick(View v) {
        if (R.id.dialog_screen_cancle_iv == v.getId()) {//点击取消
            this.dismiss();
        } else if (R.id.dialog_screen_wx_iv == v.getId()) {//微信分享
            wxImg(imagLocalPath);

        } else if (R.id.dialog_screen_pyq_iv == v.getId()) {//朋友圈分享
            wxCircrImg(imagLocalPath);
        }


    }

    /**
     * 分享到好友
     */
    private void wxImg(Bitmap WxShareData) {
        if (!Utils.isWeixinAvilible(dcontext)) {//没有安装微信
            PromptManager.ShowCustomToast(dcontext, dcontext.getResources().getString(R.string.pleaseinstanllweixin));
            return;
        }
        platform_circle = ShareSDK.getPlatform(Wechat.NAME);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
//        sp.setImagePath(WxShareData);
        sp.setImageData(WxShareData);
        platform_circle.setPlatformActionListener(this); // 设置分享事件回调
        // 执行分享
        platform_circle.share(sp);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (null != imagLocalPath) imagLocalPath.recycle();
    }

    /**
     * 分享到朋友圈
     */
    private void wxCircrImg(Bitmap WxShareData) {
        if (!Utils.isWeixinAvilible(dcontext)) {//没有安装微信
            PromptManager.ShowCustomToast(dcontext, dcontext.getResources().getString(R.string.pleaseinstanllweixin));
            return;
        }
        platform_circle = ShareSDK.getPlatform(WechatMoments.NAME);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
//        sp.setImagePath(WxShareData);
        sp.setImageData(WxShareData);
        platform_circle.setPlatformActionListener(this); // 设置分享事件回调
        // 执行分享
        platform_circle.share(sp);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (null != commentScreenListener) commentScreenListener.completShare();
        CommonScreenDialog.this.dismiss();

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        if (null != commentScreenListener) commentScreenListener.cancleShare();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        if (null != commentScreenListener) commentScreenListener.cancleShare();
    }


    /**
     * 预留的回调接口哦
     * -1时候表示弹框消失了
     */
    public interface CommentScreenListener {
        //分享成功
        void completShare();

        void cancleShare();
    }


    private Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }


        return bitmap;
    }
}
