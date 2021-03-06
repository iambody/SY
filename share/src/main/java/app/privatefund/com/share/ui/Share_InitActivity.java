package app.privatefund.com.share.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;

import app.privatefund.com.share.R;

/**
 * desc
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/3/31-11:55
 */

public class Share_InitActivity extends AppCompatActivity {
    private Button btt1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_init);
        btt1= (Button) findViewById(R.id.share_to_order);
        btt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.build("order").go(Share_InitActivity.this);
                Share_InitActivity.this.finish();
            }
        });

    }
}
