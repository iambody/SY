package app.privatefund.investor.health.mvp.model;

/**
 * @author chenlong
 */
public class HealthIntroduceNavigationEntity {

     private int id;
     private String jumpType;
     private String image2;
     private String title;
     private String level;
     private String sort;
     private String image1;
     private int isVisitorVisible;
     private String pCode;
     private String businessId;
     private String code;
     private String url;
     private int isCheck=0;//是否处于选中状态，0未选中  1选中

     public HealthIntroduceNavigationEntity(){};

     public void setIsCheck(int isCheck) {
          this.isCheck = isCheck;
     }

     public int getIsCheck() {
          return isCheck;
     }

     public int getId() {
          return id;
     }

     public void setId(int id) {
          this.id = id;
     }

     public String getJumpType() {
          return jumpType;
     }

     public void setJumpType(String jumpType) {
          this.jumpType = jumpType;
     }

     public String getImage2() {
          return image2;
     }

     public void setImage2(String image2) {
          this.image2 = image2;
     }

     public String getTitle() {
          return title;
     }

     public void setTitle(String title) {
          this.title = title;
     }

     public String getLevel() {
          return level;
     }

     public void setLevel(String level) {
          this.level = level;
     }

     public String getSort() {
          return sort;
     }

     public void setSort(String sort) {
          this.sort = sort;
     }

     public String getImage1() {
          return image1;
     }

     public void setImage1(String image1) {
          this.image1 = image1;
     }

     public int getIsVisitorVisible() {
          return isVisitorVisible;
     }

     public void setIsVisitorVisible(int isVisitorVisible) {
          this.isVisitorVisible = isVisitorVisible;
     }

     public String getpCode() {
          return pCode;
     }

     public void setpCode(String pCode) {
          this.pCode = pCode;
     }

     public String getBusinessId() {
          return businessId;
     }

     public void setBusinessId(String businessId) {
          this.businessId = businessId;
     }

     public String getCode() {
          return code;
     }

     public void setCode(String code) {
          this.code = code;
     }

     public String getUrl() {
          return url;
     }

     public void setUrl(String url) {
          this.url = url;
     }
}
