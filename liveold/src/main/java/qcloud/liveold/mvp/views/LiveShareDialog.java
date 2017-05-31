package qcloud.liveold.mvp.views;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgbsoft.lib.widget.dialog.BaseDialog;

import qcloud.liveold.R;

/**
 * desc
 * Created by yangzonghui on 2017/5/26 16:34
 * Email:yangzonghui@simuyun.com
 *  
 */
public class LiveShareDialog extends BaseDialog implements View.OnClickListener {

    private TextView shareWX;
    private TextView shareFriend;
    private Context context;
    private String shareUrl;
    private String shareTitle;
    private String shareContent;
    private String shareImg;
    private LinearLayout liveShareHide;

    public LiveShareDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public LiveShareDialog(Context context, int theme) {
        super(context, theme);
    }

    public LiveShareDialog(Context context) {
        this(context, R.style.dialog_baobei);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_share_dialog);
        bindView();
        // 添加微信平台
//        UMWXHandler wxHandler = new UMWXHandler(getContext(),
//                Contant.weixin_appID, Contant.weixin_appSecret);
//        wxHandler.addToSocialSDK();
    }

    /**
     * 绑定Id
     */
    private void bindView() {
        shareWX = (TextView) findViewById(R.id.share_weixin);
        shareFriend = (TextView) findViewById(R.id.share_friend);
        liveShareHide = (LinearLayout) findViewById(R.id.live_share_layout);
        liveShareHide.setOnClickListener(this);
        shareWX.setOnClickListener(this);
        shareFriend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.share_weixin) {//                if (!CheckApk.isWeixinAvilible(getContext())) {
//                    Toast.makeText(getContext(), "未安装微信", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                shareWeixin(shareTitle, shareContent, shareImg, shareUrl);

        } else if (i == R.id.share_friend) {//                if (!CheckApk.isWeixinAvilible(getContext())) {
//                    Toast.makeText(getContext(), "未安装微信", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                sharePengyouquan(shareTitle, shareContent, shareImg, shareUrl);

        } else if (i == R.id.live_share_layout) {
            this.dismiss();


        } else {
        }
    }

    /**
     * 分享到朋友圈
     *
     * @param shareUrl     分享地址
     * @param shareTitle   分享标题
     * @param shareContent 分享内容描述
     * @param shareImg     分享头像
     */
    private void sharePengyouquan(final String shareTitle, final String shareContent, final String shareImg, final String shareUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                WeiXinShare sh = new WeiXinShare(getContext(), "");
//                ImageFactory imageFactory = new ImageFactory();
//                Bitmap bitmap = imageFactory.urlToBitmap(shareImg);
//                //分享
//                if (bitmap != null) {
//                    sh.shareWeixinQuanBitmap(shareTitle, shareContent, shareUrl, bitmap);
//                } else {
//                    sh.shareWeixinQuanID(shareTitle, shareContent, shareUrl, R.drawable.logoshare);
//                }
            }
        }
        ).start();

        this.dismiss();
    }

    /**
     * 分享到微信
     *
     * @param shareUrl     分享地址
     * @param shareTitle   分享标题
     * @param shareContent 分享内容描述
     * @param shareImg     分享头像
     */
    private void shareWeixin(final String shareTitle, final String shareContent, final String shareImg, final String shareUrl) {
        //将头像地址转换为Bitmap
        new Thread(new Runnable() {
            @Override
            public void run() {
//                WeiXinShare sh = new WeiXinShare(getContext(), "");
//                ImageFactory imageFactory = new ImageFactory();
//                Bitmap bitmap = imageFactory.urlToBitmap(shareImg);
//                //分享
//                if (bitmap != null) {
//                    sh.shareWeixinWithBitmap(shareTitle, shareContent, shareUrl, bitmap);
//                } else {
//                    sh.shareWeixinWithID(shareTitle, shareContent, shareUrl, R.drawable.logoshare);
//                }
            }
        }
        ).start();
        this.dismiss();
    }


    /**
     * 设置数据
     *
     * @param context      上下文
     * @param shareUrl     分享地址
     * @param shareTitle   分享标题
     * @param shareContent 分享内容描述
     * @param shareImg     分享头像
     */
    public void setData(Context context, String shareUrl, String shareTitle, String shareContent, String shareImg) {
        this.context = context;
        this.shareUrl = shareUrl;
        this.shareTitle = shareTitle;
        this.shareContent = shareContent;
        this.shareImg = shareImg + "?x-oss-process=style/avata";
//        Toast.makeText(context,shareUrl,Toast.LENGTH_LONG).show();
    }
}
