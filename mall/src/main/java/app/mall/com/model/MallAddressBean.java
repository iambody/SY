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

    private String shoppingName;
    private String address;
    private String phone;
    private String defaultFlag;
    private String id;
    private String regionAddress;

    public MallAddressBean(String shopping_name, String address, String phone, String default_flag, String id,String regionAddress) {
        this.shoppingName = shopping_name;
        this.address = address;
        this.phone = phone;
        this.defaultFlag = default_flag;
        this.id = id;
        this.regionAddress = regionAddress;
    }

    public String getShopping_name() {
        return shoppingName;
    }

    public void setShopping_name(String shopping_name) {
        this.shoppingName = shopping_name;
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

    public String getDefault_flag() {
        return defaultFlag;
    }

    public void setDefault_flag(String default_flag) {
        this.defaultFlag = default_flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegionAddress() {
        return regionAddress;
    }

    public void setRegionAddress(String regionAddress) {
        this.regionAddress = regionAddress;
    }
}
