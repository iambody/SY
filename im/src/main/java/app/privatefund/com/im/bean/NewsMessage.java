package app.privatefund.com.im.bean;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by lee on 2016/11/17
 */
@MessageTag(value = "YT:newsMsg", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class NewsMessage extends MessageContent {
    private String summary;

    private String category;

    private String title;

    private String likes;

    private String infoId;

    private String date;

    private String url;

    private String isLike;

    public NewsMessage() {
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("summary",getSummary());
            jsonObj.put("category",getCategory());
            jsonObj.put("title",getTitle());
            jsonObj.put("likes",getLikes());
            jsonObj.put("infoId",getInfoId());
            jsonObj.put("date",getDate());
            jsonObj.put("url",getUrl());
            jsonObj.put("isLike", getIsLike());
            Log.e("asdasdasd", jsonObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public NewsMessage(Parcel in) {
        setSummary(ParcelUtils.readFromParcel(in));
        setCategory(ParcelUtils.readFromParcel(in));
        setTitle(ParcelUtils.readFromParcel(in));
        setLikes(ParcelUtils.readFromParcel(in));
        setInfoId(ParcelUtils.readFromParcel(in));
        setDate(ParcelUtils.readFromParcel(in));
        setUrl(ParcelUtils.readFromParcel(in));
        setIsLike(ParcelUtils.readFromParcel(in));
    }

    public NewsMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {

        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            summary = jsonObj.optString("summary");
            category = jsonObj.optString("category");
            title = jsonObj.optString("title");
            likes = jsonObj.optString("likes");
            infoId = jsonObj.optString("infoId");
            date = jsonObj.optString("date");
            url = jsonObj.optString("url");
            isLike = jsonObj.optString("isLike");
            Log.e("asdasdasd", jsonObj.toString());



        } catch (JSONException e) {
            RLog.e("JSONException", e.getMessage());
        }

    }

    /**
     * 构建一个产品消息实例。
     *
     * @return 产品消息实例。
     */
    public static NewsMessage obtain(String summary, String category, String title, String likes, String infoId, String date, String url, String isLike) {
        NewsMessage message = new NewsMessage();
        message.setSummary(summary);
        message.setCategory(category);
        message.setTitle(title);
        message.setLikes(likes);
        message.setInfoId(infoId);
        message.setDate(date);
        message.setUrl(url);
        message.setIsLike(isLike);
        return message;
    }




    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<NewsMessage> CREATOR = new Creator<NewsMessage>() {

        @Override
        public NewsMessage createFromParcel(Parcel source) {
            return new NewsMessage(source);
        }

        @Override
        public NewsMessage[] newArray(int size) {
            return new NewsMessage[size];
        }
    };

    /**
     * 描述了包含在 Parcelable 对象排列信息中的特殊对象的类型。
     *
     * @return 一个标志位，表明Parcelable对象特殊对象类型集合的排列。
     */
    public int describeContents() {
        return 0;
    }

    /**
     * 将类的数据写入外部提供的 Parcel 中。
     *
     * @param dest  对象被写入的 Parcel。
     * @param flags 对象如何被写入的附加标志。
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        ParcelUtils.writeToParcel(dest, summary);
        ParcelUtils.writeToParcel(dest, category);
        ParcelUtils.writeToParcel(dest, title);
        ParcelUtils.writeToParcel(dest, likes);
        ParcelUtils.writeToParcel(dest, infoId);
        ParcelUtils.writeToParcel(dest, date);
        ParcelUtils.writeToParcel(dest, url);
        ParcelUtils.writeToParcel(dest, isLike);
    }


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

}
