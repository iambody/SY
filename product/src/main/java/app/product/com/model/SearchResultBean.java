package app.product.com.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenlong
 */

public class SearchResultBean implements Serializable {
    private List<ResultBean> results;

    private String total;

    private int pageTotal;

    private int pageSize;

    private String type;

    private int pageIndex;

    public SearchResultBean(String total, List<String> keywords, int pageTotal, int pageSize, String type, int pageIndex) {
        this.total = total;
        this.pageTotal = pageTotal;
        this.pageSize = pageSize;
        this.type = type;
        this.pageIndex = pageIndex;
    }

    public SearchResultBean() {}

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ResultBean> getResults() {
        return results;
    }

    public void setResults(List<ResultBean> results) {
        this.results = results;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public static class ResultBean implements Serializable {

        private String state;

        private String term;

        private String investmentArea;

        private String label;

        private String productType;

        private int remainingAmount;
        //收益基准
        private String expectedYield;
        //累计净值
        private String cumulativeNet;
        //截止认购时间
        private String raiseEndTime;
        // 添加的字段
        private String summary;

        private String content;

        private String title;

        private String targetId;

        private String isMore;

        private String des;

        private String url;

        private String categoryId;

        private String category;

        private String infoType;

        private String infoName;

        private String infoSummary;

        private String infoCreateTime;

        private String infoCategory;

        private String totalNumber;

        private String imgUrl;

        private String time;

        private ResultType isPart;

        private boolean isLaster;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public String getInvestmentArea() {
            return investmentArea;
        }

        public void setInvestmentArea(String investmentArea) {
            this.investmentArea = investmentArea;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public int getRemainingAmount() {
            return remainingAmount;
        }

        public void setRemainingAmount(int remainingAmount) {
            this.remainingAmount = remainingAmount;
        }

        public String getExpectedYield() {
            return expectedYield;
        }

        public void setExpectedYield(String expectedYield) {
            this.expectedYield = expectedYield;
        }

        public String getCumulativeNet() {
            return cumulativeNet;
        }

        public void setCumulativeNet(String cumulativeNet) {
            this.cumulativeNet = cumulativeNet;
        }

        public String getRaiseEndTime() {
            return raiseEndTime;
        }

        public void setRaiseEndTime(String raiseEndTime) {
            this.raiseEndTime = raiseEndTime;
        }

        public ResultBean(ResultType isPart) {
            this.isPart = isPart;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

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

        public String getTotalNumber() {
            return totalNumber;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public void setTotalNumber(String totalNumber) {
            this.totalNumber = totalNumber;
        }

        public ResultType getIsPart() {
            return isPart;
        }

        public void setIsPart(ResultType isPart) {
            this.isPart = isPart;
        }

        public boolean isLaster() {
            return isLaster;
        }

        public void setLaster(boolean laster) {
            isLaster = laster;
        }

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }

        public String getInfoType() {
            return infoType;
        }

        public void setInfoType(String infoType) {
            this.infoType = infoType;
        }

        public String getInfoName() {
            return infoName;
        }

        public void setInfoName(String infoName) {
            this.infoName = infoName;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getInfoSummary() {
            return infoSummary;
        }

        public void setInfoSummary(String infoSummary) {
            this.infoSummary = infoSummary;
        }

        public String getInfoCreateTime() {
            return infoCreateTime;
        }

        public void setInfoCreateTime(String infoCreateTime) {
            this.infoCreateTime = infoCreateTime;
        }

        public String getIsMore() {
            return isMore;
        }

        public void setIsMore(String isMore) {
            this.isMore = isMore;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getInfoCategory() {
            return infoCategory;
        }

        public void setInfoCategory(String infoCategory) {
            this.infoCategory = infoCategory;
        }
    }

    public enum ResultType {
        INFO_HEADER, INFO_MORE, INFO_ITEM, PRODUCT_HEADER, PRODUCT_MORE,
        PRODUCT_ITEM, XUN_HEADER, XUN_MORE, XUN_ITEM, VIDEO_HEADER, VIDEO_MORE,
        VIDEO_ITEM, CUSTOM_HEADER, CUSTOM_MORE, CUSTOM_ITEM, ORDER_HEADER, ORDER_MORE, ORDER_ITEM, NO_MORE_DATA
    }
}
