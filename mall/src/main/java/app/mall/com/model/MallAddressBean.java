package app.mall.com.model;

import com.cgbsoft.lib.widget.recycler.BaseModel;

import java.io.Serializable;

/**
 * desc  商城地址
 * Created by yangzonghui on 2017/5/10 12:48
 * Email:yangzonghui@simuyun.com
 *  
 */
public class MallAddressBean extends BaseModel implements Serializable{
    public static int LIST = 0;

    private String shopping_name;
    private String address;
    private String phone;
    private int default_flag;
    private String id;

    public MallAddressBean(String shopping_name, String address, String phone, int default_flag, String id) {
        this.shopping_name = shopping_name;
        this.address = address;
        this.phone = phone;
        this.default_flag = default_flag;
        this.id = id;
    }

    public String getShopping_name() {
        return shopping_name;
    }

    public void setShopping_name(String shopping_name) {
        this.shopping_name = shopping_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDefault_flag() {
        return default_flag;
    }

    public void setDefault_flag(int default_flag) {
        this.default_flag = default_flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
