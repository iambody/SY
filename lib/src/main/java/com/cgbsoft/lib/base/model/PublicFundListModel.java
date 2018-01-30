package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.model.bean.StockIndexBean;

import java.util.List;

/**
 * @author chenlong
 */

public class PublicFundListModel {

    public List<StockIndexBean> list;

    public List<StockIndexBean> getList() {
        return list;
    }

    public void setList(List<StockIndexBean> list) {
        this.list = list;
    }
}
