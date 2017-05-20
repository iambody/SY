package app.privatefund.com.im.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cgbsoft.lib.R;
import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.lib.widget.cleverrecyclerview.CleverRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import app.privatefund.com.im.data.RongServiceAdapter;
import app.privatefund.com.im.data.RongServiceListener;
import app.privatefund.com.im.data.RongServiceModel;
import butterknife.ButterKnife;

/**
 * 融云消息弹窗
 * Created by xiaoyu.zhang on 2016/11/25 14:55
 * Email:zhangxyfs@126.com
 *  
 */
public class RongWindowService extends Service implements Constant, RongServiceListener {
    // 创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams layoutParams;
    private RongReceiver rongReceiver;
    private boolean isAddView;
    private View parent;
    private CleverRecyclerView clever_recycle;
    private ImageView iv_vcd_cancel;
    private RongServiceAdapter adapter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        rongReceiver = new RongReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RONG_SERVICE_RECEIVER);
        registerReceiver(rongReceiver, intentFilter);

        createFloatWindow();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(rongReceiver);
    }

    private void createFloatWindow() {
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        layoutParams.gravity = Gravity.CENTER | Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        parent = LayoutInflater.from(this).inflate(R.layout.service_rong_window, null);
        clever_recycle = ButterKnife.findById(parent, R.id.clever_recycle);
        iv_vcd_cancel = ButterKnife.findById(parent, R.id.iv_vcd_cancel);

        clever_recycle.setScrollAnimationDuration(300);
        clever_recycle.setOrientation(RecyclerView.HORIZONTAL);
        clever_recycle.setVisibleChildCount(1);

        adapter = new RongServiceAdapter(this);
        clever_recycle.setAdapter(adapter);

        iv_vcd_cancel.setOnClickListener(v -> {
            isAddView = false;
            mWindowManager.removeView(parent);
        });
    }

    private class RongReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!TextUtils.equals(intent.getAction(), RONG_SERVICE_RECEIVER)) {
                return;
            }
            String json = intent.getStringExtra("json");
            List<RongServiceModel> dataList = new Gson().fromJson(json, new TypeToken<List<RongServiceModel>>() {
            }.getType());
            adapter.clear();
            adapter.appendToList(dataList);

            if (!isAddView) {
                isAddView = true;
                mWindowManager.addView(parent, layoutParams);
            } else {
                mWindowManager.updateViewLayout(parent, layoutParams);
            }
        }
    }


    @Override
    public void sureListener(int position) {

    }

    @Override
    public void canceListener(int position) {

    }

    @Override
    public void onErrorClickListener() {

    }
}
