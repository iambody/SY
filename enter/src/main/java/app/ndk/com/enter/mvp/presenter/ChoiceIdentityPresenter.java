package app.ndk.com.enter.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.cache.SPreference;
import com.cgbsoft.lib.widget.MToast;

import app.ndk.com.enter.R;
import app.ndk.com.enter.mvp.contract.ChoiceIdentityContract;

import static com.cgbsoft.lib.utils.constant.Constant.IDS_ADVISER;
import static com.cgbsoft.lib.utils.constant.Constant.IDS_INVERSTOR;
import static com.cgbsoft.lib.utils.constant.Constant.IDS_KEY;

/**
 * 选择身份Presenter
 * Created by xiaoyu.zhang on 2016/11/17 13:55
 * Email:zhangxyfs@126.com
 *  
 */
public class ChoiceIdentityPresenter extends BasePresenterImpl<ChoiceIdentityContract.View> implements ChoiceIdentityContract.Presenter {

    public ChoiceIdentityPresenter(Context context, ChoiceIdentityContract.View view) {
        super(context, view);
    }

    /**
     * 页面跳转功能
     *
     * @param identity 身份
     */
    @Override
    public void nextClick(int identity) {
        //理财师处理
        Class clazz = null;
        int value = -1;
        if (identity == IDS_ADVISER) {
            value = IDS_ADVISER;
            if (SPreference.isPlayAdviserAnim(getContext().getApplicationContext())) {
//                clazz = LoginActivity.class;
            } else {
//                clazz = AnimActivity.class;
            }
            //投资人处理
        } else if (identity == IDS_INVERSTOR) {
            value = IDS_INVERSTOR;
            if (SPreference.isPlayInverstorAnim(getContext().getApplicationContext())) {
//                clazz = LoginActivity.class;
            } else {
//                clazz = AnimActivity.class;
            }
        } else {
            new MToast(getContext().getApplicationContext()).
                    show(getContext().getString(R.string.cia_choice_ids_str), Toast.LENGTH_SHORT);
        }

        if (value > 0) {
            SPreference.saveIdtentify(getContext(), value);
        }
        if (clazz != null) {
            Intent intent = new Intent(getContext(), clazz);
            intent.putExtra(IDS_KEY, value);
            getContext().startActivity(intent);
            getView().finishThis();
        }
    }
}
