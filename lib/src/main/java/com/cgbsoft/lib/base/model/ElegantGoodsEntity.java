package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

import java.util.List;

/**
 * Created by feigecal on 2017/6/30 0030.
 */

public class ElegantGoodsEntity extends BaseResult<ElegantLivingEntity.Result> {
    public static class ElegantGoodsTitleBean implements ElegantGoodsBeanInterface{

        private int customItemType;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public void setCustomItemType(int customItemType) {
            this.customItemType=customItemType;
        }

        @Override
        public int getCustomItemType() {
            return customItemType;
        }
    }
    public static class ResultMore{
        private String category;
        private List<AllNewsItemBean> rows;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public List<AllNewsItemBean> getRows() {
            return rows;
        }

        public void setRows(List<AllNewsItemBean> rows) {
            this.rows = rows;
        }
    }
    public static class Result{
        private List<ElegantGoodsCategoryBean> navigation;
        private HotListBean hot;
        private AllNewsBean all;

        public List<ElegantGoodsCategoryBean> getNavigation() {
            return navigation;
        }

        public void setNavigation(List<ElegantGoodsCategoryBean> navigation) {
            this.navigation = navigation;
        }

        public HotListBean getHot() {
            return hot;
        }

        public void setHot(HotListBean hot) {
            this.hot = hot;
        }

        public AllNewsBean getAll() {
            return all;
        }

        public void setAll(AllNewsBean all) {
            this.all = all;
        }

        @Override
        public String toString() {
            return "Result{" + "navigation=" + navigation + ", hot=" + hot + ", all=" + all + '}';
        }
    }
    public static class ElegantGoodsCategoryBean implements ElegantGoodsBeanInterface {
        private int id;
        private String title;
        private String navigationId;
        private String code;
        private int customItemType;
        private int isCheck=0;//是否处于选中状态，0未选中  1选中

        public int getIsCheck() {
            return isCheck;
        }

        public void setIsCheck(int isCheck) {
            this.isCheck = isCheck;
        }

        public String getNavigationId() {
            return navigationId;
        }

        public void setNavigationId(String navigationId) {
            this.navigationId = navigationId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "ElegantGoodsCategoryBean{" + "id=" + id + ", title='" + title + '\'' + ", code='" + code + '\'' + '}';
        }

        @Override
        public void setCustomItemType(int customItemType) {
            this.customItemType=customItemType;
        }

        @Override
        public int getCustomItemType() {
            return customItemType;
        }
    }
    public static class HotListBean{
        private String text;
        private List<HotListItemBean> rows;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<HotListItemBean> getRows() {
            return rows;
        }

        public void setRows(List<HotListItemBean> rows) {
            this.rows = rows;
        }

        @Override
        public String toString() {
            return "HotListBean{" + "text='" + text + '\'' + ", rows=" + rows + '}';
        }
    }
    public static class HotListItemBean implements ElegantGoodsBeanInterface{
        private int exchanged;
        private String id;
        private String sn;
        private String imageUrl;
        private int currentQuantity;
        private String goodsName;
        private int ydQuantity;
        private int customItemType;

        public int getExchanged() {
            return exchanged;
        }

        public void setExchanged(int exchanged) {
            this.exchanged = exchanged;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getCurrentQuantity() {
            return currentQuantity;
        }

        public void setCurrentQuantity(int currentQuantity) {
            this.currentQuantity = currentQuantity;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public int getYdQuantity() {
            return ydQuantity;
        }

        public void setYdQuantity(int ydQuantity) {
            this.ydQuantity = ydQuantity;
        }

        @Override
        public String toString() {
            return "HotListItemBean{" + "exchanged=" + exchanged + ", id='" + id + '\'' + ", sn='" + sn + '\'' + ", imageUrl='" + imageUrl + '\'' +
                    ", currentQuantity=" + currentQuantity + ", goodsName='" + goodsName + '\'' + ", ydQuantity=" + ydQuantity + '}';
        }

        @Override
        public void setCustomItemType(int customItemType) {
            this.customItemType=customItemType;
        }

        @Override
        public int getCustomItemType() {
            return customItemType;
        }
    }
    public static class AllNewsBean{
        private String text;
        private List<AllNewsItemBean> rows;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<AllNewsItemBean> getRows() {
            return rows;
        }

        public void setRows(List<AllNewsItemBean> rows) {
            this.rows = rows;
        }

        @Override
        public String toString() {
            return "AllNewsBean{" + "text='" + text + '\'' + ", rows=" + rows + '}';
        }
    }
    public static class AllNewsItemBean implements ElegantGoodsBeanInterface{
        private String sn;
        private String advert;
        private String imageUrl;
        private int itemType;
        private String saleTarget;
        private int ydLottery;
        private String goodsLabel;
        private int ydPart;
        private String brandName;
        private String id;
        private int stock;
        private String goodsImg;
        private int rmbPart;
        private String goodsDescribe;
        private int currentQuantity;
        private String goodsName;
        private String brandLogo;
        private int ydQuantity;
        private int customItemType;

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getAdvert() {
            return advert;
        }

        public void setAdvert(String advert) {
            this.advert = advert;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public String getSaleTarget() {
            return saleTarget;
        }

        public void setSaleTarget(String saleTarget) {
            this.saleTarget = saleTarget;
        }

        public int getYdLottery() {
            return ydLottery;
        }

        public void setYdLottery(int ydLottery) {
            this.ydLottery = ydLottery;
        }

        public String getGoodsLabel() {
            return goodsLabel;
        }

        public void setGoodsLabel(String goodsLabel) {
            this.goodsLabel = goodsLabel;
        }

        public int getYdPart() {
            return ydPart;
        }

        public void setYdPart(int ydPart) {
            this.ydPart = ydPart;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public String getGoodsImg() {
            return goodsImg;
        }

        public void setGoodsImg(String goodsImg) {
            this.goodsImg = goodsImg;
        }

        public int getRmbPart() {
            return rmbPart;
        }

        public void setRmbPart(int rmbPart) {
            this.rmbPart = rmbPart;
        }

        public String getGoodsDescribe() {
            return goodsDescribe;
        }

        public void setGoodsDescribe(String goodsDescribe) {
            this.goodsDescribe = goodsDescribe;
        }

        public int getCurrentQuantity() {
            return currentQuantity;
        }

        public void setCurrentQuantity(int currentQuantity) {
            this.currentQuantity = currentQuantity;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getBrandLogo() {
            return brandLogo;
        }

        public void setBrandLogo(String brandLogo) {
            this.brandLogo = brandLogo;
        }

        public int getYdQuantity() {
            return ydQuantity;
        }

        public void setYdQuantity(int ydQuantity) {
            this.ydQuantity = ydQuantity;
        }

        @Override
        public String toString() {
            return "AllNewsItemBean{" + "sn='" + sn + '\'' + ", advert='" + advert + '\'' + ", imageUrl='" + imageUrl + '\'' + ", itemType=" +
                    itemType + ", saleTarget='" + saleTarget + '\'' + ", ydLottery=" + ydLottery + ", goodsLabel='" + goodsLabel + '\'' + ", " +
                    "ydPart=" + ydPart + ", brandName='" + brandName + '\'' + ", id='" + id + '\'' + ", stock=" + stock + ", goodsImg='" + goodsImg
                    + '\'' + ", rmbPart=" + rmbPart + ", goodsDescribe='" + goodsDescribe + '\'' + ", currentQuantity=" + currentQuantity + ", " +
                    "goodsName='" + goodsName + '\'' + ", brandLogo='" + brandLogo + '\'' + ", ydQuantity=" + ydQuantity + '}';
        }

        @Override
        public void setCustomItemType(int customItemType) {
            this.customItemType=customItemType;
        }

        @Override
        public int getCustomItemType() {
            return customItemType;
        }
    }
}
