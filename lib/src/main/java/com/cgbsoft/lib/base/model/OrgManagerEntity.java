package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

/**
 * @author chenlong
 *
 * 机构经理
 */
public class OrgManagerEntity extends BaseResult<OrgManagerEntity.Result> {

    public static class Result{

        private String managerMobile;

        private String managerUid;

        private String teamManagerUid;

        public String getManagerMobile() {
            return managerMobile;
        }

        public void setManagerMobile(String managerMobile) {
            this.managerMobile = managerMobile;
        }

        public String getManagerUid() {
            return managerUid;
        }

        public void setManagerUid(String managerUid) {
            this.managerUid = managerUid;
        }

        public String getTeamManagerUid() {
            return teamManagerUid;
        }

        public void setTeamManagerUid(String teamManagerUid) {
            this.teamManagerUid = teamManagerUid;
        }
    }
}
