package com.cgbsoft.privatefund.mvp.presenter.login;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.ui.login.AnimActivity;
import com.cgbsoft.privatefund.mvp.ui.login.LoginActivity;
import com.cgbsoft.privatefund.mvp.view.login.ChoiceIdentityView;

import static com.cgbsoft.lib.utils.constant.Constant.IDS_ADVISER;
import static com.cgbsoft.lib.utils.constant.Constant.IDS_INVERSTOR;
import static com.cgbsoft.lib.utils.constant.Constant.IDS_KEY;

/**
 * 选择身份Presenter
 * Created by xiaoyu.zhang on 2016/11/17 13:55
 * Email:zhangxyfs@126.com
 *  
 */
public class ChoiceIdentityPresenter extends BasePresenter<ChoiceIdentityView> {
    private Context context;

    public ChoiceIdentityPresenter(Context context, ChoiceIdentityView view) {
        super(view);
        this.context = context;
    }

    /**
     * 页面跳转功能
     * @param identity 身份
     */
    public void nextClick(int identity) {
        //理财师处理
        Class clazz = null;
        int value = -1;
        if (identity == IDS_ADVISER) {
            value = IDS_ADVISER;
            if (SPreference.isPlayAdviserAnim(context.getApplicationContext())) {
                clazz = LoginActivity.class;
            } else {
                clazz = AnimActivity.class;
            }
            //投资人处理
        } else if (identity == IDS_INVERSTOR) {
            value = IDS_INVERSTOR;
            if (SPreference.isPlayInverstorAnim(context.getApplicationContext())) {
                clazz = LoginActivity.class;
            } else {
                clazz = AnimActivity.class;
            }
        } else {
            new MToast(context.getApplicationContext()).
                    show(context.getString(R.string.cia_choice_ids_str), Toast.LENGTH_SHORT);
        }

        if (value > 0) {
            SPreference.saveIdtentify(context, value);
        }
        if (clazz != null) {
            Intent intent = new Intent(context, clazz);
            intent.putExtra(IDS_KEY, value);
            context.startActivity(intent);
            getView().finishThis();
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        context = null;
    }
}
