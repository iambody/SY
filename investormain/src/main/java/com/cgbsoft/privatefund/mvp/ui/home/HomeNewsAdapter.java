package com.cgbsoft.privatefund.mvp.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cgbsoft.lib.base.model.HomeEntity;
import com.cgbsoft.lib.utils.imgNetLoad.Imageload;
import com.cgbsoft.privatefund.R;

import java.util.List;

/**
 * desc  ${DESC}
 * author yangzonghui  yangzonghui@simuyun.com
 * 日期 2018/4/24-下午5:32
 */

class HomeNewsAdapter extends BaseAdapter {
    public List<HomeEntity.Infomation.InfoContent> infomation;
    public Context context;
    private NewsViewHolder newsViewHolder;

    public HomeNewsAdapter(Context context, List<HomeEntity.Infomation.InfoContent> infomation) {
        this.infomation = infomation;
        this.context = context;
    }


    @Override
    public int getCount() {
        return infomation.size();
    }

    @Override
    public Object getItem(int position) {
        return infomation.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeEntity.Infomation.InfoContent infoContent = infomation.get(position);
        if (convertView==null){
            newsViewHolder = new NewsViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_news,null,false);
            newsViewHolder.title =   (TextView) convertView.findViewById(R.id.item_fragment_videoschool_title);
            newsViewHolder.time =   (TextView) convertView.findViewById(R.id.item_fragment_videoschool_time);
            newsViewHolder.readNum =   (TextView) convertView.findViewById(R.id.item_fragment_videoschool_readnum);
            newsViewHolder.imageView =   (ImageView) convertView.findViewById(R.id.item_fragment_videoschool_image_bg);
            convertView.setTag(newsViewHolder);
        }else {
             newsViewHolder = (NewsViewHolder) convertView.getTag();
        }
        newsViewHolder.time.setText(infoContent.label);
        newsViewHolder.title.setText(infoContent.title);
        newsViewHolder.readNum.setText(infoContent.readCount);
        Imageload.display(context, infoContent.imageUrl, newsViewHolder.imageView);
        return convertView;
    }

   public class NewsViewHolder{
        public TextView title;
        public TextView time;
        public TextView readNum;
        public ImageView imageView;

   }
}
