package com.cgbsoft.privatefund.model;

import java.util.List;

/**
 * @author chenlong
 */
public class MineModel {

    public MineUserInfo myInfo;

    public PrivateBank bank;

    public List<Orders> mallOrder;

    public Health healthy;

    private HealthOrder healthOrder;

    public MineUserInfo getMyInfo() {
        return myInfo;
    }

    public void setMyInfo(MineUserInfo myInfo) {
        this.myInfo = myInfo;
    }

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

    public HealthOrder getHealthOrder() {
        return healthOrder;
    }

    public void setHealthOrder(HealthOrder healthOrder) {
        this.healthOrder = healthOrder;
    }

    public static class MineUserInfo {

        private String headImageUrl;

        private String nickName;

        private String adviserName;

        private String ydTotal;

        private String memberValue;

        private String memberLevel;

        private String memberBalance;

        public String getHeadImageUrl() {
            return headImageUrl;
        }

        public void setHeadImageUrl(String headImageUrl) {
            this.headImageUrl = headImageUrl;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAdviserName() {
            return adviserName;
        }

        public void setAdviserName(String adviserName) {
            this.adviserName = adviserName;
        }

        public String getYdTotal() {
            return ydTotal;
        }

        public void setYdTotal(String ydTotal) {
            this.ydTotal = ydTotal;
        }

        public String getMemberValue() {
            return memberValue;
        }

        public void setMemberValue(String memberValue) {
            this.memberValue = memberValue;
        }

        public String getMemberLevel() {
            return memberLevel;
        }

        public void setMemberLevel(String memberLevel) {
            this.memberLevel = memberLevel;
        }

        public String getMemberBalance() {
            return memberBalance;
        }

        public void setMemberBalance(String memberBalance) {
            this.memberBalance = memberBalance;
        }
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

        private List<HealthItem> content;

        public String getAllHealthy() {
            return allHealthy;
        }

        public void setAllHealthy(String allHealthy) {
            this.allHealthy = allHealthy;
        }

        public List<HealthItem> getContent() {
            return content;
        }

        public void setContent(List<HealthItem> content) {
            this.content = content;
        }
    }

    public static class HealthItem {

        private String title;

        private String url;

        private String healthId;

        private String code;

        private String consultTime;

        private String imageUrl;

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

        public String getHealthId() {
            return healthId;
        }

        public void setHealthId(String healthId) {
            this.healthId = healthId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getConsultTime() {
            return consultTime;
        }

        public void setConsultTime(String consultTime) {
            this.consultTime = consultTime;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    public static class HealthOrder {

        private List<HealthOrderItem> content;

        public List<HealthOrderItem> getContent() {
            return content;
        }

        public void setContent(List<HealthOrderItem> content) {
            this.content = content;
        }

        public class HealthOrderItem {

            private String custCredentialsNumber;
            private String state;
            private String healthItemValues;
            private String createTime;
            private String imageUrl;
            private String orderCode;

            public String getCustCredentialsNumber() {
                return custCredentialsNumber;
            }

            public void setCustCredentialsNumber(String custCredentialsNumber) {
                this.custCredentialsNumber = custCredentialsNumber;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getHealthItemValues() {
                return healthItemValues;
            }

            public void setHealthItemValues(String healthItemValues) {
                this.healthItemValues = healthItemValues;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getOrderCode() {
                return orderCode;
            }

            public void setOrderCode(String orderCode) {
                this.orderCode = orderCode;
            }
        }
    }

}

