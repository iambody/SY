package app.product.com.model;

import com.cgbsoft.lib.base.model.bean.BaseBean;

import java.util.List;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/9-11:18
 */
public class ProductlsBean extends BaseBean {
    //产品ID
    private String productId;
    //产品类型
    private String productType;
    //产品名称
    private String productName;
    //起投金额
    private int buyStart;
    //累计净值
    private String cumulativeNet;
    //收益基准
    private double expectedYield;
    //资产标签
    private String label;
    //开户行
    private String raiseAccountName;
    //募集账号
    private String raiseAccount;
    //募集银行
    private String raiseBank;
    //产品系列
    private String series;
    //剩余额度
    private int remainingAmount;
    private String remainingAmountStr;
    //产品状态  50: 正常 60: 暂停募集 70: 已清算  80: 分销方未上线
    private String state;
    //截止认购时间
    private String raiseEndTime;
    //是否热门产品
    private String isHotProduct;
    //营销图片地址
    private String marketingImageUrl;
    //营销话术
    private String hotName;
    //营销短信
    private String smsContent;
    //产品的发行方案
    private List<Series> schemes;
    //产品的投资单元
    private List<Series> units;
    //发行方案ID
    private String schemeId;

    private String term;

    private String shortName;

    private String incomeMax;

    private String netAll;

    private String investmentArea;


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getBuyStart() {
        return buyStart;
    }

    public void setBuyStart(int buyStart) {
        this.buyStart = buyStart;
    }

    public String getCumulativeNet() {
        return cumulativeNet;
    }

    public void setCumulativeNet(String cumulativeNet) {
        this.cumulativeNet = cumulativeNet;
    }

    public double getExpectedYield() {
        return expectedYield;
    }

    public void setExpectedYield(double expectedYield) {
        this.expectedYield = expectedYield;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRaiseAccountName() {
        return raiseAccountName;
    }

    public void setRaiseAccountName(String raiseAccountName) {
        this.raiseAccountName = raiseAccountName;
    }

    public String getRaiseAccount() {
        return raiseAccount;
    }

    public void setRaiseAccount(String raiseAccount) {
        this.raiseAccount = raiseAccount;
    }

    public String getRaiseBank() {
        return raiseBank;
    }

    public void setRaiseBank(String raiseBank) {
        this.raiseBank = raiseBank;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public int getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(int remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getRemainingAmountStr() {
        return remainingAmountStr;
    }

    public void setRemainingAmountStr(String remainingAmountStr) {
        this.remainingAmountStr = remainingAmountStr;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRaiseEndTime() {
        return raiseEndTime;
    }

    public void setRaiseEndTime(String raiseEndTime) {
        this.raiseEndTime = raiseEndTime;
    }

    public String getIsHotProduct() {
        return isHotProduct;
    }

    public void setIsHotProduct(String isHotProduct) {
        this.isHotProduct = isHotProduct;
    }

    public String getMarketingImageUrl() {
        return marketingImageUrl;
    }

    public void setMarketingImageUrl(String marketingImageUrl) {
        this.marketingImageUrl = marketingImageUrl;
    }

    public String getHotName() {
        return hotName;
    }

    public void setHotName(String hotName) {
        this.hotName = hotName;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public List<Series> getSchemes() {
        return schemes;
    }

    public void setSchemes(List<Series> schemes) {
        this.schemes = schemes;
    }

    public List<Series> getUnits() {
        return units;
    }

    public void setUnits(List<Series> units) {
        this.units = units;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getIncomeMax() {
        return incomeMax;
    }

    public void setIncomeMax(String incomeMax) {
        this.incomeMax = incomeMax;
    }

    public String getNetAll() {
        return netAll;
    }

    public void setNetAll(String netAll) {
        this.netAll = netAll;
    }

    public String getInvestmentArea() {
        return investmentArea;
    }

    public void setInvestmentArea(String investmentArea) {
        this.investmentArea = investmentArea;
    }
}
