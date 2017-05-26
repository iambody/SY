package com.cgbsoft.lib;

import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.RSAUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void TestRsa(){
        String publickey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCp8pDxAhjGPMxxU4VCWo1if4djzyqxM6dScGhb5q+/wqKfY/3SvPH7XKhtF1Q/0kRjCM3tFFpdGJyqXl0Bl34o3RlGoCeCGbUxVRj2IXvsryaOeF1yi8vW7DtOu2VPePS+Hv69SUCDaJYRdSz1+bhaa1ltFoYIvyTjhr+V8umjNQIDAQAB";
        String  aaaa= null;
        try {
            aaaa = RSAUtils.encryptByPublicKey("123abc",publickey);
            LogUtils.Log("ss",aaaa);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.Log("ss",aaaa);


    }
}