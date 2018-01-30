package com.cgbsoft.privatefund.model;

/**
 * @author chenlong
 */

public class FinancialAssertModel {

    private String openAccount;

    private PrivateTreasure enjoyMoneyBaby;

    private PublicFund publicFund;

    public String getOpenAccount() {
        return openAccount;
    }

    public void setOpenAccount(String openAccount) {
        this.openAccount = openAccount;
    }

    public PrivateTreasure getEnjoyMoneyBaby() {
        return enjoyMoneyBaby;
    }

    public void setEnjoyMoneyBaby(PrivateTreasure enjoyMoneyBaby) {
        this.enjoyMoneyBaby = enjoyMoneyBaby;
    }

    public PublicFund getPublicFund() {
        return publicFund;
    }

    public void setPublicFund(PublicFund publicFund) {
        this.publicFund = publicFund;
    }

    public class PrivateTreasure {

        private String isBuyin;

        private String incomeByYear;

        private String incomeByTenThousand;

        private String survivingAssets;

        private String benefitOfCarry;

        private String incomeByYesterday;

        public String getIsBuyin() {
            return isBuyin;
        }

        public void setIsBuyin(String isBuyin) {
            this.isBuyin = isBuyin;
        }

        public String getIncomeByYear() {
            return incomeByYear;
        }

        public void setIncomeByYear(String incomeByYear) {
            this.incomeByYear = incomeByYear;
        }

        public String getIncomeByTenThousand() {
            return incomeByTenThousand;
        }

        public void setIncomeByTenThousand(String incomeByTenThousand) {
            this.incomeByTenThousand = incomeByTenThousand;
        }

        public String getSurvivingAssets() {
            return survivingAssets;
        }

        public void setSurvivingAssets(String survivingAssets) {
            this.survivingAssets = survivingAssets;
        }

        public String getBenefitOfCarry() {
            return benefitOfCarry;
        }

        public void setBenefitOfCarry(String benefitOfCarry) {
            this.benefitOfCarry = benefitOfCarry;
        }

        public String getIncomeByYesterday() {
            return incomeByYesterday;
        }

        public void setIncomeByYesterday(String incomeByYesterday) {
            this.incomeByYesterday = incomeByYesterday;
        }
    }

    public class PublicFund {

        private String isBuyin;

        private String survivingAssets;

        private String benefitOfCarry;

        private String incomeByYesterday;

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

        public String getBenefitOfCarry() {
            return benefitOfCarry;
        }

        public void setBenefitOfCarry(String benefitOfCarry) {
            this.benefitOfCarry = benefitOfCarry;
        }

        public String getIncomeByYesterday() {
            return incomeByYesterday;
        }

        public void setIncomeByYesterday(String incomeByYesterday) {
            this.incomeByYesterday = incomeByYesterday;
        }
    }
}
