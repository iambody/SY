package com.cgbsoft.lib.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cgbsoft.lib.base.model.bean.BannerBean;
import com.cgbsoft.lib.base.webview.BaseWebViewActivity;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;

import java.util.ArrayList;
import java.util.List;
import com.cgbsoft.lib.R;
import com.cgbsoft.lib.utils.tools.CollectionUtils;

/**
 * @author chenlong
 *
 * 通用的轮播图
 */
public class BannerView extends RelativeLayout implements View.OnTouchListener, ViewPager.OnPageChangeListener {

    public interface OnclickBannerItemView {
        void clickBannerItem(BannerBean bannerBean);
    }

    private ViewPager targetVp;
    private ArrayList<View> bannerList;
    private ArrayList<View> indicationList;
    private Context context;
    private int selectedBanner;
    private final static int BANNER_CHANGE = 0;
    private final static int DELAY_SCROLL_TIME = 5000;
    private OnclickBannerItemView onclickBannerItemView;
    private HomeBannerAdapter bannerAdapter;
    private  LinearLayout ly_indication;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BANNER_CHANGE:
                    targetVp.setCurrentItem(selectedBanner + 1);
                    mHandler.sendEmptyMessageDelayed(BANNER_CHANGE, DELAY_SCROLL_TIME);
                    break;
            }
        }
    };

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setOnclickBannerItemView(OnclickBannerItemView onclickBannerItemView) {
        this.onclickBannerItemView = onclickBannerItemView;
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBannerViews(context, attrs, defStyleAttr);
    }

    private void initBannerViews(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        targetVp = new ViewPager(context);
        targetVp.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        targetVp.setOnTouchListener(this);
        targetVp.setOnPageChangeListener(this);
        addView(targetVp);
    }

    /**
     * 初始化轮播和指示器
     *
     */
    public void initShowImageForNet(Activity activity, List<BannerBean> list) {
        if (ly_indication != null) {
            removeView(ly_indication);
        }
        ly_indication = new LinearLayout(activity);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = 15;
        params.addRule(ALIGN_PARENT_BOTTOM);
        params.addRule(CENTER_HORIZONTAL);
        addView(ly_indication, params);
        bannerList = new ArrayList<>();
        indicationList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ImageView iv = new ImageView(activity);
            BannerBean bannerBean = list.get(i);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            Imageload.display(context, bannerBean.getImageUrl(), iv);
            bannerList.add(iv);
            ImageView iv2 = new ImageView(activity);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(15, 0, 0, 0);
            iv2.setLayoutParams(lp);
            if (i == 0) {
                iv2.setBackgroundResource(BannerBean.ViewType.OVAL == bannerBean.getVierType() ? R.drawable.shape_banner_point_on : R.drawable.shape_banner_rectangle_on);
            } else {
                iv2.setBackgroundResource(BannerBean.ViewType.OVAL == bannerBean.getVierType() ? R.drawable.shape_banner_point_off : R.drawable.shape_banner_rectangle_off);
            }
            iv.setOnClickListener(v -> {
                if (onclickBannerItemView != null) {
                    onclickBannerItemView.clickBannerItem(bannerBean);
                } else {
                    Intent intent = new Intent(activity, BaseWebViewActivity.class);
                    intent.putExtra(WebViewConstant.push_message_url, bannerBean.getJumpUrl());
                    intent.putExtra(WebViewConstant.push_message_title, bannerBean.getTitle());
                    intent.putExtra(WebViewConstant.PAGE_SHOW_TITLE, true);
                    activity.startActivity(intent);
                }
            });
            indicationList.add(iv2);
            ly_indication.addView(iv2);
        }
        if (bannerAdapter == null) {
            bannerAdapter = new HomeBannerAdapter(bannerList);
            targetVp.setAdapter(bannerAdapter);
        } else {
            bannerAdapter.notifyData(bannerList);
        }
    }

    private void bannerPointLight(int currentPoint) {
        for (int i = 0; i < indicationList.size(); i++) {
            if (currentPoint == i) {
                indicationList.get(i).setBackgroundResource(R.drawable.shape_banner_point_on);
            } else {
                indicationList.get(i).setBackgroundResource(R.drawable.shape_banner_point_off);
            }
        }
    }

    public void startBanner() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(BANNER_CHANGE, DELAY_SCROLL_TIME);
    }

    public void endBanner() {
        mHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectedBanner = position;
        bannerPointLight(position % indicationList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHandler.removeCallbacksAndMessages(null);
                break;
            case MotionEvent.ACTION_UP:
                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendEmptyMessageDelayed(BANNER_CHANGE, DELAY_SCROLL_TIME);
                break;
            case MotionEvent.ACTION_CANCEL:
                mHandler.removeCallbacksAndMessages(null);
                break;
        }
        return false;
    }

    public class HomeBannerAdapter extends PagerAdapter {

        private List<View> views;

        public HomeBannerAdapter(List<View> views) {
            this.views = views;
        }

        public Object instantiateItem(View container, int position1) {
            int currentItem = position1 % views.size();
            if (currentItem < 0){
                currentItem = views.size() + position1;
            }
            View view = views.get(currentItem);
            ViewParent vp = view.getParent();
            if (vp!=null){
                ViewGroup parent = (ViewGroup)vp;
                parent.removeView(view);
            }
            ((ViewGroup)container).addView(view);
            return view;
        }

        public void notifyData(List<View> listView) {
            if (!CollectionUtils.isEmpty(listView)) {
                this.views = listView;
                notifyDataSetChanged();
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
//            container.removeView(views.get(position % views.size()));
        }

        public int getCount() {
            if (CollectionUtils.isEmpty(bannerList)) {
                return 0;
            }

            if (bannerList.size() == 1) {
                return 1;
            }
            return Integer.MAX_VALUE;
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }
    }
}
