package com.cgbsoft.lib.utils.tools;
/**
 * Created by datutu on 17/3/20.
 */

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * desc Ls中的view中item的测量和绑定
 * author Wangyongkui wangyongkui@simuyun.com
 * date 17/3/20 17:00
 */
public class ViewHolders {
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public static View ToView(Context mycContext, int ResourceId) {
        return LayoutInflater.from(mycContext).inflate(ResourceId, null);
    }

    /**
     * listview的head和foot显示隐藏的操作
     */
    public static void ShowHindView(boolean IsHind, View VV) {
        if (IsHind) {
            VV.setVisibility(View.GONE);
            VV.setPadding(0, -VV.getHeight(), 0, 0);
        } else {
            VV.setVisibility(View.VISIBLE);
            VV.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * scrollview里面的listview高度
     */
    public static void LSHeight(ListView listView) {

        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);

    }
}
