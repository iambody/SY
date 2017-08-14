package com.cgbsoft.privatefund.mvp.presenter.center;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.model.CardListEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.privatefund.model.CardListModelListener;
import com.cgbsoft.privatefund.model.impl.CardModelImpl;
import com.cgbsoft.privatefund.mvp.contract.center.CardCollectContract;

import java.util.List;

/**
 * Created by fei on 2017/8/10.
 */

public class CardCollectPresenterImpl extends BasePresenterImpl<CardCollectContract.CardCollectView> implements CardCollectContract.CardCollectPresenter ,CardListModelListener{
    private final CardCollectContract.CardCollectView cardView;
    private final CardModelImpl cardModel;

    public CardCollectPresenterImpl(@NonNull Context context, @NonNull CardCollectContract.CardCollectView view) {
        super(context, view);
        this.cardView=view;
        cardModel=new CardModelImpl();
    }

    @Override
    public void getCardList(String indentityCode) {
        cardView.showLoadDialog();
        cardModel.getCardList(getCompositeSubscription(),this,indentityCode);
    }

    @Override
    public void getDataSuccess(List<CardListEntity.CardBean> cards) {
        cardView.hideLoadDialog();
        cardView.getCardListSuccess(cards);
    }

    @Override
    public void getDataError(Throwable error) {
        cardView.hideLoadDialog();
    }
}
