package com.cgbsoft.lib.utils.previewphoto;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * /**
 * 类       名:
 * 说       明: 图片预览
 */

public class PrePhotoInfo implements Parcelable {

    private String path;
//    private String smallPath;
//    private int smallHeight;
//    private int smallWidth;
//    private String localPath;


    public PrePhotoInfo() {

    }

    public PrePhotoInfo(Parcel in) {
        path = in.readString();
//        smallPath = in.readString();
//        smallHeight = in.readInt();
//        smallWidth = in.readInt();
//        localPath = in.readString();
    }

    public static final Creator<PrePhotoInfo> CREATOR = new Creator<PrePhotoInfo>() {
        @Override
        public PrePhotoInfo createFromParcel(Parcel in) {
            return new PrePhotoInfo(in);
        }

        @Override
        public PrePhotoInfo[] newArray(int size) {
            return new PrePhotoInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
//        dest.writeString(smallPath);
//        dest.writeInt(smallHeight);
//        dest.writeInt(smallWidth);
//        dest.writeString(localPath);
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
//
//    public String getSmallPath() {
//        return smallPath;
//    }
//
//    public void setSmallPath(String smallPath) {
//        this.smallPath = smallPath;
//    }
//
//    public int getSmallHeight() {
//        return smallHeight;
//    }
//
//    public void setSmallHeight(int smallHeight) {
//        this.smallHeight = smallHeight;
//    }
//
//    public int getSmallWidth() {
//        return smallWidth;
//    }
//
//    public void setSmallWidth(int smallWidth) {
//        this.smallWidth = smallWidth;
//    }
//
//    public String getLocalPath() {
//        return localPath;
//    }
//
//    public void setLocalPath(String localPath) {
//        this.localPath = localPath;
//    }
}
