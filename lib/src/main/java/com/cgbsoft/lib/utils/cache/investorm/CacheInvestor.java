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
        SharedPreferences sp = context.getSharedPreferences(PRODUCT_FILTRT, Context.MODE_PRIVATE);
        return sp.getString("productfilter", "");
    }

    /**
     * 保存产品列表的筛选条件
     */
    public static void saveProductFilterCache(Context context, String catchstring) {
        SharedPreferences sp = context.getSharedPreferences(PRODUCT_FILTRT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("productfilter", catchstring);
        editor.commit();
    }
}
