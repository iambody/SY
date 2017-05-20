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
 * Created by lee on 2016/11/10.
 */
@MessageTag(value = "YT:pdfMsg", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class PdfMessage extends MessageContent {

    private String pdfUrl;
    private String pdfName;


    public PdfMessage() {
    }
    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("pdfUrl",getPdfUrl());
            jsonObj.put("pdfName",getPdfName());
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


    public PdfMessage(Parcel in) {
        setPdfUrl(ParcelUtils.readFromParcel(in));
        setPdfName(ParcelUtils.readFromParcel(in));
    }

    public PdfMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {

        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            pdfUrl = jsonObj.optString("pdfUrl");
            pdfName = jsonObj.optString("pdfName");

        } catch (JSONException e) {
            RLog.e("JSONException", e.getMessage());
        }

    }

    /**
     * 构建一个产品消息实例。
     *
     * @return 产品消息实例。
     */
    public static PdfMessage obtain(String pdfUrl, String pdfName) {
        PdfMessage message = new PdfMessage();
        message.setPdfUrl(pdfUrl);
        message.setPdfName(pdfName);
        return message;
    }




    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<PdfMessage> CREATOR = new Creator<PdfMessage>() {

        @Override
        public PdfMessage createFromParcel(Parcel source) {
            return new PdfMessage(source);
        }

        @Override
        public PdfMessage[] newArray(int size) {
            return new PdfMessage[size];
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

        ParcelUtils.writeToParcel(dest, pdfUrl);
        ParcelUtils.writeToParcel(dest, pdfName);
    }


    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }
}
