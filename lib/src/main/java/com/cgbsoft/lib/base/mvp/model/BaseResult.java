package com.cgbsoft.lib.base.mvp.model;

import android.text.TextUtils;

/**
 * Created by user on 2016/11/4.
 */

public class BaseResult<T> {
    public String message;
    public String code;

    public T result;

    public boolean isOk() {
        return TextUtils.equals("200", code);
    }
}
