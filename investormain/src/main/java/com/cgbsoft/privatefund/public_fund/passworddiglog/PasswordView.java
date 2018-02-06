package com.cgbsoft.privatefund.public_fund.passworddiglog;

/**
 * Created by wangpeng on 18-1-29.
 *
 */
interface PasswordView {

    //void setError(String error);

    String getPassWord();

    void clearPassword();

    void setPassword(String password);

    void setPasswordVisibility(boolean visible);

    void togglePasswordVisibility();

    void setOnPasswordChangedListener(CustomPasswordView.OnPasswordChangedListener listener);

    void setPasswordType(PasswordType passwordType);
}
