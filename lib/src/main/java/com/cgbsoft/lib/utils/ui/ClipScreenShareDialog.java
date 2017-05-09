//package com.cgbsoft.lib.utils.ui;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.util.TypedValue;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.WebView;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cgbsoft.lib.R;
//import com.cgbsoft.lib.widget.BaseDialog;
//
///**
// * TODO
// * TODO Created by xiaoyu.zhang on 2017/1/19 11:52
// * TODO Email:zhangxyfs@126.com
// *  
// */
//public class ClipScreenShareDialog extends BaseDialog {
//    private View view;
//    private TextView mWeixin;
//    private TextView penyouquan;
//    private ImageView closeImage;
//    private WebView targerView;
//    private ImageView shareImage;
//
//    public ClipScreenShareDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
//        super(context, cancelable, cancelListener);
//    }
//
//    public ClipScreenShareDialog(Context context) {
//        super(context, R.style.dialog_share_bg);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.clip_screen_dialog);
//        setCancelable(true);
//        bindViews();
//    }
//
//    public void setTargetView(WebView view) {
//        this.targerView = view;
//    }
//
//    private void bindViews() {
//        view = (View) findViewById(R.id.main_con);
//        mWeixin = (TextView) findViewById(R.id.weixin);
//        penyouquan = (TextView) findViewById(R.id.penyouquan);
//        closeImage = (ImageView) findViewById(R.id.close);
//        shareImage = (ImageView) findViewById(R.id.share_image);
//
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cancel();
//            }
//        });
//
//        mWeixin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ClipScreenShareDialog.this.dismiss();
//
//                if (!CheckApk.isWeixinAvilible(getContext())) {
//                    Toast.makeText(getContext(), "未安装微信", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                Bitmap bitmap = ToolsUtils.webviewCutScreen(targerView);
//                Bitmap bm = FileUtils.compressImage(bitmap);
//                ShareDialog shareDialog = new ShareDialog(getContext());
//                shareDialog.show();
//                shareDialog.cancel();
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                WeiXinShare share = new WeiXinShare(getContext(), "");
//                share.shareWeixinWithPic(bm);
//            }
//        });
//
//        Bitmap bitmap = ToolsUtils.webviewCutScreen(targerView);
//        ViewGroup.LayoutParams layoutParams = shareImage.getLayoutParams();
//        layoutParams.width = (int) ((float) bitmap.getWidth() / (float) bitmap.getHeight() * dp2px(340));
//        layoutParams.height = dp2px(340);
//        shareImage.setLayoutParams(layoutParams);
//        shareImage.setImageBitmap(bitmap);
//
////        shareImage.setImageResource(R.drawable.background_cebian);
//
//
//        penyouquan.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                ClipScreenShareDialog.this.dismiss();
//
//                if (!CheckApk.isWeixinAvilible(getContext())) {
//                    Toast.makeText(getContext(), "未安装微信", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                Bitmap bitmap = ToolsUtils.webviewCutScreen(targerView);
//                ShareDialog shareDialog = new ShareDialog(getContext());
//
//                Bitmap bm = FileUtils.compressImage(bitmap);
//
//                shareDialog.show();
//                shareDialog.cancel();
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                WeiXinShare share = new WeiXinShare(getContext(), "");
//                share.shareWeixinquanWithPic(bm);
//            }
//        });
//
//        closeImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//    }
//
//    private int dp2px(int dp) {
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
//                getContext().getResources().getDisplayMetrics());
//    }
//
//}
