package com.commui.prompt.mvp.model;

import com.cgbsoft.lib.widget.recycler.BaseModel;

/**
 * desc  我的任务bean
 * Created by yangzonghui on 2017/5/11 11:49
 * Email:yangzonghui@simuyun.com
 *  
 */
public class MyTaskBean extends BaseModel {

    public static int LIST = 0;

    public static final int ITEM_VIDEO = 1;
    public static final int ITEM_PROD = 2;
    public static final int ITEM_SHARE_PROD = 3;
    public static final int ITEM_INFO = 4;
    public static final int ITEM_SHARE_INFO = 5;
    public static final int ITEM_SIGN = 6;

    public static final int TASK_STATE_FINISH = 1;
    public static final int TASK_STATE_UNFINISH = 0;

    public String name;
    public String content;
    public int item_status;
    public int status;//0未完成，1已完成
    public String id;

}
