package com.smtl.edi.web.pojo;

/**
 *
 * @author nm
 */
public class User {

    private String code;
    private String name;
    private String mail;
    private String ccmail;
    private String ediType;

    private String codecoFlag;
    private String coarriFlag;
    private String coedorFlag;
    private String cosecrFlag;
    private String vesdepFlag;

    public String getCoedorFlag() {
        return coedorFlag;
    }

    public void setCoedorFlag(String coedorFlag) {
        this.coedorFlag = coedorFlag;
    }

    public String getCosecrFlag() {
        return cosecrFlag;
    }

    public void setCosecrFlag(String cosecrFlag) {
        this.cosecrFlag = cosecrFlag;
    }

    public String getVesdepFlag() {
        return vesdepFlag;
    }

    public void setVesdepFlag(String vesdepFlag) {
        this.vesdepFlag = vesdepFlag;
    }

    private String actFlag;

    public String getActFlag() {
        return actFlag;
    }

    public void setActFlag(String actFlag) {
        this.actFlag = actFlag;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCcmail() {
        return ccmail;
    }

    public void setCcmail(String ccmail) {
        this.ccmail = ccmail;
    }

    public String getEdiType() {
        return ediType;
    }

    public void setEdiType(String ediType) {
        this.ediType = ediType;
    }

    public String getCodecoFlag() {
        return codecoFlag;
    }

    public void setCodecoFlag(String codecoFlag) {
        this.codecoFlag = codecoFlag;
    }

    public String getCoarriFlag() {
        return coarriFlag;
    }

    public void setCoarriFlag(String coarriFlag) {
        this.coarriFlag = coarriFlag;
    }

}
