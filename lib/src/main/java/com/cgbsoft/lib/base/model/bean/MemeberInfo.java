package com.cgbsoft.lib.base.model.bean;

import java.util.List;

/**
 * @author chenlong
 */
public class MemeberInfo {
    private String level;
    private String currentWealthNumber;
    private List<MemeberProject> item;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCurrentWealthNumber() {
        return currentWealthNumber;
    }

    public void setCurrentWealthNumber(String currentWealthNumber) {
        this.currentWealthNumber = currentWealthNumber;
    }

    public List<MemeberProject> getItem() {
        return item;
    }

    public void setItem(List<MemeberProject> item) {
        this.item = item;
    }

    public class MemeberProject {
        private String projectType;

        private List<MemeberItemProject> projectList;

        public String getProjectType() {
            return projectType;
        }

        public void setProjectType(String projectType) {
            this.projectType = projectType;
        }

        public List<MemeberItemProject> getProjectList() {
            return projectList;
        }

        public void setProjectList(List<MemeberItemProject> projectList) {
            this.projectList = projectList;
        }
    }

    public class MemeberItemProject {
         private String projectName;
         private String frequencyInfo;

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getFrequencyInfo() {
            return frequencyInfo;
        }

        public void setFrequencyInfo(String frequencyInfo) {
            this.frequencyInfo = frequencyInfo;
        }
    }
}
