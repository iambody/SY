package app.privatefund.com.vido.mvc;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/15-10:32
 */
public class Contant {
    //网络状态
    public static final int NET_STAUS_NOT = 0;
    public static final int NET_STAUS_WIFI = 1;
    public static final int NET_STAUS_MOBILE = 2;


    public static final int RECEIVER_SEND_CODE = 99;
    public static final int RECEIVER_SEND_CODE_NEW_INFO = 100;
    public static final String VIDEO_BROADCAST = "com.cgbsoft.privatefund.service.video.broadcast";  //视频播放时点击了Home键
    public static final String PHONE_BROADCAST = "com.cgbsoft.privatefund.service.phone.broadcast";  //视频播放时电话接入
    public static final String ACTION_PLAY = "com.cgbsoft.privatefund.service.player";               //后台播放
    public static final String series = "series";
    public static final String ACTION_DOWNLOAD = "com.cgbsoft.privatefund.service.download";         //后台下载
    public static final String ACTION_PERCENT = "com.cgbsoft.privatefund.service.percent";           //后台下载进度
    public static final String ACTION_CACHE = "com.cgbsoft.privatefund.service.cache";
}
