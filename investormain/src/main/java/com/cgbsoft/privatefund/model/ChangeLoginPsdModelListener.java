package com.cgbsoft.privatefund.model;

/**
 * Created by sunfei on 2017/7/12 0012.
 */

public interface ChangeLoginPsdModelListener {
    void changePsdSuccess(String s);
    void changePsdError(Throwable error);
}
