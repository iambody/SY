package com.cgbsoft.privatefund.model;

/**
 * Created by feigecal on 2017/7/12 0012.
 */

public interface DatumManageModelListener {
    void verifyIndentitySuccess(String result, String hasIdCard, String title, String credentialCode,String status,String statusCode);
    void verifyIndentityError(Throwable error);
}
