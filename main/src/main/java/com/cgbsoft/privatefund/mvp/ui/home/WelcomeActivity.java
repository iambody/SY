package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Intent;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cgbsoft.lib.base.model.bean.AppResources;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.presenter.WelcomePersenter;
import com.cgbsoft.privatefund.mvp.view.WelcomeView;

/**
 * 欢迎页
 * Created by xiaoyu.zhang on 2016/11/16 09:01
 * Email:zhangxyfs@126.com
 *  
 */
public class WelcomeActivity extends BaseActivity implements WelcomeView {
    private ImageView iv_wel_background;
    private ImageView iv_wel_bottom;

    private WelcomePersenter welcomePersenter;
    private String[] PERMISSIONS = new String[]{PERMISSION_AUDIO, PERMISSION_CAMERA, PERMISSION_STORAGE, PERMISSION_PHONE, PERMISSION_LOCATION};


    @Override
    public void before() {
        super.before();
        isNeedGoneNavigationBar = true;
//        isLaunched = TextUtils.isEmpty(OtherDataProvider.queryByTitle(getApplicationContext(), SharePreferenceUtils.IS_ALREADY_LAUNCH));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        // 缺少权限时, 进入权限配置页面
        if (needPermissions(PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE_ASK_PERMISSIONS, PERMISSIONS);
        } else {
            beforeInit();
        }
        beforeInit();
    }

    @Override
    protected int layoutID() {
        return 0;
    }

    @Override
    protected void init() {

    }

    private void beforeInit(){
        setContentView(R.layout.activity_welcome);
        welcomePersenter = new WelcomePersenter(this);
        welcomePersenter.getData();
    }

    @Override
    public void getDataSucc(AppResources result) {

    }

    @Override
    public void getDataError(Throwable error) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        welcomePersenter.detachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        } else {
            beforeInit();
        }
    }
}
