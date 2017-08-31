package com.commui.prompt.mvp.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgbsoft.lib.widget.recycler.BaseAdapter;
import com.cgbsoft.privatefund.bean.commui.DayTaskBean;
import com.commui.prompt.mvp.holder.MyTaskHeaderHolder;
import com.commui.prompt.mvp.holder.MyTaskListHolder;
import com.commui.prompt.mvp.listener.MyTaskListener;
import com.commui.prompt.mvp.model.MyTaskBean;

import app.privatefund.com.cmmonui.R;

/**
 * desc  我的任务列表适配器
 * Created by yangzonghui on 2017/5/11 12:06
 * Email:yangzonghui@simuyun.com
 *  
 */
public class MyTaskAdapter extends BaseAdapter<DayTaskBean, MyTaskListener, RecyclerView.ViewHolder> {


    public MyTaskAdapter(MyTaskListener listener) {
        super(listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MyTaskBean.ISHEADER) {
            return new MyTaskHeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_task_header, parent,false), listener);
        }else if (viewType == MyTaskBean.LIST) {
            return new MyTaskListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_task, parent,false), listener);
        }
        return onCreateErrorViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DayTaskBean model = list.get(position);
        if (model.type == MyTaskBean.ISHEADER) {

        }else if (model.type == MyTaskBean.LIST) {
            MyTaskListHolder dth = (MyTaskListHolder) holder;
            dth.tv_idt_name.setText(model.getTaskName());
            dth.tv_idt_content.setText(model.getTaskDescribe().concat(model.getTaskCoin()).concat("个云豆"));
            String status = model.getStatus();
            int statusInt = Integer.parseInt(status);
            dth.toDone.setEnabled(statusInt==1?false:true);
        } else {
            bindErrorHolder(model, holder);
        }
    }

//    private String getContent(String content, String beanNum, String status) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<font color='#666666'>");
//        sb.append(content);
//        if (status.equals("0")) {
//            sb.append("</font>");
//            sb.append(isToC ? "<font color='#f47900'>" : "<font color='#ea1202'>");
//        }
//        sb.append(beanNum);
//        sb.append("个云豆</font>");
//        return sb.toString();
//    }
}
