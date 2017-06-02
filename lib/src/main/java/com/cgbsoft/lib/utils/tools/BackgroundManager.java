package com.cgbsoft.lib.utils.tools;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.rxjava.RxBus;

import java.util.Calendar;

/**
 * 检测程序运行在后台是否超过五分钟
 *
 * @author chenlong
 */
public class BackgroundManager implements Application.ActivityLifecycleCallbacks {

  public static final int EXPIRE_IN_SECOND = 120;
  public static final int EXPIRE_DAN_WEI = Calendar.SECOND;
  private static final int EXPIRE_IN_MINUTE = 5;

  private Calendar expireDate;
  private boolean isDisplay = false;
  private Activity currentActivity;

  public Activity getCurrentActivity() {
    return currentActivity;
  }

  public BackgroundManager(Application application) {
    application.registerActivityLifecycleCallbacks(this);
  }

  @Override
  public void onActivityResumed(Activity activity) {
    if (activity.getClass().getSimpleName().equals("MainPageActivity")) {
      expireDate = Calendar.getInstance();
    }
    currentActivity = activity;
    if (expireDate != null && Calendar.getInstance().after(expireDate) && isDisplay && "MainPageActivity".equals(activity.getClass().getSimpleName())) {
      RxBus.get().post(RxConstant.ON_ACTIVITY_RESUME_OBSERVABLE);
      Log.i("onActivityResumed", "--------isDisplay=" + isDisplay);
    }
    isDisplay = false;
  }

  @Override
  public void onActivityPaused(Activity activity) {
    if (expireDate != null) {
      expireDate = Calendar.getInstance();
      expireDate.add(Calendar.SECOND, EXPIRE_IN_SECOND);
      isDisplay = true;
    }
    Log.i("onActivityPaused", "--------isDisplay=" + isDisplay);
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
  public void onActivityDestroyed(Activity activity) {}
}
