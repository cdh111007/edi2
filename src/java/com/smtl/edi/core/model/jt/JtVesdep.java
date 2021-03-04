package com.smtl.edi.core.model.jt;

import static com.EDIHelper.SEGMENT_TERMINATOR;

/**
 *
 * @author nm
 */
public class JtVesdep {

    private int totalLines = 0;

    public static void main(String[] args) {

        JtVesdep vesdep = new JtVesdep();

        JtVesdep.SEG00 seg00 = vesdep.new SEG00();
        vesdep.SEG00(seg00);

        JtVesdep.SEG10 seg10 = vesdep.new SEG10();
        vesdep.SEG10(seg10);

        JtVesdep.SEG15 seg15 = vesdep.new SEG15();
        vesdep.SEG15(seg15);

        JtVesdep.SEG16 seg16 = vesdep.new SEG16();
        vesdep.SEG16(seg16);

        JtVesdep.SEG99 seg99 = vesdep.new SEG99();
        vesdep.SEG99(seg99);

        System.out.println((vesdep.toString()).replaceAll("null", ""));

    }

    public void setTotalLines(int totalLines) {
        this.totalLines = totalLines;
    }

    private SEG00 seg00;
    private SEG10 seg10;
    private SEG15 seg15;
    private SEG16 seg16;
    private SEG99 seg99;

    @Override
    public String toString() {
        return "" + seg00 + seg10 + seg15 + seg16 + seg99;
    }

    public JtVesdep SEG00(SEG00 seg00) {
        this.seg00 = seg00;
        return this;
    }

    public JtVesdep SEG10(SEG10 seg10) {
        this.seg10 = seg10;
        return this;
    }

    public JtVesdep SEG15(SEG15 seg15) {
        this.seg15 = seg15;
        return this;
    }

    public JtVesdep SEG16(SEG16 seg16) {
        this.seg16 = seg16;
        return this;
    }

    public JtVesdep SEG99(SEG99 seg99) {
        this.seg99 = seg99;
        return this;
    }

    public class SEG00 {

        private String recordId = "00";
        private String msgType = "VESDEP";
        private String fileDesc = "VESSEL DEPARTURE";//GATE-IN REPORT / GATE-OUT REPORT
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

    public class SEG10 {

        private String recordId = "10";
        private String vesselCode;
        private String vessel;
        private String voyage;
        private String carrierCode;
        private String carrier;
        private String nationalityCode;
        private String linerId;
        private String confirmTime;
        private String sailingTime;
        private String berthTime;

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

        public String getCarrierCode() {
            return carrierCode;
        }

        public void setCarrierCode(String carrierCode) {
            this.carrierCode = carrierCode;
        }

        public String getCarrier() {
            return carrier;
        }

        public void setCarrier(String carrier) {
            this.carrier = carrier;
        }

        public String getNationalityCode() {
            return nationalityCode;
        }

        public void setNationalityCode(String nationalityCode) {
            this.nationalityCode = nationalityCode;
        }

        public String getLinerId() {
            return linerId;
        }

        public void setLinerId(String linerId) {
            this.linerId = linerId;
        }

        public String getConfirmTime() {
            return confirmTime;
        }

        public void setConfirmTime(String confirmTime) {
            this.confirmTime = confirmTime;
        }

        public String getSailingTime() {
            return sailingTime;
        }

        public void setSailingTime(String sailingTime) {
            this.sailingTime = sailingTime;
        }

        public String getBerthTime() {
            return berthTime;
        }

        public void setBerthTime(String berthTime) {
            this.berthTime = berthTime;
        }

        @Override
        public String toString() {
            totalLines++;
            return recordId + ":"
                    + vesselCode + ":"
                    + vessel + ":"
                    + voyage + ":"
                    + carrierCode + ":"
                    + carrier + ":"
                    + nationalityCode + ":"
                    + linerId + ":"
                    + confirmTime + ":"
                    + sailingTime + ":"
                    + berthTime
                    + SEGMENT_TERMINATOR;
        }

    }

    public class SEG15 {

        private String recordId = "15";

        private String dischargeBegin;
        private String dischargeEnd;

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public String getDischargeBegin() {
            return dischargeBegin;
        }

        public void setDischargeBegin(String dischargeBegin) {
            this.dischargeBegin = dischargeBegin;
        }

        public String getDischargeEnd() {
            return dischargeEnd;
        }

        public void setDischargeEnd(String dischargeEnd) {
            this.dischargeEnd = dischargeEnd;
        }

        @Override
        public String toString() {
            totalLines++;
            return recordId + ":" + dischargeBegin + ":"
                    + dischargeEnd + SEGMENT_TERMINATOR;
        }

    }

    public class SEG16 {

        private String recordId = "16";
        private String loadingBegin;
        private String loadingEnd;

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public String getLoadingBegin() {
            return loadingBegin;
        }

        public void setLoadingBegin(String loadingBegin) {
            this.loadingBegin = loadingBegin;
        }

        public String getLoadingEnd() {
            return loadingEnd;
        }

        public void setLoadingEnd(String loadingEnd) {
            this.loadingEnd = loadingEnd;
        }

        @Override
        public String toString() {
            totalLines++;
            return recordId + ":" + loadingBegin + ":"
                    + loadingEnd + SEGMENT_TERMINATOR;
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
