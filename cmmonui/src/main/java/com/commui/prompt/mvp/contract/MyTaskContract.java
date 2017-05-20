package com.commui.prompt.mvp.contract;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.commui.prompt.mvp.model.MyTaskBean;

import java.util.ArrayList;

/**
 * desc  我的任务
 * Created by yangzonghui on 2017/5/11 11:53
 * Email:yangzonghui@simuyun.com
 *  
 */
public interface MyTaskContract {

    interface Presenter extends BasePresenter {
        //获取任务列表
        void getTaskList();

        //完成任务
        void finishTask(String id);
    }


    interface View extends BaseView {

        void getTaskLitSuc(ArrayList<MyTaskBean> list);

        void finishTaskSuc(String id);

    }
}
