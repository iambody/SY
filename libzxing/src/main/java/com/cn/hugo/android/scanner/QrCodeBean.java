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
}
