package com.cgbsoft.privatefund.bean.commui;

import com.cgbsoft.lib.widget.recycler.BaseModel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;


/**
 * desc  数据库中保存任务
 * Created by yangzonghui on 2017/5/11 17:53
 * Email:yangzonghui@simuyun.com
 *  
 */
public class DayTaskBean extends BaseModel implements Comparable<Object> {
    private String taskCoin;
    private String status;
    private String taskType;
    private String taskDescribe;
    private String taskName;

    public String getTaskCoin() {
        return taskCoin;
    }

    public void setTaskCoin(String taskCoin) {
        this.taskCoin = taskCoin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskDescribe() {
        return taskDescribe;
    }

    public void setTaskDescribe(String taskDescribe) {
        this.taskDescribe = taskDescribe;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public int compareTo(Object another) {
        DayTaskBean b = (DayTaskBean) another;
        return Integer.parseInt(this.status) - Integer.parseInt(b.status);
    }
}
