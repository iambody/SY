package com.cgbsoft.privatefund.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author chenlong
 */
public  class DiscoveryListModel implements Parcelable {

    private String id;

    private String image;

    private String category;

    private String title;

    private String views;

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.image);
        dest.writeString(this.category);
        dest.writeString(this.title);
        dest.writeString(this.views);
    }

    protected DiscoveryListModel(Parcel in) {
        this.id = in.readString();
        this.image = in.readString();
        this.category = in.readString();
        this.title = in.readString();
        this.views = in.readString();
    }

    public static final Creator<DiscoveryListModel> CREATOR = new Creator<DiscoveryListModel>() {
        @Override
        public DiscoveryListModel createFromParcel(Parcel source) {
            return new DiscoveryListModel(source);
        }

        @Override
        public DiscoveryListModel[] newArray(int size) {
            return new DiscoveryListModel[size];
        }
    };
}