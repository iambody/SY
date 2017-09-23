package com.cgbsoft.privatefund.utils.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/9/23-17:22
 */
public class AppInstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager manager = context.getPackageManager();
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
//            Toast.makeText(context, "安装成功"+packageName, Toast.LENGTH_LONG).show();
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
//            Toast.makeText(context, "卸载成功"+packageName, Toast.LENGTH_LONG).show();
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
//            Toast.makeText(context, "替换成功"+packageName, Toast.LENGTH_LONG).show();

            if (!packageName.equals("com.cgbsoft.privatefund")) return;
//            PackageIconUtils packageIconUtils = new PackageIconUtils(context, context.getPackageManager(), "com.simuyun.adviser.MainActivity0", "com.simuyun.adviser.MainActivity1");
//            boolean isAutumn = SkineColorManager.isautumnHoliay();

            try {
                Intent intent4 = context.getPackageManager().getLaunchIntentForPackage("com.cgbsoft.privatefund");
                context.startActivity(intent4);
            } catch (Exception e) {
//                Toast.makeText(this, "没有安装", Toast.LENGTH_LONG).show();
            }
//


        }


    }

}
