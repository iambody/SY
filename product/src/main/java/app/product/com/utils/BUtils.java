package app.product.com.utils;

import java.util.ArrayList;
import java.util.List;

import app.product.com.model.Series;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/9-09:34
 */
public class BUtils {
    public static List<Series> arrayListClone(List<Series> list) {
        List<Series> seriesList = new ArrayList<>();
        for (Series series1 : list) {
            seriesList.add(series1.clone());
        }
        return seriesList;
    }
}
