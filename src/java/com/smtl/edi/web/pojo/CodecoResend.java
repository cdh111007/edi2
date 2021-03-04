package com.smtl.edi.web.pojo;

import java.sql.Timestamp;

/**
 *
 * @author nm
 */
public class CodecoResend {

    private String cstcode;
    private String coper;
    private String cntrno;
    private Timestamp inouttm;
    private String flag;
    private String mode;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     *
     * @return
     */
    public String getCstcode() {
        return cstcode;
    }

    /**
     *
     * @param cstcode
     */
    public void setCstcode(String cstcode) {
        this.cstcode = cstcode;
    }

    /**
     *
     * @return
     */
    public String getCoper() {
        return coper;
    }

    /**
     *
     * @param coper
     */
    public void setCoper(String coper) {
        this.coper = coper;
    }

    /**
     *
     * @return
     */
    public String getCntrno() {
        return cntrno;
    }

    /**
     *
     * @param cntrno
     */
    public void setCntrno(String cntrno) {
        this.cntrno = cntrno;
    }

    /**
     *
     * @return
     */
    public Timestamp getInouttm() {
        return inouttm;
    }

    /**
     *
     * @param inouttm
     */
    public void setInouttm(Timestamp inouttm) {
        this.inouttm = inouttm;
    }

    /**
     *
     * @return
     */
    public String getFlag() {
        return flag;
    }

    /**
     *
     * @param flag
     */
    public void setFlag(String flag) {
        this.flag = flag;
    }

}
