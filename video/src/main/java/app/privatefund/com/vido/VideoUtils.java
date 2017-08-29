package app.privatefund.com.vido;

import android.graphics.Bitmap;

import com.cgbsoft.lib.utils.rxjava.RxSubscriber;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/23-17:01
 */
public class VideoUtils {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String getDeadLine(String data) {
        java.util.Date end_time = null;
        String dateString = "已结束";

        try {
            end_time = dateFormat.parse(data);
            long l = end_time.getTime() - System.currentTimeMillis();
            int day = (int) (l / 1000 / 60 / 60 / 24);
            int hour = (int) (l / 1000 % (3600 * 24) / 60 / 60);
            int min = (int) (l / 1000 % 3600 / 60);

            if (day > 0) {
                if (hour % 24 > 17) {
                    dateString = (day + 1) + "天";
                } else {
                    dateString = day + "天";
                }
            } else if (hour > 0) {
                dateString = hour + "小时";
            } else if (min >= 0) {
                dateString = min + "分";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    void getPicShow(List<File> files) {
        Observable.from(files).flatMap(new Func1<File, Observable<File>>() {
            @Override
            public Observable<File> call(File file) {
                return Observable.from(file.listFiles());
            }
        }).filter(new Func1<File, Boolean>() {
            @Override
            public Boolean call(File file) {
                return file.getName().endsWith(".png");
            }
        }).map(new Func1<File, Bitmap>() {
            @Override
            public Bitmap call(File file) {
                return null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new RxSubscriber<Bitmap>() {
            @Override
            protected void onEvent(Bitmap bitmap) {

            }

            @Override
            protected void onRxError(Throwable error) {

            }
        });

    }

}
