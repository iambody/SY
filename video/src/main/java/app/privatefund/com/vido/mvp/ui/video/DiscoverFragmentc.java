package app.privatefund.com.vido.mvp.ui.video;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.webview.BaseWebNetConfig;
import com.cgbsoft.lib.base.webview.BaseWebview;
import com.cgbsoft.lib.base.webview.CwebNetConfig;
import com.cgbsoft.lib.contant.RouteConfig;
import com.cgbsoft.lib.utils.tools.DataStatistApiParam;
import com.cgbsoft.lib.utils.tools.NavigationUtils;
import com.cgbsoft.lib.utils.tools.UiSkipUtils;
import com.chenenyu.router.Router;

import java.net.URLDecoder;
import java.util.HashMap;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.R2;
import app.privatefund.com.vido.VideoNavigationUtils;
import app.privatefund.com.vido.mvp.contract.video.DiscoverTocContract;
import app.privatefund.com.vido.mvp.presenter.video.DiscoverTocPresenter;
import butterknife.BindView;
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
        videoDiscoverWeb.setClick(result -> goDetail(result));
    }

    public void goDetail(String res) {
        try {
            String baseParams = URLDecoder.decode(res, "utf-8");
            String[] split = baseParams.split(":");
            if (split[1].contains("liveVideo")){
                HashMap<String,Object>map = new HashMap<>();
                map.put("liveJson",split[2]);
                map.put("type","webJoinLive");
                NavigationUtils.startActivityByRouter(getActivity(),RouteConfig.GOTOLIVE,map);
            }else {
                String url = split[2];
                String title = split[3];
                if (!url.contains("http")) {
                    url = BaseWebNetConfig.baseParentUrl + url;
                }

                VideoNavigationUtils.startInfomationDetailActivity(baseActivity, url, title, 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected DiscoverTocPresenter createPresenter() {
        return new DiscoverTocPresenter(baseActivity, this);
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
        Router.build(RouteConfig.GODTOSOUSOU).with("SEARCH_TYPE_PARAMS", "2").go(baseActivity);
    }

    @OnClick(R2.id.video_discover_history_txt)
    public void onVideoDiscoverHistoryTxtClicked() {//
//        UiSkipUtils.toNextActivity(baseActivity, PlayRecordActivity.class);
        UiSkipUtils.toNextActivity(baseActivity, VideoHistoryListActivity.class);
        DataStatistApiParam.onVideoPlayHistoryToC();
    }

    @OnClick(R2.id.video_discover_download_txt)
    public void onVideoDiscoverDownloadTxtClicked() {
        UiSkipUtils.toNextActivity(baseActivity, VideoDownloadListActivity.class);
        DataStatistApiParam.onVideoPlayDownToC();
    }
}
