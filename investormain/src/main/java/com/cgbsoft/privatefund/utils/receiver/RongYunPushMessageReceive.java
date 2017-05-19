package com.cgbsoft.privatefund.utils.receiver;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.cgbsoft.lib.InvestorAppli;
import com.cgbsoft.lib.base.webview.WebViewConstant;
import com.cgbsoft.privatefund.R;

import app.ndk.com.enter.mvp.ui.start.WelcomeActivity;
import app.privatefund.com.im.utils.RongCouldUtil;
import io.rong.imkit.RongContext;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * @chenlong 推送消息接收器
 */
public class RongYunPushMessageReceive extends PushMessageReceiver {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        Log.i(this.getClass().getName(), "messageinfo=----isPUsh=" + pushNotificationMessage.getPushFlag());
        //if (RongCouldUtil.getInstance().hideConversation(pushNotificationMessage.getSenderId())) {
            // PushNotificationManager.getInstance().onReceivePush(pushNotificationMessage);
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_VIEW);
//            RongPushClient.ConversationType conversationType = pushNotificationMessage.getConversationType();
//            intent.setData(uri);
            if (!Boolean.parseBoolean(pushNotificationMessage.getPushFlag())) {
                return true;
            }

            Uri uri = Uri.parse("rong://" + RongContext.getInstance().getPackageName()).buildUpon().appendPath("conversationList").build();
            Log.d("onReceivePushMessage", uri.toString());
            Intent intent = new Intent(context, WelcomeActivity.class);
            intent.putExtra(WebViewConstant.PUSH_MESSAGE_OBJECT_NAME, pushNotificationMessage);
            intent.putExtra(WebViewConstant.PUSH_MESSAGE_RONGYUN_URL_NAME, uri);
            Notification notification = null;
            PendingIntent pendingIntent = PendingIntent.getActivity(RongContext.getInstance(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            if (Build.VERSION.SDK_INT < 11) {
//                notification = new Notification(RongContext.getInstance().getApplicationInfo().icon,"", System.currentTimeMillis());
//                notification.setLatestEventInfo(RongContext.getInstance().getBaseContext(),"title","content"+pushNotificationMessage.getObjectName(),pendingIntent);
//                notification.flags = Notification.FLAG_AUTO_CANCEL;
//                notification.defaults  = Notification.DEFAULT_SOUND;
//            } else {
                String showTile;
                if (!TextUtils.isEmpty(pushNotificationMessage.getSenderName()) &&!pushNotificationMessage.getSenderName().contains("INTIME")) {
                    showTile = pushNotificationMessage.getSenderName();
                } else {
                    showTile = pushNotificationMessage.getPushContent();
                }
                Resources resources = InvestorAppli.getContext().getResources();
                notification = new Notification.Builder(RongContext.getInstance())
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.logoshare))
                    .setSmallIcon(R.drawable.logoshare)
                    .setTicker(pushNotificationMessage.getSenderName())
                    .setContentTitle(showTile)
                    .setContentText(pushNotificationMessage.getPushContent())
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .build();
//            }
            NotificationManager nm = (NotificationManager) RongContext.getInstance().getSystemService(RongContext.getInstance().NOTIFICATION_SERVICE);
            nm.notify((int) (100 * Math.random()), notification);
            return true;
        //}
        //return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        if (!TextUtils.isEmpty(pushNotificationMessage.getSenderId()) && RongCouldUtil.getInstance().customConversation(pushNotificationMessage.getSenderId())) {
            // PageJumpMananger.jumpPageFromToMainActivity(context, pushNotificationMessage);
            Intent notificationIntent = new Intent(context, WelcomeActivity.class);
            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            notificationIntent.setAction(Intent.ACTION_MAIN);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(notificationIntent);
            return true;
        }
        return false;
    }
}
