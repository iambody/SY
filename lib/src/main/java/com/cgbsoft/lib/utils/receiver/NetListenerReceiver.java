package com.cgbsoft.lib.utils.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cgbsoft.lib.Appli;
import com.cgbsoft.lib.mvp.model.video.VideoInfoModel;
import com.cgbsoft.lib.utils.constant.RxConstant;
import com.cgbsoft.lib.utils.constant.VideoStatus;
import com.cgbsoft.lib.utils.db.DaoUtils;
import com.cgbsoft.lib.utils.rxjava.RxBus;
import com.cgbsoft.lib.utils.tools.NetUtils;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;

import java.util.List;

/**
 * Created by win8 -1 on 2015/8/13.
 */
public class NetListenerReceiver extends BroadcastReceiver implements RxConstant {
    private DownloadManager downloadManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        DaoUtils daoUtils = new DaoUtils(Appli.getContext(), DaoUtils.W_VIDEO);
        if (downloadManager == null) {
            downloadManager = DownloadService.getDownloadManager();
        }


        NetUtils.NetState netState = NetUtils.getNetState();
        if (netState != NetUtils.NetState.NET_WIFI) {
            downloadManager.stopAllTask();
            List<VideoInfoModel> list = daoUtils.getAllVideoInfo();
            for (int i = 0; i < list.size(); i++) {
                VideoInfoModel model = list.get(i);
                if (model.status == VideoStatus.DOWNLOADING || model.status == VideoStatus.WAIT) {
                    model.status = VideoStatus.NONE;
                    daoUtils.saveOrUpdateVideoInfo(model);
                }
            }
            daoUtils.destory();

            RxBus.get().post(DOWNLOAD_TO_LIST_OBSERVABLE, true);
        }
    }
}
