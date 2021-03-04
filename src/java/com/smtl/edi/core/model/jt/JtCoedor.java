package com.smtl.edi.core.model.jt;

import static com.EDIHelper.SEGMENT_TERMINATOR;
import com.smtl.edi.util.StringUtil;
import java.util.LinkedList;
import java.util.List;

/**
 * 场存报文
 *
 * @author nm
 */
public class JtCoedor {

    private int totalLines = 0;

    public static void main(String[] args) {

        JtCoedor coedor = new JtCoedor();

        JtCoedor.SEG00 seg00 = coedor.new SEG00();
        coedor.SEG00(seg00);

        for (int i = 0; i < 10; i++) {

            JtCoedor.SEG5X seg5x = coedor.new SEG5X();

            JtCoedor.SEG5X.SEG50 seg50 = coedor.new SEG5X().new SEG50();
            seg5x.SEG50(seg50);

            JtCoedor.SEG5X.SEG52 seg52 = coedor.new SEG5X().new SEG52();
            seg5x.SEG52(seg52);

            coedor.getSeg5xs().add(seg5x);
        }

        JtCoedor.SEG99 seg99 = coedor.new SEG99();
        coedor.SEG99(seg99);

        System.out.println((coedor.toString()).replaceAll("null", ""));

    }

    public class SEG5X {

        private SEG50 seg50;
        private SEG52 seg52;

        public class SEG50 {

            private String recordId = "50";
            private String ctnNo;
            private String ctnType;
            private String ctnStatus;// E空 F重 L拼
            private String stockDays;
            private String stockLocation;
            private String inboundVessel;
            private String inboundVoyage;//包含航向
            private String outboundVessel;
            private String outboundVoyage;//包含航向
            private String billNo;
            private String dischargeTime;
            private String inGateTime;
            private String vesselIE;//I进口 E出口 T中转

            public String getStockDays() {
                return stockDays;
            }

            public void setStockDays(String stockDays) {
                this.stockDays = stockDays;
            }

            public String getStockLocation() {
                return stockLocation;
            }

            public void setStockLocation(String stockLocation) {
                this.stockLocation = stockLocation;
            }

            public String getInboundVessel() {
                return inboundVessel;
            }

            public void setInboundVessel(String inboundVessel) {
                this.inboundVessel = inboundVessel;
            }

            public String getInboundVoyage() {
                return inboundVoyage;
            }

            public void setInboundVoyage(String inboundVoyage) {
                this.inboundVoyage = inboundVoyage;
            }

            public String getOutboundVessel() {
                return outboundVessel;
            }

            public void setOutboundVessel(String outboundVessel) {
                this.outboundVessel = outboundVessel;
            }

            public String getOutboundVoyage() {
                return outboundVoyage;
            }

            public void setOutboundVoyage(String outboundVoyage) {
                this.outboundVoyage = outboundVoyage;
            }

            public String getDischargeTime() {
                return dischargeTime;
            }

            public void setDischargeTime(String dischargeTime) {
                this.dischargeTime = dischargeTime;
            }

            public String getInGateTime() {
                return inGateTime;
            }

            public void setInGateTime(String inGateTime) {
                this.inGateTime = inGateTime;
            }

            public void setRecordId(String recordId) {
                this.recordId = recordId;
            }

            public void setCtnNo(String ctnNo) {
                this.ctnNo = ctnNo;
            }

            public void setCtnType(String ctnType) {
                this.ctnType = ctnType;
            }

            public void setCtnStatus(String ctnStatus) {
                this.ctnStatus = ctnStatus;
            }

            public void setVesselIE(String vesselIE) {
                this.vesselIE = vesselIE;
            }

            public void setBillNo(String billNo) {
                this.billNo = billNo;
            }

            @Override
            public String toString() {
                totalLines++;
                return recordId + ":"
                        + ctnNo + ":"
                        + ctnType + ":"
                        + ctnStatus + ":"
                        + stockDays + ":" + stockLocation + ":"
                        + inboundVessel + ":" + inboundVoyage + ":"
                        + outboundVessel + ":" + outboundVoyage + ":"
                        + billNo + ":"
                        + dischargeTime + ":"
                        + inGateTime + ":"
                        + vesselIE
                        + SEGMENT_TERMINATOR;
            }

        }

        public class SEG52 {

            private String recordId = "52";
            private String dmgTypeCode;//残损类型
            private String dmgType;
            private String dmgAreaCode;//残损范围
            private String dmgArea;
            private String dmgSeverity;//残损程度

            public String getRecordId() {
                return recordId;
            }

            public void setRecordId(String recordId) {
                this.recordId = recordId;
            }

            public String getDmgTypeCode() {
                return dmgTypeCode;
            }

            public void setDmgTypeCode(String dmgTypeCode) {
                this.dmgTypeCode = dmgTypeCode;
            }

            public String getDmgType() {
                return dmgType;
            }

            public void setDmgType(String dmgType) {
                this.dmgType = dmgType;
            }

            public String getDmgAreaCode() {
                return dmgAreaCode;
            }

            public void setDmgAreaCode(String dmgAreaCode) {
                this.dmgAreaCode = dmgAreaCode;
            }

            public String getDmgArea() {
                return dmgArea;
            }

            public void setDmgArea(String dmgArea) {
                this.dmgArea = dmgArea;
            }

            public String getDmgSeverity() {
                return dmgSeverity;
            }

            public void setDmgSeverity(String dmgSeverity) {
                this.dmgSeverity = dmgSeverity;
            }

            @Override
            public String toString() {
                totalLines++;
                return recordId + ":" + dmgTypeCode + ":" + dmgType + ":" + dmgAreaCode + ":" + dmgArea + ":" + dmgSeverity + SEGMENT_TERMINATOR;
            }

        }

        public SEG5X SEG50(SEG50 seg50) {
            this.seg50 = seg50;
            return this;
        }

        public SEG5X SEG52(SEG52 seg52) {
            this.seg52 = seg52;
            return this;
        }

        @Override
        public String toString() {
            return "" + seg50 + seg52;
        }

    }

    private SEG00 seg00;

    private SEG99 seg99;
    private List<SEG5X> seg5xs = new LinkedList<>();

    public List<SEG5X> getSeg5xs() {
        return seg5xs;
    }

    public void setSeg5xs(List<SEG5X> seg5xs) {
        this.seg5xs = seg5xs;
    }

    String seg5xs() {
        StringBuilder sb = new StringBuilder();
        for (SEG5X seg5x : seg5xs) {
            sb.append(seg5x);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "" + seg00 + seg5xs() + seg99;
    }

    public JtCoedor SEG00(SEG00 seg00) {
        this.seg00 = seg00;
        return this;
    }

    public JtCoedor SEG99(SEG99 seg99) {
        this.seg99 = seg99;
        return this;
    }

    public class SEG00 {

        private String recordId = "00";
        private String msgType = "COEDOR";
        private String fileDesc = "STOCK REPORT";//GATE-IN REPORT / GATE-OUT REPORT
        private String fileFunc = "9";
        private String sender;
        private String recipient;
        private String fileCreateTime;//YYYYMMDDHHMM

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public void setFileDesc(String fileDesc) {
            this.fileDesc = fileDesc;
        }

        public void setFileFunc(String fileFunc) {
            this.fileFunc = fileFunc;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        public void setFileCreateTime(String fileCreateTime) {
            this.fileCreateTime = fileCreateTime;
        }

        @Override
        public String toString() {
            totalLines++;
            return recordId + ":" + msgType + ":"
                    + fileDesc + ":" + fileFunc + ":"
                    + sender + ":" + recipient + ":" + fileCreateTime + SEGMENT_TERMINATOR;
        }

    }

    public class SEG01 {

        private String recordId = "01";

        private String sender;
        private String recipient;

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        public String getRecordId() {
            return recordId;
        }

        public String getSender() {
            return sender;
        }

        public String getRecipient() {
            return recipient;
        }

        @Override
        public String toString() {
            totalLines++;
            return recordId + ":"
                    + sender + ":" + recipient + SEGMENT_TERMINATOR;
        }

    }

    public class SEG11 {

        private String recordId = "10";
        private String ctnOperatorCode;
        private String ctnOperatorName;

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public String getCtnOperatorCode() {
            return ctnOperatorCode;
        }

        public void setCtnOperatorCode(String ctnOperatorCode) {
            this.ctnOperatorCode = ctnOperatorCode;
        }

        public String getCtnOperatorName() {
            return ctnOperatorName;
        }

        public void setCtnOperatorName(String ctnOperatorName) {
            this.ctnOperatorName = ctnOperatorName;
        }

        @Override
        public String toString() {
            totalLines++;
            return recordId + ":"
                    + ctnOperatorCode + (StringUtil.isEmpty(ctnOperatorName) ? "" : ":" + ctnOperatorName)
                    + SEGMENT_TERMINATOR;
        }

    }

    public class SEG99 {

        private String recordId = "99";

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        @Override
        public String toString() {
            totalLines++;
            return recordId + ":" + totalLines + SEGMENT_TERMINATOR;
        }

    }
}
