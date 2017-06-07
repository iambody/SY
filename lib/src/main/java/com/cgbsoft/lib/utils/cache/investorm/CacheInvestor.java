package com.cgbsoft.lib.utils.cache.investorm;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/9-09:09
 */
public class CacheInvestor implements CacheTocConstant {

    /**
     * 获取产品列表的筛选条件的string
     */

    public static String getProductFilterCache(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PRODUCT_CACHE, Context.MODE_PRIVATE);
        return sp.getString(PRODUCT_FILTRT, "");
    }

    /**
     * 保存产品列表的筛选条件
     */
    public static void saveProductFilterCache(Context context, String catchstring) {
        SharedPreferences sp = context.getSharedPreferences(PRODUCT_CACHE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PRODUCT_FILTRT, catchstring);
        editor.commit();
    }


    /**
     * 保存第一页产品列表的数据
     */
    public static void saveProductls(Context context,String productstring){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PRODUCT_CACHE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(PRODUCT_LS,productstring);
        editor.commit();
    }
    /**
     * 获取第一页数据
     */
    public static  String getProductls(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PRODUCT_CACHE,Context.MODE_PRIVATE);
        return sharedPreferences.getString(PRODUCT_LS,"");
    }
}
