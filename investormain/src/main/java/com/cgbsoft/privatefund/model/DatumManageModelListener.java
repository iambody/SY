package com.cgbsoft.privatefund.model;

import com.cgbsoft.lib.base.model.bean.CredentialStateMedel;

/**
 * Created by feigecal on 2017/7/12 0012.
 */
public interface DatumManageModelListener {
    void verifyIndentitySuccess(String result, String hasIdCard, String title, String credentialCode,String status,String statusCode,String customerName,String credentialNumber,String credentialTitle,String existStatus);
    void verifyIndentityError(Throwable error);
    void verifyIndentitySuccessV3(CredentialStateMedel credentialStateMedel);
    void getLivingCountSuccess(String s);
    void getLivingCountError(Throwable error);
    void getCredentialDetialSuccess(CredentialModel credentialModel);
    void getCredentialDetialError(Throwable error);
    void uploadOtherCrendtialSuccess(String s);
    void uploadOtherCrendtialError(Throwable error);
}
