package app.privatefund.com.vido.mvp.ui.video;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.AppInfStore;
import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.base.mvp.ui.BaseFragment;
import com.cgbsoft.lib.base.mvp.ui.BaseLazyFragment;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.PromptManager;
import com.cgbsoft.lib.widget.adapter.FragmentAdapter;
import com.cgbsoft.privatefund.bean.video.VideoAllModel;
import com.google.gson.Gson;

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
import app.privatefund.com.vido.mvp.contract.video.VideoSchoolAllInfContract;
import app.privatefund.com.vido.mvp.presenter.video.VideoSchoolAllInfPresenter;
import butterknife.BindView;

/**
 * desc   私享云财富里面的视频模块
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期  2017/6/23-14:50
 */
public class VideoSchoolFragment extends BaseFragment<VideoSchoolAllInfPresenter> implements VideoSchoolAllInfContract.View {
    @BindView(R2.id.video_videolist_indicator)
    MagicIndicator videoVideolistIndicator;
    //导航器
    CommonNavigator commonNavigator;
    @BindView(R2.id.video_videolist_pager)
    ViewPager videoVideolistPager;
    //导航器的Ap
    VideoNavigationsAdapter videoNavigationAdapter;

    //Fragment的Ap
    FragmentAdapter fragmentAdapter;
    //所有需要的fragment的集合

    List<BaseLazyFragment> lazyFragments = new ArrayList<>();

    @Override
    protected int layoutID() {
        return R.layout.fragment_videolist;
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        commonNavigator = new CommonNavigator(baseActivity);
        videoNavigationAdapter = new VideoNavigationsAdapter(baseActivity, new ArrayList<>());

        commonNavigator.setAdapter(videoNavigationAdapter);
        commonNavigator.setSmoothScroll(true);

        videoVideolistIndicator.setNavigator(commonNavigator);

        fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), lazyFragments);
        videoVideolistPager.setOffscreenPageLimit(20);
        //fragment的适配器填充
        videoVideolistPager.setAdapter(fragmentAdapter);
        ViewPagerHelper.bind(videoVideolistIndicator, videoVideolistPager);
        initCache();
        getPresenter().getVideoSchoolAllInf();
    }

    /*设置缓存*/
    private void initCache() {
        if (null != AppManager.getVideoSchoolCache(baseActivity))
            freashAp(AppManager.getVideoSchoolCache(baseActivity));
    }

    @Override
    protected VideoSchoolAllInfPresenter createPresenter() {
        return new VideoSchoolAllInfPresenter(baseActivity, this);
    }

    private void freashAp(VideoAllModel videoAllModel) {
        lazyFragments = new ArrayList<>();
        for (int i = 0; i < videoAllModel.category.size(); i++) {
            VidoListFragment baseLazyFragment = new VidoListFragment();
            Bundle bundle = new Bundle();
            bundle.putString(VidoListFragment.FRAGMETN_PARAMS, String.valueOf(videoAllModel.category.get(i).value));
            baseLazyFragment.setArguments(bundle);
            lazyFragments.add(baseLazyFragment);
            if (0 == i) baseLazyFragment.freshData(videoAllModel.video);
        }
        videoNavigationAdapter.FreashAp(videoAllModel.category);
        fragmentAdapter.freshAp(lazyFragments);
    }

    @Override
    public void getSchoolAllDataSucc(String data) {
        if (BStrUtils.isEmpty(data)) return;
        VideoAllModel videoAllModel = new Gson().fromJson(data, VideoAllModel.class);
        freashAp(videoAllModel);
        AppInfStore.saveVideoSchoolData(baseActivity, videoAllModel);
    }

    @Override
    public void getSchoolAllDataError(String message) {
        PromptManager.ShowCustomToast(baseActivity, message);
    }

    /**
     * Navigations的Ap
     */
    public class VideoNavigationsAdapter extends CommonNavigatorAdapter {
        //上下文
        Context adapterContext;
        //数据的列表
        List<VideoAllModel.VideoCategory> categoryList;
        //视图填充器
        LayoutInflater layoutInflater;


        public VideoNavigationsAdapter(Context adaptercontext, List<VideoAllModel.VideoCategory> categoryList) {
            super();
            this.categoryList = categoryList;
            this.adapterContext = adaptercontext;
            this.layoutInflater = LayoutInflater.from(adaptercontext);
        }

        /**
         * 刷新navagations的Ap
         */

        public void FreashAp(List<VideoAllModel.VideoCategory> videoCategories) {
            this.categoryList = videoCategories;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return null == categoryList ? 0 : categoryList.size();
        }

        @Override
        public IPagerTitleView getTitleView(Context context, int i) {
            VideoAllModel.VideoCategory videoCategory = categoryList.get(i);

            CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);

            View view = layoutInflater.inflate(R.layout.view_item_navigation, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.view_item_navigation_iv);

            TextView textViewdd = (TextView) view.findViewById(R.id.view_item_navigation_txt);

            Imageload.display(adapterContext, 0 == i ? videoCategory.prelog : videoCategory.norlog, imageView);
            BStrUtils.SetTxt(textViewdd, videoCategory.text);
            commonPagerTitleView.setContentView(view);
            commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                @Override
                public void onSelected(int i, int i1) {//被选中
                    Imageload.display(adapterContext, videoCategory.prelog, imageView);
                    textViewdd.setTextColor(adapterContext.getResources().getColor(R.color.app_golden));
                }

                @Override
                public void onDeselected(int i, int i1) {//取消被选中状态
                    Imageload.display(adapterContext, videoCategory.norlog, imageView);
                    textViewdd.setTextColor(adapterContext.getResources().getColor(R.color.black));
                }

                @Override
                public void onLeave(int i, int i1, float v, boolean b) {

                }

                @Override
                public void onEnter(int i, int i1, float v, boolean b) {

                }
            });
            commonPagerTitleView.setOnClickListener(v -> videoVideolistPager.setCurrentItem(i));
            return commonPagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
            indicator.setLineWidth(DimensionPixelUtil.dip2px(context, 30));
            indicator.setLineHeight(DimensionPixelUtil.dip2px(context, 3));
            indicator.setYOffset(UIUtil.dip2px(context, 3));
            indicator.setColors(context.getResources().getColor(app.privatefund.com.vido.R.color.app_golden));
            indicator.setXOffset(UIUtil.dip2px(context, 30));
            return indicator;
        }
    }


}
