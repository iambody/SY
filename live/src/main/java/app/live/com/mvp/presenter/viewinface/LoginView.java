package app.live.com.mvp.presenter.viewinface;


/**
 * 登录回调
 */
public interface LoginView extends MvpView{

    //登录直播
    void liveLoginSucc();

    void liveLoginFail(String module, int errCode, String errMsg);



}
