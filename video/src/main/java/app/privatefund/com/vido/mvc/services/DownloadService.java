package app.privatefund.com.vido.mvc.services;

import android.app.Service;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cgbsoft.lib.utils.tools.SDCardUtil;
import com.cgbsoft.lib.widget.MToast;
import com.lidroid.xutils.http.HttpHandler;

import app.privatefund.com.vido.mvc.Contant;
import app.privatefund.com.vido.mvc.bean.SchoolVideo;
import app.privatefund.com.vido.mvc.utils.ToolsUtils;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/15-10:29
 */
public class DownloadService extends Service {

        private static final String TAG = DownloadService.class.getSimpleName();
        private String tencentVideoId;
//        private DatabaseUtils dbUtils;
        private SchoolVideo video;
        private HttpHandler handler;

        public int onStartCommand(Intent intent, int flags, int startid) {

            Log.i(TAG, "xf onStartCommand" );
            try {
                if (intent.getAction().equals(Contant.ACTION_DOWNLOAD)) {
                    tencentVideoId = intent.getStringExtra("tencentVideoId");
                    int downloadtype = intent.getIntExtra("downloadtype", 0); //0 表示高清 1 标清
                    if (null != tencentVideoId){

                        long freeSize = SDCardUtil.getSDCardFreeSize();
                        if (freeSize > 500) {  //判断剩余空间超过500M
//todo  删除屏蔽了
//                            video = dbUtils.getSchoolVideo(tencentVideoId);
                            if (video != null) {
                                if (video.getStatus() == 0 || video.getStatus() == 1) {
                                    String downloadUrl;
                                    if (downloadtype == 0) {
                                        downloadUrl = video.getHDVideoUrl();
                                    } else {
                                        downloadUrl = video.getSDVideoUrl();
                                    }

                                    Log.i(TAG, "onStartCommand handler=" + handler);
                                    handler = ToolsUtils.getInstance().handler;

                                    //判读网络环境，4G网络不下载，暂停
//                                if(ToolsUtils.getWifiStatus(this) == Contant.NET_STAUS_WIFI) {
                                    if (null == handler) {
                                        video.setStatus(1);  //下载中
                                        //todo  删除屏蔽了
//                                        dbUtils.saveSchoolVideo(video);
                                        String fileSavePath = ToolsUtils.getDownloadFilePath(tencentVideoId, downloadtype);
                                        video.setDownloadTime(System.currentTimeMillis());
                                        //todo  删除屏蔽了
//                                        handler = ToolsUtils.getInstance().downloadFile(this, downloadUrl, fileSavePath, dbUtils, video, downloadtype);
//                                        sendCacheBroadcast(this, true);
                                    }
//                                }else{
//                                    new MToast(this).showLoginFailure("已添加到缓存队列，非wifi环境下不自动下载");
//                                }

                                }
                            }
                        }else{
                            //todo  删除屏蔽了
//                            new MToast(this).showLoginFailure(this.getString(R.string.local_video_lack_free_space));

                        }
                    }

                    Log.i(TAG, "onStartCommand tencentVideoId=" + tencentVideoId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return START_STICKY;
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        public void onDestroy() {
            Log.i(TAG, "onDestroy destroy");
            if(null != handler){
                handler.cancel();
            }
            System.gc();
        }

        public void onCreate() {
            super.onCreate();
            //todo  删除屏蔽了
//            dbUtils = new DatabaseUtils(this);
            Log.i(TAG, "DownloadService onCreate！！！");
        }

        private void toStopService(){
            stopSelf();
            Log.i(TAG, "setOnClickListener");
        }

        //通知界面缓存字段修改
//    private static void sendCacheBroadcast(Context context,boolean cacheflag) {
//        Intent intent = new Intent();
//        Log.i(TAG, "sendPercentBroadcast cacheflag=" + cacheflag);
//        intent.putExtra("cacheflag", cacheflag);
//        intent.setAction(Contant.ACTION_CACHE);
//        context.sendBroadcast(intent);
//    }

}
