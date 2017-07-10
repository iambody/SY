package app.mall.com.model;

import rx.subscriptions.CompositeSubscription;

/**
 * 生活家model接口
 * Created by sunfei on 2017/6/30 0030.
 */

public interface ElegantLivingModel {
    void getElegantLivingBanners(CompositeSubscription subscription,ElegantLivingModelListener listener,int offset);
}
