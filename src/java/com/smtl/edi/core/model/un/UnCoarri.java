package com.smtl.edi.core.model.un;

import static com.EDIHelper.SEGMENT_TERMINATOR;
import com.smtl.edi.util.StringUtil;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author nm
 */
public class UnCoarri {

    private int unhToUntLines = 0;

    public static void main(String[] args) {

        UnCoarri coarri = new UnCoarri();

        UnCoarri.UNB unb = coarri.new UNB();
        UnCoarri.UNH unh = coarri.new UNH();
        UnCoarri.BGM bgm = coarri.new BGM();
        UnCoarri.TDT tdt = coarri.new TDT();
        UnCoarri.LOC loc = coarri.new LOC();
        UnCoarri.NAD_CF nad_cf = coarri.new NAD_CF();

        for (int i = 0; i < 10; i++) {

            UnCoarri.EQD eqd = coarri.new EQD();

            UnCoarri.EQD.RFF rff = eqd.new RFF();
            eqd.RFF(rff);

            UnCoarri.EQD.TMD tmd = eqd.new TMD();
            eqd.TMD(tmd);

            UnCoarri.EQD.DTM dtm = eqd.new DTM();
            eqd.DTM(dtm);

            UnCoarri.EQD.LOC9 loc_ = eqd.new LOC9();
            eqd.LOC(loc_);

            UnCoarri.EQD.MEA mea = eqd.new MEA();
            eqd.MEA(mea);

            UnCoarri.EQD.DIM dim = eqd.new DIM();
            eqd.DIM(dim);

            UnCoarri.EQD.TMP tmp = eqd.new TMP();
            eqd.TMP(tmp);

            UnCoarri.EQD.DGS dgs = eqd.new DGS();
            eqd.DGS(dgs);

            UnCoarri.EQD.SEL sel = eqd.new SEL();
            eqd.SEL(sel);

//            UnCoarri.EQD.DAM dam = eqd.new DAM();
//            eqd.DAM(dam);
            coarri.getEqds().add(eqd);
        }

        UnCoarri.CNT cnt = coarri.new CNT();
        UnCoarri.UNT unt = coarri.new UNT();
        UnCoarri.UNZ unz = coarri.new UNZ();

        coarri.UNH(unh).UNB(unb).BGM(bgm).TDT(tdt).LOC(loc).NAD_CF(nad_cf).CNT(cnt).UNT(unt).UNZ(unz);

        System.out.println((coarri.toString()).replaceAll("null", ""));

    }

    @Override
    public String toString() {
        return "" + unb + unh + bgm + tdt
                + StringUtil.blankIfNull(rff)
                + StringUtil.blankIfNull(loc)
                + StringUtil.blankIfNull(loc11)
                + StringUtil.blankIfNull(loc9)
                + StringUtil.blankIfNull(dtm132)
                + StringUtil.blankIfNull(dtm133)
                + StringUtil.blankIfNull(nadCA)
                + StringUtil.blankIfNull(nadCF)
                + StringUtil.blankIfNull(nadMS)
                + eqds() + cnt + unt + unz;
    }

    String eqds() {
        StringBuilder sb = new StringBuilder();
        for (EQD eqd : eqds) {
            sb.append(eqd);
        }
        return sb.toString();
    }

    private UNB unb;
    private UNH unh;
    private BGM bgm;
    private TDT tdt;
    private RFF rff;
    private DTM132 dtm132;
    private DTM133 dtm133;
    private LOC loc;
    private LOC9 loc9;
    private LOC11 loc11;
    private NAD_CA nadCA;
    private NAD_CF nadCF;
    private NAD_MS nadMS;
    private List<EQD> eqds = new LinkedList<>();
    private CNT cnt;
    private UNT unt;
    private UNZ unz;

    public UnCoarri UNH(UNH unh) {
        this.unh = unh;
        return this;
    }

    public UnCoarri UNB(UNB unb) {
        this.unb = unb;
        return this;
    }

    public UnCoarri BGM(BGM bgm) {
        this.bgm = bgm;
        return this;
    }

    public UnCoarri TDT(TDT tdt) {
        this.tdt = tdt;
        return this;
    }

    public UnCoarri RFF(RFF rff) {
        this.rff = rff;
        return this;
    }

    public UnCoarri LOC(LOC loc) {
        this.loc = loc;
        return this;
    }

    public UnCoarri LOC9(LOC9 loc9) {
        this.loc9 = loc9;
        return this;
    }

    public UnCoarri LOC11(LOC11 loc11) {
        this.loc11 = loc11;
        return this;
    }

    public UnCoarri NAD_CF(NAD_CF nadCF) {
        this.nadCF = nadCF;
        return this;
    }

    public UnCoarri NAD_MS(NAD_MS nadMS) {
        this.nadMS = nadMS;
        return this;
    }

    public UnCoarri NAD_CA(NAD_CA nadCA) {
        this.nadCA = nadCA;
        return this;
    }

    public UnCoarri CNT(CNT cnt) {
        this.cnt = cnt;
        return this;
    }

    public UnCoarri UNT(UNT unt) {
        this.unt = unt;
        return this;
    }

    public UnCoarri UNZ(UNZ unz) {
        this.unz = unz;
        return this;
    }

    public class UNB {
        //UNB

        private String syntaxId = "UNOA";
        private String syntaxVer = "2";
        private String senderId;
        private String recipientId;
        private String dateTimePreparation;
        private String ctrlRef;

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        @Override
        public String toString() {
            return "UNB+" + syntaxId + ":"
                    + syntaxVer + "+"
                    + senderId + "+"
                    + recipientId + "+"
                    + dateTimePreparation + "+"
                    + ctrlRef + SEGMENT_TERMINATOR;
        }

        public void setSyntaxId(String syntaxId) {
            this.syntaxId = syntaxId;
        }

        public void setSyntaxVer(String syntaxVer) {
            this.syntaxVer = syntaxVer;
        }

        public void setRecipientId(String recipientId) {
            this.recipientId = recipientId;
        }

        public void setDateTimePreparation(String dateTimePreparation) {
            this.dateTimePreparation = dateTimePreparation;
        }

        public void setCtrlRef(String ctrlRef) {
            this.ctrlRef = ctrlRef;
        }

    }

    public class UNH {
        //UNH

        private String msgRefNo = "";
        private String msgType = "COARRI";
        private String msgTypeVer = "D";
        private String msgTypeRelNo = "95B";
        private String ctrlAgcy = "UN";
        private String assignedCode = "ITG12";
        private String senderId = "";

        @Override
        public String toString() {
            unhToUntLines++;
            return "UNH+"
                    + msgRefNo + "+"
                    + msgType + ":"
                    + msgTypeVer + ":"
                    + msgTypeRelNo + ":"
                    + ctrlAgcy + ":"
                    + assignedCode + (StringUtil.isNotEmpty(senderId) ? ("+"
                    + senderId) : "") + SEGMENT_TERMINATOR;
        }

        public void setMsgRefNo(String msgRefNo) {
            this.msgRefNo = msgRefNo;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public void setMsgTypeVer(String msgTypeVer) {
            this.msgTypeVer = msgTypeVer;
        }

        public void setMsgTypeRelNo(String msgTypeRelNo) {
            this.msgTypeRelNo = msgTypeRelNo;
        }

        public void setCtrlAgcy(String ctrlAgcy) {
            this.ctrlAgcy = ctrlAgcy;
        }

        public void setAssignedCode(String assignedCode) {
            this.assignedCode = assignedCode;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

    }

    public class BGM {

        //BGM
        private String msgName;
        private String msgNo;
        private String msgFunCode = "9";

        public void setMsgName(String msgName) {
            this.msgName = msgName;
        }

        public void setMsgNo(String msgNo) {
            this.msgNo = msgNo;
        }

        public void setMsgFunCode(String msgFunCode) {
            this.msgFunCode = msgFunCode;
        }

        @Override
        public String toString() {
            unhToUntLines++;
            return "BGM+" + msgName + "+" + msgNo + "+" + msgFunCode + SEGMENT_TERMINATOR;
        }

    }

    public class DTM132 {

        private String dtmQua = "132";
        private String dtm;
        private String dtmFmtQua = "203";

        public void setDtmQua(String dtmQua) {
            this.dtmQua = dtmQua;
        }

        public void setDtm(String dtm) {
            this.dtm = dtm;
        }

        public void setDtmFmtQua(String dtmFmtQua) {
            this.dtmFmtQua = dtmFmtQua;
        }

        @Override
        public String toString() {
            unhToUntLines++;
            return "DTM+" + dtmQua + ":" + dtm + ":" + dtmFmtQua + SEGMENT_TERMINATOR;
        }

    }

    public UnCoarri DTM(DTM132 dtm) {
        this.dtm132 = dtm;
        return this;
    }

    public class DTM133 {

        private String dtmQua = "133";
        private String dtm;
        private String dtmFmtQua = "203";

        public void setDtmQua(String dtmQua) {
            this.dtmQua = dtmQua;
        }

        public void setDtm(String dtm) {
            this.dtm = dtm;
        }

        public void setDtmFmtQua(String dtmFmtQua) {
            this.dtmFmtQua = dtmFmtQua;
        }

        @Override
        public String toString() {
            unhToUntLines++;
            return "DTM+" + dtmQua + ":" + dtm + ":" + dtmFmtQua + SEGMENT_TERMINATOR;
        }

    }

    public UnCoarri DTM(DTM133 dtm) {
        this.dtm133 = dtm;
        return this;
    }

    public class TDT {

        //TDT
        private String transportStageQua = "20";
        private String voyage;
        private String transportMode = "1";
        private String carrier;
        private String carrierQua = "172";
        private String transportVesselCallSign;
        private String transportCodeQua = "103";
        private String transportVesselName;
        private String countryCode;
        private String transportNo;

        @Override
        public String toString() {
            unhToUntLines++;
            return "TDT+" + transportStageQua
                    + "+" + voyage
                    + "+" + transportMode
                    + "++" + carrier
                    + ":" + carrierQua
                    + "+++" + transportVesselCallSign
                    + ":" + transportCodeQua
                    + ":" + StringUtil.blankIfEmpty(transportNo)
                    + ":" + transportVesselName
                    + (countryCode == null ? "" : ":" + countryCode)
                    + SEGMENT_TERMINATOR;
        }

        public String getTransportNo() {
            return transportNo;
        }

        public void setTransportNo(String transportNo) {
            this.transportNo = transportNo;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public void setTransportStageQua(String transportStageQua) {
            this.transportStageQua = transportStageQua;
        }

        public void setVoyage(String voyage) {
            this.voyage = voyage;
        }

        public void setTransportMode(String transportMode) {
            this.transportMode = transportMode;
        }

        public void setCarrier(String carrier) {
            this.carrier = carrier;
        }

        public void setCarrierQua(String carrierQua) {
            this.carrierQua = carrierQua;
        }

        public void setTransportVesselCallSign(String transportVesselCallSign) {
            this.transportVesselCallSign = transportVesselCallSign;
        }

        public void setTransportCodeQua(String transportCodeQua) {
            this.transportCodeQua = transportCodeQua;
        }

        public void setTransportVesselName(String transportVesselName) {
            this.transportVesselName = transportVesselName;
        }

    }

    public class RFF {

        private String rffQua;
        private String rffVon = "VON";
        private String voyage = "";

        public String getRffQua() {
            return rffQua;
        }

        public void setRffQua(String rffQua) {
            this.rffQua = rffQua;
        }

        public String getRffVon() {
            return rffVon;
        }

        public void setRffVon(String rffVon) {
            this.rffVon = rffVon;
        }

        public String getVoyage() {
            return voyage;
        }

        public void setVoyage(String voyage) {
            this.voyage = voyage;
        }

        @Override
        public String toString() {
            unhToUntLines++;
            return "RFF+" + rffVon + ":" + voyage + SEGMENT_TERMINATOR;
        }

    }

    public class LOC {

        //LOC
        private String locQua;
        private String locId;
        private String locCodeQua = "139";
        private String respAgcy = "6";

        public void setLocCodeQua(String locCodeQua) {
            this.locCodeQua = locCodeQua;
        }

        public void setLocQua(String locQua) {
            this.locQua = locQua;
        }

        public void setLocId(String locId) {
            this.locId = locId;
        }

        public void setRespAgcy(String respAgcy) {
            this.respAgcy = respAgcy;
        }

        @Override
        public String toString() {
            unhToUntLines++;
            return "LOC+" + locQua + "+" + locId
                    + (StringUtil.isEmpty(locCodeQua) ? "" : (":" + locCodeQua))
                    + (StringUtil.isEmpty(respAgcy) ? "" : (":" + respAgcy)) + SEGMENT_TERMINATOR;
        }

    }

    public class LOC9 {

        //LOC
        private String locQua = "9";
        private String locId;
        private String locCodeQua = "139";
        private String respAgcy = "6";

        public void setLocCodeQua(String locCodeQua) {
            this.locCodeQua = locCodeQua;
        }

        public void setLocQua(String locQua) {
            this.locQua = locQua;
        }

        public void setLocId(String locId) {
            this.locId = locId;
        }

        public void setRespAgcy(String respAgcy) {
            this.respAgcy = respAgcy;
        }

        @Override
        public String toString() {
            unhToUntLines++;
            return "LOC+" + locQua + "+" + locId
                    + (StringUtil.isEmpty(locCodeQua) ? "" : (":" + locCodeQua))
                    + (StringUtil.isEmpty(respAgcy) ? "" : (":" + respAgcy)) + SEGMENT_TERMINATOR;
        }

    }

    public class LOC11 {

        //LOC
        private String locQua = "11";
        private String locId;
        private String locCodeQua = "139";
        private String respAgcy = "6";

        public void setLocCodeQua(String locCodeQua) {
            this.locCodeQua = locCodeQua;
        }

        public void setLocQua(String locQua) {
            this.locQua = locQua;
        }

        public void setLocId(String locId) {
            this.locId = locId;
        }

        public void setRespAgcy(String respAgcy) {
            this.respAgcy = respAgcy;
        }

        @Override
        public String toString() {
            unhToUntLines++;
            return "LOC+" + locQua + "+" + locId
                    + (StringUtil.isEmpty(locCodeQua) ? "" : (":" + locCodeQua))
                    + (StringUtil.isEmpty(respAgcy) ? "" : (":" + respAgcy)) + SEGMENT_TERMINATOR;
        }

    }

    public class NAD_CF {

        //NAD
        private String partyQua = "CF";
        private String partyId;
        private String partyIdCode = "160";
        private String partyRespAgcyCode = "87";

        public void setPartyQua(String partyQua) {
            this.partyQua = partyQua;
        }

        public void setPartyId(String partyId) {
            this.partyId = partyId;
        }

        public void setPartyIdCode(String partyIdCode) {
            this.partyIdCode = partyIdCode;
        }

        public void setPartyRespAgcyCode(String partyRespAgcyCode) {
            this.partyRespAgcyCode = partyRespAgcyCode;
        }

        @Override
        public String toString() {
            unhToUntLines++;
            return "NAD+" + partyQua + "+" + partyId + ":" + partyIdCode + ":" + partyRespAgcyCode + SEGMENT_TERMINATOR;
        }

    }

    public class NAD_MS {

        //NAD
        private String partyQua = "MS";
        private String partyId;

        public void setPartyQua(String partyQua) {
            this.partyQua = partyQua;
        }

        public void setPartyId(String partyId) {
            this.partyId = partyId;
        }

        @Override
        public String toString() {
            unhToUntLines++;
            return "NAD+" + partyQua + "+" + partyId + SEGMENT_TERMINATOR;
        }

    }

    public class NAD_CA {

        //NAD
        private String partyQua = "CA";
        private String partyId;
        private String partyIdCode = "160";
        private String partyRespAgcyCode = "184";

        public void setPartyQua(String partyQua) {
            this.partyQua = partyQua;
        }

        public void setPartyId(String partyId) {
            this.partyId = partyId;
        }

        public void setPartyIdCode(String partyIdCode) {
            this.partyIdCode = partyIdCode;
        }

        public void setPartyRespAgcyCode(String partyRespAgcyCode) {
            this.partyRespAgcyCode = partyRespAgcyCode;
        }

        @Override
        public String toString() {
            unhToUntLines++;
            return "NAD+" + partyQua + "+" + partyId + ":" + partyIdCode + ":" + partyRespAgcyCode + SEGMENT_TERMINATOR;
        }

    }

    public List<EQD> getEqds() {
        return eqds;
    }

    public void setEqds(List<EQD> eqds) {
        this.eqds = eqds;
    }

    public class EQD {

        private String eqpQua = "CN";
        private String eqpId;
        private String eqpSizeType;
        private String eqpSizeTypeQua = "102";
        private String eqpRespAgcyCode = "5";
        private String eqpStatus;
        private String efIndicator;

        private RFF rff;

        public EQD RFF(RFF rff) {
            this.rff = rff;
            return this;
        }

        public class RFF {

            private String refQua;
            private String refNo;

            public void setRefQua(String refQua) {
                this.refQua = refQua;
            }

            public void setRefNo(String refNo) {
                this.refNo = refNo;
            }

            @Override
            public String toString() {
                unhToUntLines++;
                return "RFF+" + refQua + ":" + refNo + SEGMENT_TERMINATOR;
            }

        }

        private NAD_CF nadCf;

        public EQD NAD_CF(NAD_CF nadCf) {
            this.nadCf = nadCf;
            return this;
        }

        public class NAD_CF {

            //NAD
            private String partyQua = "CF";
            private String partyId;
            private String partyIdCode = "172";
            private String partyRespAgcyCode = "20";

            public void setPartyQua(String partyQua) {
                this.partyQua = partyQua;
            }

            public void setPartyId(String partyId) {
                this.partyId = partyId;
            }

            public void setPartyIdCode(String partyIdCode) {
                this.partyIdCode = partyIdCode;
            }

            public void setPartyRespAgcyCode(String partyRespAgcyCode) {
                this.partyRespAgcyCode = partyRespAgcyCode;
            }

            @Override
            public String toString() {
                unhToUntLines++;
                return "NAD+" + partyQua + "+" + partyId + ":" + partyIdCode + ":" + partyRespAgcyCode + SEGMENT_TERMINATOR;
            }

        }

        public class TMD {

            private String movType = "3";

            public void setMovType(String movType) {
                this.movType = movType;
            }

            @Override
            public String toString() {
                unhToUntLines++;
                return "TMD+" + movType + SEGMENT_TERMINATOR;
            }

        }

        private TMD tmd;

        public EQD TMD(TMD tmd) {
            this.tmd = tmd;
            return this;
        }

        public class DTM {

            private String dtmQua = "7";
            private String dtm;
            private String dtmFmtQua = "203";

            public void setDtmQua(String dtmQua) {
                this.dtmQua = dtmQua;
            }

            public void setDtm(String dtm) {
                this.dtm = dtm;
            }

            public void setDtmFmtQua(String dtmFmtQua) {
                this.dtmFmtQua = dtmFmtQua;
            }

            @Override
            public String toString() {
                unhToUntLines++;
                return "DTM+" + dtmQua + ":" + dtm + ":" + dtmFmtQua + SEGMENT_TERMINATOR;
            }

        }

        private DTM dtm;

        public EQD DTM(DTM dtm) {
            this.dtm = dtm;
            return this;
        }

        public class LOC9 {

            private String loadQua = "9";
            private String loadPort;
            private String locCodeQua = "139";
            private String locCodeRespAgcy = "6";

            public void setLoadQua(String loadQua) {
                this.loadQua = loadQua;
            }

            public void setLoadPort(String loadPort) {
                this.loadPort = loadPort;
            }

            public void setLocCodeQua(String locCodeQua) {
                this.locCodeQua = locCodeQua;
            }

            public void setLocCodeRespAgcy(String locCodeRespAgcy) {
                this.locCodeRespAgcy = locCodeRespAgcy;
            }

            @Override
            public String toString() {
                unhToUntLines++;
                return "LOC+" + loadQua + "+" + loadPort + ":" + locCodeQua + ":" + locCodeRespAgcy + SEGMENT_TERMINATOR;
            }

        }

        private LOC9 loc9;

        public EQD LOC(LOC9 loc) {
            this.loc9 = loc;
            return this;
        }

        public class LOC11 {

            private String discQua = "11";
            private String discPort;
            private String locCodeQua = "139";
            private String locCodeRespAgcy = "6";

            public void setDiscQua(String discQua) {
                this.discQua = discQua;
            }

            public void setDiscPort(String discPort) {
                this.discPort = discPort;
            }

            public void setLocCodeQua(String locCodeQua) {
                this.locCodeQua = locCodeQua;
            }

            public void setLocCodeRespAgcy(String locCodeRespAgcy) {
                this.locCodeRespAgcy = locCodeRespAgcy;
            }

            @Override
            public String toString() {
                unhToUntLines++;
                return "LOC+" + discQua + "+" + discPort + ":" + locCodeQua + ":" + locCodeRespAgcy + SEGMENT_TERMINATOR;
            }

        }

        private LOC11 loc11;

        public EQD LOC(LOC11 loc) {
            this.loc11 = loc;
            return this;
        }

        public class MEA {

            private String meaQua = "AAE";
            private String meaDim = "G";//For Gross Weight
            private String meaUnitQua = "KGM";
            private String meaVal;

            public void setMeaQua(String meaQua) {
                this.meaQua = meaQua;
            }

            public void setMeaDim(String meaDim) {
                this.meaDim = meaDim;
            }

            public void setMeaUnitQua(String meaUnitQua) {
                this.meaUnitQua = meaUnitQua;
            }

            public void setMeaVal(String meaVal) {
                this.meaVal = meaVal;
            }

            @Override
            public String toString() {
                unhToUntLines++;
                return "MEA+" + meaQua + "+" + meaDim + "+" + meaUnitQua + ":" + meaVal + SEGMENT_TERMINATOR;
            }

        }

        private MEA mea;

        public EQD MEA(MEA mea) {
            this.mea = mea;
            return this;
        }

        public class DIM {

            private String dimQua;
            private String meaUnitQua;
            private String length;
            private String width;
            private String height;

            @Override
            public String toString() {
                unhToUntLines++;
                return "DIM+" + dimQua + "+" + meaUnitQua + ":" + length + ":" + width + ":" + height + SEGMENT_TERMINATOR;
            }

            public void setDimQua(String dimQua) {
                this.dimQua = dimQua;
            }

            public void setMeaUnitQua(String meaUnitQua) {
                this.meaUnitQua = meaUnitQua;
            }

            public void setLength(String length) {
                this.length = length;
            }

            public void setWidth(String width) {
                this.width = width;
            }

            public void setHeight(String height) {
                this.height = height;
            }

        }

        private DIM dim;

        public EQD DIM(DIM dim) {
            this.dim = dim;
            return this;
        }

        public class TMP {

            private String tmpQua = "2";
            private String tmpVal;
            //CEL:Celsius FAH:Fahrenheit
            private String meaUnitQua = "CEL";

            public void setTmpQua(String tmpQua) {
                this.tmpQua = tmpQua;
            }

            public void setTmpVal(String tmpVal) {
                this.tmpVal = tmpVal;
            }

            public void setMeaUnitQua(String meaUnitQua) {
                this.meaUnitQua = meaUnitQua;
            }

            @Override
            public String toString() {
                unhToUntLines++;
                return "TMP+" + tmpQua + "+" + tmpVal + ":" + meaUnitQua + SEGMENT_TERMINATOR;
            }

        }

        private TMP tmp;

        public EQD TMP(TMP tmp) {
            this.tmp = tmp;
            return this;
        }

        public class DGS {

            private String dgsRegu = "IMD";
            private String dgsCodeId;
            private String dgsNo;

            public void setDgsRegu(String dgsRegu) {
                this.dgsRegu = dgsRegu;
            }

            public void setDgsCodeId(String dgsCodeId) {
                this.dgsCodeId = dgsCodeId;
            }

            public void setDgsNo(String dgsNo) {
                this.dgsNo = dgsNo;
            }

            @Override
            public String toString() {
                unhToUntLines++;
                return "DGS+" + dgsRegu + "+" + dgsCodeId + "+" + dgsNo + SEGMENT_TERMINATOR;
            }

        }

        private DGS dgs;

        public EQD DGS(DGS dgs) {
            this.dgs = dgs;
            return this;
        }

        public class SEL {

            private String sealNo;
            private String sealingPartyCode = "CA";

            public void setSealNo(String sealNo) {
                this.sealNo = sealNo;
            }

            public void setSealingPartyCode(String sealingPartyCode) {
                this.sealingPartyCode = sealingPartyCode;
            }

            @Override
            public String toString() {
                unhToUntLines++;
                return "SEL+" + sealNo + "+" + sealingPartyCode + SEGMENT_TERMINATOR;
            }

        }

        private SEL sel;

        public EQD SEL(SEL sel) {
            this.sel = sel;
            return this;
        }

        public void setEqpStatus(String eqpStatus) {
            this.eqpStatus = eqpStatus;
        }

        @Override
        public String toString() {
            unhToUntLines++;
            return "EQD+" + eqpQua + "+" + eqpId + "+" + eqpSizeType + ":"
                    + eqpSizeTypeQua + ":" + eqpRespAgcyCode + "++" + eqpStatus
                    + "+" + efIndicator + SEGMENT_TERMINATOR
                    + rff + tmd + dtm + loc9 + loc11 + mea
                    + StringUtil.blankIfNull(dim)
                    + StringUtil.blankIfNull(tmp)
                    + sel
                    + StringUtil.blankIfNull(dgs)
                    + StringUtil.blankIfNull(dam)
                    + StringUtil.blankIfNull(nadCf);
        }

        public void setEqpQua(String eqpQua) {
            this.eqpQua = eqpQua;
        }

        public void setEqpId(String eqpId) {
            this.eqpId = eqpId;
        }

        public void setEqpSizeType(String eqpSizeType) {
            this.eqpSizeType = eqpSizeType;
        }

        public void setEqpSizeTypeQua(String eqpSizeTypeQua) {
            this.eqpSizeTypeQua = eqpSizeTypeQua;
        }

        public void setEqpRespAgcyCode(String eqpRespAgcyCode) {
            this.eqpRespAgcyCode = eqpRespAgcyCode;
        }

        public void setEfIndicator(String efIndicator) {
            this.efIndicator = efIndicator;
        }

        public class DAM {

            private String damDetailsQua = "1";

            public void setDamDetailsQua(String damDetailsQua) {
                this.damDetailsQua = damDetailsQua;
            }

            @Override
            public String toString() {
                unhToUntLines++;
                return "DAM+" + damDetailsQua + SEGMENT_TERMINATOR;
            }

        }

        private DAM dam;

        public EQD DAM(DAM dam) {
            this.dam = dam;
            return this;
        }
    }

    public class CNT {

        private String ctrlId = "16";

        public void setCtrlId(String ctrlId) {
            this.ctrlId = ctrlId;
        }

        @Override
        public String toString() {
            unhToUntLines++;
            return "CNT+" + ctrlId + ":" + eqds.size() + SEGMENT_TERMINATOR;
        }

    }

    public class UNT {

        private String msgRefNo;

        public void setMsgRefNo(String msgRefNo) {
            this.msgRefNo = msgRefNo;
        }

        @Override
        public String toString() {
            unhToUntLines++;
            return "UNT+" + unhToUntLines + "+" + msgRefNo + SEGMENT_TERMINATOR;
        }

    }

    public class UNZ {

        private String numUNH = "1";
        private String msgRefNo;

        public void setNumUNH(String numUNH) {
            this.numUNH = numUNH;
        }

        public void setMsgRefNo(String msgRefNo) {
            this.msgRefNo = msgRefNo;
        }

        @Override
        public String toString() {
            return "UNZ+" + numUNH + "+" + msgRefNo + SEGMENT_TERMINATOR;
        }

    }
}
