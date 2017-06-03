package com.cn.hugo.android.scanner;

/**
 * @author chenlong
 */

public class QrCodeBean {

    private String partyId;

    private String partyName;

    private String fatherId;

    private String deadLine;

    private String fatherName;

    public QrCodeBean(String partyId) {
        this.partyId = partyId;
    }

    public QrCodeBean(String partyId, String partyName, String fatherId, String deadLine, String fatherName) {
        this.partyId = partyId;
        this.partyName = partyName;
        this.fatherId = fatherId;
        this.deadLine = deadLine;
        this.fatherName = fatherName;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }
}
