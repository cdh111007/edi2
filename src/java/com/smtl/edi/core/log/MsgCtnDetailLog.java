package com.smtl.edi.core.log;

import com.smtl.edi.util.DatetimeUtil;
import java.sql.Timestamp;

/**
 * 记录报文明细
 *
 * @author Administrator
 */
public class MsgCtnDetailLog {

    private String customer;
    private String msgType;//codeco coarri
    private String msgName;//gate-in/out load/disch
    private String ctnNo;
    private String vslName;
    private String voyage;
    private String vslRef;
    private Timestamp inQuayTime;
    private Timestamp outQuayTime;

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        msgName = msgName.toUpperCase();
        this.msgName = msgName;
    }

    public String getVslName() {
        return vslName;
    }

    public void setVslName(String vslName) {
        this.vslName = vslName;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        customer = customer.toUpperCase();
        this.customer = customer;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        msgType = msgType.toUpperCase();
        this.msgType = msgType;
    }

    public String getCtnNo() {
        return ctnNo;
    }

    public void setCtnNo(String ctnNo) {
        this.ctnNo = ctnNo;
    }

    public String getVoyage() {
        return voyage;
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    public String getVslRef() {
        return vslRef;
    }

    public void setVslRef(String vslRef) {
        this.vslRef = vslRef;
    }

    public Timestamp getInQuayTime() {
        return inQuayTime;
    }

    public void setInQuayTime(Timestamp inQuayTime) {
        if (inQuayTime == null) {
            inQuayTime = DatetimeUtil.toTimestamp(DatetimeUtil.toCalendar("1970-01-01 00:00:00"));
        }
        this.inQuayTime = inQuayTime;
    }

    public Timestamp getOutQuayTime() {
        return outQuayTime;
    }

    public void setOutQuayTime(Timestamp outQuayTime) {
        if (outQuayTime == null) {
            outQuayTime = DatetimeUtil.toTimestamp(DatetimeUtil.toCalendar("1970-01-01 00:00:00"));
        }
        this.outQuayTime = outQuayTime;
    }

}
