package app.privatefund.com.share.bean;

import android.view.View;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/20-21:04
 */
public class ShareViewBean {
    private int Postion;
    private View ShareView;

    public int getPostion() {
        return Postion;
    }

    public void setPostion(int postion) {
        Postion = postion;
    }

    public View getShareView() {
        return ShareView;
    }

    public void setShareView(View shareView) {
        ShareView = shareView;
    }

    public ShareViewBean(int postion, View shareView) {
        Postion = postion;
        ShareView = shareView;
    }

    public ShareViewBean() {
    }
}
