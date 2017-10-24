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

        private String subtitle;

        private String imageUrl;

        private String effectPosition;

        private String fitSymptom;

        private String fitCrowd;

        private String headImage;

        private String userNickName;

        private String judgment;

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

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
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

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public String getUserNickName() {
            return userNickName;
        }

        public void setUserNickName(String userNickName) {
            this.userNickName = userNickName;
        }

        public String getJudgment() {
            return judgment;
        }

        public void setJudgment(String judgment) {
            this.judgment = judgment;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
