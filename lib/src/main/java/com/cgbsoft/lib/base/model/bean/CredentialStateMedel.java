package com.cgbsoft.lib.base.model.bean;

import java.io.Serializable;

/**
 * Created by zhaojiaqi on 2017/11/3.
 * <p>
 * "credentialDetailId": 8630,
 * "credentialCode": "100101",
 * "customerType": "10",
 * "credentialState": "50",
 * "customerImageState": "0",
 * "idCardStateName": "已通过",
 * "customerIdentity": "1001",
 * "credentialTypeName": "身份证",
 * "customerLivingbodyState": "0",
 * "credentialStateName": "已通过",
 * "idCardState": "50"
 */
public class CredentialStateMedel implements Serializable{
    private String credentialDetailId;
    private String credentialCode;
    private String customerType;
    private String credentialState;
    private String customerImageState;
    private String idCardStateName;
    private String customerIdentity;
    private String credentialTypeName;
    private String customerLivingbodyState;
    private String credentialStateName;
    private String idCardState;


    public CredentialStateMedel() {
    }

    public String getCredentialDetailId() {
        return credentialDetailId;
    }

    public CredentialStateMedel(String credentialDetailId, String credentialCode, String customerType, String credentialState, String customerImageState, String idCardStateName, String customerIdentity, String credentialTypeName, String customerLivingbodyState, String credentialStateName, String idCardState) {
        this.credentialDetailId = credentialDetailId;
        this.credentialCode = credentialCode;
        this.customerType = customerType;
        this.credentialState = credentialState;
        this.customerImageState = customerImageState;
        this.idCardStateName = idCardStateName;
        this.customerIdentity = customerIdentity;
        this.credentialTypeName = credentialTypeName;
        this.customerLivingbodyState = customerLivingbodyState;
        this.credentialStateName = credentialStateName;
        this.idCardState = idCardState;
    }

    public void setCredentialDetailId(String credentialDetailId) {
        this.credentialDetailId = credentialDetailId;
    }

    public String getCredentialCode() {
        return credentialCode;
    }

    public void setCredentialCode(String credentialCode) {
        this.credentialCode = credentialCode;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCredentialState() {
        return credentialState;
    }

    public void setCredentialState(String credentialState) {
        this.credentialState = credentialState;
    }

    public String getCustomerImageState() {
        return customerImageState;
    }

    public void setCustomerImageState(String customerImageState) {
        this.customerImageState = customerImageState;
    }

    public String getIdCardStateName() {
        return idCardStateName;
    }

    public void setIdCardStateName(String idCardStateName) {
        this.idCardStateName = idCardStateName;
    }

    public String getCustomerIdentity() {
        return customerIdentity;
    }

    public void setCustomerIdentity(String customerIdentity) {
        this.customerIdentity = customerIdentity;
    }

    public String getCredentialTypeName() {
        return credentialTypeName;
    }

    public void setCredentialTypeName(String credentialTypeName) {
        this.credentialTypeName = credentialTypeName;
    }

    public String getCustomerLivingbodyState() {
        return customerLivingbodyState;
    }

    public void setCustomerLivingbodyState(String customerLivingbodyState) {
        this.customerLivingbodyState = customerLivingbodyState;
    }

    public String getCredentialStateName() {
        return credentialStateName;
    }

    public void setCredentialStateName(String credentialStateName) {
        this.credentialStateName = credentialStateName;
    }

    public String getIdCardState() {
        return idCardState;
    }

    public void setIdCardState(String idCardState) {
        this.idCardState = idCardState;
    }
}
