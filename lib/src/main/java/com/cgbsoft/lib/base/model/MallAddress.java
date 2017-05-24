package com.cgbsoft.lib.base.model;

/**
 * desc
 * Created by yangzonghui on 2017/5/21 18:21
 * Email:yangzonghui@simuyun.com
 *  
 */
public class MallAddress {
    private String phone;
    private String id;
    private String name;
    private String address;

    public MallAddress(String phone, String id, String name, String address) {
        this.phone = phone;
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
