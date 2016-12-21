package com.cgbsoft.lib.mvp.ui.video.model;

import com.cgbsoft.lib.widget.recycler.BaseModel;

/**
 * Created by xiaoyu.zhang on 2016/12/12 18:15
 * Email:zhangxyfs@126.com
 *  
 */
public class VideoDownloadListModel extends BaseModel {
    public static int LIST = 1;

    public String videoId;
    public String videoUrl;
    public String videoCoverUrl;
    public String videoTitle;
    public String progressStr;
    public String speedStr;
    public int status; //下载完成:2 / 下载中：0  / 未下载：1
    public int downloadtype;
    public long downloadTime;
    public String localPath;

    public int max;
    public int progress;

    public boolean isCheck;
}
