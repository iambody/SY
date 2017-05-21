package app.privatefund.com.share.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/20-21:11
 */
public class ShareUtils {
    /**
     * 获取屏幕宽高
     * Type 1返回 宽度 2返回高度
     */
    public static int GetWidhAndHeight(Context PcoContext, int Type) {
        WindowManager manager = (WindowManager)PcoContext.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics dm=new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        int width2=dm.widthPixels;
        int height2=dm.heightPixels;
        return 1==Type? width2: height2;
    }
    /**
     * Textview放置文本
     */
    public static void setTxt(TextView T, String Str) {
        T.setText(nullToStr(Str));
    }
    /**
     * 判断如果
     **/
    public static String nullToStr(String str) {

        if (null == str || "".equals(str)) {
            return "--";
        }
        return str;
    }
}
