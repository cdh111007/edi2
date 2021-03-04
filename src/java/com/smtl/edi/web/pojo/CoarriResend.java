package com.smtl.edi.web.pojo;

import java.sql.Timestamp;

/**
 *
 * @author nm
 */
public class CoarriResend {

    private String vnamcd;
    private String voyage;
    private Timestamp ushuttm;//卸船关闭时间
    private Timestamp lshuttm;//装船关闭时间
    private Timestamp endtm;//完工时间

    public Timestamp getEndtm() {
        return endtm;
    }

    public void setEndtm(Timestamp endtm) {
        this.endtm = endtm;
    }

    /**
     *
     * @return
     */
    public String getVnamcd() {
        return vnamcd;
    }

    /**
     *
     * @param vnamcd
     */
    public void setVnamcd(String vnamcd) {
        this.vnamcd = vnamcd;
    }

    /**
     *
     * @return
     */
    public String getVoyage() {
        return voyage;
    }

    /**
     *
     * @param voyage
     */
    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    /**
     *
     * @return
     */
    public Timestamp getUshuttm() {
        return ushuttm;
    }

    /**
     *
     * @param ushuttm
     */
    public void setUshuttm(Timestamp ushuttm) {
        this.ushuttm = ushuttm;
    }

    /**
     *
     * @return
     */
    public Timestamp getLshuttm() {
        return lshuttm;
    }

    /**
     *
     * @param lshuttm
     */
    public void setLshuttm(Timestamp lshuttm) {
        this.lshuttm = lshuttm;
    }

}
