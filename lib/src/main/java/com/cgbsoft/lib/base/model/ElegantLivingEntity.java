package com.cgbsoft.lib.base.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

import java.util.List;

/**
 * Created by sunfei on 2017/6/30 0030.
 */

public class ElegantLivingEntity extends BaseResult<ElegantLivingEntity.Result> {
    public static class Result{
        private List<ElegantLivingBean> rows;

        public List<ElegantLivingBean> getRows() {
            return rows;
        }

        public void setRows(List<ElegantLivingBean> rows) {
            this.rows = rows;
        }
    }

    public static class ElegantLivingBean implements Parcelable {

        private String url;
        private String banner;
        private String title;
        private String code;


        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        protected ElegantLivingBean(Parcel in) {
            url = in.readString();
            banner = in.readString();
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }

        public ElegantLivingBean() {
        }

        public ElegantLivingBean(String url, String banner) {
            this.url = url;
            this.banner = banner;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(url);
            dest.writeString(banner);
        }
        public static final Creator<ElegantLivingBean> CREATOR = new Creator<ElegantLivingBean>() {
            @Override
            public ElegantLivingBean createFromParcel(Parcel in) {
                return new ElegantLivingBean(in);
            }

            @Override
            public ElegantLivingBean[] newArray(int size) {
                return new ElegantLivingBean[size];
            }
        };

        @Override
        public String toString() {
            return "ElegantLivingBean{" + "url='" + url + '\'' + ", banner='" + banner + '\'' + '}';
        }
    }
}
