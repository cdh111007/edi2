package com.smtl.edi.core.model.jt;

import static com.EDIHelper.SEGMENT_TERMINATOR;
import com.smtl.edi.util.StringUtil;
import com.smtl.edi.util.ValidationUtil;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author nm
 */
public class JtXxzxCoarri {

    private int totalLines = 0;

    public static void main(String[] args) {

        JtXxzxCoarri coarri = new JtXxzxCoarri();

        JtXxzxCoarri.SEG00 seg00 = coarri.new SEG00();
        coarri.SEG00(seg00);

        JtXxzxCoarri.SEG10 seg10 = coarri.new SEG10();
        coarri.SEG10(seg10);

        JtXxzxCoarri.SEG14 seg14 = coarri.new SEG14();
        coarri.SEG14(seg14);

        //箱子循环
        for (int i = 0; i < 10; i++) {

            JtXxzxCoarri.SEG5X seg5x = coarri.new SEG5X();

            JtXxzxCoarri.SEG5X.SEG50 seg50 = coarri.new SEG5X().new SEG50();
            seg5x.SEG50(seg50);

            JtXxzxCoarri.SEG5X.SEG52 seg52 = coarri.new SEG5X().new SEG52();
            seg5x.SEG52(seg52);

            for (int j = 0; j < Double.valueOf(Math.random() * 3).intValue(); j++) {
                JtXxzxCoarri.SEG5X.SEG53 seg53 = coarri.new SEG5X().new SEG53();
                seg5x.getSeg53s().add(seg53);
            }

            coarri.getSeg5xs().add(seg5x);
        }

        JtXxzxCoarri.SEG6X seg6x = coarri.new SEG6X();

        JtXxzxCoarri.SEG6X.SEG60 seg60 = coarri.new SEG6X().new SEG60();
        seg6x.SEG60(seg60);
        coarri.getSeg6xs().add(seg6x);

        JtXxzxCoarri.SEG99 seg99 = coarri.new SEG99();
        coarri.SEG99(seg99);

        System.out.println((coarri.toString()).replaceAll("null", ""));

    }

    public class SEG5X {

        private SEG50 seg50;
        private SEG52 seg52;

        public class SEG50 {

            private String recordId = "50";
            private String ctnNo;
            private String ctnSizeType;
            private String ctnOperatorCode;
            private String ctnOperator;
            private String ctnStatus;
            private String billNo;
            private String sealNo;
            private String stowageLoc;//船舶贝位
            private String cargoName;
            private String hazFlag;
            private String cargoWeight;
            private String transhipFlag = "N";//中转标记
            private String tradeType;//贸易类型
            private String firstVessel;
            private String firstVoyage;
            private String inOutQuayTime;//进出码头时间
            private String payerName;

            public String getInOutQuayTime() {
                return inOutQuayTime;
            }

            public void setInOutQuayTime(String inOutQuayTime) {
                this.inOutQuayTime = inOutQuayTime;
            }

            public String getCargoName() {
                return cargoName;
            }

            public void setCargoName(String cargoName) {
                this.cargoName = cargoName;
            }

            public String getHazFlag() {
                return hazFlag;
            }

            public void setHazFlag(String hazFlag) {
                this.hazFlag = hazFlag;
            }

            public String getCargoWeight() {
                return cargoWeight;
            }

            public void setCargoWeight(String cargoWeight) {
                this.cargoWeight = cargoWeight;
            }

            public String getTranshipFlag() {
                return transhipFlag;
            }

            public void setTranshipFlag(String transhipFlag) {
                this.transhipFlag = transhipFlag;
            }

            public String getTradeType() {
                return tradeType;
            }

            public void setTradeType(String tradeType) {
                this.tradeType = tradeType;
            }

            public String getFirstVessel() {
                return firstVessel;
            }

            public void setFirstVessel(String firstVessel) {
                this.firstVessel = firstVessel;
            }

            public String getFirstVoyage() {
                return firstVoyage;
            }

            public void setFirstVoyage(String firstVoyage) {
                this.firstVoyage = firstVoyage;
            }

            public String getPayerName() {
                return payerName;
            }

            public void setPayerName(String payerName) {
                this.payerName = payerName;
            }

            public void setBillNo(String billNo) {
                this.billNo = billNo;
            }

            public void setRecordId(String recordId) {
                this.recordId = recordId;
            }

            public void setCtnNo(String ctnNo) {
                this.ctnNo = ctnNo;
            }

            public void setCtnSizeType(String ctnSizeType) {
                this.ctnSizeType = ctnSizeType;
            }

            public void setCtnOperatorCode(String ctnOperatorCode) {
                this.ctnOperatorCode = ctnOperatorCode;
            }

            public void setCtnOperator(String ctnOperator) {
                this.ctnOperator = ctnOperator;
            }

            public void setCtnStatus(String ctnStatus) {
                this.ctnStatus = ctnStatus;
            }

            public void setSealNo(String sealNo) {
                this.sealNo = sealNo;
            }

            public void setStowageLoc(String stowageLoc) {
                this.stowageLoc = stowageLoc;
            }

            @Override
            public String toString() {
                totalLines++;
                return recordId
                        + ":" + ctnNo
                        + ":" + ctnSizeType
                        + ":" + ctnOperatorCode
                        + ":" + ctnOperator
                        + ":" + ctnStatus
                        + ":" + billNo
                        + ":" + sealNo
                        + ":" + stowageLoc
                        + ":" + cargoName
                        + ":" + hazFlag
                        + ":" + cargoWeight
                        + ":" + transhipFlag
                        + ":" + tradeType
                        + ":" + firstVessel
                        + ":" + firstVoyage
                        + ":" + inOutQuayTime
                        + (StringUtil.isEmpty(payerName) ? "" : ":" + payerName) + SEGMENT_TERMINATOR;
            }

        }

        public class SEG52 {

            private String recordId = "52";
            private String dischargePortCode;
            private String dischargePortName;
            private String loadPortCode;
            private String loadPortName;
            private String destPortCode;
            private String destPortName;
            private String grossWeight;//箱毛重

            public void setGrossWeight(String grossWeight) {
                this.grossWeight = grossWeight;
            }

            public void setRecordId(String recordId) {
                this.recordId = recordId;
            }

            public void setDischargePortCode(String dischargePortCode) {
                this.dischargePortCode = dischargePortCode;
            }

            public void setDischargePortName(String dischargePortName) {
                this.dischargePortName = dischargePortName;
            }

            public void setLoadPortCode(String loadPortCode) {
                this.loadPortCode = loadPortCode;
            }

            public void setLoadPortName(String loadPortName) {
                this.loadPortName = loadPortName;
            }

            public void setDestPortCode(String destPortCode) {
                this.destPortCode = destPortCode;
            }

            public void setDestPortName(String destPortName) {
                this.destPortName = destPortName;
            }

            @Override
            public String toString() {
                totalLines++;
                return recordId + ":"
                        + dischargePortCode + ":"
                        + dischargePortName + ":"
                        + loadPortCode + ":"
                        + loadPortName + ":"
                        + destPortCode + ":"
                        + destPortName + ":"
                        + grossWeight + SEGMENT_TERMINATOR;
            }

        }

        public class SEG53 {

            private String recordId = "53";
            private String dmgTypeCode;//残损类型代码
            private String dmgType;//残损类型
            private String dmgAreaCode;//残损范围代码
            private String dmgArea;//残损范围
            private String dmgSeverity;//残损程度

            @Override
            public String toString() {
                totalLines++;
                return recordId + ":"
                        + dmgTypeCode + ":"
                        + dmgType + ":"
                        + dmgAreaCode + ":"
                        + dmgArea + ":"
                        + dmgSeverity + SEGMENT_TERMINATOR;
            }

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

        }

        public SEG5X SEG50(SEG50 seg50) {
            this.seg50 = seg50;
            return this;
        }

        public SEG5X SEG52(SEG52 seg52) {
            this.seg52 = seg52;
            return this;
        }

        private List<SEG53> seg53s = new LinkedList<>();

        public List<SEG53> getSeg53s() {
            return seg53s;
        }

        public void setSeg53s(List<SEG53> seg53s) {
            this.seg53s = seg53s;
        }

        String seg53s() {
            StringBuilder sb = new StringBuilder();
            for (SEG53 seg53 : seg53s) {
                sb.append(seg53);
            }
            return sb.toString();
        }

        @Override
        public String toString() {
            return "" + seg50 + seg52 + ((ValidationUtil.isValid(seg53s)) ? seg53s() : "");
        }

    }

    private SEG00 seg00;
    private SEG10 seg10;
    private SEG14 seg14;
    private SEG99 seg99;
    private List<SEG5X> seg5xs = new LinkedList<>();
    private List<SEG6X> seg6xs = new LinkedList<>();

    public List<SEG6X> getSeg6xs() {
        return seg6xs;
    }

    public void setSeg6xs(List<SEG6X> seg6xs) {
        this.seg6xs = seg6xs;
    }

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

    String seg6xs() {
        StringBuilder sb = new StringBuilder();
        for (SEG6X seg6x : seg6xs) {
            sb.append(seg6x);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "" + seg00 + seg10 + ((ValidationUtil.isValid(seg14)) ? seg14 : "") + seg5xs() + seg6xs() + seg99;
    }

    public JtXxzxCoarri SEG00(SEG00 seg00) {
        this.seg00 = seg00;
        return this;
    }

    public JtXxzxCoarri SEG10(SEG10 seg10) {
        this.seg10 = seg10;
        return this;
    }

    public JtXxzxCoarri SEG14(SEG14 seg14) {
        this.seg14 = seg14;
        return this;
    }

    public JtXxzxCoarri SEG99(SEG99 seg99) {
        this.seg99 = seg99;
        return this;
    }

    public class SEG00 {

        private String recordId = "00";
        private String msgType = "COARRI";
        private String fileDesc;
        private String fileFunc = "9";
        private String sender;
        private String receiver;
        private String fileCreateTime;
        private String other = "TC:TC";

        public String getOther() {
            return other;
        }

        public void setOther(String other) {
            this.other = other;
        }

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

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public void setFileCreateTime(String fileCreateTime) {
            this.fileCreateTime = fileCreateTime;
        }

        @Override
        public String toString() {
            totalLines++;
            return recordId + ":"
                    + msgType + ":"
                    + fileDesc + ":"
                    + fileFunc + ":"
                    + sender + ":"
                    + receiver + ":"
                    + fileCreateTime + ":"
                    + other + SEGMENT_TERMINATOR;
        }

    }

    public class SEG10 {

        private String recordId = "10";
        private String vesselCode;
        private String vesselName;
        private String voyage;
        private String countryCode;
        private String linerType;//班轮类型Y = 班轮  N = 非班轮 H = 核心班轮
        private String berthingTime;
        private String departureTime;
        private String startDischargingTime;
        private String completeDischargingTime;
        private String startLoadingTime;
        private String completeLoadingTime;
        private String totalCtns;
        private String berthNo;
        private String IMO;//船舶识别号
        private String yqb = "1";
        private String berthingDraft;
        private String departureDraft;
        private String cargoName;
        private String totalWeight = "0";
        private String teu;
        private String zkl = "0";
        private String sxk = "0";
        private String payerName;//付费人名称

        public String getBerthNo() {
            return berthNo;
        }

        public void setBerthNo(String berthNo) {
            this.berthNo = berthNo;
        }

        public String getIMO() {
            return IMO;
        }

        public void setIMO(String IMO) {
            this.IMO = IMO;
        }

        public String getYqb() {
            return yqb;
        }

        public void setYqb(String yqb) {
            this.yqb = yqb;
        }

        public String getBerthingDraft() {
            return berthingDraft;
        }

        public void setBerthingDraft(String berthingDraft) {
            this.berthingDraft = berthingDraft;
        }

        public String getDepartureDraft() {
            return departureDraft;
        }

        public void setDepartureDraft(String departureDraft) {
            this.departureDraft = departureDraft;
        }

        public String getCargoName() {
            return cargoName;
        }

        public void setCargoName(String cargoName) {
            this.cargoName = cargoName;
        }

        public String getTotalWeight() {
            return totalWeight;
        }

        public void setTotalWeight(String totalWeight) {
            this.totalWeight = totalWeight;
        }

        public String getTeu() {
            return teu;
        }

        public void setTeu(String teu) {
            this.teu = teu;
        }

        public String getZkl() {
            return zkl;
        }

        public void setZkl(String zkl) {
            this.zkl = zkl;
        }

        public String getSxk() {
            return sxk;
        }

        public void setSxk(String sxk) {
            this.sxk = sxk;
        }

        public String getPayerName() {
            return payerName;
        }

        public void setPayerName(String payerName) {
            this.payerName = payerName;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public void setVesselCode(String vesselCode) {
            this.vesselCode = vesselCode;
        }

        public void setVesselName(String vesselName) {
            this.vesselName = vesselName;
        }

        public void setVoyage(String voyage) {
            this.voyage = voyage;
        }

        public void setLinerType(String linerType) {
            this.linerType = linerType;
        }

        public void setBerthingTime(String berthingTime) {
            this.berthingTime = berthingTime;
        }

        public void setDepartureTime(String departureTime) {
            this.departureTime = departureTime;
        }

        public void setStartDischargingTime(String startDischargingTime) {
            this.startDischargingTime = startDischargingTime;
        }

        public void setCompleteDischargingTime(String completeDischargingTime) {
            this.completeDischargingTime = completeDischargingTime;
        }

        public void setStartLoadingTime(String startLoadingTime) {
            this.startLoadingTime = startLoadingTime;
        }

        public void setCompleteLoadingTime(String completeLoadingTime) {
            this.completeLoadingTime = completeLoadingTime;
        }

        public void setTotalCtns(String totalCtns) {
            this.totalCtns = totalCtns;
        }

        @Override
        public String toString() {
            totalLines++;
            return recordId + ":"
                    + vesselCode + ":"
                    + vesselName + ":"
                    + voyage + ":"
                    + countryCode + ":"
                    + linerType + ":"
                    + berthingTime + ":"
                    + departureTime + ":"
                    + startDischargingTime + ":"
                    + completeDischargingTime + ":"
                    + startLoadingTime + ":"
                    + completeLoadingTime + ":"
                    + totalCtns + ":"
                    + berthNo + ":"
                    + IMO + ":"
                    + yqb + ":"
                    + berthingDraft + ":"
                    + departureDraft + ":"
                    + cargoName + ":"
                    + totalWeight + ":"
                    + teu + ":"
                    + zkl + ":"
                    + sxk + (StringUtil.isEmpty(payerName) ? "" : ":" + payerName) + SEGMENT_TERMINATOR;
        }

    }

    public class SEG14 {

        private String recordId = "14";
        private String tallyCompany;//理货公司
        private String chiefTally;//理货组长
        private String chiefOfficer;//船长大副

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public String getTallyCompany() {
            return tallyCompany;
        }

        public void setTallyCompany(String tallyCompany) {
            this.tallyCompany = tallyCompany;
        }

        public String getChiefTally() {
            return chiefTally;
        }

        public void setChiefTally(String chiefTally) {
            this.chiefTally = chiefTally;
        }

        public String getChiefOfficer() {
            return chiefOfficer;
        }

        public void setChiefOfficer(String chiefOfficer) {
            this.chiefOfficer = chiefOfficer;
        }

        @Override
        public String toString() {
            totalLines++;
            return recordId + ":"
                    + tallyCompany + ":"
                    + chiefTally + ":"
                    + chiefOfficer
                    + SEGMENT_TERMINATOR;
        }

    }

    public class SEG6X {

        private SEG60 seg60;

        public SEG6X SEG60(SEG60 seg60) {
            this.seg60 = seg60;
            return this;
        }

        public class SEG60 {

            private String recordId = "60";
            private String ctnSize;
            private String emptyCtnCount = "";
            private String fullCtnCount = "";
            private String hazCargoFlag = "N";
            private String hazCargoNo = "DANGER_CARGO_NO";

            public String getRecordId() {
                return recordId;
            }

            public void setRecordId(String recordId) {
                this.recordId = recordId;
            }

            public String getCtnSize() {
                return ctnSize;
            }

            public void setCtnSize(String ctnSize) {
                this.ctnSize = ctnSize;
            }

            public String getEmptyCtnCount() {
                return emptyCtnCount;
            }

            public void setEmptyCtnCount(String emptyCtnCount) {
                this.emptyCtnCount = emptyCtnCount;
            }

            public String getFullCtnCount() {
                return fullCtnCount;
            }

            public void setFullCtnCount(String fullCtnCount) {
                this.fullCtnCount = fullCtnCount;
            }

            public String getHazCargoFlag() {
                return hazCargoFlag;
            }

            public void setHazCargoFlag(String hazCargoFlag) {
                this.hazCargoFlag = hazCargoFlag;
            }

            public String getHazCargoNo() {
                return hazCargoNo;
            }

            public void setHazCargoNo(String hazCargoNo) {
                this.hazCargoNo = hazCargoNo;
            }

            @Override
            public String toString() {
                totalLines++;
                return recordId
                        + ":" + ctnSize
                        + ":" + emptyCtnCount
                        + ":" + fullCtnCount
                        + ":" + hazCargoFlag
                        + (hazCargoFlag.equalsIgnoreCase("N") ? "" : ":" + hazCargoNo) + SEGMENT_TERMINATOR;
            }

        }

        @Override
        public String toString() {
            return "" + seg60;
        }
    }

    public class SEG99 {

        private final String recordId = "99";

        @Override
        public String toString() {
            totalLines++;
            return recordId + ":" + totalLines + SEGMENT_TERMINATOR;
        }

    }
}
