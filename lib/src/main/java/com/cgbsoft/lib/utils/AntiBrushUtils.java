package com.cgbsoft.lib.utils;

import android.util.Log;

import com.cgbsoft.lib.utils.tools.MD5Utils;

import java.util.Random;

/**
 * @author chenlong
 *
 */
public class AntiBrushUtils {
    private static final int X = 10;
    private static final int Y = 6;
    private static final int Z = 10;
    private static final int U = 4;

    /**
     * 得到防刷新值
     * @return
     */
    public static String getAntiBrushValue() {
        long ts = getTimes();
        String parts1 = String.valueOf(getRondomEnd());
        String parts2 = String.valueOf(ts);
        String parts3 = MD5Utils.getMD5(String.valueOf(getEts(ts)));
        Log.i("getAntiBrushValue","--parts1=" +parts1 + "----pars2=" +parts2 + "----part3=" + parts3);
        return parts1.concat(parts2).concat(parts3);
    }

    private static long getEts(long times) {
        return ((times / X) + (times >> Y) + ((times / Z) << U)) / 2 + times;
    }

    private static int getRondomEnd() {
        Random random = new Random(10);
        int end = random.nextInt();
        while (end == 0 || end%2 == 0) {
            end = random.nextInt();
        }
        return end;
    }

    private static long getTimes() {
        return System.currentTimeMillis();
    }
}
