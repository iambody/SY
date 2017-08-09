package com.cgbsoft.privatefund.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.lib.utils.tools.BStrUtils;
import com.cgbsoft.lib.utils.tools.CollectionUtils;
import com.cgbsoft.lib.utils.tools.DimensionPixelUtil;
import com.cgbsoft.lib.utils.tools.Utils;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.model.DiscoverModel;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.List;

/**
 * @author chenlong
 */
public class DiscoverIndicatorAdapter extends CommonNavigatorAdapter {

    Context adapterContext;
    List<DiscoverModel.DiscoverCategory> categoryList;
    LayoutInflater layoutInflater;
    private ViewPager viewPager;
    private int screenWidth;

    public DiscoverIndicatorAdapter(Context adaptercontext, ViewPager viewPager) {
        super();
        this.adapterContext = adaptercontext;
        this.layoutInflater = LayoutInflater.from(adaptercontext);
        this.viewPager = viewPager;
        this.screenWidth = Utils.getWidthHeight(adapterContext)[0];
    }

    public void setData(List<DiscoverModel.DiscoverCategory> list) {
        if (!CollectionUtils.isEmpty(list)) {
            this.categoryList = list;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return null == categoryList ? 0 : categoryList.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, int i) {
        DiscoverModel.DiscoverCategory discoverCategory = categoryList.get(i);
        CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
        View view = layoutInflater.inflate(R.layout.view_discovery_item_navigation, null);

        ImageView imageView = (ImageView) view.findViewById(app.privatefund.com.vido.R.id.view_item_navigation_iv);
        TextView textViewdd = (TextView) view.findViewById(app.privatefund.com.vido.R.id.view_item_navigation_txt);

        LinearLayout.LayoutParams layoutParamsView = new LinearLayout.LayoutParams(screenWidth/4, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParamsView.leftMargin = DimensionPixelUtil.dip2px(context, 80);
        layoutParamsView.rightMargin = DimensionPixelUtil.dip2px(context, 80);
        view.setLayoutParams(layoutParamsView);

        Imageload.display(adapterContext, 0 == i ? discoverCategory.prelog : discoverCategory.norlog, imageView);
        BStrUtils.SetTxt(textViewdd, discoverCategory.text);
        commonPagerTitleView.setContentView(view);
        commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
            @Override
            public void onSelected(int i, int i1) {//被选中
                Imageload.display(adapterContext, discoverCategory.prelog, imageView);
                textViewdd.setTextColor(adapterContext.getResources().getColor(app.privatefund.com.vido.R.color.app_golden));
            }

            @Override
            public void onDeselected(int i, int i1) {//取消被选中状态
                Imageload.display(adapterContext, discoverCategory.norlog, imageView);
                textViewdd.setTextColor(adapterContext.getResources().getColor(app.privatefund.com.vido.R.color.black));
            }

            @Override
            public void onLeave(int i, int i1, float v, boolean b) {

            }

            @Override
            public void onEnter(int i, int i1, float v, boolean b) {

            }
        });
        commonPagerTitleView.setOnClickListener(v -> viewPager.setCurrentItem(i));
        return commonPagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(DimensionPixelUtil.dip2px(context, 30), DimensionPixelUtil.dip2px(context, 5));
        indicator.setLayoutParams(layoutParams);
        indicator.setYOffset(UIUtil.dip2px(context, 3));
        indicator.setColors(context.getResources().getColor(app.privatefund.com.vido.R.color.app_golden));
        indicator.setXOffset(UIUtil.dip2px(context, 10));
        return indicator;
    }
}
