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

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BANNER_CHANGE:
                    targetVp.setCurrentItem(selectedBanner + 1);
                    mHandler.sendEmptyMessageDelayed(BANNER_CHANGE, 3000);
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
        LinearLayout ly_indication = new LinearLayout(activity);
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
            lp.setMargins(10, 0, 0, 0);
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
        HomeBannerAdapter bannerAdapter = new HomeBannerAdapter(bannerList, activity);
        targetVp.setAdapter(bannerAdapter);
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
//        selectedBanner = position % bannerList.size();
//        bannerPointLight(position % indicationList.size());
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
                mHandler.sendEmptyMessageDelayed(BANNER_CHANGE, DELAY_SCROLL_TIME);
                break;
            case MotionEvent.ACTION_CANCEL:
                mHandler.sendEmptyMessageDelayed(BANNER_CHANGE, DELAY_SCROLL_TIME);
                break;
        }
        return false;
    }

//    public class HomeBannerAdapter extends PagerAdapter {
//        private List<View> views;
//        private Context context;
//
//        public HomeBannerAdapter(List<View> views, Context context) {
//            this.context = context;
//            this.views = views;
//        }
//
//        @Override
//        public Object instantiateItem(View arg0, int arg1) {
////            if (views.size() <= arg1) {
////                return null;
////            }
//            final int currentItem = arg1 % views.size();
////            ViewGroup v = (ViewGroup)views.get(currentItem).getParent();
////            if (v != null) {
////                v.removeView(views.get(currentItem));
////            }
////            ((ViewPager) arg0).addView(views.get(currentItem), 0);
//            return views.get(currentItem);
//        }
//
//        public void destroyItem(View container, int position, Object object) {
//            ((ViewPager) container).removeView((View) object);
//        }
//
//        public int getCount() {
//            return Integer.MAX_VALUE;
//        }
//
//        public boolean isViewFromObject(View arg0, Object arg1) {
//            return (arg0 == arg1);
//        }
//    }

    public class HomeBannerAdapter extends PagerAdapter {

        private List<View> views;
        private Context context;

        public HomeBannerAdapter(List<View> views, Context context) {
            this.context = context;
            this.views = views;
        }

        public Object instantiateItem(View container, int position) {
            final int currentItem = position % views.size();
//            if (views.get(currentItem).getParent() != null) {
//                ((ViewPager) container).addView(views.get(currentItem));
//            }
//            return views.get(currentItem);
            ViewGroup parent = (ViewGroup) views.get(currentItem).getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            ((ViewPager)container).addView(views.get(currentItem));
            return views.get(currentItem);
        }

        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        public int getCount() {
            return Integer.MAX_VALUE;
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }
    }

//    /**
//     * 在本地Drawable中使用轮播和指示器
//     */
//    public void initShowImageForLocal(Activity activity, int[] img_urls) {
//        LinearLayout ly_indication = new LinearLayout(activity);
//        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.bottomMargin = 15;
//        params.addRule(ALIGN_PARENT_BOTTOM);
//        params.addRule(CENTER_HORIZONTAL);
//        addView(ly_indication, params);
//        bannerList = new ArrayList<View>();
//        indicationList = new ArrayList<View>();
//        for (int i = 0; i < img_urls.length; i++) {
//            ImageView iv = new ImageView(activity);
//            iv.setScaleType(ImageView.ScaleType.FIT_XY);
//            iv.setBackgroundResource(img_urls[i]);
//            bannerList.add(iv);
//            ImageView iv2 = new ImageView(activity);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(8, 0, 0, 0);
//            iv2.setLayoutParams(lp);
//            if (i == 0) {
//                iv2.setBackgroundResource(R.drawable.home_top_ic_point_on);
//            } else {
//                iv2.setBackgroundResource(R.drawable.home_top_ic_point_off);
//            }
//            indicationList.add(iv2);
//            ly_indication.addView(iv2);
//        }
//        HomeBannerAdapter bannerAdapter = new HomeBannerAdapter(bannerList, activity);
//        targetVp.setAdapter(bannerAdapter);
//        targetVp.setCurrentItem(bannerList.size() * 1000);
//        selectedBanner = bannerList.size() * 1000;
//    }
}
