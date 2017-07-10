package app.mall.com.mvp.presenter;


import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.model.ElegantLivingEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;

import java.util.List;

import app.mall.com.model.ElegantLivingModelListener;
import app.mall.com.model.impl.ElegantLivingModelImpl;
import app.mall.com.mvp.contract.ElegantLivingContract;

/**
 * 生活家presenter的实现
 * Created by sunfei on 2017/6/28 0028.
 */

public class ElegantLivingPresenterImpl extends BasePresenterImpl<ElegantLivingContract.ElegantLivingView> implements ElegantLivingContract.ElegantLivingPresenter,ElegantLivingModelListener {
    /*Presenter作为中间层，持有View和Model的引用*/
    private final ElegantLivingContract.ElegantLivingView elegantLivingView;
    private final ElegantLivingModelImpl elegantLivingModel;

    public ElegantLivingPresenterImpl(@NonNull Context context, @NonNull ElegantLivingContract.ElegantLivingView view) {
        super(context, view);
        this.elegantLivingView=view;
        elegantLivingModel = new ElegantLivingModelImpl();
    }

    /**
     * 获取生活家banner
     */
    @Override
    public void getElegantLivingBanners(int offset){
        elegantLivingView.showLoadDialog();
        elegantLivingModel.getElegantLivingBanners(getCompositeSubscription(),this,offset);
    }

    @Override
    public void onModelSuccess(ElegantLivingEntity.Result result) {
        elegantLivingView.hideLoadDialog();
        List<ElegantLivingEntity.ElegantLivingBean> rows = result.getRows();
        elegantLivingView.updateUi(rows);
    }

    @Override
    public void onModelError() {
        elegantLivingView.updateError();
        elegantLivingView.hideLoadDialog();
    }
}
