package com.cgbsoft.privatefund.model;

/**
 * @author chenlong
 */
//{
//        "message": "",
//        "result": {
//        "sxbInfo": {
//        "growThrate":"1.34",
//        "allud":"3452",
//        "survivingAssets":"100000000",
//        "yestincome":"1111",
//        "addincome":"22.87"
//        },
//        "hasAccount": "1",
//        "gmInfo": {
//        "addincome": "61061.06",
//        "yestincome": "0.00",
//        "survivingAssets": "741741.75"
//        }
//        },
//        "code": ""
//        }

//    {
//            "message": "",
//            "result": {
//            "sxbInfo": {
//            "growThrate": "0.0000",
//            "riskLevel": "03",
//            "fundcode": "202107",
//            "addincome": "2840.36",
//            "allud": "0.00",
//            "buyIn": true,
//            "yestincome": "713.61",
//            "survivingAssets": "5981228.50"
//            },
//            "hasAccount": true,
//            "gmInfo": {
//            "addincome": "222108096.00",
//            "buyIn": true,
//            "yestincome": "57926452.00",
//            "survivingAssets": "500422213632.00"
//            }
//            },
//            "code": ""
//            }

public class FinancialAssertModel {

    private String hasAccount;

    private PrivateTreasure sxbInfo;

    private PublicFund gmInfo;

    public String getHasAccount() {
        return hasAccount;
    }

    public void setHasAccount(String hasAccount) {
        this.hasAccount = hasAccount;
    }

    public PrivateTreasure getSxbInfo() {
        return sxbInfo;
    }

    public void setSxbInfo(PrivateTreasure sxbInfo) {
        this.sxbInfo = sxbInfo;
    }

    public PublicFund getGmInfo() {
        return gmInfo;
    }

    public void setGmInfo(PublicFund gmInfo) {
        this.gmInfo = gmInfo;
    }

    public class PrivateTreasure {

        private String buyIn;

        private String growThrate;

        private String riskLevel;

        private String fundCode;

        private String allUD;

        private String survivingAssets;

        private String addIncome;

        private String yestIncome;

        public String isBuyIn() {
            return buyIn;
        }

        public void setBuyIn(String buyIn) {
            this.buyIn = buyIn;
        }

        public String getRiskLevel() {
            return riskLevel;
        }

        public void setRiskLevel(String riskLevel) {
            this.riskLevel = riskLevel;
        }

        public String getBuyIn() {
            return buyIn;
        }

        public String getFundcode() {
            return fundCode;
        }

        public void setFundcode(String fundcode) {
            this.fundCode = fundcode;
        }

        public String getSurvivingAssets() {
            return survivingAssets;
        }

        public void setSurvivingAssets(String survivingAssets) {
            this.survivingAssets = survivingAssets;
        }

        public String getGrowThrate() {
            return growThrate;
        }

        public void setGrowThrate(String growThrate) {
            this.growThrate = growThrate;
        }

        public String getAllud() {
            return allUD;
        }

        public void setAllud(String allud) {
            this.allUD = allud;
        }

        public String getAddincome() {
            return addIncome;
        }

        public void setAddincome(String addincome) {
            this.addIncome = addincome;
        }

        public String getYestincome() {
            return yestIncome;
        }

        public void setYestincome(String yestincome) {
            this.yestIncome = yestincome;
        }
    }

    public class PublicFund {

        private String buyIn;

        private String survivingAssets;

        private String addIncome;

        private String yestIncome;

        public String getSurvivingAssets() {
            return survivingAssets;
        }

        public void setSurvivingAssets(String survivingAssets) {
            this.survivingAssets = survivingAssets;
        }

        public String getAddincome() {
            return addIncome;
        }

        public void setAddincome(String addincome) {
            this.addIncome = addincome;
        }

        public String getYestincome() {
            return yestIncome;
        }

        public void setYestincome(String yestincome) {
            this.yestIncome = yestincome;
        }

        public String isBuyIn() {
            return buyIn;
        }

        public void setBuyIn(String buyIn) {
            this.buyIn = buyIn;
        }
    }
}
