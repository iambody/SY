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

public class FinancialAssertModel {

    private String openAccount;

    private PrivateTreasure sxbInfo;

    private PublicFund gmInfo;

    public String getOpenAccount() {
        return openAccount;
    }

    public void setOpenAccount(String openAccount) {
        this.openAccount = openAccount;
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

        private String isBuyin;

        private String growThrate;

        private String allud;

        private String survivingAssets;

        private String addincome;

        private String yestincome;

        public String getIsBuyin() {
            return isBuyin;
        }

        public void setIsBuyin(String isBuyin) {
            this.isBuyin = isBuyin;
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
            return allud;
        }

        public void setAllud(String allud) {
            this.allud = allud;
        }

        public String getAddincome() {
            return addincome;
        }

        public void setAddincome(String addincome) {
            this.addincome = addincome;
        }

        public String getYestincome() {
            return yestincome;
        }

        public void setYestincome(String yestincome) {
            this.yestincome = yestincome;
        }
    }

    public class PublicFund {

        private String isBuyin;

        private String survivingAssets;

        private String addincome;

        private String yestincome;

        public String getIsBuyin() {
            return isBuyin;
        }

        public void setIsBuyin(String isBuyin) {
            this.isBuyin = isBuyin;
        }

        public String getSurvivingAssets() {
            return survivingAssets;
        }

        public void setSurvivingAssets(String survivingAssets) {
            this.survivingAssets = survivingAssets;
        }

        public String getAddincome() {
            return addincome;
        }

        public void setAddincome(String addincome) {
            this.addincome = addincome;
        }

        public String getYestincome() {
            return yestincome;
        }

        public void setYestincome(String yestincome) {
            this.yestincome = yestincome;
        }
    }
}
