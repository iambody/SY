package app.privatefund.com.vido.mvp.ui.video;


import android.os.Bundle;
import android.view.View;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;

import app.privatefund.com.vido.R;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/23-14:50
 */
public class VideoListFragment extends BaseFragment {
    @Override
    protected int layoutID() {
        return R.layout.fragment_videolist;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {

    }

    @Override
    protected BasePresenterImpl createPresenter() {
        return null;
    }
}
