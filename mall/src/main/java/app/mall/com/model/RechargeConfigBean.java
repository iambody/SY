package app.mall.com.model;

import java.io.Serializable;
import java.util.List;

/**
 * desc
 * Created by yangzonghui on 2017/5/21 15:15
 * Email:yangzonghui@simuyun.com
 *  
 */
public class RechargeConfigBean implements Serializable {
    private String waresId;
    private String notifyUrl;
    private String titleRightImg;
    private String titleRightUrl;
    private List<RechargeLevel> rechargeLevel;
    private List<PayMethod> chargeLevel;

    public List<PayMethod> getPayMethodList() {
        return chargeLevel;
    }

    public void setPayMethodList(List<PayMethod> chargeLevel) {
        this.chargeLevel = chargeLevel;
    }

    public static class RechargeLevel implements Serializable {
        private int ydAmount;
        private double donationRatio;

        public int getYdAmount() {
            return ydAmount;
        }

        public void setYdAmount(int ydAmount) {
            this.ydAmount = ydAmount;
        }

        public double getDonationRatio() {
            return donationRatio;
        }

        public void setDonationRatio(double donationRatio) {
            this.donationRatio = donationRatio;
        }
    }

    public String getWaresId() {
        return waresId;
    }

    public void setWaresId(String waresId) {
        this.waresId = waresId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public List<RechargeLevel> getLevels() {
        return rechargeLevel;
    }

    public void setLevels(List<RechargeLevel> levels) {
        rechargeLevel = levels;
    }

    public String getTitleRightImg() {
        return titleRightImg;
    }

    public void setTitleRightImg(String titleRightImg) {
        this.titleRightImg = titleRightImg;
    }

    public String getTitleRightUrl() {
        return titleRightUrl;
    }

    public void setTitleRightUrl(String titleRightUrl) {
        this.titleRightUrl = titleRightUrl;
    }
}