package app.privatefund.investor.health.mvp.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenlong
 */
public class HealthProjectListEntity implements Serializable{

    private int total;

    private List<HealthProjectItemEntity> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<HealthProjectItemEntity> getRows() {
        return rows;
    }

    public void setRows(List<HealthProjectItemEntity> rows) {
        this.rows = rows;
    }

    public static class HealthProjectItemEntity {
        private String id;

        private String title;

        private String sutTitle;

        private String imageUrl;

        private String effectPosition;

        private String fitSymptom;

        private String fitCrowd;

        private String userThumbnail;

        private String userFrom;

        private String comment;

        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSutTitle() {
            return sutTitle;
        }

        public void setSutTitle(String sutTitle) {
            this.sutTitle = sutTitle;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getEffectPosition() {
            return effectPosition;
        }

        public void setEffectPosition(String effectPosition) {
            this.effectPosition = effectPosition;
        }

        public String getFitSymptom() {
            return fitSymptom;
        }

        public void setFitSymptom(String fitSymptom) {
            this.fitSymptom = fitSymptom;
        }

        public String getFitCrowd() {
            return fitCrowd;
        }

        public void setFitCrowd(String fitCrowd) {
            this.fitCrowd = fitCrowd;
        }

        public String getUserThumbnail() {
            return userThumbnail;
        }

        public void setUserThumbnail(String userThumbnail) {
            this.userThumbnail = userThumbnail;
        }

        public String getUserFrom() {
            return userFrom;
        }

        public void setUserFrom(String userFrom) {
            this.userFrom = userFrom;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
