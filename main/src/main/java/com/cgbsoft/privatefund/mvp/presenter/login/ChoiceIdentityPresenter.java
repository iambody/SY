package com.cgbsoft.privatefund.mvp.presenter.login;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.presenter.BasePresenter;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.utils.cache.UserDataProvider;
import com.cgbsoft.lib.utils.tools.DataStatisticsUtils;
import com.cgbsoft.lib.widget.MToast;
import com.cgbsoft.privatefund.R;
import com.cgbsoft.privatefund.mvp.ui.login.AnimActivity;
import com.cgbsoft.privatefund.mvp.ui.login.LoginActivity;
import com.cgbsoft.privatefund.mvp.view.login.ChoiceIdentityView;

import java.util.HashMap;

import static com.cgbsoft.lib.utils.constant.Constant.IDS_ADVISER;
import static com.cgbsoft.lib.utils.constant.Constant.IDS_INVERSTOR;
import static com.cgbsoft.lib.utils.constant.Constant.IDS_KEY;

/**
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

    public void nextClick(String identity) {
        //理财师处理
        Class clazz = null;
        String value = null;
        if (TextUtils.equals(identity, IDS_ADVISER)) {
            value = IDS_ADVISER;
            if (!SPreference.isPlayAdviserAnim(context.getApplicationContext())) {
                clazz = LoginActivity.class;
            } else {
                clazz = AnimActivity.class;
            }
            //投资人处理
        } else if (TextUtils.equals(identity, IDS_INVERSTOR)) {
            value = IDS_INVERSTOR;
            if (!SPreference.isPlayInverstorAnim(context.getApplicationContext())) {
                clazz = LoginActivity.class;
            } else {
                clazz = AnimActivity.class;
            }
        } else {
            new MToast(context.getApplicationContext()).
                    show(context.getString(R.string.cia_choice_ids_str), Toast.LENGTH_SHORT);
        }

        if (!TextUtils.isEmpty(value)) {
            UserDataProvider.updateUserIDENT(context.getApplicationContext(), value);
        }
        if (clazz != null) {
            Intent intent = new Intent(context, AnimActivity.class);
            intent.putExtra(IDS_KEY, value);
            context.startActivity(intent);
            getView().finishThis();
        }

        HashMap<String, String> data = new HashMap<>();
        data.put("grp", "1001");
        data.put("act", "10002");
        data.put("arg1", "选择进入");
        DataStatisticsUtils.push(context, data);
    }

    /**
     * 数据统计
     * @param act
     * @param arg1
     */
    public void toDataStatistics(String act, String arg1) {
        HashMap<String, String> data = new HashMap<>();
        data.put("grp", "1001");
        data.put("act", act);
        data.put("arg1", arg1);
        DataStatisticsUtils.push(context, data);
    }

    @Override
    public void detachView() {
        super.detachView();
        context = null;
    }
}
