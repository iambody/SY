package app.product.com.model;

import android.text.TextUtils;

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

    public static String formatSeries(String targ) {
        String series;
        targ = TextUtils.isEmpty(targ) ? "0" : targ;
        switch (targ) {
            case "0"://"泰山":
                series = "1";
                break;
            case "1":
                series = "1";
                break;
            case "2":
                series = "1";
                break;
            case "3"://"恒山":
                series = "1";
                break;
            case "4"://"嵩山":
                series = "1";
                break;
            case "6"://"黄河":
                series = "2";
                break;
            case "5"://"长江":
                series = "2";
                break;
            case "7"://"澜沧江":
                series = "2";
                break;
            case "8"://"亚马逊":
                series = "2";
                break;
            case "9"://"昆仑山":
                series = "1";
                break;
            default:
                series = "0";
                break;
        }
        return series;
    }
}
