package app.privatefund.com.vido.mvc.bean;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/15-11:22
 */
public class PostZan {
    private int zancount;
    private String tencentVideoId;

    public PostZan(int zancount,String tencentVideoId){
        this.zancount = zancount;
        this.tencentVideoId = tencentVideoId;

    }

    public int getZancount() {
        return zancount;
    }

    public void setZancount(int zancount) {
        this.zancount = zancount;
    }

    public String getTencentVideoId() {
        return tencentVideoId;
    }

    public void setTencentVideoId(String tencentVideoId) {
        this.tencentVideoId = tencentVideoId;
    }

}
