package com.cgbsoft.lib.listener.listener;

/**
 * Created by fei on 2017/9/21.
 */

public interface SoProgressListener {
    void onProgress(long currentBytes, long contentLength, boolean done);
}
