package app.mall.com.mvp.ui;

import java.util.ArrayList;

/**
 * desc
 * Created by yangzonghui on 2017/7/26 14:53
 * Email:yangzonghui@simuyun.com
 *  
 */
public class Province {

    private String province;

    private ArrayList<City> city = new ArrayList<>();

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public ArrayList<City> getCity() {
        return city;
    }

    public void setCity(ArrayList<City> city) {
        this.city = city;
    }

    private class City{
        private String n;
        private ArrayList<Area> areas = new ArrayList<>();

        public String getN() {
            return n;
        }

        public void setN(String n) {
            this.n = n;
        }

        public ArrayList<Area> getAreas() {
            return areas;
        }

        public void setAreas(ArrayList<Area> areas) {
            this.areas = areas;
        }
    }


    private class Area{
        private String s = "";

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }
    }
}
