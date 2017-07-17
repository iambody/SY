package app.mall.com.mvp.presenter;


import android.content.Context;
import android.support.annotation.NonNull;

import com.cgbsoft.lib.base.model.ElegantGoodsBeanInterface;
import com.cgbsoft.lib.base.model.ElegantGoodsEntity;
import com.cgbsoft.lib.base.mvp.presenter.impl.BasePresenterImpl;
import com.cgbsoft.lib.utils.tools.LogUtils;

import java.util.ArrayList;
import java.util.List;

import app.mall.com.model.ElegantGoodsModelListener;
import app.mall.com.model.impl.ElegantGoodsModelImpl;
import app.mall.com.mvp.contract.ElegantGoodsContract;

/**
 * Created by sunfei on 2017/6/28 0028.
 */

public class ElegantGoodsPresenterImpl extends BasePresenterImpl<ElegantGoodsContract.ElegantGoodsView> implements ElegantGoodsContract.ElegantGoodsPresenter,ElegantGoodsModelListener {


    private final ElegantGoodsModelImpl elegantGoodsModel;
    private final ElegantGoodsContract.ElegantGoodsView elegantGoodsView;

    public ElegantGoodsPresenterImpl(@NonNull Context context, @NonNull ElegantGoodsContract.ElegantGoodsView view) {
        super(context, view);
        this.elegantGoodsView=view;
        elegantGoodsModel=new ElegantGoodsModelImpl();
    }

    @Override
    public void getElegantGoodsFirst(int offset) {
        elegantGoodsView.showLoadDialog();
        elegantGoodsModel.getElegantGoodsFirst(getCompositeSubscription(),this,offset);
    }

    @Override
    public void getElegantGoodsMore(int offset,String category) {
        LogUtils.Log("aaa","showLoadDialog");
        elegantGoodsView.showLoadDialog();
        elegantGoodsModel.getElegantGoodsMore(getCompositeSubscription(),this,offset,category);
    }

    @Override
    public void onModelFirstSuccess(ElegantGoodsEntity.Result result) {
        ArrayList<ElegantGoodsBeanInterface> datas = new ArrayList<>();
        List<ElegantGoodsEntity.ElegantGoodsCategoryBean> categorys = result.getNavigation();
        for (int i=0;i<categorys.size();i++) {
            ElegantGoodsEntity.ElegantGoodsCategoryBean elegantGoodsCategoryBean = categorys.get(i);
            if (elegantGoodsCategoryBean.getCode().equals("300200")) {
                elegantGoodsCategoryBean.setIsCheck(1);
                break;
            }
        }
        ElegantGoodsEntity.HotListBean hot = result.getHot();
        List<ElegantGoodsEntity.HotListItemBean> rows = hot.getRows();
        if (null != rows && rows.size() > 0) {
            String hotName = hot.getText();
            ElegantGoodsEntity.ElegantGoodsTitleBean elegantGoodsTitleBean = new ElegantGoodsEntity.ElegantGoodsTitleBean();
            elegantGoodsTitleBean.setName(hotName);
            elegantGoodsTitleBean.setCustomItemType(0);//类型0为标题
            datas.add(elegantGoodsTitleBean);
            for (ElegantGoodsEntity.HotListItemBean hotItem : rows) {
                hotItem.setCustomItemType(1);//类型1为热门清单
            }
            datas.addAll(rows);
        }
        ElegantGoodsEntity.AllNewsBean all = result.getAll();
        List<ElegantGoodsEntity.AllNewsItemBean> allRows = all.getRows();
        if (null != allRows && allRows.size() > 0) {
            String allName = all.getText();
            ElegantGoodsEntity.ElegantGoodsTitleBean elegantGoodsTitleBean = new ElegantGoodsEntity.ElegantGoodsTitleBean();
            elegantGoodsTitleBean.setName(allName);
            elegantGoodsTitleBean.setCustomItemType(0);//类型0为标题
            datas.add(elegantGoodsTitleBean);
            for (ElegantGoodsEntity.AllNewsItemBean allItem : allRows) {
                allItem.setCustomItemType(2);//类型2为全部清单
            }
            datas.addAll(allRows);
        }
        elegantGoodsView.updateUi(categorys,datas);
        elegantGoodsView.hideLoadDialog();
    }

    @Override
    public void onModelFirstError(Throwable error) {
        elegantGoodsView.updateFirstError(error);
        elegantGoodsView.hideLoadDialog();
//        try {
//            StringBuilder sb = new StringBuilder();
//            InputStream open = getContext().getResources().getAssets().open("result.json");
//            BufferedReader bis = new BufferedReader(new InputStreamReader(open));
//            String line = "";
//            while ((line = bis.readLine()) != null) {
//                sb.append(line);
//            }
//            String sbs=sb.toString();
//            ElegantGoodsEntity.Result result = new Gson().fromJson(sbs, ElegantGoodsEntity.Result.class);
//            onModelFirstSuccess(result);
//        } catch (IOException e) {
//            LogUtils.Log("aaa","printStackTrace===");
//            e.printStackTrace();
//        }
    }

    @Override
    public void onModelMoreSuccess(ElegantGoodsEntity.ResultMore result) {
        ArrayList<ElegantGoodsBeanInterface> datas = new ArrayList<>();
        List<ElegantGoodsEntity.AllNewsItemBean> allRows = result.getRows();
        if (null != allRows && allRows.size() > 0) {
            for (ElegantGoodsEntity.AllNewsItemBean allItem : allRows) {
                allItem.setCustomItemType(2);//类型2为全部清单
            }
            datas.addAll(allRows);
        }
        elegantGoodsView.updateUiMore(datas);
        elegantGoodsView.hideLoadDialog();
    }

    @Override
    public void onModelMoreError(Throwable error) {
        elegantGoodsView.updateMoreError(error);
        elegantGoodsView.hideLoadDialog();
    }
}
