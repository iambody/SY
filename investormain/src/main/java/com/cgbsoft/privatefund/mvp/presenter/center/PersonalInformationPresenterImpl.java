package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.mvp.contract.center.PersonalInformationContract;

/**
 * Created by sunfei on 2017/7/8 0008.
 */

public class PersonalInformationPresenterImpl extends BasePresenterImpl<PersonalInformationContract.PersonalInformationView> implements PersonalInformationContract.PersonalInformationPresenter {
    public PersonalInformationPresenterImpl(@NonNull Context context, @NonNull PersonalInformationContract.PersonalInformationView view) {
        super(context, view);
    }
}
