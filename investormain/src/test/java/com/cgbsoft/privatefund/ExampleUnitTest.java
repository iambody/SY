package com.cgbsoft.privatefund;

import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.LogUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        String test = "/o1KRhUQ303PwOQFqQ2Y0aXKsOvQrh5DsVlrUBChz0Dsz6IF6Vi3SXqGQ==dGV4tiFt/yuM9tDuZmwkLTdjz2UQm/5aSw3bXSddX4MwpHse3cvh9yEsw3EJI";
        String ssss = BStrUtils.decodeSimpleEncrypt(test);
//        LogUtils.Log("sddaa", "ssaaaasss");
        String resut="V4tiFt/yuM9tDuZmwkLTdjz2UQm/5aSw3bXSddX4MwpHse3cvh9yEsw3EJIGo/1KRhUQ303PwOQFqQ2Y0aXKsOvQrh5DsVlrUBChz0Dsz6IF6Vi3SXqGQ==d";
        LogUtils.Log("ffs", "ssssdds");
    }
}