package com.smtl.edi.core.model.jt;

import static com.EDIHelper.SEGMENT_TERMINATOR;

/**
 *
 * @author nm
 */
public class JtCosecr {

    private int totalLines = 0;

    public void setTotalLines(int totalLines) {
        this.totalLines = totalLines;
    }

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

    private SEG00 seg00;
    private SEG10 seg10;
    private SEG99 seg99;

    @Override
    public String toString() {
        return "" + seg00 + seg10 + seg99;
    }

    public JtCosecr SEG00(SEG00 seg00) {
        this.seg00 = seg00;
        return this;
    }

    public JtCosecr SEG10(SEG10 seg10) {
        this.seg10 = seg10;
        return this;
    }

    public JtCosecr SEG99(SEG99 seg99) {
        this.seg99 = seg99;
        return this;
    }

    public class SEG00 {

        private String recordId = "00";
        private String msgType = "COSECR";
        private String fileDesc = "STEVEDORING END CONFIRM REPORT";//GATE-IN REPORT / GATE-OUT REPORT
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
        private String vesselNameCN;
        private String vesselNameEN;
        private String importVoyage;
        private String exportVoyage;
        private String ats;
        private String ate;
        private String placeCode;

        public String getPlaceCode() {
            return placeCode;
        }

        public void setPlaceCode(String placeCode) {
            this.placeCode = placeCode;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public String getVesselNameCN() {
            return vesselNameCN;
        }

        public void setVesselNameCN(String vesselNameCN) {
            this.vesselNameCN = vesselNameCN;
        }

        public String getVesselNameEN() {
            return vesselNameEN;
        }

        public void setVesselNameEN(String vesselNameEN) {
            this.vesselNameEN = vesselNameEN;
        }

        public String getImportVoyage() {
            return importVoyage;
        }

        public void setImportVoyage(String importVoyage) {
            this.importVoyage = importVoyage;
        }

        public String getExportVoyage() {
            return exportVoyage;
        }

        public void setExportVoyage(String exportVoyage) {
            this.exportVoyage = exportVoyage;
        }

        public String getAts() {
            return ats;
        }

        public void setAts(String ats) {
            this.ats = ats;
        }

        public String getAte() {
            return ate;
        }

        public void setAte(String ate) {
            this.ate = ate;
        }

        @Override
        public String toString() {
            totalLines++;
            return recordId + ":"
                    + vesselNameCN + ":"
                    + vesselNameEN + ":"
                    + importVoyage + ":"
                    + exportVoyage + ":"
                    + ats + ":"
                    + ate + ":"
                    + placeCode
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
