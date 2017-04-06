package com.cgbsoft.lib;

import android.content.Context;

/**
 * desc  进行不同application的管理
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/5-17:32
 */
public class AppManager {
    /**
     * 获取是否是理财师的标识
     * @param PcContext
     * @return
     */
    public static boolean IsAdViser(Context PcContext) {
        return AppInfStore.Get_IsAdviser(PcContext);
    }

//    public static boolean IsAdViser

}
