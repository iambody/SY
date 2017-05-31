package qcloud.liveold.mvp.presenters.viewinface;


/**
 * 登录回调
 */
public interface LoginView extends MvpView{

    void loginLiveSucc();

    void loginLiveFail();

    void getLiveSignSuc(String s);
}
