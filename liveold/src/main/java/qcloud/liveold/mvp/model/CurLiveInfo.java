package qcloud.liveold.mvp.model;


import com.cgbsoft.lib.AppManager;
import com.cgbsoft.lib.BaseApplication;
import com.cgbsoft.lib.utils.cache.SPreference;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 当前直播信息页面
 */
public class CurLiveInfo {
    private static int members;
    private static int admires;
    private static String title;
    private static double lat1;
    private static double long1;
    private static String address = "";
    private static String coverurl = "";
    private static int check_num = 0;
    private static long joinTime;
    private static String chatId;

    public static int roomNum;

    public static String hostID;
    public static String hostName;
    public static String hostAvator;
    //是否可分享
    public static int isShare;
    //分享URL
    public static String shareUrl;
    //是否可评论
    public static int allowChat;
    //房间广告语
    public static String slogan;

    public static int currentRequestCount = 0;

    public static String getSlogan() {
        return slogan;
    }

    public static void setSlogan(String slogan) {
        CurLiveInfo.slogan = slogan;
    }

    public static String getChatId() {
        return chatId;
    }

    public static void setChatId(String chatId) {
        CurLiveInfo.chatId = chatId;
    }

    public static long getJoinTime() {
        return joinTime;
    }

    public static void setJoinTime(long joinTime) {
        CurLiveInfo.joinTime = joinTime;
    }

    public static int getCheck_num() {
        return check_num;
    }

    public static int getIsShare() {
        return isShare;
    }

    public static void setIsShare(int isShare) {
        CurLiveInfo.isShare = isShare;
    }

    public static String getShareUrl() {
        try {
            return String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb19b10cf492481cf&redirect_uri=%s&response_type=code&scope=snsapi_login&state=00000#wechat_redirect",
                    URLEncoder.encode(
                            String.format(
                                    "%s?userId=%s&roomId=%d",
                                    SPreference.getString(BaseApplication.getContext(), "liveShareUrl"),
                                    AppManager.getUserId(BaseApplication.getContext()),
                                    SPreference.getInt(BaseApplication.getContext(), "liveRoomNum")
                            ), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setShareUrl(String shareUrl) {
        CurLiveInfo.shareUrl = shareUrl;
        SPreference.putString(BaseApplication.getContext(), "liveShareUrl", shareUrl);
    }

    public static int getAllowChat() {
        return allowChat;
    }

    public static void setAllowChat(int allowChat) {
        CurLiveInfo.allowChat = allowChat;
    }

    public static void setCheck_num(int check_num) {
        CurLiveInfo.check_num = check_num;
    }

    public static int getCurrentRequestCount() {
        return currentRequestCount;
    }

    public static int getIndexView() {
        return indexView;
    }

    public static void setIndexView(int indexView) {
        CurLiveInfo.indexView = indexView;
    }

    public static int indexView = 0;

    public static void setCurrentRequestCount(int currentRequestCount) {
        CurLiveInfo.currentRequestCount = currentRequestCount;
    }

    public static String getHostID() {
        return hostID;
    }

    public static void setHostID(String hostID) {
        CurLiveInfo.hostID = hostID;
    }

    public static String getHostName() {
        return hostName;
    }

    public static void setHostName(String hostName) {
        CurLiveInfo.hostName = hostName;
    }

    public static String getHostAvator() {
        return hostAvator;
    }

    public static void setHostAvator(String hostAvator) {
        CurLiveInfo.hostAvator = hostAvator;
    }

    public static int getMembers() {
        return members;
    }

    public static void setMembers(int members) {
        CurLiveInfo.members = members;
    }

    public static int getAdmires() {
        return admires;
    }

    public static void setAdmires(int admires) {
        CurLiveInfo.admires = admires;
    }

    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        CurLiveInfo.title = title;
    }

    public static double getLat1() {
        return lat1;
    }

    public static void setLat1(double lat1) {
        CurLiveInfo.lat1 = lat1;
    }

    public static double getLong1() {
        return long1;
    }

    public static void setLong1(double long1) {
        CurLiveInfo.long1 = long1;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        CurLiveInfo.address = address;
    }

    public static int getRoomNum() {
        return roomNum;
    }

    public static void setRoomNum(int roomNum) {
        CurLiveInfo.roomNum = roomNum;
        SPreference.putInt(BaseApplication.getContext(), "liveRoomNum", roomNum);
    }

    public static String getCoverurl() {
        return coverurl;
    }

    public static void setCoverurl(String coverurl) {
        CurLiveInfo.coverurl = coverurl;
    }

    public static String getChatRoomId() {
        return "" + roomNum;
    }
}
