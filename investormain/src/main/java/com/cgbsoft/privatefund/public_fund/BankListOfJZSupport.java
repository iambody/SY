package com.cgbsoft.privatefund.public_fund;

import java.util.List;

/**
 * Created by wangpeng on 18-1-30.
 * <p>
 * 金证支持的银行卡列表类
 */

public class BankListOfJZSupport {
    private String errorCode; // "0000" 代表成功
    private String errorMessage; // 消息
    private List<List<BankOfJZSupport>> datasets; // 银行卡列表


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<BankOfJZSupport> getDatasets() {
        return datasets == null?null:datasets.get(0);
    }

    public void setDatasets(List<List<BankOfJZSupport>> datasets) {
        this.datasets = datasets;
    }

    public class BankOfJZSupport {
        private String banknameid;// 银行id
        private String channelid; // 网点代码
        private String paycenterid; // 支付渠道代码
        private String fullname; // 支付网点代码
        private String name; // 支付网点简称
        private String maxbuysingle; // 最大单笔交易
        private String minbuysingle; // 最小单笔交易
        private String isopenplaninfo; //是否开通了定投标志 1 代表支持定投
        private int showseq; //展示顺序


        public String getBanknameid() {
            return banknameid;
        }

        public void setBanknameid(String banknameid) {
            this.banknameid = banknameid;
        }

        public String getChannelid() {
            return channelid;
        }

        public void setChannelid(String channelid) {
            this.channelid = channelid;
        }

        public String getPaycenterid() {
            return paycenterid;
        }

        public void setPaycenterid(String paycenterid) {
            this.paycenterid = paycenterid;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMaxbuysingle() {
            return maxbuysingle;
        }

        public void setMaxbuysingle(String maxbuysingle) {
            this.maxbuysingle = maxbuysingle;
        }

        public String getMinbuysingle() {
            return minbuysingle;
        }

        public void setMinbuysingle(String minbuysingle) {
            this.minbuysingle = minbuysingle;
        }

        public String getIsopenplaninfo() {
            return isopenplaninfo;
        }

        public void setIsopenplaninfo(String isopenplaninfo) {
            this.isopenplaninfo = isopenplaninfo;
        }

        public int getShowseq() {
            return showseq;
        }

        public void setShowseq(int showseq) {
            this.showseq = showseq;
        }
    }
}
