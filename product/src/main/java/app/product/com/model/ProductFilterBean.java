package app.product.com.model;

import com.cgbsoft.lib.base.model.bean.BaseBean;

import java.util.List;

/**
 * desc   产品模块筛选的javabean
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/8-21:23
 */
public class ProductFilterBean extends BaseBean {


    public FilterItem series;
    public FilterItem orderBy;
    public List<FilterItem> filter;

    public List<FilterItem> getFilter() {
        return filter;
    }

    public void setFilter(List<FilterItem> filter) {
        this.filter = filter;
    }

    public FilterItem getSeries() {
        return series;
    }

    public void setSeries(FilterItem series) {
        this.series = series;
    }

    public FilterItem getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(FilterItem orderBy) {
        this.orderBy = orderBy;
    }


}
