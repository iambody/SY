package com.cgbsoft.privatefund.mvp.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cgbsoft.lib.base.model.bean.BannerBean;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.widget.BannerView;
import com.cgbsoft.privatefund.mvp.contract.home.DiscoverContract;
import com.cgbsoft.privatefund.mvp.presenter.home.DiscoveryPresenter;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.vido.R;
import butterknife.BindView;

/**
 * @author chenlong
 * 资讯页面
 */
public class DiscoveryFragment extends BaseFragment<DiscoveryPresenter> implements DiscoverContract.View {

    @BindView(R.id.discover_bannerview)
    BannerView discoveryBannerView;

    @Override
    protected int layoutID() {
        return R.layout.fragment_discover_list;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        List<BannerBean> list = new ArrayList<>();
        list.add(new BannerBean(false, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498574380597&di=9d45fb18e608e1102bb6951b884b10c6&imgtype=0&src=http%3A%2F%2Fn1.itc.cn%2Fimg8%2Fwb%2Frecom%2F2016%2F04%2F15%2F146070551257980148.GIF", BannerBean.ViewType.OVAL));
        list.add(new BannerBean(false, "http://dimg08.c-ctrip.com/images/tg/132/715/635/f6d1d5683770473bb31d19743e7df6bd.jpg", BannerBean.ViewType.OVAL));
        list.add(new BannerBean(false, "http://img4.duitang.com/uploads/item/201509/04/20150904204338_RcSCF.jpeg", BannerBean.ViewType.OVAL));
        list.add(new BannerBean(false, "http://h.hiphotos.baidu.com/lvpics/h=800/sign=d3cb72cb38292df588c3a1158c305ce2/b812c8fcc3cec3fdbb261091d488d43f8794273d.jpg", BannerBean.ViewType.OVAL));
        list.add(new BannerBean(false, "http://pic.58pic.com/58pic/12/00/76/78b58PICVWs.jpg", BannerBean.ViewType.OVAL));
        list.add(new BannerBean(false, "http://youimg1.c-ctrip.com/target/fd/tg/g1/M04/7E/C3/CghzflVTERSAaOlcAAGrbgRCst0677.jpg", BannerBean.ViewType.OVAL));
        discoveryBannerView.initShowImageForNet(getActivity(), list);
        discoveryBannerView.setOnclickBannerItemView(bannerBean -> {
            Toast.makeText(getActivity(), bannerBean.getPositon(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        discoveryBannerView.startBanner();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        discoveryBannerView.endBanner();
    }

    @Override
    protected DiscoveryPresenter createPresenter() {
        return null;
    }

    @Override
    public void getSuccess() {

    }

    @Override
    public void getFailure() {

    }
}
