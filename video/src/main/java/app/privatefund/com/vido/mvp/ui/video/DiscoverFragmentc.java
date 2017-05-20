package app.privatefund.com.vido.mvp.ui.video;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.chenenyu.router.Router;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.R2;
import app.privatefund.com.vido.mvc.LocalVideoActivity;
import app.privatefund.com.vido.mvc.PlayRecordActivity;
import app.privatefund.com.vido.mvp.contract.video.DiscoverTocContract;
import app.privatefund.com.vido.mvp.contract.video.VideoHistoryListContract;
import app.privatefund.com.vido.mvp.presenter.video.DiscoverTocPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/15-14:40
 */
public class DiscoverFragmentc extends BaseFragment<DiscoverTocPresenter> implements DiscoverTocContract.view {


    @BindView(R2.id.video_discover_sousou_lay)
    LinearLayout videoDiscoverSousouLay;
    @BindView(R2.id.video_discover_history_txt)
    TextView videoDiscoverHistoryTxt;
    @BindView(R2.id.video_discover_download_txt)
    TextView videoDiscoverDownloadTxt;
    @BindView(R2.id.video_discover_web)
    BaseWebview videoDiscoverWeb;


    @Override
    protected int layoutID() {
        return R.layout.vido_layout_discoverfragment;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        videoDiscoverWeb.loadUrls(CwebNetConfig.discoverPage);
    }

    @Override
    protected DiscoverTocPresenter createPresenter() {
        return null;
    }

    @Override
    public void testview() {
        if (videoDiscoverWeb != null) {

            videoDiscoverWeb.loadUrl("javascript:refresh()");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R2.id.video_discover_sousou_lay)
    public void onVideoDiscoverSousouLayClicked() {
        Router.build(RouteConfig.GODTOSOUSOU).with("SEARCH_TYPE_PARAMS", "3").go(baseActivity);
    }

    @OnClick(R2.id.video_discover_history_txt)
    public void onVideoDiscoverHistoryTxtClicked() {//
//        UiSkipUtils.toNextActivity(baseActivity, PlayRecordActivity.class);

        UiSkipUtils.toNextActivity(baseActivity, VideoHistoryListActivity.class);
    }

    @OnClick(R2.id.video_discover_download_txt)
    public void onVideoDiscoverDownloadTxtClicked() {
        UiSkipUtils.toNextActivity(baseActivity, VideoDownloadListActivity.class);
    }
}
