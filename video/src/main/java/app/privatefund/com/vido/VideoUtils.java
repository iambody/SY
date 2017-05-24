package app.privatefund.com.vido;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
}
