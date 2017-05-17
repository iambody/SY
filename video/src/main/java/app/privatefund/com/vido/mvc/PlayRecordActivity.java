package app.privatefund.com.vido.mvc;

import android.database.DatabaseUtils;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvc.BaseMvcActivity;

import java.util.ArrayList;
import java.util.HashMap;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.mvc.adapter.RecordAdapter;
import app.privatefund.com.vido.mvc.bean.SchoolVideo;

/**
 * desc  播放历史记录页面
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/15-14:09
 */
public class PlayRecordActivity extends BaseMvcActivity implements View.OnClickListener{

    private LinearLayout belowLayout;
    private ListView recordList;
    private RecordAdapter recordAdapter;
    private boolean isEditMode = false;
    private TextView delete;
    private TextView choiceAll;
    private boolean isChoiceAll = false;
//    private DatabaseUtils db;
    private ArrayList<SchoolVideo> schoolVideoListFromTime;
    private TextView playnull;
    private boolean isKouCheng;
    public static final String KOUCHENG_PARAM = "koucheng";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_record);
        isKouCheng = getIntent().getBooleanExtra(KOUCHENG_PARAM, false);
        initRegisterTitleBar();
        showTileLeft();
        titleRight.setBackgroundResource(R.drawable.local_video_delete);
        bindView();
        showTileMid(isKouCheng ? "我的课程" : "播放记录");
        showCompoundDrawable(playnull, isKouCheng ? ContextCompat.getDrawable(baseContext, R.drawable.mine_course_no) : ContextCompat.getDrawable(baseContext, R.drawable.bfjl_kong));
    }

    private void bindView() {
        belowLayout = (LinearLayout) findViewById(R.id.below_layout);
        recordList = (ListView) findViewById(R.id.play_record_list);
        delete = (TextView) findViewById(R.id.delete);
        choiceAll = (TextView) findViewById(R.id.choice_all);
        playnull = (TextView) findViewById(R.id.play_null);
        delete.setOnClickListener(this);
        choiceAll.setOnClickListener(this);
//        db = new DatabaseUtils(this);
        //todo 从数据库独处数据
//        schoolVideoListFromTime = (ArrayList<SchoolVideo>) db.getSchoolVideoListFromTime();
        schoolVideoListFromTime=new ArrayList<>();
        if (schoolVideoListFromTime != null && schoolVideoListFromTime.size() > 0) {
            playnull.setVisibility(View.INVISIBLE);
        }else {
            titleRight.setVisibility(View.INVISIBLE);
        }
        recordAdapter = new RecordAdapter(this, schoolVideoListFromTime);
        recordList.setAdapter(recordAdapter);
        titleRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(R.id.title_right==v.getId()){
            if (isEditMode) {
                recordAdapter.hideCheck();
                recordAdapter.notifyDataSetChanged();
                belowLayout.setVisibility(View.GONE);
                titleRight.setBackgroundResource(R.drawable.local_video_delete);
                titleRight.setText("");
                isEditMode = false;
            } else {
                recordAdapter.showCheck();
                recordAdapter.notifyDataSetChanged();
                belowLayout.setVisibility(View.VISIBLE);
                titleRight.setText("取消");
                titleRight.setBackgroundColor(0x00ffffff);
                isEditMode = true;
            }
        }else if(R.id.choice_all==v.getId()){
            if (isChoiceAll) {
                choiceAll.setText("全选");
                recordAdapter.cancelChoiceAll();
                delete.setText("删除");
                delete.setTextColor(0xff999999);
                isChoiceAll = false;
            } else {
                delete.setText(String.format("删除(%d)", schoolVideoListFromTime.size()));
                if (AppManager.isInvestor(baseContext)) {
                    delete.setTextColor(ContextCompat.getColor(baseContext, R.color.orange));
                } else {
                    delete.setTextColor(0xfff22502);
                }
                choiceAll.setText("取消全选");
                recordAdapter.choiceAll();
                isChoiceAll = true;
            }
        }else if(R.id.delete==v.getId()){
            delete.setText("删除");
            delete.setTextColor(0xff999999);
            HashMap<String, Boolean> deleteList = recordAdapter.getDelete();
            for (String str : deleteList.keySet()) {
                if (deleteList.get(str)) {
//                    db.removeFromRecord(str);
                    for (int i = 0; i < schoolVideoListFromTime.size(); i++) {
                        if (schoolVideoListFromTime.get(i).getTencentVideoId().equals(str)) {
                            schoolVideoListFromTime.remove(i);
                        }
                    }
                }
            }
            if (schoolVideoListFromTime != null && schoolVideoListFromTime.size() > 0) {
                playnull.setVisibility(View.INVISIBLE);
            }else {
                playnull.setVisibility(View.VISIBLE);
                titleRight.setVisibility(View.INVISIBLE);
            }
            recordAdapter.removeCheck();
            recordAdapter.notifyDataSetChanged();
            recordAdapter.cancelChoiceAll();
            choiceAll.setText("全选");
            belowLayout.setVisibility(View.GONE);
            recordAdapter.hideCheck();
            recordAdapter.notifyDataSetChanged();
            titleRight.setBackgroundResource(R.drawable.local_video_delete);
            titleRight.setText("");
            isEditMode = false;
//            EventBus.getDefault().post(new RecordPalayChange());
        }


    }

    private void showCompoundDrawable(TextView textView, Drawable drawable) {
        if (textView != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(null, drawable, null, null);
        }
    }

//    public void onEventMainThread(RecordEvent recordEvent) {
//        if (recordEvent.getCount() > 0) {
//            delete.setText(String.format("删除(%d)", recordEvent.getCount()));
//            delete.setTextColor(0xfff22502);
//        } else {
//            delete.setText("删除");
//            delete.setTextColor(0xff999999);
//        }
//    }

}
