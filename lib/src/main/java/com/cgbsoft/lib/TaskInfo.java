package com.cgbsoft.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.cgbsoft.lib.utils.net.ApiClient;
import com.cgbsoft.lib.utils.rxjava.RxSubscriber;
import com.cgbsoft.privatefund.bean.commui.DayTaskBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * desc 任务完成情况，以及做完任务获取云豆
 * Created by yangzonghui on 2017/5/25 14:42
 * Email:yangzonghui@simuyun.com
 *  
 */
public class TaskInfo {

    private static final String TASK_INFO_XML = "task_info_xml";
    private static final String TASK_INFO = "task_info";

    private static SharedPreferences getBasePreference(Context context) {
        return context.getSharedPreferences("TASK_INFO", Context.MODE_PRIVATE);
    }

    /**
     * 保存/更新任务状态
     *
     * @param jsonArray
     */
    public static void saveTaskStatus(JSONArray jsonArray) {
        SharedPreferences.Editor ed = getBasePreference(BaseApplication.getContext()).edit();
        ed.putString(TASK_INFO, jsonArray.toString());
        ed.commit();
    }

    /**
     * 任务完成
     *
     * @param taskName 签到，查看产品，分享产品，查看资讯，分享资讯，查看视频
     */
    public static void complentTask(String taskName) {
        DayTaskBean taskBean = getTaskBean(taskName);
        if (checkTaskState(taskBean, taskName)) {
            return;
        } else if (!taskName.equals("每日签到")) {
            getCoinTask(taskName, taskBean);
        }
    }

    /**
     * 完成任务领豆
     * @param taskName
     * @param taskBean
     */
    private static void getCoinTask(String taskName, DayTaskBean taskBean) {
        ApiClient.addTaskCoin(taskBean.getTaskType(), taskName).subscribe(new RxSubscriber<String>() {
            @Override
            protected void onEvent(String s) {
                try {
                    JSONObject ja = new JSONObject(s).getJSONObject("result");
                    String ratio = ja.getString("ratio");
                    int coinRatioNum = ja.getInt("coinRatioNum");
                    int coinNum = ja.getInt("coinNum");

                    if (ratio.equals("1.0")) {
                        Toast.makeText(BaseApplication.getContext(), "完成【" + taskName + "】任务，获得" + coinRatioNum + "个云豆", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BaseApplication.getContext(), "完成【" + taskName + "】任务，获得" + coinRatioNum + "（" + coinNum + " X " + ratio + "）个云豆", Toast.LENGTH_SHORT).show();
                    }
//                        MApplication.getUser().setMyPoint(MApplication.getUser().getMyPoint() + coinRatioNum);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onRxError(Throwable error) {
                error.toString();
            }
        });

    }

    /**
     * 查看任务完成情况
     */
    private static boolean checkTaskState(DayTaskBean taskBean, String taskName) {
        if (taskBean != null) {
            String status = taskBean.getStatus();
            if (status.equals("1")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 获取任务 详情
     *
     * @param taskName
     * @return
     */
    private static DayTaskBean getTaskBean(String taskName) {
        SharedPreferences sp = getBasePreference(BaseApplication.getContext());
        String taskStr = sp.getString(TASK_INFO, "");
        ArrayList<DayTaskBean> taskBeans = new Gson().fromJson(taskStr, new TypeToken<ArrayList<DayTaskBean>>() {
        }.getType());

        for (DayTaskBean taskBean : taskBeans) {
            if (taskBean.getTaskName().equals(taskName)) {
                return taskBean;
            }
        }
        return null;
    }
}
