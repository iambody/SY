package com.cgbsoft.privatefund.model;

/**
 * Created by feigecal on 2017/7/12 0012.
 */

public interface PersonalInformationModelListener {
    void updateSuccess();
    void updateError(Throwable error);
    void uploadImgSuccess();
    void uploadImgError(Throwable error);
    void verifyIndentitySuccess(String result, String hasIdCard,String title,String credentialCode);
    void verifyIndentityError(Throwable error);
}
