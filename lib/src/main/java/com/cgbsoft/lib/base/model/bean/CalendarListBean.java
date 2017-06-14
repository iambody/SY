package com.cgbsoft.lib.base.model.bean;

import java.io.Serializable;
import java.util.List;

public class CalendarListBean implements Serializable {

    public enum CalendarPrompt {
      NONE("0"), CURRENT("5"), TEN("10"), THREE("30");
        private String value;
        CalendarPrompt(String value) {
           this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static int findIndex(String name) {
            for (CalendarPrompt prompt : CalendarPrompt.values()) {
                if (prompt.name().equals(name)) {
                    return prompt.ordinal();
                }
            }
            return 0;
        }
    }

    // 工作日期
    private String todoDate;

    // 星期
    private String dayOfWeek;

    private List<CalendarBean> todos;

    public static class CalendarBean implements Serializable {

        // ID
        private String id;

        // 标题
        private String title;

        // 地址
        private String address;

        // 内容
        private String content;

        // 开始时间
        private String startTime;

        // 结束时间
        private String endTime;

        // 触发事件
        private String todoDate;

        private String eventId;

        // 是否是我的工作
        private String isMy;

        private String isPrompt = "0";

        private String alert;

        private CalendarPrompt calendarPrompt;

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getTodoDate() {
            return todoDate;
        }

        public void setTodoDate(String todoDate) {
            this.todoDate = todoDate;
        }

        public String getIsPrompt() {
            return isPrompt;
        }

        public void setIsPrompt(String isPrompt) {
            this.isPrompt = isPrompt;
        }

        public String getIsMy() {
            return isMy;
        }

        public CalendarPrompt getCalendarPrompt() {
            return calendarPrompt;
        }

        public void setCalendarPrompt(CalendarPrompt calendarPrompt) {
            this.calendarPrompt = calendarPrompt;
        }

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String isMy() {
            return isMy;
        }

        public void setIsMy(String isMy) {
            this.isMy = isMy;
        }
    }

    public String getTodoDate() {
        return todoDate;
    }

    public void setTodoDate(String todoDate) {
        this.todoDate = todoDate;
    }

    public List<CalendarBean> getTodos() {
        return todos;
    }

    public void setTodos(List<CalendarBean> todos) {
        this.todos = todos;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }


}