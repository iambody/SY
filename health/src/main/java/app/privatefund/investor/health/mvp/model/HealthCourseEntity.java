package app.privatefund.investor.health.mvp.model;


import com.cgbsoft.lib.base.mvp.model.BaseResult;

import java.util.List;

/**
 * @author chenlong
 */
public class HealthCourseEntity extends BaseResult<HealthCourseEntity.Result> {

    public static class Result {

        private int total;

        private List<HealthCourseListModel> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<HealthCourseListModel> getRows() {
            return rows;
        }

        public void setRows(List<HealthCourseListModel> rows) {
            this.rows = rows;
        }
    }

    public static class HealthCourseListModel{

        private String id;

        private String detailUrl;

        private String title;

        private String releaseDate;

        private String readCount;

        private String thumbnailUrl;

        private String shortName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDetailUrl() {
            return detailUrl;
        }

        public void setDetailUrl(String detailUrl) {
            this.detailUrl = detailUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public String getReadCount() {
            return readCount;
        }

        public void setReadCount(String readCount) {
            this.readCount = readCount;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }
    }
}
