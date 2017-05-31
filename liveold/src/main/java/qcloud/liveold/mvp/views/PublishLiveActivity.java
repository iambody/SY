//package qcloud.liveold.mvp.views;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.RadioGroup;
//
//import com.cgbsoft.privatefund.R;
//import com.cgbsoft.privatefund.activity.MApplication;
//import com.cgbsoft.privatefund.live.model.CurLiveInfo;
//import com.cgbsoft.privatefund.live.model.MySelfInfo;
//import com.cgbsoft.privatefund.live.presenters.UploadHelper;
//import com.cgbsoft.privatefund.live.presenters.viewinface.UploadView;
//import com.cgbsoft.privatefund.live.utils.Constants;
//import com.cgbsoft.privatefund.live.utils.SxbLog;
//import com.cgbsoft.privatefund.live.views.customviews.BaseActivity;
//
///**
// * 直播发布类
// */
//public class PublishLiveActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, UploadView {
//    private UploadHelper mPublishLivePresenter;
//    private static final String TAG = PublishLiveActivity.class.getSimpleName();
//
//    private int uploadPercent = 0;
//
//    private Button start, join;
//    private ImageButton close;
//    private RadioGroup rad;
//    private EditText title;
//    private int check_num;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_live);
//        start = (Button) findViewById(R.id.start);
//        join = (Button) findViewById(R.id.join);
//        close = (ImageButton) findViewById(R.id.close);
//        rad = (RadioGroup) findViewById(R.id.radio_group);
//        title = (EditText) findViewById(R.id.name_live);
//        rad.setOnCheckedChangeListener(this);
//        close.setOnClickListener(this);
//        start.setOnClickListener(this);
//        join.setOnClickListener(this);
//        mPublishLivePresenter = new UploadHelper(this, this);
//        // 提前更新sig
//        mPublishLivePresenter.updateSig();
//    }
//
//    @Override
//    protected void onDestroy() {
//        mPublishLivePresenter.onDestory();
//        super.onDestroy();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.close:
//                finish();
//                break;
//            case R.id.start:
//                Intent intent = new Intent(this, LiveActivity.class);
//                intent.putExtra(Constants.ID_STATUS, Constants.HOST);
//                MySelfInfo.getInstance().setIdStatus(Constants.HOST);
//                MySelfInfo.getInstance().setJoinRoomWay(true);
//                CurLiveInfo.setTitle(title.getText().toString());
//                CurLiveInfo.setHostID(MApplication.getUserid());
////                CurLiveInfo.setHostID(MySelfInfo.getInstance().getId());
//                CurLiveInfo.setRoomNum(MySelfInfo.getInstance().getMyRoomNum());
//                CurLiveInfo.setCheck_num(check_num);
//                startActivity(intent);
//                SxbLog.i(TAG, "PerformanceTest  publish Live     " + SxbLog.getTime());
//                this.finish();
//                break;
//        }
//    }
//
//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        switch (checkedId) {
//            case R.id.choice_1:
//                check_num = 0;
//                break;
//            case R.id.choice_2:
//                check_num = 3;
//                break;
//            case R.id.choice_3:
//                check_num = 8;
//                break;
//            default:
//                check_num = 0;
//                break;
//        }
//    }
//
//    @Override
//    public void onUploadProcess(int percent) {
//
//    }
//
//    @Override
//    public void onUploadResult(int code, String url) {
//
//    }
//}
