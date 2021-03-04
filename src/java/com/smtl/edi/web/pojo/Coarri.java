package com.smtl.edi.web.pojo;

import com.smtl.edi.util.StringUtil;
import java.sql.Timestamp;

/**
 *
 * @author nm
 */
public class Coarri {

    private String vslName;
    private String voyage;
    private String iefg;
    private String ldfg;
    private String cstCode;
    private Timestamp createdAt;
    private String logId;
    private String sendFlag;
    private String filename;

    public String getFilename() {
        if (StringUtil.isNotEmpty(filename)) {
            filename = filename.substring(filename.lastIndexOf("\\") + 1);
        }
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(String sendFlag) {
        this.sendFlag = sendFlag;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    /**
     *
     * @return
     */
    public String getVslName() {
        return vslName;
    }

    /**
     *
     * @param vslName
     */
    public void setVslName(String vslName) {
        this.vslName = vslName;
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
    public String getIefg() {
        if (iefg != null) {
            if (iefg.equalsIgnoreCase("2")) {
                iefg = "E";
            }
            if (iefg.equalsIgnoreCase("3")) {
                iefg = "I";
            }
        }
        return iefg;
    }

    /**
     *
     * @param iefg
     */
    public void setIefg(String iefg) {
        this.iefg = iefg;
    }

    /**
     *
     * @return
     */
    public String getLdfg() {
        if (ldfg != null) {
            if (ldfg.equalsIgnoreCase("270")) {
                ldfg = "LOAD REPORT";
            }
            if (ldfg.equalsIgnoreCase("98")) {
                ldfg = "DISCHARGE REPORT";
            }
        }
        return ldfg;
    }

    /**
     *
     * @param ldfg
     */
    public void setLdfg(String ldfg) {
        this.ldfg = ldfg;
    }

    /**
     *
     * @return
     */
    public String getCstCode() {
        return cstCode;
    }

    /**
     *
     * @param cstCode
     */
    public void setCstCode(String cstCode) {
        this.cstCode = cstCode;
    }

    /**
     *
     * @return
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Coarri{" + "vslName=" + vslName + ", voyage=" + voyage + ", iefg=" + iefg + ", ldfg=" + ldfg + ", cstCode=" + cstCode + ", createdAt=" + createdAt + ", logId=" + logId + '}';
    }

}
