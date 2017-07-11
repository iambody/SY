package com.cgbsoft.privatefund.model;

import java.util.List;

/**
 * @author chenlong
 */
public class MineModel {
    public PrivateBank bank;

    public List<Orders> mallOrder;

    public Health healthy;

    public PrivateBank getBank() {
        return bank;
    }

    public void setBank(PrivateBank bank) {
        this.bank = bank;
    }

    public List<Orders> getMallOrder() {
        return mallOrder;
    }

    public void setMallOrder(List<Orders> mallOrder) {
        this.mallOrder = mallOrder;
    }

    public Health getHealthy() {
        return healthy;
    }

    public void setHealthy(Health healthy) {
        this.healthy = healthy;
    }

    public static class PrivateBank {

        private String durationAmt;
        private String durationUnit;
        private String debtAmt;
        private String debtUnit;
        private String equityAmt;
        private String equityUnit;
        private String equityRatio;
        private String debtRatio;

        public String getDebtRatio() {
            return debtRatio;
        }

        public void setDebtRatio(String debtRatio) {
            this.debtRatio = debtRatio;
        }

        public String getDurationAmt() {
            return durationAmt;
        }

        public void setDurationAmt(String durationAmt) {
            this.durationAmt = durationAmt;
        }

        public String getDurationUnit() {
            return durationUnit;
        }

        public void setDurationUnit(String durationUnit) {
            this.durationUnit = durationUnit;
        }

        public String getDebtAmt() {
            return debtAmt;
        }

        public void setDebtAmt(String debtAmt) {
            this.debtAmt = debtAmt;
        }

        public String getDebtUnit() {
            return debtUnit;
        }

        public void setDebtUnit(String debtUnit) {
            this.debtUnit = debtUnit;
        }

        public String getEquityAmt() {
            return equityAmt;
        }

        public void setEquityAmt(String equityAmt) {
            this.equityAmt = equityAmt;
        }

        public String getEquityUnit() {
            return equityUnit;
        }

        public void setEquityUnit(String equityUnit) {
            this.equityUnit = equityUnit;
        }

        public String getEquityRatio() {
            return equityRatio;
        }

        public void setEquityRatio(String equityRatio) {
            this.equityRatio = equityRatio;
        }
    }

    public static class Orders {

        private String goodsStatusName;

        private int count;

        private String goodsStatusCode;

        public String getGoodsStatusName() {
            return goodsStatusName;
        }

        public void setGoodsStatusName(String goodsStatusName) {
            this.goodsStatusName = goodsStatusName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getGoodsStatusCode() {
            return goodsStatusCode;
        }

        public void setGoodsStatusCode(String goodsStatusCode) {
            this.goodsStatusCode = goodsStatusCode;
        }
    }

    public static class Health {

        private String allHealthy;

        private List<HealthItem> HealthItem;

        public String getAllHealthy() {
            return allHealthy;
        }

        public void setAllHealthy(String allHealthy) {
            this.allHealthy = allHealthy;
        }

        public List<MineModel.HealthItem> getHealthItem() {
            return HealthItem;
        }

        public void setHealthItem(List<MineModel.HealthItem> healthItem) {
            HealthItem = healthItem;
        }
    }

    public static class HealthItem {

        private String title;

        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

