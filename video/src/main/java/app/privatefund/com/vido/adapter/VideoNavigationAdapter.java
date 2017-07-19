package app.privatefund.com.vido.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import app.privatefund.com.vido.R;
import com.cgbsoft.privatefund.bean.video.VideoAllModel;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/6/23-19:46
 */
public class VideoNavigationAdapter extends CommonNavigatorAdapter {
    //上下文
    Context adapterContext;
    //数据的列表
    List<VideoAllModel.VideoCategory> categoryList;
    //视图的列表
    List<View> viewList;
    //视图填充器
    LayoutInflater layoutInflater;


    public VideoNavigationAdapter(Context adaptercontext, List<VideoAllModel.VideoCategory> categoryList) {
        this.categoryList = categoryList;
        this.viewList = getInitView(categoryList);
        this.adapterContext = adaptercontext;
        this.layoutInflater = LayoutInflater.from(adaptercontext);
    }

    private List<View> getInitView(List<VideoAllModel.VideoCategory> categoryList) {
        List<View> views = new ArrayList<>();
        for (int i = 0; i < categoryList.size(); i++) {
            View view = layoutInflater.inflate(R.layout.view_item_navigation, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.view_item_navigation_iv);
            TextView textView = (TextView) view.findViewById(R.id.view_item_navigation_txt);
            //开始设置图片和文字

//            BStrUtils.SetTxt(textView, categoryList.get(i).text);
//            Imageload.display(adapterContext, 0 == i ? categoryList.get(i).prelog : categoryList.get(i).norlog, imageView);

            views.add(view);
        }
        return views;
    }


    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, int i) {
        CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
        View view = viewList.get(i);
        ImageView imageView = (ImageView) view.findViewById(R.id.view_item_navigation_iv);
        TextView textView = (TextView) view.findViewById(R.id.view_item_navigation_txt);

        commonPagerTitleView.setContentView(view);
        commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
            @Override
            public void onSelected(int i, int i1) {//被选中
//                Imageload.display(adapterContext, categoryList.get(i).prelog, imageView);
                textView.setTextColor(adapterContext.getResources().getColor(R.color.app_golden));
            }

            @Override
            public void onDeselected(int i, int i1) {//取消被选中状态
//                Imageload.display(adapterContext, categoryList.get(i).norlog, imageView);
                textView.setTextColor(adapterContext.getResources().getColor(R.color.black));
            }

            @Override
            public void onLeave(int i, int i1, float v, boolean b) {

            }

            @Override
            public void onEnter(int i, int i1, float v, boolean b) {

            }
        });


        return commonPagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        return null;
    }


}
