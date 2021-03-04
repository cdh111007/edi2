package com.smtl.edi.core.model.un;

import static com.EDIHelper.SEGMENT_TERMINATOR;
import com.smtl.edi.core.model.un.UnCodeco.EQD.SEL;
import com.smtl.edi.core.model.un.UnCodeco.GIDS.GID;
import com.smtl.edi.util.StringUtil;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author nm
 */
public class UnCodeco {

    private int unhToUntLines = 0;

    public static void main(String[] args) {

        UnCodeco codeco = new UnCodeco();

        UnCodeco.UNB unb = codeco.new UNB();
        UnCodeco.UNH unh = codeco.new UNH();
        UnCodeco.BGM bgm = codeco.new BGM();
        UnCodeco.TDT tdt = codeco.new TDT();
        UnCodeco.LOC loc = codeco.new LOC();
        UnCodeco.NAD_MS nad = codeco.new NAD_MS();

        int gidId = 0;

        for (int i = 0; i < 3; i++) {

            UnCodeco.GIDS gids = codeco.new GIDS();

            gidId++;

            UnCodeco.GIDS.GID gid = codeco.new GIDS().new GID();
            gid.setGidId(String.valueOf(gidId));
            gids.GID(gid);

            UnCodeco.GIDS.TMP tmp_ = codeco.new GIDS().new TMP();
            tmp_.setTmpVal("25" + i);
            gids.TMP(tmp_);

            UnCodeco.GIDS.SGP sgp = codeco.new GIDS().new SGP();
            sgp.setCtnNo("CIMU0231003" + i);
            gids.SGP(sgp);

            UnCodeco.GIDS.DGS dgs = codeco.new GIDS().new DGS();
            dgs.setDgsCode("10.3" + i);
            gids.DGS(dgs);

            codeco.getGids().add(gids);
        }

        for (int i = 0; i < 10; i++) {

            UnCodeco.EQD eqd = codeco.new EQD();

            UnCodeco.EQD.DIM dim = eqd.new DIM();
            UnCodeco.EQD.DTM dtm = eqd.new DTM();
            UnCodeco.EQD.LOC loc_ = eqd.new LOC();
            UnCodeco.EQD.MEA mea = eqd.new MEA();
            UnCodeco.EQD.RFF rff_ = eqd.new RFF();

            UnCodeco.EQD.SEL sel1 = eqd.new SEL();
            UnCodeco.EQD.SEL sel2 = eqd.new SEL();
            eqd.getSels().add(sel1);
            eqd.getSels().add(sel2);

            eqd.RFF(rff_).DTM(dtm).LOC(loc_).MEA(mea).DIM(dim);

            codeco.getEqds().add(eqd);
        }

        UnCodeco.CNT cnt = codeco.new CNT();
        UnCodeco.UNT unt = codeco.new UNT();
        UnCodeco.UNZ unz = codeco.new UNZ();

        codeco.UNB(unb).UNH(unh).BGM(bgm).TDT(tdt).LOC(loc).NAD(nad).CNT(cnt).UNT(unt).UNZ(unz);

        System.out.println((codeco.toString()).replaceAll("null", ""));

    }

    @Override
    public String toString() {
        return "" + unb
                + unh
                + bgm
                + StringUtil.blankIfNull(tdt)
                + StringUtil.blankIfNull(loc)
                + StringUtil.blankIfNull(nadMS)
                + StringUtil.blankIfNull(nadCA)
                + StringUtil.blankIfNull(nadCF)
                + gids()
                + eqds()
                + cnt
                + unt
                + unz;
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
    private LOC loc;
    private NAD_MS nadMS;
    private NAD_CF nadCF;
    private NAD_CA nadCA;
    private CNT cnt;
    private UNT unt;
    private UNZ unz;

    public UnCodeco UNB(UNB unb) {
        this.unb = unb;
        return this;
    }

    public UnCodeco UNH(UNH unh) {
        this.unh = unh;
        return this;
    }

    public UnCodeco BGM(BGM bgm) {
        this.bgm = bgm;
        return this;
    }

    public UnCodeco TDT(TDT tdt) {
        this.tdt = tdt;
        return this;
    }

    public UnCodeco LOC(LOC loc) {
        this.loc = loc;
        return this;
    }

    public UnCodeco NAD(NAD_MS nadMS) {
        this.nadMS = nadMS;
        return this;
    }

    public UnCodeco NAD(NAD_CF nadCF) {
        this.nadCF = nadCF;
        return this;
    }

    public UnCodeco NAD(NAD_CA nadCA) {
        this.nadCA = nadCA;
        return this;
    }

    public UnCodeco CNT(CNT cnt) {
        this.cnt = cnt;
        return this;
    }

    public UnCodeco UNT(UNT unt) {
        this.unt = unt;
        return this;
    }

    public UnCodeco UNZ(UNZ unz) {
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
            return "UNB" + "+" + syntaxId + ":"
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

        private String msgRefNo;
        private String msgType = "CODECO";
        private String msgTypeVer = "D";
        private String msgTypeRelNo = "95B";
        private String ctrlAgcy = "UN";
        private String assignedCode = "ITG14";

        @Override
        public String toString() {
            unhToUntLines++;
            return "UNH" + "+"
                    + msgRefNo + "+"
                    + msgType + ":"
                    + msgTypeVer + ":"
                    + msgTypeRelNo + ":"
                    + ctrlAgcy + ":"
                    + assignedCode + SEGMENT_TERMINATOR;
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
            return "BGM" + "+" + msgName + "+" + msgNo + "+" + msgFunCode + SEGMENT_TERMINATOR;
        }

    }

    public class TDT {

        //TDT
        private String transportStageQua = "20";
        private String voyage;
        private String transportMode = "1";
        private String carrier;
        private String carrierQua = "172";
        private String respAgcy = "20";
        private String transportVesselCallSign;
        private String transportCodeQua = "103";
        private String transportVesselName;

        public void setRespAgcy(String respAgcy) {
            this.respAgcy = respAgcy;
        }

        @Override
        public String toString() {
            unhToUntLines++;
            return "TDT+" + transportStageQua + "+" + voyage + "+"
                    + transportMode + "++" + carrier + ":"
                    + carrierQua + ":" + respAgcy + "+++" + transportVesselCallSign
                    + ":" + transportCodeQua + "::" + transportVesselName + SEGMENT_TERMINATOR;
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

        private String refQua = "VON";
        private String voyage;

        public void setRefQua(String refQua) {
            this.refQua = refQua;
        }

        public void setVoyage(String voyage) {
            this.voyage = voyage;
        }

        @Override
        public String toString() {
            unhToUntLines++;
            return "RFF+" + refQua + ":" + voyage + SEGMENT_TERMINATOR;
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
            return "LOC+" + locQua + "+" + locId + ":" + locCodeQua + ":" + respAgcy + SEGMENT_TERMINATOR;
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
        /**
         * Tag	Ind.	Value	Description 3035	PARTY QUALIFIER. MS	Message Sender CF
         * Container Operator / lessee
         *
         * 3039	Party ID identification Identification of message sender
         *
         * 1131	Code list qualifier 160	Party ID (for 3035 = MS) 172	Carrier
         * Code (for 3035 = CF)
         *
         * 3055	Code list responsible agency, coded ZZZ	mutually agreed (for
         * 1131 = 160) 166	US NMFCA (SCAC) (for 1131 = 172) 20	BIC (for 1131 =
         * 172)
         *
         */
        private String partyQua = "MS";
        private String partyId;
        private String partyIdCode = "160";
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

    private List<EQD> eqds = new LinkedList<>();

    public List<EQD> getEqds() {
        return eqds;
    }

    public void setEqds(List<EQD> eqds) {
        this.eqds = eqds;
    }

    private List<GIDS> gids = new LinkedList<>();

    public List<GIDS> getGids() {
        return gids;
    }

    public void setGids(List<GIDS> gids) {
        this.gids = gids;
    }

    String gids() {
        StringBuilder sb = new StringBuilder();
        for (GIDS gid : gids) {
            sb.append(gid);
        }
        return sb.toString();
    }

    public class GIDS {

        @Override
        public String toString() {
            return "" + gid + tmp + sgp + dgs;
        }

        private GID gid;

        public GIDS GID(GID gid) {
            this.gid = gid;
            return this;
        }

        private TMP tmp;

        public GIDS TMP(TMP tmp) {
            this.tmp = tmp;
            return this;
        }

        private SGP sgp;

        public GIDS SGP(SGP sgp) {
            this.sgp = sgp;
            return this;
        }

        private DGS dgs;

        public GIDS DGS(DGS dgs) {
            this.dgs = dgs;
            return this;
        }

        public class GID {

            private String gidQua = "GID";
            private String gidId;

            public String getGidId() {
                return gidId;
            }

            public void setGidId(String gidId) {
                this.gidId = gidId;
            }

            public String getGidQua() {
                return gidQua;
            }

            public void setGidQua(String gidQua) {
                this.gidQua = gidQua;
            }

            @Override
            public String toString() {
                unhToUntLines++;
                return "GID+" + gidId + SEGMENT_TERMINATOR;
            }

        }

        public class TMP {

            private String tmpQua = "TMP";
            private String tmpId = "2";
            private String tmpVal;
            private String tmpIndicator = "CEL";

            public String getTmpQua() {
                return tmpQua;
            }

            public void setTmpQua(String tmpQua) {
                this.tmpQua = tmpQua;
            }

            public String getTmpId() {
                return tmpId;
            }

            public void setTmpId(String tmpId) {
                this.tmpId = tmpId;
            }

            public String getTmpVal() {
                return tmpVal;
            }

            public void setTmpVal(String tmpVal) {
                this.tmpVal = tmpVal;
            }

            public String getTmpIndicator() {
                return tmpIndicator;
            }

            public void setTmpIndicator(String tmpIndicator) {
                this.tmpIndicator = tmpIndicator;
            }

            @Override
            public String toString() {
                unhToUntLines++;
                return tmpQua + "+" + tmpId + "+" + tmpVal + ":" + tmpIndicator + SEGMENT_TERMINATOR;
            }

        }

        public class SGP {

            private String sgpQua = "SGP";
            private String ctnNo;

            public String getSgpQua() {
                return sgpQua;
            }

            public void setSgpQua(String sgpQua) {
                this.sgpQua = sgpQua;
            }

            public String getCtnNo() {
                return ctnNo;
            }

            public void setCtnNo(String ctnNo) {
                this.ctnNo = ctnNo;
            }

            @Override
            public String toString() {
                unhToUntLines++;
                return sgpQua + "+" + ctnNo + SEGMENT_TERMINATOR;
            }

        }

        public class DGS {

            private String dgsQua = "DGS";
            private String dgsId = "IMO";
            private String dgsCode;

            public String getDgsQua() {
                return dgsQua;
            }

            public void setDgsQua(String dgsQua) {
                this.dgsQua = dgsQua;
            }

            public String getDgsId() {
                return dgsId;
            }

            public void setDgsId(String dgsId) {
                this.dgsId = dgsId;
            }

            public String getDgsCode() {
                return dgsCode;
            }

            public void setDgsCode(String dgsCode) {
                this.dgsCode = dgsCode;
            }

            @Override
            public String toString() {
                unhToUntLines++;
                return dgsQua + "+" + dgsId + "+" + dgsCode + SEGMENT_TERMINATOR;
            }

        }
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

        public class TMP {

            private String tmpQua = "2";
            private String tmpVal;
            private String meaUnitQua;

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

        public UnCodeco.EQD TMP(TMP tmp) {
            this.tmp = tmp;
            return this;
        }

        public class LOC {

            private String locQua = "165";
            private String locId;
            private String locCodeQua = "139";
            private String locCodeRespAgcy = "6";
            private String locOther;

            public String getLocOther() {
                return locOther;
            }

            public void setLocOther(String locOther) {
                this.locOther = locOther;
            }

            public void setLocQua(String locQua) {
                this.locQua = locQua;
            }

            public void setLocId(String locId) {
                this.locId = locId;
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
                return "LOC+" + locQua + "+" + locId + ":" + locCodeQua + ":" + locCodeRespAgcy
                        + (StringUtil.isNotEmpty(locOther) ? "+" + locOther : "") + SEGMENT_TERMINATOR;
            }

        }

        private LOC loc;

        public EQD LOC(LOC loc) {
            this.loc = loc;
            return this;
        }

        public class MEA {

            private String meaQua = "AAE";
            private String meaDim = "G";
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

        String sels() {
            StringBuilder sb = new StringBuilder();
            for (SEL sel : sels) {
                sb.append(sel);
            }
            return sb.toString();
        }

        private List<SEL> sels = new LinkedList<>();

        public List<SEL> getSels() {
            return sels;
        }

        public void setSels(List<SEL> sels) {
            this.sels = sels;
        }

        public void setEqpStatus(String eqpStatus) {
            this.eqpStatus = eqpStatus;
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

        public class NAD {

            //NAD
            private String partyQua = "CF";
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

        private NAD nad;

        public EQD NAD(NAD nad) {
            this.nad = nad;
            return this;
        }

        @Override
        public String toString() {
            unhToUntLines++;
            return "EQD+" + eqpQua + "+" + eqpId + "+" + eqpSizeType + ":" + eqpSizeTypeQua + ":" + eqpRespAgcyCode + "++" + eqpStatus + "+" + efIndicator + SEGMENT_TERMINATOR
                    + StringUtil.blankIfNull(rff)
                    + StringUtil.blankIfNull(dtm)
                    + StringUtil.blankIfNull(loc)
                    + StringUtil.blankIfNull(mea)
                    + StringUtil.blankIfNull(dim)
                    + StringUtil.blankIfNull(tmp)
                    + StringUtil.blankIfNull(dgs)
                    + sels()
                    + StringUtil.blankIfNull(dam)
                    + StringUtil.blankIfNull(tdt)
                    + StringUtil.blankIfNull(nad);
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

        private TDT tdt;

        public EQD TDT(TDT tdt) {
            this.tdt = tdt;
            return this;
        }

        public class TDT {

            private String transportStageQua = "1";
            private String refNo;
            private String transportMode;
            private String carrierName;
            private String transportId;

            public void setTransportStageQua(String transportStageQua) {
                this.transportStageQua = transportStageQua;
            }

            public void setRefNo(String refNo) {
                this.refNo = refNo;
            }

            public void setTransportMode(String transportMode) {
                this.transportMode = transportMode;
            }

            public void setCarrierName(String carrierName) {
                this.carrierName = carrierName;
            }

            public void setTransportId(String transportId) {
                this.transportId = transportId;
            }

            @Override
            public String toString() {
                unhToUntLines++;
                return "TDT+" + transportStageQua + "+" + refNo + (StringUtil.isEmpty(transportMode) ? "" : ("+" + transportMode))
                        + (StringUtil.isEmpty(carrierName) ? "" : ("+:::" + carrierName)) + (StringUtil.isEmpty(transportId) ? "" : ("+++" + transportId)) + SEGMENT_TERMINATOR;
            }

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
