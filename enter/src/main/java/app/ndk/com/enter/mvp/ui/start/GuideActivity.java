package app.ndk.com.enter.mvp.ui.start;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cgbsoft.lib.base.mvp.ui.BaseActivity;
import com.jude.rollviewpager.RollPagerView;

import app.ndk.com.enter.R;
import app.ndk.com.enter.R2;
import app.ndk.com.enter.mvp.contract.start.GuideContract;
import app.ndk.com.enter.mvp.presenter.start.GuidePresenter;
import butterknife.BindView;


/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/7/21-09:41
 */
public class GuideActivity extends BaseActivity<GuidePresenter> implements GuideContract.view {
    @BindView(R2.id.guide_bannerview)
    RollPagerView guideBannerview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int layoutID() {
        return R.layout.activity_guide;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected GuidePresenter createPresenter() {
        return new GuidePresenter(baseContext, this);
    }
}
