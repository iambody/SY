package com.cgbsoft.privatefund.bean.video;

import com.cgbsoft.lib.widget.recycler.BaseModel;

/**
 * Created by xiaoyu.zhang on 2016/12/12 18:15
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoHistoryModel extends BaseModel {
    public static int LIST = 1;

    public String videoId;
    public String videoUrl;
    public String videoCoverUrl;
    public String videoTitle;
    public String time;

    public boolean isCheck;
}
