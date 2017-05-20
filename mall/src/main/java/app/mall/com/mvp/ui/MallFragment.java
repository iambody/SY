package app.mall.com.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;

import app.mall.com.mvp.presenter.MallFragmentPresenter;
import app.mall.com.mvp.presenter.MallPresenter;
import butterknife.BindView;
import butterknife.OnClick;
import qcloud.mall.R;
import qcloud.mall.R2;

/**
 *
 */
public class MallFragment extends BaseFragment<MallPresenter> {

    @BindView(R2.id.mall_rule)
    TextView mallRule;
    @BindView(R2.id.mall_web_view)
    BaseWebview videoDiscoverWeb;

    @Override
    protected int layoutID() {
        return R.layout.fragment_mall;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        videoDiscoverWeb.loadUrls(CwebNetConfig.clubPage);
    }

    @Override
    protected MallPresenter createPresenter() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R2.id.mall_rule)
    public void onVideoDiscoverSousouLayClicked() {
//        new Intent(getActivity(),PushMsg)
    }
}
