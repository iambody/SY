package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

import java.util.List;

/**
 * @author chenlong
 *         <p>
 *         用户所属群组列表
 */
public class GroupListEntity extends BaseResult<GroupListEntity.Result> {

    public static class Result {

        private List<Group> result;

        public List<Group> getResult() {
            return result;
        }

        public void setResult(List<Group> result) {
            this.result = result;
        }
    }

    public static class Group {
        private String id;
        private String name;
        private String headImgUrl;

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

        public String getHeadImgUrl() {
            return headImgUrl;
        }

        public void setHeadImgUrl(String headImgUrl) {
            this.headImgUrl = headImgUrl;
        }
    }
}
