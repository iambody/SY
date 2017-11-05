package com.cgbsoft.privatefund.model;

/**
 * Created by zhaojiaqi on 2017/11/3.
 */

public interface CredentialModelListener {
    void getCrentialSuccess(String s);
    void getCrentialError(Throwable error);

}
