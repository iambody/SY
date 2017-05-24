package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

/**
 * @author chenlong
 *
 * 群组消息
 */
public class GroupInfoEntity extends BaseResult<GroupInfoEntity.Result> {

    public static class Result {

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
