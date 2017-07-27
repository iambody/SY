package qcloud.liveold.mvp.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.NavigationUtils;

import qcloud.liveold.R;

public class EndLiveActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_live);
        Button backMain = (Button) findViewById(R.id.back_main);
        backMain.setOnClickListener(this);
        TextView count = (TextView) findViewById(R.id.count);
        if (AppManager.isInvestor(this)){
            count.setTextColor(getResources().getColor(R.color.app_golden));
            LinearLayout endlive_bg = (LinearLayout) findViewById(R.id.end_live_bg);
            endlive_bg.setBackgroundResource(R.drawable.bg_live_end_c);
            backMain.setBackgroundResource(R.drawable.golden_shape_sel_btn1);
        }

        count.setText(String.format("%d", getIntent().getIntExtra("userNum", 0)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back_main) {
            if (AppManager.isInvestor(this)) {
                NavigationUtils.startActivityByRouter(this, RouteConfig.GOTOCMAINHONE);
            } else {
            }
            this.finish();

        } else {
        }
    }
}
