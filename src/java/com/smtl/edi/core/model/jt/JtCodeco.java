package com.smtl.edi.core.model.jt;

import static com.EDIHelper.SEGMENT_TERMINATOR;
import com.smtl.edi.util.StringUtil;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author nm
 */
public class JtCodeco {

    private int totalLines = 0;

    public static void main(String[] args) {

        JtCodeco codeco = new JtCodeco();

        JtCodeco.SEG00 seg00 = codeco.new SEG00();
        codeco.SEG00(seg00);

        JtCodeco.SEG10 seg10 = codeco.new SEG10();
        codeco.SEG10(seg10);

        for (int i = 0; i < 10; i++) {

            JtCodeco.SEG5X seg5x = codeco.new SEG5X();

            JtCodeco.SEG5X.SEG50 seg50 = codeco.new SEG5X().new SEG50();
            seg5x.SEG50(seg50);

//            JtCodeco.SEG5X.SEG51 seg51 = codeco.new SEG5X().new SEG51();
//            seg5x.SEG51(seg51);
//
//            JtCodeco.SEG5X.SEG52 seg52 = codeco.new SEG5X().new SEG52();
//            seg5x.SEG52(seg52);

            JtCodeco.SEG5X.SEG53 seg53 = codeco.new SEG5X().new SEG53();
            seg5x.SEG53(seg53);

            codeco.getSeg5xs().add(seg5x);
        }

        JtCodeco.SEG99 seg99 = codeco.new SEG99();
        codeco.SEG99(seg99);

        System.out.println((codeco.toString()).replaceAll("null", ""));

    }

    public class SEG5X {

        private SEG50 seg50;
        private SEG51 seg51;
        private SEG52 seg52;
        private SEG53 seg53;

        public class SEG50 {

            private String recordId = "50";
            private String ctnNo;
            private String ctnType;
            private String ctnStatus;// E空 F重 L拼
            private String inOutGatePurpose;//I进口 E出口 V装 D拆
            private String billNo;
            private String grossWeight;
            private String sealNo;
            private String inGateTime;
            private String outGateTime;

            public String getInGateTime() {
                return inGateTime;
            }

            public void setInGateTime(String inGateTime) {
                this.inGateTime = inGateTime;
            }

            public String getOutGateTime() {
                return outGateTime;
            }

            public void setOutGateTime(String outGateTime) {
                this.outGateTime = outGateTime;
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

            public void setInOutGatePurpose(String inOutGatePurpose) {
                this.inOutGatePurpose = inOutGatePurpose;
            }

            public void setBillNo(String billNo) {
                this.billNo = billNo;
            }

            public void setGrossWeight(String grossWeight) {
                this.grossWeight = grossWeight;
            }

            public void setSealNo(String sealNo) {
                this.sealNo = sealNo;
            }

            @Override
            public String toString() {
                totalLines++;
                return recordId + ":" + ctnNo + ":" + ctnType + ":"
                        + ctnStatus + ":" + inOutGatePurpose + "::" + billNo + ":"
                        + grossWeight + ":" + sealNo + ":" + inGateTime
                        + ((StringUtils.isBlank(outGateTime)) ? "" : ":" + outGateTime)
                        + SEGMENT_TERMINATOR;
            }

        }

        public class SEG51 {

            private String recordId = "51";
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

        public class SEG52 {

            private String recordId = "52";
            private String transportMode = "3";
            private String trailerLicNo;//拖车牌号
            private String carrierCode;//承运人代码
            private String fromOrTo;//进门来自何处 出门到哪里

            public String getTrailerLicNo() {
                return trailerLicNo;
            }

            public void setTrailerLicNo(String trailerLicNo) {
                this.trailerLicNo = trailerLicNo;
            }

            public String getCarrierCode() {
                return carrierCode;
            }

            public void setCarrierCode(String carrierCode) {
                this.carrierCode = carrierCode;
            }

            public String getFromOrTo() {
                return fromOrTo;
            }

            public void setFromOrTo(String fromOrTo) {
                this.fromOrTo = fromOrTo;
            }

            public void setRecordId(String recordId) {
                this.recordId = recordId;
            }

            public void setTransportMode(String transportMode) {
                this.transportMode = transportMode;
            }

            @Override
            public String toString() {
                totalLines++;
                return recordId + ":" + transportMode + (StringUtil.isEmpty(trailerLicNo) ? "" : ":" + trailerLicNo)
                        + (StringUtil.isEmpty(carrierCode) ? "" : ":" + carrierCode)
                        + (StringUtil.isEmpty(fromOrTo) ? "" : ":" + fromOrTo) + SEGMENT_TERMINATOR;
            }

        }

        public class SEG53 {

            private String recordId = "10";
            private String vesselCode;
            private String vessel;
            private String voyage;
            private String ctnOperatorCode;
            private String ctnOperator;

            public String getCtnOperatorCode() {
                return ctnOperatorCode;
            }

            public void setCtnOperatorCode(String ctnOperatorCode) {
                this.ctnOperatorCode = ctnOperatorCode;
            }

            public String getCtnOperator() {
                return ctnOperator;
            }

            public void setCtnOperator(String ctnOperator) {
                this.ctnOperator = ctnOperator;
            }

            public String getRecordId() {
                return recordId;
            }

            public void setRecordId(String recordId) {
                this.recordId = recordId;
            }

            public String getVesselCode() {
                return vesselCode;
            }

            public void setVesselCode(String vesselCode) {
                this.vesselCode = vesselCode;
            }

            public String getVessel() {
                return vessel;
            }

            public void setVessel(String vessel) {
                this.vessel = vessel;
            }

            public String getVoyage() {
                return voyage;
            }

            public void setVoyage(String voyage) {
                this.voyage = voyage;
            }

            @Override
            public String toString() {
                totalLines++;
                return recordId + ":" + vesselCode + ":"
                        + vessel + ":" + voyage + ":"
                        + ctnOperatorCode + ":" + ctnOperator + SEGMENT_TERMINATOR;
            }

        }

        public SEG5X SEG50(SEG50 seg50) {
            this.seg50 = seg50;
            return this;
        }

        public SEG5X SEG51(SEG51 seg51) {
            this.seg51 = seg51;
            return this;
        }

        public SEG5X SEG52(SEG52 seg52) {
            this.seg52 = seg52;
            return this;
        }

        public SEG5X SEG53(SEG53 seg53) {
            this.seg53 = seg53;
            return this;
        }

        @Override
        public String toString() {
            return "" + seg53 + seg50 + seg51 + seg52;
        }

    }

    private SEG00 seg00;
    private SEG10 seg10;

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
        return "" + seg00 + seg10 + seg5xs() + seg99;
    }

    public JtCodeco SEG00(SEG00 seg00) {
        this.seg00 = seg00;
        return this;
    }

    public JtCodeco SEG10(SEG10 seg10) {
        this.seg10 = seg10;
        return this;
    }

    public JtCodeco SEG99(SEG99 seg99) {
        this.seg99 = seg99;
        return this;
    }

    public class SEG00 {

        private String recordId = "00";
        private String msgType = "CODECO";
        private String fileDesc;//GATE-IN REPORT / GATE-OUT REPORT
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

    /**
     * 重箱一般要求这些信息
     */
    public class SEG10 {

        private String recordId = "10";
        private String vesselCode;
        private String vesselName;
        private String voyage;
        private String ctnOperatorCode;
        private String ctnOperatorName;
        private String ieFlag;//I 进口 E 出口

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public String getVesselCode() {
            return vesselCode;
        }

        public void setVesselCode(String vesselCode) {
            this.vesselCode = vesselCode;
        }

        public String getVesselName() {
            return vesselName;
        }

        public void setVesselName(String vesselName) {
            this.vesselName = vesselName;
        }

        public String getVoyage() {
            return voyage;
        }

        public void setVoyage(String voyage) {
            this.voyage = voyage;
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

        public String getIeFlag() {
            return ieFlag;
        }

        public void setIeFlag(String ieFlag) {
            this.ieFlag = ieFlag;
        }

        @Override
        public String toString() {
            totalLines++;
            return recordId + ":"
                    + vesselCode + ":"
                    + vesselName + ":"
                    + voyage + ":"
                    + ctnOperatorCode + (StringUtil.isEmpty(ctnOperatorName) ? "" : ":" + ctnOperatorName)
                    + (StringUtil.isEmpty(ieFlag) ? "" : ":" + ieFlag)
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
