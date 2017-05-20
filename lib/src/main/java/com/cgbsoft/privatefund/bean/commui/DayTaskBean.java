package com.cgbsoft.privatefund.bean.commui;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;


/**
 * desc  数据库中保存任务
 * Created by yangzonghui on 2017/5/11 17:53
 * Email:yangzonghui@simuyun.com
 *  
 */
@Entity
public class DayTaskBean {
    @Id()
    private Long id;
    private String taskName;
    private String createDate;
    private int taskType;
    private String content;
    private int status;
    private int coinNum;
    private String resetDay;
    private String adviserId;
    private int state = 0;  //0 未完成  1 已完成
    @Generated(hash = 1709584021)
    public DayTaskBean(Long id, String taskName, String createDate, int taskType,
            String content, int status, int coinNum, String resetDay,
            String adviserId, int state) {
        this.id = id;
        this.taskName = taskName;
        this.createDate = createDate;
        this.taskType = taskType;
        this.content = content;
        this.status = status;
        this.coinNum = coinNum;
        this.resetDay = resetDay;
        this.adviserId = adviserId;
        this.state = state;
    }
    @Generated(hash = 406175250)
    public DayTaskBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTaskName() {
        return this.taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public String getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public int getTaskType() {
        return this.taskType;
    }
    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getCoinNum() {
        return this.coinNum;
    }
    public void setCoinNum(int coinNum) {
        this.coinNum = coinNum;
    }
    public String getResetDay() {
        return this.resetDay;
    }
    public void setResetDay(String resetDay) {
        this.resetDay = resetDay;
    }
    public String getAdviserId() {
        return this.adviserId;
    }
    public void setAdviserId(String adviserId) {
        this.adviserId = adviserId;
    }
    public int getState() {
        return this.state;
    }
    public void setState(int state) {
        this.state = state;
    }
}
