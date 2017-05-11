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
    //这个是在筛选标签时候我们需要进行的标记是否选择过了
    private boolean isChecked;


    public Series(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public Series(String name, String key, boolean isChecked) {
        this.name = name;
        this.key = key;
        this.isChecked = isChecked;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
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
