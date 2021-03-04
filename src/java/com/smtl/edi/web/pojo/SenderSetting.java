package com.smtl.edi.web.pojo;

/**
 *
 * @author nm
 */
public class SenderSetting {

    private String cstCode;
    private String cstSender;
    private String cstReceiver;
    private String ediCate;

    public String getCstCode() {
        return cstCode;
    }

    public void setCstCode(String cstCode) {
        this.cstCode = cstCode;
    }

    public String getCstSender() {
        return cstSender;
    }

    public void setCstSender(String cstSender) {
        this.cstSender = cstSender;
    }

    public String getCstReceiver() {
        return cstReceiver;
    }

    public void setCstReceiver(String cstReceiver) {
        this.cstReceiver = cstReceiver;
    }

    public String getEdiCate() {
        return ediCate;
    }

    public void setEdiCate(String ediCate) {
        this.ediCate = ediCate;
    }

}
