package com.cgbsoft.lib.utils.tools;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author chenlong
 */
public class BackgroundManager implements Application.ActivityLifecycleCallbacks {

  public static final int EXPIRE_IN_SECOND = 120;
  public static final int EXPIRE_DAN_WEI = Calendar.SECOND;
  private static final int EXPIRE_IN_MINUTE = 5;

  private Calendar expireDate = Calendar.getInstance();
  private boolean isDisplay = false;
  private Activity currentActivity;
  private HashMap<Activity, List<Dialog>> hashMap = new HashMap();
  public Activity getCurrentActivity() {
    return currentActivity;
  }

  public BackgroundManager(Application application) {
    application.registerActivityLifecycleCallbacks(this);
  }

  @Override
  public void onActivityResumed(Activity activity) {
    currentActivity = activity;
    if (Calendar.getInstance().after(expireDate) && isDisplay && "MainPageActivity".equals(activity.getClass().getSimpleName())) {
      RxBus.get().post(RxConstant.ON_ACTIVITY_RESUME_OBSERVABLE, true);
      Log.i("onActivityResumed", "--------isDisplay=" + isDisplay);
    }
    isDisplay = false;
  }

  @Override
  public void onActivityPaused(Activity activity) {
      expireDate = Calendar.getInstance();
      expireDate.add(Calendar.SECOND, EXPIRE_IN_SECOND);
      isDisplay = true;
    Log.i("onActivityPaused", "=========isDisplay=" + isDisplay);
    RxBus.get().post(RxConstant.PAUSR_HEALTH_VIDEO, 7);//发送除3以外的任意数字来暂停视频
  }

  public Calendar getExpireData() {
    return expireDate;
  }

  @Override
  public void onActivityStopped(Activity activity) {
  }

  @Override
  public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

  @Override
  public void onActivityStarted(Activity activity) {}

  @Override
  public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

  @Override
  public void onActivityDestroyed(Activity activity) {
    System.out.println("-------activity=" + activity);
    if (activity != null && !CollectionUtils.isEmpty(hashMap.get(activity))) {
      for(Dialog dialog: hashMap.get(activity)) {
        if (dialog != null && dialog.isShowing()) {
          dialog.dismiss();
        }
      }
      hashMap.remove(activity);
    }
  }

  public void bindActivityDialog(Dialog dialog) {
    if (currentActivity != null && !currentActivity.isFinishing()) {
       if (hashMap.containsKey(currentActivity)) {
         List<Dialog> list = hashMap.get(currentActivity);
         if (dialog != null) {
           list.add(dialog);
         }
       } else {
         List<Dialog> list = new ArrayList<>();
         if (dialog != null) {
           list.add(dialog);
           hashMap.put(currentActivity, list);
         }
       }
    }
  }
}
