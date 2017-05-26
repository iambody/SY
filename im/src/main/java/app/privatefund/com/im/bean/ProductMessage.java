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
 * 产品消息
 * Created by lee on 2016/5/23.
 */
@MessageTag(value = "YT:productMsg", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class ProductMessage extends MessageContent {
    private String productName;
    private String productType;
    private String productId;
    private String typeCode,schemeId;

    public ProductMessage() {
    }

    public ProductMessage(Parcel in) {
        setProductId(ParcelUtils.readFromParcel(in));
        setSchemeId(ParcelUtils.readFromParcel(in));
        setProductName(ParcelUtils.readFromParcel(in));
        setProductType(ParcelUtils.readFromParcel(in));
        setTypeCode(ParcelUtils.readFromParcel(in));
    }

    public ProductMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(jsonStr);
            productId = jsonObj.optString("productId");
            schemeId = jsonObj.optString("schemeId");
            productName = jsonObj.optString("productName");
            productType = jsonObj.optString("productType");
            typeCode = jsonObj.optString("typeCode");
        } catch (Exception e) {
            RLog.e("JSONException", e.getMessage());
        }

    }

    /**
     * 构建一个产品消息实例。
     *
     * @return 产品消息实例。
     */
    public static ProductMessage obtain(String productType, String productId, String schemeId, String productName, String typeCode) {
        ProductMessage message = new ProductMessage();
        message.setProductId(productId);
        message.setSchemeId(schemeId);
        message.setProductName(productName);
        message.setProductType(productType);
        message.setTypeCode(typeCode);
        return message;
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("productId",getProductId());
            jsonObj.put("schemeId",getSchemeId());
            jsonObj.put("productName",getProductName());
            jsonObj.put("productType",getProductType());
            jsonObj.put("typeCode",getTypeCode());
            Log.e("asdasdasd",jsonObj.toString());
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


    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<ProductMessage> CREATOR = new Creator<ProductMessage>() {

        @Override
        public ProductMessage createFromParcel(Parcel source) {
            return new ProductMessage(source);
        }

        @Override
        public ProductMessage[] newArray(int size) {
            return new ProductMessage[size];
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

        ParcelUtils.writeToParcel(dest, productId);
        ParcelUtils.writeToParcel(dest, schemeId);
        ParcelUtils.writeToParcel(dest, productName);
        ParcelUtils.writeToParcel(dest, productType);
        ParcelUtils.writeToParcel(dest, typeCode);
    }
}
