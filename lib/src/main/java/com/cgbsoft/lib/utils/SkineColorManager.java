package com.cgbsoft.lib.utils;

import java.util.Calendar;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/9/21-10:56
 */
public class SkineColorManager {

    //中秋节*******
    //结束时间
    private static final int END_YEAR = 2017;
    private static final int END_MONTH = 9;
    private static final int END_DAY = 5;
    private static final int END_HOUR = 23;
    private static final int END_MINITE = 59;
    private static final int END_SECOND = 59;

    //开始时间 be
    private static final int BEGIN_YEAR = 2017;
    private static final int BEGIN_MONTH = 8;
    private static final int BEGIN_DAY = 24;
    private static final int BEGIN_HOUR = 23;
    private static final int BEGIN_MINITE = 59;
    private static final int BEGIN_SECOND = 59;


    public static boolean isautumnHoliay() {
        //结束时间
        Calendar endcal = Calendar.getInstance();
        endcal.set(END_YEAR, END_MONTH, END_DAY, END_HOUR, END_MINITE, END_SECOND);
        //开始时间
        Calendar begincal = Calendar.getInstance();
        begincal.set(BEGIN_YEAR, BEGIN_MONTH, BEGIN_DAY, BEGIN_HOUR, BEGIN_MINITE, BEGIN_SECOND);


        return Calendar.getInstance().before(endcal) && Calendar.getInstance().after(begincal);
    }
}
