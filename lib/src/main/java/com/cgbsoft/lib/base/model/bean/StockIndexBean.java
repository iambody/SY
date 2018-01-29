package com.cgbsoft.lib.base.model.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlong
 */
public class StockIndexBean {

    private String id;

    private String name;

    private String index;

    private String rate;

    private String gain;

    private String turnover;

    private String volume;

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

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getGain() {
        return gain;
    }

    public void setGain(String gain) {
        this.gain = gain;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }


    public static List<StockIndexBean> fillValueFromJson(JSONArray jsonArray) {
        List<StockIndexBean> resultList = new ArrayList<>();
        try {
            if (jsonArray != null) {
                for (int i = 0 ; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    StockIndexBean stockIndexBean = new StockIndexBean();
                    stockIndexBean.setGain(jsonObject.getString("gain"));
                    stockIndexBean.setId(jsonObject.getString("id"));
                    stockIndexBean.setIndex(jsonObject.getString("index"));
                    stockIndexBean.setName(jsonObject.getString("name"));
                    stockIndexBean.setRate(jsonObject.getString("rate"));
                    stockIndexBean.setTurnover(jsonObject.getString("turnover"));
                    stockIndexBean.setVolume(jsonObject.getString("volume"));
                    resultList.add(stockIndexBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
