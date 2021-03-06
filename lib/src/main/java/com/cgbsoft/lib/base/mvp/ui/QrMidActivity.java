package com.cgbsoft.lib.base.mvp.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.permission.MyPermissionsActivity;
import com.cgbsoft.lib.permission.MyPermissionsChecker;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.chenenyu.router.annotation.Route;

import java.util.HashMap;

/**
 * Created by fei on 2017/8/1.
 */
@Route(RouteConfig.GOTO_TWO_CODE_ACTIVITY)
public class QrMidActivity extends Activity {
    private String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private int REQUEST_CODE_PERMISSION = 2000; // 请求码
    private MyPermissionsChecker mPermissionsChecker;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.qrmidactivity);
        String isCallBack = getIntent().getStringExtra("isCallBack");
        HashMap hashMap = new HashMap();
        hashMap.put("isCallBack",isCallBack);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (null == mPermissionsChecker) {
                mPermissionsChecker = new MyPermissionsChecker(this);
            }
            // 缺少权限时, 进入权限配置页面
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                startPermissionsActivity();
//                return;
            } else {
                NavigationUtils.startActivityByRouter(this, RouteConfig.GOTO_TWO_CODE_READ_ACTIVITY,hashMap);
                this.finish();
            }
        } else {
            NavigationUtils.startActivityByRouter(this, RouteConfig.GOTO_TWO_CODE_READ_ACTIVITY,hashMap);
            this.finish();
        }
    }
    private void startPermissionsActivity() {
        MyPermissionsActivity.startActivityForResult(this, REQUEST_CODE_PERMISSION, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String isCallBack = getIntent().getStringExtra("isCallBack");
        HashMap hashMap = new HashMap();
        hashMap.put("isCallBack",isCallBack);

        if (requestCode == REQUEST_CODE_PERMISSION&&resultCode==MyPermissionsActivity.PERMISSIONS_GRANTED) {
            NavigationUtils.startActivityByRouter(this, RouteConfig.GOTO_TWO_CODE_READ_ACTIVITY,hashMap);
            this.finish();
        } else if (requestCode == REQUEST_CODE_PERMISSION && resultCode == MyPermissionsActivity.PERMISSIONS_DENIED) {
            this.finish();
        }
    }
}

