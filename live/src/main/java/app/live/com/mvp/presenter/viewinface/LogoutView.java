package app.live.com.mvp.presenter.viewinface;


/**
 * 登出回调
 */
public interface LogoutView extends MvpView{

    void logoutSucc();

    void logoutFail();
}
