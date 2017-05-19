package app.privatefund.com.im.bean;

public class GroupInfo {

    private String id;

    private String name;

    private String headImageUrl;

    public GroupInfo(String id) {
        this.id = id;
    }

    public GroupInfo(String id, String name, String headImageUrl) {
        this.headImageUrl = headImageUrl;
        this.id = id;
        this.name = name;
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

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }
}
