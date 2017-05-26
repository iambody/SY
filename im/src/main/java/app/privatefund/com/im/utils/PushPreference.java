package app.privatefund.com.im.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author chenlong
 *
 * 保存消息sp类
 */
public class PushPreference {

    private static String UN_READ_INFOMATION_SP = "rongyun_unread_info";

    public static final String PUSH_INFO_OBJECT_KEY = "pushactivity_object_key_c";

    public static SharedPreferences getBase(Context context) {
        return context.getApplicationContext().getSharedPreferences(UN_READ_INFOMATION_SP, Context.MODE_PRIVATE);
    }

    public static String getPushInfo(Context context) {
        return getBase(context).getString(PUSH_INFO_OBJECT_KEY, "");
    }

    public static void savePushInfo(Context context, String values) {
        getBase(context).edit().putString(PUSH_INFO_OBJECT_KEY, values).apply();
    }
}
