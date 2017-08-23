package app.privatefund.adviser.mvp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import app.privatefund.adviser.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * desc
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 17/3/31-13:09
 */

public class AdviserTestActivity extends AppCompatActivity {
    @BindView(R.id.adviser_test_share_bt)
    Button adviserTestShareBt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adviser_test);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.adviser_test_share_bt)
    public void onViewClicked() {
//        CommonShareDialog dialog=new CommonShareDialog(AdviserTestActivity.this,1,null,null);
//        dialog.show();
    }
}
