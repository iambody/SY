package app.privatefund.com.vido.mvc.bean;

import java.io.Serializable;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/15-11:14
 */
public class MyTaskBean implements Serializable, Comparable{
    private int id;
    private String taskName;
    private String createDate;
    private int taskType;
    private String content;
    private int status;
    private int coinNum;
    private String resetDay;
    private String adviserId;
    private int state = 0;  //0 未完成  1 已完成  2 已领取


    public String getAdviserId() {
        return adviserId;
    }

    public void setAdviserId(String adviserId) {
        this.adviserId = adviserId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCoinNum() {
        return coinNum;
    }

    public void setCoinNum(int coinNum) {
        this.coinNum = coinNum;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getResetDay() {
        return resetDay;
    }

    public void setResetDay(String resetDay) {
        this.resetDay = resetDay;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    @Override
    public int compareTo(Object another) {
        MyTaskBean b = (MyTaskBean) another;
        return this.state - b.state;
    }
}
