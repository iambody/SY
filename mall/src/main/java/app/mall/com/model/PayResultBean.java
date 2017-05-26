package app.mall.com.model;

/**
 * desc
 * Created by yangzonghui on 2017/4/23 16:57
 * Email:yangzonghui@simuyun.com
 *  
 */
public class PayResultBean {
    public PayResultBean(String result, float price, int payType) {
        this.result = result;
        this.price = price;
        this.payType = payType;
    }

    private String result;
    private float price;
    private int payType;

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
