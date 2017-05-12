package app.product.com.model;

import com.cgbsoft.lib.base.model.bean.BaseBean;

import java.util.List;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/8-21:49
 */
public class FilterItem extends BaseBean {
    private String key;//
    private String name;//":"排序",
    private String type;//":"radio",
    private List<Series> items;
    //如果是需要输入框的条件 也就是text 输入时候的最大最小数
    private String minNumber="";
    private String MaxNumber="";
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Series> getItems() {
        return items;
    }

    public void setItems(List<Series> items) {
        this.items = items;
    }

    public String getMinNumber() {
        return minNumber;
    }

    public void setMinNumber(String minNumber) {
        this.minNumber = minNumber;
    }

    public String getMaxNumber() {
        return MaxNumber;
    }

    public void setMaxNumber(String maxNumber) {
        MaxNumber = maxNumber;
    }
}
