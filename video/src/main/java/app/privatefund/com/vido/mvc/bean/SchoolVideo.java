package app.privatefund.com.vido.mvc.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/12-19:38
 */
public class SchoolVideo implements Serializable {
    /*
	 * "SDVideoUrl": "", "likes": "0", "HDVideoUrl": "", "videoSummary":
	 * "\n产品简介：\n      1.用于新三板标的企业股权投资\n      2.投资顾问实力雄厚，信达资产下的首泰金信，管理规模已超100亿\n      3.投资团队经验丰富，何江、余黎，投资眼光精准、擅长股权投资\n      4.投资理念严密科学，企业甄别方式独特、投资方案及流程完备\n\n主讲人：\n      何  江    首泰基金PE及新三板业务总裁\n\n主讲人简介：\n      何江先生是业内享有盛名的职业投资人，拥有超过二十五年的海外和中国资本市场的操作经验。同时担任北京约基工业股份有限公司董事，广州宏城通用飞机服务公司董事，北京嘉值惟实投资中心（有限合伙）合伙人及美国Jacobs Investment合伙人。 "
	 * , "videoId": "dcb777479b944069b339ef567ee313c4", "isLiked": 0,
	 * "tencentAppId": "", "createDate": "2016-01-21", "tencentVideoId": "",
	 * "coverImageUrl":
	 * "http://www.simuyun.com/peyunupload//2016-01-21/817c76d45ef84ee2a661626efc435d0e.jpg",
	 * "shortName": "此基金募集的资金用于新三板标的企业的股权投资", "videoName": "长江四号股权投资基金"
	 */
    private String SDVideoUrl;
    private String HDVideoUrl;
    private String likes = "0";
    private String videoSummary;
    private String videoId;
    private int isLiked;
    private String tencentAppId;
    private String createDate;
    private String tencentVideoId;
    private String coverImageUrl;
    private String shortName;
    private String videoName;
    private int currentTime;  //增加当前播放时间
    private int id = 0;            //增加id
    private int status;       //下载完成:2 / 下载中：1  / 未下载：0
    private long finalPlayTime;  //最后一次播放时间
    private long size;          //下载文件大小
    private double percent;       //下载百分百
    private int downloadtype;     //下载清晰度  0 表示高清 1 标清
    private long downloadTime;   //下载时间
    private int encrypt;          //1:没有加密 2：加密
    private int hasRecord = 1;    //是否在播放列表中显示   1显示 0不显示
    private ArrayList<Comment> comment;
    private ArrayList<ProductBean> product;
    private String playCount;
    private String presenter;
    private String shareUrl; // 分享url
    private String shareImg;
    private String canShare;

    public String getCanShare() {
        return canShare;
    }

    public void setCanShare(String canShare) {
        this.canShare = canShare;
    }

    public String getPresenter() {
        return presenter;
    }

    public void setPresenter(String presenter) {
        this.presenter = presenter;
    }

    public String getPlayCount() {
        return playCount;
    }

    public void setPlayCount(String playCount) {
        this.playCount = playCount;
    }

    public ArrayList<Comment> getComment() {
        return comment;
    }

    public void setComment(ArrayList<Comment> comment) {
        this.comment = comment;
    }

    public ArrayList<ProductBean> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<ProductBean> product) {
        this.product = product;
    }

    public int getHasRecord() {
        return hasRecord;
    }

    public void setHasRecord(int hasRecord) {
        this.hasRecord = hasRecord;
    }

    public int getDownloadtype() {
        return downloadtype;
    }

    public void setDownloadtype(int downloadtype) {
        this.downloadtype = downloadtype;
    }

    public long getFinalPlayTime() {
        return finalPlayTime;
    }

    public void setFinalPlayTime(long finalPlayTime) {
        this.finalPlayTime = finalPlayTime;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getSDVideoUrl() {
        return  SDVideoUrl;
    }

    public String getHDVideoUrl() {
        return HDVideoUrl;
    }

    public void setHDVideoUrl(String HDVideoUrl) {
        this.HDVideoUrl = HDVideoUrl;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public int getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(int isLiked) {
        this.isLiked = isLiked;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setSDVideoUrl(String SDVideoUrl) {
        this.SDVideoUrl = SDVideoUrl;
    }

    public String getVideoSummary() {
        return videoSummary;
    }

    public void setVideoSummary(String videoSummary) {
        this.videoSummary = videoSummary;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTencentAppId() {
        return tencentAppId;
    }

    public void setTencentAppId(String tencentAppId) {
        this.tencentAppId = tencentAppId;
    }

    public String getTencentVideoId() {
        return tencentVideoId;
    }

    public void setTencentVideoId(String tencentVideoId) {
        this.tencentVideoId = tencentVideoId;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public int getId(){ return id;}

    public void setId(int id){this.id = id;}

    public int getCurrentTime(){ return currentTime;}

    public void setCurrentTime(int currentTime){this.currentTime = currentTime;}

    public int getStatus(){ return status;}

    public void setStatus(int status){this.status = status;}

    public long getSize(){ return size;}

    public void setSize(long size){this.size = size;}

    public double getPercent(){ return percent;}

    public void setPercent(double percent){this.percent = percent;}

    public long getDownloadTime(){ return downloadTime;}

    public void setDownloadTime(long downloadTime){this.downloadTime = downloadTime;}

    public int getEncrypt(){ return encrypt;}

    public void setEncrypt(int encrypt){this.encrypt = encrypt;}

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareImg() {
        return shareImg;
    }

    public void setShareImg(String shareImg) {
        this.shareImg = shareImg;
    }
}
