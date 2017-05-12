package app.product.com.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import app.product.com.listener.Dcontant;
import app.product.com.model.HistorySearch;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/11-21:35
 */
public class ProductDataManger implements Dcontant{

    public static final Map<Class<?>, Integer> TYPES;

    static {
        TYPES = new HashMap<Class<?>, Integer>();
        TYPES.put(byte.class, CLZ_BYTE);
        TYPES.put(short.class, CLZ_SHORT);
        TYPES.put(int.class, CLZ_INTEGER);
        TYPES.put(long.class, CLZ_LONG);
        TYPES.put(String.class, CLZ_STRING);
        TYPES.put(boolean.class, CLZ_BOOLEAN);
        TYPES.put(float.class, CLZ_FLOAT);
        TYPES.put(double.class, CLZ_DOUBLE);
    }
    //保存数据
    public static void saveSousouHistory(Context pcContext,HistorySearch historyBean){
        SharedPreferences sp = pcContext.getSharedPreferences(SOUSOUHISTORY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();


        Class<? extends HistorySearch> clazz = historyBean.getClass();
        Field[] arrFiled = clazz.getDeclaredFields();
        try {
            for (Field f : arrFiled) {
                 int type = TYPES.get(f.getType());
                 switch (type) {
                 case CLZ_BYTE:
                 case CLZ_SHORT:
                 case CLZ_INTEGER:
                 editor.putInt(f.getName(), f.getInt(historyBean));
                 break;
                 case CLZ_LONG:
                 editor.putLong(f.getName(), f.getLong(historyBean));
                 break;
                 case CLZ_STRING:
                     editor.putString(f.getName(), (String) f.get(historyBean));
                 break;
                 case CLZ_BOOLEAN:
                 editor.putBoolean(f.getName(), f.getBoolean(editor));
                 break;
                 }
                editor.commit();
            }
        } catch (IllegalArgumentException e) {

            e.printStackTrace();
        } catch (IllegalAccessException e) {

            e.printStackTrace();
        }

        editor.commit();
    }
    //获取历史数据
    public static String getSousouHistory(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SOUSOUHISTORY, Context.MODE_PRIVATE);
        return sp.getString("history", "");
    }

}
