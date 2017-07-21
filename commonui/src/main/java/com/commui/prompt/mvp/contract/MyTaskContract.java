package com.commui.prompt.mvp.contract;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;
import com.cgbsoft.privatefund.bean.commui.DayTaskBean;
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
        /**
         * 显示loading弹窗
         */
        void showLoadDialog();
        /**
         * 隐藏loading弹窗
         */
        void hideLoadDialog();

        void getTaskLitSuc(ArrayList<DayTaskBean> list);
        void getTaskListErr(Throwable error);

        void finishTaskSuc(String id);

        void signSuc();
        void signErr(Throwable error);
    }
}
