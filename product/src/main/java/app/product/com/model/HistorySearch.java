package app.product.com.model;

/**
 *  搜索历史的bean
 */

public class HistorySearch {

    private String id;

    private String name;

    private String type;

    private long time;

    private String userId;

    public HistorySearch(){};

    public HistorySearch(String id, String name, String type, long time, String userId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.time = time;
        this.userId = userId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
