package com.cgbsoft.adviser.mvp.contract;

import com.cgbsoft.adviser.mvp.ui.college.adapter.CollegeAdapter;
import com.cgbsoft.adviser.mvp.ui.college.model.CollegeModel;
import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.base.mvp.view.BaseView;

import java.util.List;

/**
 * Created by xiaoyu.zhang on 2016/12/1 13:41
 * Email:zhangxyfs@126.com
 *  
 */
public interface CollegeContract {
    interface Presenter extends BasePresenter {

        void getCollegeData(CollegeAdapter adapter, boolean isRef);

    }

    interface View extends BaseView {
        void getCollegeHeadDataSucc(List<CollegeModel> modelList, boolean isRef);

        void getCollegeHeadDataFail(boolean isRef);
    }

}
