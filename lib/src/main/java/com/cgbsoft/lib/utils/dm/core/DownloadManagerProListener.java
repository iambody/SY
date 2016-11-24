package com.cgbsoft.lib.utils.dm.core;

import com.cgbsoft.lib.utils.dm.report.listener.DownloadManagerListener;

/**
 * 
 *  Created by xiaoyu.zhang on 2016/11/24 17:23
 *  Email:zhangxyfs@126.com
 *  
 */
public class DownloadManagerProListener implements DownloadManagerListener {
    @Override
    public void OnDownloadStarted(long taskId) {

    }

    @Override
    public void OnDownloadPaused(long taskId) {

    }

    @Override
    public void onDownloadProcess(long taskId, double percent, long downloadedLength) {

    }

    @Override
    public void OnDownloadFinished(long taskId) {

    }

    @Override
    public void OnDownloadRebuildStart(long taskId) {

    }

    @Override
    public void OnDownloadRebuildFinished(long taskId) {

    }

    @Override
    public void OnDownloadCompleted(long taskId) {

    }

    @Override
    public void connectionLost(long taskId) {

    }
}
