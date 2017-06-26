package app.privatefund.com.vido.mvp.ui.video;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.mvp.ui.BaseLazyFragment;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.widget.adapter.FragmentAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.vido.R;
import app.privatefund.com.vido.R2;
import app.privatefund.com.vido.bean.VideoAllModel;
import app.privatefund.com.vido.mvp.contract.video.VideoListContract;
import app.privatefund.com.vido.mvp.presenter.video.VideoListPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * desc   私享云财富里面的视频模块
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期  2017/6/23-14:50
 */
public class VideoListFragment extends BaseFragment<VideoListPresenter> implements VideoListContract.View {
    @BindView(R2.id.video_videolist_indicator)
    MagicIndicator videoVideolistIndicator;

    //导航器的Ap
    VideoNavigationsAdapter videoNavigationAdapter;
    //导航器
    CommonNavigator commonNavigator;
    @BindView(R2.id.video_videolist_pager)
    ViewPager videoVideolistPager;
    Unbinder unbinder;

    @Override
    protected int layoutID() {
        return R.layout.fragment_videolist;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        commonNavigator = new CommonNavigator(baseActivity);
        videoNavigationAdapter = new VideoNavigationsAdapter(baseActivity, null);
        commonNavigator.setAdapter(videoNavigationAdapter);
        videoVideolistIndicator.setNavigator(commonNavigator);


        List<BaseLazyFragment> lazyFragments = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            VidoListInfFragment baseLazyFragment = new VidoListInfFragment(i + "");
            Bundle bundle = new Bundle();
//            bundle.putString("postion", i + "");
            baseLazyFragment.setArguments(bundle);
            lazyFragments.add(baseLazyFragment);
        }
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), lazyFragments);
        videoVideolistPager.setOffscreenPageLimit(20);
        videoVideolistPager.setAdapter(fragmentAdapter);
        ViewPagerHelper.bind(videoVideolistIndicator, videoVideolistPager);
    }

    @Override
    protected VideoListPresenter createPresenter() {
        return new VideoListPresenter(baseActivity, this);
    }


    @Override
    public void getVideoDataSucc(String data) {

    }

    @Override
    public void getVideoDataError(String message) {

    }


    public class VideoNavigationsAdapter extends CommonNavigatorAdapter {
        //上下文
        Context adapterContext;
        //数据的列表
        List<VideoAllModel.VideoCategory> categoryList;
        //视图的列表
//        List<View> viewList;
        //视图填充器
        LayoutInflater layoutInflater;


        public VideoNavigationsAdapter(Context adaptercontext, List<VideoAllModel.VideoCategory> categoryList) {
            super();
            this.categoryList = categoryList;

            this.adapterContext = adaptercontext;
            this.layoutInflater = LayoutInflater.from(adaptercontext);
        }

        @Override
        public int getCount() {
            return 8;
//            return categoryList.size();
        }

        @Override
        public IPagerTitleView getTitleView(Context context, int i) {
            CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);

            View view = layoutInflater.inflate(R.layout.view_item_navigation, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.view_item_navigation_iv);
            TextView textViewdd = (TextView) view.findViewById(R.id.view_item_navigation_txt);

            commonPagerTitleView.setContentView(view);
            commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                @Override
                public void onSelected(int i, int i1) {//被选中
                    Imageload.display(adapterContext, R.drawable.bfjl_kong, imageView);
                    textViewdd.setTextColor(adapterContext.getResources().getColor(R.color.app_golden));
                }

                @Override
                public void onDeselected(int i, int i1) {//取消被选中状态
//                Imageload.display(adapterContext, categoryList.get(i).norlog, imageView);
                    Imageload.display(adapterContext, R.drawable.logo, imageView);
                    textViewdd.setTextColor(adapterContext.getResources().getColor(R.color.black));
                }

                @Override
                public void onLeave(int i, int i1, float v, boolean b) {

                }

                @Override
                public void onEnter(int i, int i1, float v, boolean b) {

                }
            });
            commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    videoVideolistIndicator.onPageSelected(i);
                    videoVideolistPager.setCurrentItem(i);
                }
            });

            return commonPagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);

            indicator.setYOffset(UIUtil.dip2px(context, 3));
            indicator.setColors(getResources().getColor(R.color.app_golden));
            indicator.setXOffset(UIUtil.dip2px(context, 10));
            return indicator;
        }


    }

}
