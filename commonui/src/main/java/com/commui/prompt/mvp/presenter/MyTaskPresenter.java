package com.commui.prompt.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.TaskInfo;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.privatefund.bean.commui.DayTaskBean;
import com.commui.prompt.mvp.contract.MyTaskContract;
import com.commui.prompt.mvp.model.MyTaskBean;
import com.commui.prompt.mvp.ui.SignInDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * desc  任务presenter
 * Created by yangzonghui on 2017/5/11 11:51
 * Email:yangzonghui@simuyun.com
 *  
 */
public class MyTaskPresenter extends BasePresenterImpl<MyTaskContract.View> implements MyTaskContract.Presenter {

    private DaoUtils daoUtils;

    public MyTaskPresenter(@NonNull Context context, @NonNull MyTaskContract.View view) {
        super(context, view);
        daoUtils = new DaoUtils(getContext(), DaoUtils.W_TASK);
    }

    @Override
    public void getTaskList() {
        getView().showLoadDialog();
        ApiClient.initDayTask().subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject ja = new JSONObject(s);
                    JSONArray result = ja.getJSONArray("result");
                    TaskInfo.saveTaskStatus(result);
                    ArrayList<DayTaskBean> taskBeans = new Gson().fromJson(result.toString(), new TypeToken<ArrayList<DayTaskBean>>() {
                    }.getType());

                    getView().getTaskLitSuc(taskBeans);
                    getView().hideLoadDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                try {
                    getView().getTaskListErr(error);
                    getView().hideLoadDialog();
                }catch (Exception e){

                }

            }
        });
    }

    //生成adapter数据结构
    private MyTaskBean createModel(String name, int status) {
        MyTaskBean model = new MyTaskBean();
        model.type = MyTaskBean.LIST;
        model.name = name;
        model.status = status;//未完成0，已完成1

        String content = "", beanNum = "";

        if (TextUtils.equals(name, "学习视频")) {
            model.item_status = MyTaskBean.ITEM_VIDEO;
            content = "观看学院视频超过5分钟可获得";
            beanNum = "10";
        } else if (TextUtils.equals(name, "查看产品")) {
            content = "查看在线产品可获得";
            beanNum = "2";
            model.item_status = MyTaskBean.ITEM_PROD;
        } else if (TextUtils.equals(name, "分享产品")) {
            content = "成功分享产品到微信好友可获得";
            beanNum = "5";
            model.item_status = MyTaskBean.ITEM_SHARE_PROD;
        } else if (TextUtils.equals(name, "查看资讯")) {
            content = "阅读最新资讯/公告可获得";
            beanNum = "2";
            model.item_status = MyTaskBean.ITEM_INFO;
        } else if (TextUtils.equals(name, "分享资讯")) {
            content = "成功分享资讯到微信可获得";
            beanNum = "5";
            model.item_status = MyTaskBean.ITEM_SHARE_INFO;
        } else if (TextUtils.equals(name, "每日签到")) {
            content = "每日签到一次，获得";
            beanNum = "1-10";
            model.item_status = MyTaskBean.ITEM_SIGN;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<font color='#666666'>");
        sb.append(content);
        if (model.status == 0) {
            sb.append("</font>");
            sb.append(AppManager.isInvestor(getContext()) ? "<font color='#f47900'>" : "<font color='#ea1202'>");
        }
        sb.append(beanNum);
        sb.append("个云豆</font>");
        model.content = sb.toString();
        return model;
    }

    @Override
    public void finishTask(String id) {
        switch (id) {
            case MyTaskBean.ITEM_SIGN + "":
                getView().showLoadDialog();
                addSubscription(ApiClient.testSignIn(AppManager.getUserId(getContext())).subscribe(new RxSubscriber<String>() {
                    protected void onEvent(String s) {
                        getView().hideLoadDialog();
                        try {
                            JSONObject response = new JSONObject(s);
                            if (response.has("msg")) {
                                new MToast(getContext()).show(response.getString("msg"), 0);
                            } else {

                                SignInDialog signDialog = new SignInDialog(getContext());
                                signDialog.setData(response);
                                signDialog.show();
                                getView().signSuc();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onRxError(Throwable error) {
                        getView().signErr(error);
                        getView().hideLoadDialog();
                    }
                }));
                break;
        }

    }
}
