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

        boolean a=BStrUtils.isChineseStr("2180129219");
        boolean b=BStrUtils.isChineseStr("218012个9");

     int ssss=   BStrUtils.postionChineseStr("292个月");
        Log.i("s", "sss");
    }
}