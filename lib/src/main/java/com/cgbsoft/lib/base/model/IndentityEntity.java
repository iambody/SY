package com.cgbsoft.lib.base.model;

import com.cgbsoft.lib.base.mvp.model.BaseResult;
import com.cgbsoft.lib.widget.SelectIndentityItem;

import java.util.List;

/**
 * Created by fei on 2017/8/11.
 */

public class IndentityEntity extends BaseResult<IndentityEntity.Result>{

    public static class Result{
        private List<IndentityBean> result;

        public List<IndentityBean> getResult() {
            return result;
        }

        public void setResult(List<IndentityBean> result) {
            this.result = result;
        }
    }
    public static class IndentityBean{


        private List<IndentityItem> result;
        private String title;
        private String code;

        public List<IndentityItem> getResult() {
            return result;
        }

        public void setResult(List<IndentityItem> result) {
            this.result = result;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
    public static class IndentityItem{
        private String code;
        private String title;
        private String credentialCode;
        private boolean isCheck;

        public String getCredentialCode() {
            return credentialCode;
        }

        public void setCredentialCode(String credentialCode) {
            this.credentialCode = credentialCode;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }
    }
}
