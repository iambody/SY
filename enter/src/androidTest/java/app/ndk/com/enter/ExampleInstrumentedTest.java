package app.ndk.com.enter;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cgbsoft.lib.utils.tools.LogUtils;
import com.cgbsoft.lib.utils.tools.RSAUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("app.ndk.com.enter.test", appContext.getPackageName());
    }

    @Test
    public void SSS() throws Exception {
        String publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCp8pDxAhjGPMxxU4VCWo1if4djzyqxM6dScGhb5q+/wqKfY/3SvPH7XKhtF1Q/0kRjCM3tFFpdGJyqXl0Bl34o3RlGoCeCGbUxVRj2IXvsryaOeF1yi8vW7DtOu2VPePS+Hv69SUCDaJYRdSz1+bhaa1ltFoYIvyTjhr+V8umjNQIDAQAB";
        String sss = RSAUtils.encryptByPublicKey("12346", publickey);
        LogUtils.Log("s", sss);

    }

}
