package com.cgbsoft.adviser.mvp.ui.college.model;

import com.cgbsoft.lib.widget.recycler.BaseModel;

/**
 * Created by xiaoyu.zhang on 2016/12/1 10:59
 * Email:zhangxyfs@126.com
 *  
 */
public class CollegeModel extends BaseModel {
    public static final int HEAD = 1;
    public static final int COMM_HEAD = 2;
    public static final int COMM = 3;
    public static final int OHTER_HEAD = 4;
    public static final int OTHER = 5;

    public String headBgUrl;//头部大图url
    public String headBgContent;//头部大图内容

    public String videoId;
    public String videoPlayUrl;
    public String bottomVideoImgUrl;//下方视频图片地址
    public String bottomVideoContent;//下方视频内容

    public boolean isVisable = true;

    public CollegeModel() {

    }

    public CollegeModel(int type) {
        this.type = type;
    }
}
