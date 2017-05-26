package com.cgbsoft.lib.base.model;

import android.net.Uri;

import com.cgbsoft.lib.base.mvp.model.BaseResult;

/**
 * @author chenlong
 */
public class RongUserEntity extends BaseResult<RongUserEntity.Result> {

    public static class Result {

        private String id;
        private String name;
        private Uri portraitUri;

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

        public Uri getPortraitUri() {
            return portraitUri;
        }

        public void setPortraitUri(Uri portraitUri) {
            this.portraitUri = portraitUri;
        }
    }
}
