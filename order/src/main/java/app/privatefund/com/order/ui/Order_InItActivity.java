package app.privatefund.com.order.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;

import app.privatefund.com.order.R;

/**
 * desc
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/3/31-11:49
 */

public class Order_InItActivity extends Activity {
    private Button BT1;
    private Button ToLogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder_init);
        BT1= (Button) findViewById(R.id.oder_test_bt);
        BT1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.build("share").go(Order_InItActivity.this);

            }
        });
        ToLogin= (Button) findViewById(R.id.oder_test_tologin_bt);
        ToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.build("login").go(Order_InItActivity.this);
            }
        });
    }
}
