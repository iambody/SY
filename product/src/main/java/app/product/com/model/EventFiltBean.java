package app.product.com.model;

import java.util.List;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/10-21:55
 */
public class EventFiltBean {
    private List<FilterItem>filterItemList;

    public EventFiltBean() {
    }

    public EventFiltBean(List<FilterItem> filterItemList) {
        this.filterItemList = filterItemList;
    }

    public List<FilterItem> getFilterItemList() {
        return filterItemList;
    }

    public void setFilterItemList(List<FilterItem> filterItemList) {
        this.filterItemList = filterItemList;
    }
}
