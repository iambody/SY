package com.cgbsoft.privatefund.model;

/**
 * Created by fei on 2017/8/12.
 */

public interface UploadIndentityModelListener {
    void uploadIndentitySuccess(String result);
    void uploadIndentityError(Throwable error);
}
