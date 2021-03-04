package com.smtl.edi.web.pojo;

/**
 *
 * @author nm
 */
public class FtpSetting {

    private String cstCode;
    private String ftpIP;
    private String ftpUser;
    private String ftpPwd;
    private String ftpPort;
    private String ftpPath;
    private String ediCate;

    public String getCstCode() {
        return cstCode;
    }

    public void setCstCode(String cstCode) {
        this.cstCode = cstCode;
    }

    public String getFtpIP() {
        return ftpIP;
    }

    public void setFtpIP(String ftpIP) {
        this.ftpIP = ftpIP;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPwd() {
        return ftpPwd;
    }

    public void setFtpPwd(String ftpPwd) {
        this.ftpPwd = ftpPwd;
    }

    public String getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(String ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    public String getEdiCate() {
        return ediCate;
    }

    public void setEdiCate(String ediCate) {
        this.ediCate = ediCate;
    }

}
