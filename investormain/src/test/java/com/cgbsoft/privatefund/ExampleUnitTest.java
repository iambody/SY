package com.cgbsoft.privatefund;

import android.util.Log;

import com.cgbsoft.lib.utils.tools.BStrUtils;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        Map<String, Object> params = new HashMap<>();
//        params.put("a", "aaa");
//        params.put("b", null);
//        params.put("c", "c");
//        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
//
//        try {
//            JSONObject object = new JSONObject();
//
//            while (iterator.hasNext()) {
//                Map.Entry entry = iterator.next();
//                object.put(entry.getKey().toString(), entry.getValue());
//            }
//            String ss=object.toString();
//            Log.i("s", "sss");
//
//        } catch (Exception e) {
//            Log.i("s", "sss");
//        }
//        Log.i("s", "sss");
        String saaaas = "最长3个月";

        boolean aaaa = BStrUtils.hasDigit(saaaas);
        boolean bbbb = BStrUtils.hasDigit("osossosos");
        boolean ccc = BStrUtils.hasDigit("最长的月");
        int postionstart = BStrUtils.beginPostionDigit(saaaas);
        int postionend = BStrUtils.lastPostionDigit(saaaas);
        int postionendss = BStrUtils.beginPostionDigit("最长23个月");
        int postionendsssss = BStrUtils.lastPostionDigit("最长23个月");

        int postionendssssss = BStrUtils.beginPostionDigit("最长234个月");
        int postionendsssssssss = BStrUtils.lastPostionDigit("最长234个月");
        Log.i("s", "sss");

    }
}