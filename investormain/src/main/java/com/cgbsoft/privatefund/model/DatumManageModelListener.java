package com.cgbsoft.privatefund.model;

/**
 * Created by feigecal on 2017/7/12 0012.
 */

public interface DatumManageModelListener {
    void verifyIndentitySuccess(String result, String hasIdCard, String title, String credentialCode,String status,String statusCode,String customerName,String credentialNumber,String credentialTitle,String existStatus);
    void verifyIndentityError(Throwable error);
    void verifyIndentitySuccessV3(CredentialStateMedel credentialStateMedel);
    void getLivingCountSuccess(String s);
    void getLivingCountError(Throwable error);
}
