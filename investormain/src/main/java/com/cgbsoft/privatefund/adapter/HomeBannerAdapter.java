package com.cgbsoft.privatefund.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.util.List;

import app.privatefund.com.vido.bean.VideoAllModel;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/30-19:58
 */
public class HomeBannerAdapter extends StaticPagerAdapter {

    List<VideoAllModel.Banner> banners;

    LayoutInflater inflater;


    Context Apcontext;


    public HomeBannerAdapter(List<VideoAllModel.Banner> banners) {
        this.banners = banners;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        VideoAllModel.Banner banner = banners.get(position);
        View view = inflater.inflate(com.cgbsoft.lib.R.layout.item_imagecycleview, null);
        ImageView imageView = (ImageView) view.findViewById(com.cgbsoft.lib.R.id.item_imagecycleview_iv);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Imageload.display(Apcontext, banner.imageURLString, imageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(baseActivity, BaseWebViewActivity.class);
//                intent.putExtra(WebViewConstant.push_message_url, banner.extension_url);
//                intent.putExtra(WebViewConstant.push_message_title, banner.title);
//                intent.putExtra(WebViewConstant.PAGE_SHOW_TITLE, true);
//                baseActivity.startActivity(intent);
            }
        });


        return view;
    }


    @Override
    public int getCount() {
        return null == banners ? 0 : banners.size();
    }

}
