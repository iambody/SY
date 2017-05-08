package com.cgbsoft.lib;

import android.content.Context;

/**
 * desc  进行不同application的管理 注意 防止误操作无修改 此处只能取状态不能村状态
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/4/5-17:32
 */
public class AppManager {
    /**
     * 获取是否是理财师的标识
     * @param PcContext
     * @return
     */
//    public static boolean isAdViser(Context PcContext) {
//        return AppInfStore.Get_IsAdviser(PcContext);
//    }
    /**
     * 获取是否是理财师的标识
     * @param PcContext
     * @return
     */
    public static boolean isInvestor(Context PcContext) {
        return !AppInfStore.Get_IsAdviser(PcContext);
    }
}
