package app.privatefund.com.vido.mvc.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.cgbsoft.lib.utils.tools.SDCardUtil;
import com.cgbsoft.lib.widget.MToast;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.mvc.Contant;
import app.privatefund.com.vido.mvc.PlayVideoActivity;
import app.privatefund.com.vido.mvc.bean.SchoolVideo;
import app.privatefund.com.vido.mvc.services.DownloadService;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/15-10:08
 */
public class ToolsUtils {

    private static final String TAG = ToolsUtils.class.getSimpleName();
    private static final int ARRAY_LEN = 500;
    public static HttpHandler handler;
    private static ToolsUtils instance = null;

    //进入视频播放页面
    public static void toPlayVideoActivity(Context context, SchoolVideo schoolVideo) {
        try {
            Intent intent = new Intent(context, PlayVideoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("schoolvideo", schoolVideo);
//            intent.putExtras(bundle);
            intent.putExtra("videoId", schoolVideo.getVideoId());
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //进入视频播放页面
    public static void toPlayVideoActivity(Context context, String videoId) {
        try {
            Intent intent = new Intent(context, PlayVideoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("schoolvideo", schoolVideo);
//            intent.putExtras(bundle);
            intent.putExtra("videoId", videoId);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    //下载视频线程
//    public static HttpHandler downloadFile(final Context context, final String fileurl, final String filesavapath,
//                                           final DatabaseUtils dbUtils, final SchoolVideo video, final int downloadtype) {
//        final long startTime = System.currentTimeMillis();
//        final long alreadySize = (long) video.getPercent() * video.getSize() / 100;
//        HttpUtils http = new HttpUtils();
//        handler = http.download(fileurl, filesavapath, true, false,
//                new RequestCallBack<File>() {
//                    public void onStart() {
//                        Log.i("downloadFile", "fileurl=" + fileurl + ",filesavapath=" + filesavapath);
//                    }
//
//                    public void onSuccess(ResponseInfo<File> responseInfo) {
//                        try {
//                            Log.i("downloadFile", "ok");
//                            handler.cancel();
//                            handler = null;
//                            video.setStatus(2);
//                            boolean ret = ToolsUtils.encryptFile(video.getTencentVideoId(), downloadtype);
//                            if (ret) {
//                                video.setEncrypt(2);
//                            } else {
//                                video.setEncrypt(1);
//                            }
//                            dbUtils.saveSchoolVideo(video);
//                            sendPercentBroadcast(context, 0, 0, video.getTencentVideoId(), downloadtype, 0, true);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    public void onFailure(HttpException error, String msg) {
//                        try {
//                            Log.i("downloadFile", "onFailure=" + msg);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    //可以实时更新下载进度
//                    public void onLoading(long total, long current, boolean isUploading) {
//                        super.onLoading(total, current, isUploading);
//                        try {
//                            long currentTime = System.currentTimeMillis();
////                            Log.i("downloadFile", "xf time=" + (currentTime - startTime) + ",size=" + current / 1024 + ",current=" + current + ",alreadySize=" + alreadySize);
//                            float speed = 0;
//                            if (current > 0) {
//                                float second = (currentTime - startTime) / 1000;
////                                speed = current / 1024 / second;
//                                if (second > 0) {
//                                    speed = (current - alreadySize) / 1024 / second;
//                                }
//                                Log.i("downloadFile", "second=" + second + ",speed=" + speed + ",id=" + video.getTencentVideoId());
//                            }
//
//                            video.setSize(total);
//                            video.setPercent(current * 100 / total);
//                            video.setStatus(1);
//                            video.setDownloadtype(downloadtype);
//                            dbUtils.saveSchoolVideo(video);
//
//                            sendPercentBroadcast(context, total, current, video.getTencentVideoId(), downloadtype, speed, false);
//                            String pross = current * 100 / total + "%";//计算下载进度
//                            Log.i("downloadFile", pross + ",speed=" + speed + ",currentTime=" + currentTime + ",startTime=" + startTime + ",current=" + current);
//
//                            if (ToolsUtils.getWifiStatus(context) == Contant.NET_STAUS_MOBILE) {
//                                new MToast(context).showLoginFailure("当前是手机上网环境，为节省您手机流量，将暂停视频下载！");
//                                Log.i("downloadFile", "非wifi环境停止下载！");
//                                handler.cancel();
//                                handler = null;
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//        );
//        return handler;
//    }

    //下载进度广播
    private static void sendPercentBroadcast(Context context, long total, long current,
                                             String tencentVideoId, int downloadtype, float speed, boolean complete) {
        Intent intent = new Intent();
        Log.i(TAG, "sendPercentBroadcast total=" + total);
        intent.putExtra("total", total);
        intent.putExtra("current", current);
        intent.putExtra("tencentVideoId", tencentVideoId);
        intent.putExtra("downloadtype", downloadtype);
        intent.putExtra("speed", speed);
        intent.putExtra("complete", complete);
        intent.setAction(Contant.ACTION_PERCENT);
        context.sendBroadcast(intent);
    }

    //获取下载文件 downloadtype: 0 表示高清 1 标清
    public static String getDownloadFilePath(String tencentVideoId, int downloadtype) {
        String fileSavePath = SDCardUtil.getSDCardVideoDir() + tencentVideoId + "_" + downloadtype + ".cache";
        return fileSavePath;
    }

    public static String getDownloadFileTempPath(String tencentVideoId, int downloadtype) {
        String tempPath = SDCardUtil.getSDCardVideoDir() + tencentVideoId + "_" + downloadtype + ".tp";
        return tempPath;
    }

    public static boolean encryptFile(String tencentVideoId, int downloadtype) {
        boolean ret = false;
        try {
            String fileSavePath = ToolsUtils.getDownloadFilePath(tencentVideoId, downloadtype);
            String tempPath = getDownloadFileTempPath(tencentVideoId, downloadtype);
            File f = new File(fileSavePath);

            if (f.exists()) {
                RandomAccessFile file = new RandomAccessFile(fileSavePath, "rw");
                int count = 0;
                byte[] b = new byte[ARRAY_LEN];
                int t;
                while (((t = file.read()) != -1) && count < ARRAY_LEN) {
                    b[count] = (byte) t;
                    count++;
                }

                RandomAccessFile tempFile = new RandomAccessFile(tempPath, "rw");
                tempFile.write(b);
                tempFile.close();

                count = 0;
                file.seek(0);
                while (count < ARRAY_LEN) {
                    file.write(97);
                    count++;
                    file.seek(count);
                }

                file.close();
            }
            ret = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    public static boolean decryptFile(String tencentVideoId, int downloadtype) {
        boolean ret = false;
        try {
            byte[] b = new byte[ARRAY_LEN];
            String fileSavePath = ToolsUtils.getDownloadFilePath(tencentVideoId, downloadtype);
            String tempPath = getDownloadFileTempPath(tencentVideoId, downloadtype);

            File tempFile = new File(tempPath);
            if (tempFile.exists()) {
                RandomAccessFile tempRandomFile = new RandomAccessFile(tempPath, "rw");
                tempRandomFile.read(b);
                tempRandomFile.close();

                File saveFile = new File(fileSavePath);
                if (saveFile.exists()) {
                    RandomAccessFile file = new RandomAccessFile(saveFile, "rw");
                    int count = 0;
                    int t;

                    file.seek(0);
                    while (count < ARRAY_LEN) {
                        file.write(b[count]);
                        count++;
                        file.seek(count);
                    }
                    file.close();
                    ret = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    //下载服务
    public static void toDownloadService(Context context, String tencentVideoId, int downloadtype) {
        //0 表示高清 1 标清
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(Contant.ACTION_DOWNLOAD);
        intent.putExtra("downloadtype", downloadtype);
        intent.putExtra("tencentVideoId", tencentVideoId);
        context.startService(intent);
    }

    //删除下载文件
    public static void deleteDownloadFile(String tencentVideoId, int downloadtype) {
        try {
            String filePath = getDownloadFilePath(tencentVideoId, downloadtype);
            String tempfilePath = getDownloadFileTempPath(tencentVideoId, downloadtype);
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }

            file = new File(tempfilePath);
            if (file.exists()) {
                file.delete();
            }
            file = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取wifi状态
    public static int getWifiStatus(Context context) {

        int ret = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(null==connectivityManager)return Contant.NET_STAUS_MOBILE;
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {   //无网络
            ret = Contant.NET_STAUS_NOT;
        } else if (wifiNetInfo.isConnected()) {   //wifi
            ret = Contant.NET_STAUS_WIFI;
        } else if (!wifiNetInfo.isConnected()) {   //手机流量
            ret = Contant.NET_STAUS_MOBILE;
        }
        return ret;
    }

    //停止后台播放视频服务
    public static void serviceStop(Context context) {
        Intent intent = new Intent();
        intent.setAction(Contant.ACTION_PLAY);
        context.stopService(intent);
    }

    public static ToolsUtils getInstance() {
        if (instance == null) {
            instance = new ToolsUtils();
        }
        return instance;
    }

    public static void telHotline(final Context context) {
        //todo  删除屏蔽了
//        new IOSDialog(context, "", "拨打电话：" + context.getString(R.string.hotline), "取消", "确定") {
//
//            public void left() {
//                this.cancel();
//            }
//
//            public void right() {
//                Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + context.getString(R.string.hotline)));
//                context.startActivity(phoneIntent);
//                this.cancel();
//            }
//        }.show();
    }

    public class HotlineClickableSpan extends ClickableSpan {

        String string;
        Context context;

        public HotlineClickableSpan(String str, Context context) {
            super();
            this.string = str;
            this.context = context;
        }

        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.RED);
            ds.setFlags(Paint.UNDERLINE_TEXT_FLAG);
        }

        public void onClick(View widget) {
            ToolsUtils.telHotline(context);
        }
    }

    //认证拨打热线
    public void setHotlineTel(Context context, TextView textview, String content, String endStr) {
        String contentstr = content;
        String hotline = context.getString(R.string.hotline);
        SpannableString spanhotline = new SpannableString(hotline);
        ClickableSpan clickhotline = new HotlineClickableSpan(hotline, context);
        spanhotline.setSpan(clickhotline, 0, hotline.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textview.setText(contentstr);
        textview.append(spanhotline);
        textview.append(endStr);
        textview.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //跳转主页
    public static void toMainActivity(Context context) {
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.putExtra("jumpId", 0);
//        context.startActivity(intent);
    }

    public static void setTitleRightHotline(Context context, TextView textview) {
        textview.setVisibility(View.VISIBLE);
        textview.setBackgroundResource(R.drawable.hotline);
    }

    public static boolean fileCopy(String sourcepath, String destpath) {
        boolean ret = false;
        try {
            byte[] buffer = new byte[1024];
            File sourcefile = new File(sourcepath);
            File destfile = new File(destpath);
            if (sourcefile.exists()) {
                FileInputStream in = new FileInputStream(sourcefile);
                FileOutputStream out = new FileOutputStream(destfile);
                while (true) {
                    int ins = in.read(buffer);
                    if (ins == -1) {
                        in.close();
                        out.flush();
                        out.close();
                        ret = true;
                    } else {
                        out.write(buffer, 0, ins);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    public static void showToast(Context context, String error) {
        try {
            JSONObject jsonObject = new JSONObject(error);
            if (null != jsonObject && null != jsonObject.optString("message")) {
                new MToast(context).show(jsonObject.getString("message").toString(), 0);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断下载视频是否已经被删除
    public static SchoolVideo isFileExist(Context context, SchoolVideo video) {
        //TODO 删除屏蔽
//        DatabaseUtils dbUtils;
//        try {
//            dbUtils = new DatabaseUtils(context);
//            String tencentVideoId = video.getTencentVideoId();
//            int downloadtype = video.getDownloadtype();
//            String filePath = getDownloadFilePath(tencentVideoId, downloadtype);
//            File file = new File(filePath);
//            if (!file.exists()) {
//                video.setStatus(0);
//                video.setSize(0);
//                video.setPercent(0);
//                dbUtils.saveSchoolVideo(video);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return video;
    }

    //取消下载
    public static boolean setCancelHttpHandler() {
        boolean ret = false;
        if (null != handler) {
            if (!handler.isCancelled()) {
                handler.cancel();
                ret = true;
                handler = null;
            }
        }
        return ret;
    }

    //判断文件是否存在
    public static boolean isFileExist(String filePath) {
        boolean ret = true;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                ret = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 截取webView快照(webView加载的整个内容的显示部分大小)
     *
     * @param webView
     * @return
     */
    public static Bitmap webviewCutScreen(WebView webView) {
        Picture picture = webView.capturePicture();
        Bitmap b = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        picture.draw(c);
        return b;
    }

}
