package com.cgbsoft.privatefund.bean.living;

/**
 * desc  拍照人脸后需要返回的人脸的本地路径和远程路径
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/11/5-17:43
 */
public class FaceInf {
    private String faceRemotePath;
    private String faceLocalPath;

    private String pageTage;

    public FaceInf() {
    }

    public String getPageTage() {
        return pageTage;
    }

    public void setPageTage(String pageTage) {
        this.pageTage = pageTage;
    }

    public String getFaceRemotePath() {
        return faceRemotePath;
    }

    public void setFaceRemotePath(String faceRemotePath) {
        this.faceRemotePath = faceRemotePath;
    }

    public String getFaceLocalPath() {
        return faceLocalPath;
    }

    public void setFaceLocalPath(String faceLocalPath) {
        this.faceLocalPath = faceLocalPath;
    }

    public FaceInf(String faceRemotePath, String faceLocalPath,String pageTage) {
        this.faceRemotePath = faceRemotePath;
        this.faceLocalPath = faceLocalPath;
        this.pageTage=pageTage;
    }
}
