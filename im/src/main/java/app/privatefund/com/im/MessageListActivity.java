package app.privatefund.com.im;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.TrackingDataManger;
import com.chenenyu.router.annotation.Route;
import com.umeng.analytics.MobclickAgent;

import app.privatefund.com.im.fragment.MainMessageFragment;

/**
 * @author chenlong
 */
@Route(RouteConfig.IM_MESSAGE_LIST_ACTIVITY)
public class MessageListActivity extends BaseActivity {
    public static final String IS_MESSAGE_LIST = "is_message_list";
    public static final String IS_NOTICE_MESSAGE_LIST = "is_notice_message_list";

    @Override
    protected int layoutID() {
        return R.layout.activity_message_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        MainMessageFragment mainMessageFragment = new MainMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_MESSAGE_LIST, true);
        bundle.putBoolean(IS_NOTICE_MESSAGE_LIST, getIntent().getBooleanExtra(IS_NOTICE_MESSAGE_LIST, false));
        RxBus.get().post(RxConstant.REFRUSH_UNREADER_INFO_NUMBER_OBSERVABLE, 0);
        mainMessageFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.message_list, mainMessageFragment).commit();
        TrackingDataManger.imIn(MessageListActivity.this);
    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TrackingDataManger.imBack(MessageListActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
