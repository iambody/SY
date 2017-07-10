package app.mall.com.model;


import com.cgbsoft.lib.base.model.ElegantGoodsEntity;

/**
 * 尚品model层处理数据结果的监听
 * Created by sunfei on 2017/6/30 0030.
 */

public interface ElegantGoodsModelListener {
    /**
     * 成功时回调
     *
     * @param result
     */
    void onModelFirstSuccess(ElegantGoodsEntity.Result result);
    /**
     * 失败时回调，简单处理，没做什么
     */
    void onModelFirstError(Throwable error);
    /**
     * 成功时回调
     *
     * @param result
     */
    void onModelMoreSuccess(ElegantGoodsEntity.ResultMore result);
    /**
     * 失败时回调，简单处理，没做什么
     */
    void onModelMoreError(Throwable error);
}
