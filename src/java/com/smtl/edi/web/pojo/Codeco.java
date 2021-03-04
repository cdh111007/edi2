package com.smtl.edi.web.pojo;

import com.smtl.edi.util.StringUtil;
import java.sql.Timestamp;

/**
 *
 * @author nm
 */
public class Codeco {

    private String ctnNo;
    private String ctnOperator;
    private String cstCode;
    private String inoutfg;
    private Timestamp createAt;
    private String logId;
    private String inOutMode;
    private String inYardTime;
    private String outYardTime;
    private String sendFlag;
    private String filename;
    private String msgType;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

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

    public String getInYardTime() {
        return inYardTime;
    }

    public void setInYardTime(String inYardTime) {
        this.inYardTime = inYardTime;
    }

    public String getOutYardTime() {
        return outYardTime;
    }

    public void setOutYardTime(String outYardTime) {
        this.outYardTime = outYardTime;
    }

    public String getInOutMode() {
        return inOutMode;
    }

    public void setInOutMode(String inOutMode) {
        this.inOutMode = inOutMode;
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
    public String getCtnNo() {
        return ctnNo;
    }

    /**
     *
     * @param ctnNo
     */
    public void setCtnNo(String ctnNo) {
        this.ctnNo = ctnNo;
    }

    /**
     *
     * @return
     */
    public String getCtnOperator() {
        return ctnOperator;
    }

    /**
     *
     * @param ctnOperator
     */
    public void setCtnOperator(String ctnOperator) {
        this.ctnOperator = ctnOperator;
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
    public String getInoutfg() {
        if (inoutfg != null) {
            if (inoutfg.equalsIgnoreCase("34")) {
                inoutfg = "GATE-IN REPORT";
            }
            if (inoutfg.equalsIgnoreCase("36")) {
                inoutfg = "GATE-OUT REPORT";
            }
        }
        return inoutfg;
    }

    /**
     *
     * @param inoutfg
     */
    public void setInoutfg(String inoutfg) {
        this.inoutfg = inoutfg;
    }

    /**
     *
     * @return
     */
    public Timestamp getCreateAt() {
        return createAt;
    }

    /**
     *
     * @param createAt
     */
    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Codeco{" + "cntrno=" + ctnNo + ", copercd=" + ctnOperator + ", custcode=" + cstCode + ", inoutfg=" + inoutfg + ", createdon=" + createAt + '}';
    }

}
