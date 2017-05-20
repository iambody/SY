package app.live.com.mvp.view;

import com.cgbsoft.lib.base.mvp.view.BaseView;

/**
 * desc  腾讯直播登录
 * Created by yangzonghui on 2017/5/16 11:58
 * Email:yangzonghui@simuyun.com
 *  
 */
public interface TXLoginView extends BaseView {
    //登录成功
    void loginSucc();

    //登录失败
    void loginFail(String module, int errCode, String errMsg);
}
