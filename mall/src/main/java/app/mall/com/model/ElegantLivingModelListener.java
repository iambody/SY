package app.mall.com.model;

import com.cgbsoft.lib.base.model.ElegantLivingEntity;

/**
 * 生活家model层处理数据结果的监听
 * Created by sunfei on 2017/6/30 0030.
 */

public interface ElegantLivingModelListener {
    /**
     * 成功时回调
     *
     * @param result
     */
    void onModelSuccess(ElegantLivingEntity.Result result);
    /**
     * 失败时回调，简单处理，没做什么
     */
    void onModelError();
}
