package app.mall.com.model;

import java.io.Serializable;

/**
 * desc 充值方式
 * Created by yangzonghui on 2017/4/27 13:54
 * Email:yangzonghui@simuyun.com
 *  
 */
public class PayMethod implements Serializable {
    private String name;
    private int maxLimit;
    private String typeCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    public int getTypeCode() {
        return Integer.decode(typeCode);
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}
