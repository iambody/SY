package app.ndk.com.enter.mvp.ui.start;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cgbsoft.lib.utils.tools.PromptManager;

import app.ndk.com.enter.R;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/12/4-11:25
 */
public class testaaa extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        PromptManager.ShowCustomToast(this,"哈哈哈哈哈");
    }
}
