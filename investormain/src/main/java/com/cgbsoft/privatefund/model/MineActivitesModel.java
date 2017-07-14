package com.cgbsoft.privatefund.model;

import java.util.List;

/**
 * @author chenlong
 */
public class MineActivitesModel {

    private List<ActivitesItem> rows;

    private int residual;

    public List<ActivitesItem> getRows() {
        return rows;
    }

    public void setRows(List<ActivitesItem> rows) {
        this.rows = rows;
    }

    public int getResidual() {
        return residual;
    }

    public void setResidual(int residual) {
        this.residual = residual;
    }

    public static class ActivitesItem {
        private String id;

        private String code;

        private String title;

        private String mainImage;

        private String city;

        private String liveVideoNo;

        private String playbackVideoUrl;

        private String startTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMainImage() {
            return mainImage;
        }

        public void setMainImage(String mainImage) {
            this.mainImage = mainImage;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getLiveVideoNo() {
            return liveVideoNo;
        }

        public void setLiveVideoNo(String liveVideoNo) {
            this.liveVideoNo = liveVideoNo;
        }

        public String getPlaybackVideoUrl() {
            return playbackVideoUrl;
        }

        public void setPlaybackVideoUrl(String playbackVideoUrl) {
            this.playbackVideoUrl = playbackVideoUrl;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }
    }
}