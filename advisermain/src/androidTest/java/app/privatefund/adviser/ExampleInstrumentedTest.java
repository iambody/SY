package app.privatefund.adviser;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cgbsoft.lib.utils.tools.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

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

        assertEquals("app.privatefund.adviser.test", appContext.getPackageName());
    }

    @Test
    public void TestGson(){
        String a="{\"a\":\"100\",\"b\":[\n" +
                "{\"b1\":\"b_value1\",\"b2\":\"b_value2\"},\n" +
                "{\"b1\":\"b_value1\",\"b2\":\"b_value2\"}],\n" +
                "\"c\":{\"c1\":\"c_value1\",\"c2\":\"c_value2\"}\n" +
                "}";

        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<JsonBean>() {}.getType();
        JsonBean jsonBean = gson.fromJson(a, type);

        LogUtils.Log("a",jsonBean.toString());
    }


}
