package app.product.com.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    /**
     * 过滤掉特色字符
     * @return
     */
    public static String replaceSpeialStr(String resouceStr) {
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(resouceStr);
        return m.replaceAll("").trim();
    }
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
