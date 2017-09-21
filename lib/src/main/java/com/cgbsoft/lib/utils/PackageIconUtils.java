package com.cgbsoft.lib.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/9/21-11:04
 */
public class PackageIconUtils {

    private PackageManager mPackageManager;

    //默认组件
    private ComponentName componentNameDefault;
    private ComponentName componentName1;

    public PackageIconUtils(Activity context, PackageManager mPackageManager, String Class0Name, String Class1Name) {
        this.mPackageManager = mPackageManager;
        componentNameDefault = new ComponentName(context, Class0Name);
        componentName1 = new ComponentName(context, Class1Name);
    }


    /**
     * 设置第icon2图标生效
     */
    public void enableComponentName1() {
        disableComponent(componentNameDefault);
        enableComponent(componentName1);
    }

    /**
     * 设置第icon3图标生效
     */
    public void enableComponentDefault() {
        disableComponent(componentName1);
        enableComponent(componentNameDefault);
    }

    /**
     * 启动组件
     *
     * @param componentName 组件名
     */
    private void enableComponent(ComponentName componentName) {
        //此方法用以启用和禁用组件，会覆盖Androidmanifest文件下定义的属性
        mPackageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * 禁用组件
     *
     * @param componentName 组件名
     */
    private void disableComponent(ComponentName componentName) {
        mPackageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
