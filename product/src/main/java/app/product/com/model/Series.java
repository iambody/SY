package app.product.com.model;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/8-18:14
 */
public class Series implements Cloneable{
    private String name;
    private String key;

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

    @Override
    public Series clone(){
        Series series = null;
        try {
            series = (Series) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return series;
    }
}
