package app.product.com.model;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/8-18:14
 */
public class Series implements Cloneable {
    //产品筛选列表中的系列
    private String name;
    private String key;

    //产品列表中用到的 units 数据  //产品的投资单元
//    private String name;
    private String id;
    //产品列表中用到的schemes列表
//    private String id;
    private String schemeName;
    private String state;

    public Series(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Series() {
    }

    @Override
    public Series clone() {
        Series series = null;
        try {
            series = (Series) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return series;
    }
}
