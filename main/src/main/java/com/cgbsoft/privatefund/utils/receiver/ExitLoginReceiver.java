package com.cgbsoft.privatefund.utils.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.cgbsoft.lib.utils.constant.Constant;
import com.cgbsoft.privatefund.utils.service.ExitLoginService;

/**
 * Created by xiaoyu.zhang on 2016/12/21.
 */

public class ExitLoginReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null)
            return;
        String action = intent.getAction();
        if (TextUtils.equals(action, Constant.RECEIVER_EXIT_ACTION)) {
            ExitLoginService.startService(intent.getIntExtra(Constant.RECEIVER_ERRORCODE, -1));
        }
    }
}
