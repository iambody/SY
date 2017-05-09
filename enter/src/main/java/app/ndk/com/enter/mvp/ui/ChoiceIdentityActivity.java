package app.ndk.com.enter.mvp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.widget.ScrollingImageView;

import app.ndk.com.enter.R;
import app.ndk.com.enter.R2;
import app.ndk.com.enter.mvp.contract.ChoiceIdentityContract;
import app.ndk.com.enter.mvp.presenter.ChoiceIdentityPresenter;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择身份
 * Created by xiaoyu.zhang on 2016/11/17 13:53
 * Email:zhangxyfs@126.com
 *  
 */
public class ChoiceIdentityActivity extends BaseActivity<ChoiceIdentityPresenter>
        implements ChoiceIdentityContract.View  {
    @BindView(R2.id.siv_aci_bg)
    ScrollingImageView siv_aci_bg;//背景滚动图

    @BindView(R2.id.rg_aci)
    RadioGroup rg_aci;//单选按钮组

    @BindView(R2.id.rb_aci_inverstor)
    RadioButton rb_aci_inverstor;//投资人按钮

    @BindView(R2.id.rb_aci_adviser)
    RadioButton rb_aci_adviser;//理财师按钮

    @BindView(R2.id.btn_aci_next)
    Button btn_aci_next;//下一步按钮

    private int identity = -1;

    private boolean isExit;

    @Override
    protected int layoutID() {
        return R.layout.activity_choice_identity;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (!AppManager.isInvestor(baseContext)) {
            rb_aci_inverstor.setVisibility(View.GONE);
            identity=IDS_ADVISER;
            rb_aci_adviser.setChecked(true);
        } else {
            rb_aci_adviser.setVisibility(View.GONE);
            identity=IDS_INVERSTOR;
            rb_aci_inverstor.setChecked(true);
        }
        getPresenter().nextClick(identity);
        finish();
    }

    @Override
    protected void data() {

    }

    @Override
    protected ChoiceIdentityPresenter createPresenter() {
        return new ChoiceIdentityPresenter(this, this);
    }

    @OnClick(R2.id.btn_aci_next)
    void nextClick() {
        baseContext.finish();
        getPresenter().nextClick(identity);
        toDataStatistics(1001, 10002, "选择进入");
    }

    @Override
    public void finishThis() {
        finish();
    }


//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        switch (checkedId) {
//            case R2.id.rb_aci_inverstor:
//                identity = IDS_INVERSTOR;
//                toDataStatistics(1001, 10000, "投资人");
//                break;
//            case R2.id.rb_aci_adviser:
//                identity = IDS_ADVISER;
//                toDataStatistics(1001, 10000, "理财师");
//                break;
//        }
//        //保存身份状态
////        SPreference.saveIdtentify(getApplicationContext(), identity);
//    }

    @Override
    public void onBackPressed() {
        isExit = exitBy2Click();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        MainTabManager.getInstance().destory();

        if (isExit) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }
}
