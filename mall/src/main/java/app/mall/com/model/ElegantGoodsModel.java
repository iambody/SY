package app.mall.com.model;

import rx.subscriptions.CompositeSubscription;

/**
 * 尚品model接口
 * Created by sunfei on 2017/6/30 0030.
 */

public interface ElegantGoodsModel {
    void getElegantGoodsFirst(CompositeSubscription subscription, ElegantGoodsModelListener listener, int offset);
    void getElegantGoodsMore(CompositeSubscription subscription, ElegantGoodsModelListener listener, int offset,String category);
}
