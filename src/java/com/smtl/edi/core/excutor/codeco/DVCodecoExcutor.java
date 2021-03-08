package com.smtl.edi.core.excutor.codeco;

import com.EDIHelper;
import static com.EDIHelper.PS_CODECO_CUSTOMER;
import static com.EDIHelper.buildFilename;
import static com.EDIHelper.logMsgDetails;
import com.smtl.edi.core.SQLQueryConstants;
import com.smtl.edi.core.log.MsgCtnDetailLog;
import com.smtl.edi.core.model.un.UnCodeco;
import com.smtl.edi.core.model.un.UnCodeco.CNT;
import com.smtl.edi.core.model.un.UnCodeco.EQD.DTM;
import com.smtl.edi.core.model.un.UnCodeco.EQD.LOC;
import com.smtl.edi.core.model.un.UnCodeco.EQD.MEA;
import com.smtl.edi.core.model.un.UnCodeco.EQD.NAD;
import com.smtl.edi.core.model.un.UnCodeco.EQD.SEL;
import com.smtl.edi.core.model.un.UnCodeco.GIDS;
import com.smtl.edi.core.model.un.UnCodeco.GIDS.GID;
import com.smtl.edi.core.model.un.UnCodeco.GIDS.SGP;
import com.smtl.edi.core.model.un.UnCodeco.NAD_MS;
import com.smtl.edi.core.model.un.UnCodeco.UNH;
import com.smtl.edi.core.model.un.UnCodeco.UNT;
import com.smtl.edi.util.DatetimeUtil;
import com.smtl.edi.util.DbUtil;
import com.smtl.edi.util.ExceptionUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import static com.EDIHelper.logSend;
import static com.EDIHelper.write;
import com.smtl.edi.core.model.jt.JtCodeco;
import com.smtl.edi.core.task.mail.ExceptionNotifyTask;
import static com.smtl.edi.util.DatetimeUtil.YYYYMMDDHHMM;
import com.smtl.edi.util.PropertiesUtil;
import com.smtl.edi.util.StringUtil;
import static com.smtl.edi.util.StringUtil.buildSqlInClause;
import com.smtl.edi.util.ValidationUtil;
import com.smtl.edi.vo.DateRange;
import java.sql.Connection;
import java.util.Arrays;

/**
 * 拆装箱
 *
 * @author Administrator
 */
public class DVCodecoExcutor {

    public static void main(String[] args) {
        jt("TCWD", new DateRange("20201210213000", "20201210220000"), true);
    }
    private static final Logger LOGGER = Logger.getLogger(DVCodecoExcutor.class);

    /**
     * 根据时间周期创建交通部格式拆装箱报文
     *
     *
     * @param range@param customer
     * @param redo
     * @param ctnNos
     */
    public static void jt(String customer, DateRange range, boolean redo, String... ctnNos) {

        List<String> cstCodes = Arrays.asList(PropertiesUtil.getValue("dv_codeco_1_cst_code").split("\\,"));

        String sql = cstCodes.contains(customer) ? SQLQueryConstants.SQL_CODECO_DEV_VAN_CTN_1 : SQLQueryConstants.SQL_CODECO_DEV_VAN_CTN;

        sql = sql + " and CTN_OPERATOR=? ";

        if (ValidationUtil.isValid(ctnNos)) {
            sql = sql + "and ctn_no in(" + buildSqlInClause(ctnNos) + ") ";
        }

        try {

            customer = customer.toUpperCase();

            PS_CODECO_CUSTOMER.setString(1, customer);
            PS_CODECO_CUSTOMER.setString(2, "CODECO");
            ResultSet rsCust = PS_CODECO_CUSTOMER.executeQuery();

            String sender, receiver;
            if (rsCust.next()) {
                sender = rsCust.getString("cst_sender");
                receiver = rsCust.getString("cst_receiver");
            } else {
                return;
            }

            Connection con = DbUtil.getConnection();

            String[] jobTypes = {"GATE-IN REPORT", "GATE-OUT REPORT"};

            for (int i = 0; i < jobTypes.length; i++) {

                PreparedStatement psCtn = con.prepareStatement(sql);

                try (PreparedStatement psCtnOperator = DbUtil.preparedStatement(con, SQLQueryConstants.SQL_CTN_OPERATOR)) {

                    psCtnOperator.setString(1, customer);
                    ResultSet rsCtnOperator = psCtnOperator.executeQuery();

                    while (rsCtnOperator.next()) {

                        String ctnOperator = rsCtnOperator.getString("ctn_operator");
                        if (StringUtil.isNotEmpty(ctnOperator)) {
                            ctnOperator = ctnOperator.toUpperCase();
                        }

                        JtCodeco codeco_ = new JtCodeco();

                        JtCodeco.SEG00 seg00 = codeco_.new SEG00();
                        seg00.setSender(sender);
                        seg00.setRecipient(receiver);
                        seg00.setFileDesc(jobTypes[i]);
                        seg00.setFileCreateTime(DatetimeUtil.now(DatetimeUtil.YYYYMMDDHHMM));
                        codeco_.SEG00(seg00);

                        JtCodeco.SEG10 seg10 = codeco_.new SEG10();
                        seg10.setCtnOperatorCode(ctnOperator);
                        codeco_.SEG10(seg10);

                        psCtn.setString(1, customer);
                        psCtn.setString(2, jobTypes[i]);
                        psCtn.setString(3, range.getBegin());
                        psCtn.setString(4, range.getEnd());
                        psCtn.setString(5, ctnOperator);

                        List<MsgCtnDetailLog> logs = new LinkedList<>();

                        ResultSet rsCtn = psCtn.executeQuery();

                        while (rsCtn.next()) {

                            String billNo = rsCtn.getString("bill_no");
                            String sealNo = rsCtn.getString("seal_no");
                            String ctnType = rsCtn.getString("ctn_size") + rsCtn.getString("ctn_type");
                            String ctnStatus = rsCtn.getString("ctn_status");

                            JtCodeco.SEG5X seg5x = codeco_.new SEG5X();

                            JtCodeco.SEG5X.SEG50 seg50 = seg5x.new SEG50();
                            seg50.setBillNo(billNo);
                            seg50.setGrossWeight(rsCtn.getString("gross_weight"));
                            seg50.setCtnNo(rsCtn.getString("ctn_no"));
                            seg50.setCtnStatus(ctnStatus);
                            seg50.setCtnType(ctnType);
                            if ("GATE-IN REPORT".equals(jobTypes[i])) {
                                seg50.setInOutGatePurpose("E");//I进口 E出口 V装 D拆
                            } else {
                                seg50.setInOutGatePurpose("I");//I进口 E出口 V装 D拆
                            }
                            if (i == 0) {
                                seg50.setInGateTime(rsCtn.getString("out_yard_time"));
                            } else {
                                seg50.setOutGateTime(DatetimeUtil.format(DatetimeUtil.halfAnHourAgo(rsCtn.getString("out_yard_time")), YYYYMMDDHHMM));
                            }
                            seg50.setSealNo(sealNo);

                            seg5x.SEG50(seg50);

                            JtCodeco.SEG5X.SEG52 seg52 = seg5x.new SEG52();
                            seg52.setTransportMode("3");

                            seg5x.SEG52(seg52);

                            codeco_.getSeg5xs().add(seg5x);

                            MsgCtnDetailLog log = new MsgCtnDetailLog();

                            log.setCustomer(customer);
                            log.setMsgName(jobTypes[i]);
                            log.setMsgType("codeco");
                            log.setCtnNo(rsCtn.getString("ctn_no"));
                            log.setInQuayTime((rsCtn.getString("in_yard_time") == null) ? null : DatetimeUtil.toTimestamp(DatetimeUtil.toCalendar(rsCtn.getString("in_yard_time"))));
                            log.setOutQuayTime((rsCtn.getString("out_yard_time") == null) ? null : DatetimeUtil.toTimestamp(DatetimeUtil.toCalendar(rsCtn.getString("out_yard_time"))));

                            logs.add(log);

                        }

                        JtCodeco.SEG99 seg99 = codeco_.new SEG99();
                        codeco_.SEG99(seg99);

                        String logId = String.valueOf(EDIHelper.getLogSeq());

                        if (!codeco_.getSeg5xs().isEmpty()) {
                            String report = codeco_.toString().replaceAll("null", "");
                            System.out.println(report);
                            String filename = buildFilename("CODECO", customer, sender,
                                    receiver, "DV", logId, "txt");
                            boolean ok = write(filename, report);
                            if (ok) {
                                logSend(logId, customer, "DV", "JT", filename, report, redo);
                                logMsgDetails(Long.valueOf(logId), logs);
                            }
                        } else {
                            logSend(logId, customer, "DV", "JT", "", "", redo);
                        }

                    }

                }

                DbUtil.close(psCtn);

            }

        } catch (SQLException ex) {
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            ExceptionNotifyTask.notify(ex, new String[]{customer, "codeco"});
        }

    }

    /**
     * 根据时间周期创建联合国格式拆装箱报文
     *
     * @param customer
     * @param range
     * @param redo
     * @param ctnNos
     */
    public static void un(String customer, DateRange range, boolean redo, String... ctnNos) {

        List<String> cstCodes = Arrays.asList(PropertiesUtil.getValue("dv_codeco_1_cst_code").split("\\,"));

        String sql = cstCodes.contains(customer) ? SQLQueryConstants.SQL_CODECO_DEV_VAN_CTN_1 : SQLQueryConstants.SQL_CODECO_DEV_VAN_CTN;

        if (ValidationUtil.isValid(ctnNos)) {
            sql = sql + "and ctn_no in(" + buildSqlInClause(ctnNos) + ") ";
        }

        PreparedStatement psCtn = null;
        customer = customer.toUpperCase();

        try {

            PS_CODECO_CUSTOMER.setString(1, customer);
            PS_CODECO_CUSTOMER.setString(2, "CODECO");
            ResultSet rsCustomer = PS_CODECO_CUSTOMER.executeQuery();

            String sender, receiver;
            if (rsCustomer.next()) {
                sender = rsCustomer.getString("cst_sender");
                receiver = rsCustomer.getString("cst_receiver");
            } else {
                return;
            }

            String[] jobTypes = {"GATE-IN REPORT", "GATE-OUT REPORT"};

            for (int i = 0; i < jobTypes.length; i++) {

                List<MsgCtnDetailLog> logs = new LinkedList<>();

                String logId = String.valueOf(EDIHelper.getLogSeq());

                psCtn = DbUtil.getConnection().prepareStatement(sql);

                UnCodeco codeco = new UnCodeco();

                //UNB
                UnCodeco.UNB unb = codeco.new UNB();
                unb.setSenderId(sender);
                unb.setRecipientId(receiver);
                unb.setDateTimePreparation(DatetimeUtil.now(DatetimeUtil.YYYYMMDD_HHMM));
                unb.setCtrlRef(logId);
                codeco.UNB(unb);

                String refNo = DatetimeUtil.now(DatetimeUtil.YYYYMMDDHHMMSS);
                //UNH
                UnCodeco.UNH unh = codeco.new UNH();
                unh.setMsgRefNo(refNo);
                if ("COS".equalsIgnoreCase(customer)) {
                    unh.setMsgTypeVer("1");
                }
                if ("HLC".equalsIgnoreCase(customer)) {
                    unh.setAssignedCode("ITG13");
                }
                codeco.UNH(unh);

                //BGM
                UnCodeco.BGM bgm = codeco.new BGM();
                bgm.setMsgNo(logId);
                if (jobTypes[i].contains("GATE-IN REPORT")) {
                    bgm.setMsgName("097");
                } else {
                    bgm.setMsgName("153");
                }

                codeco.BGM(bgm);

                //NAD
                if ("EVG".equalsIgnoreCase(customer)
                        || "TCLC".equalsIgnoreCase(customer)
                        || "HMM".equalsIgnoreCase(customer)) {
                    UnCodeco.NAD_MS nad_ms = codeco.new NAD_MS();
                    nad_ms.setPartyId("CNTACT");
                    codeco.NAD(nad_ms);
                }

                if ("HLC".equalsIgnoreCase(customer)) {
                    UnCodeco.NAD_MS nad_ms = codeco.new NAD_MS();
                    nad_ms.setPartyId(sender);
                    codeco.NAD(nad_ms);
                }

                psCtn.setString(1, customer);
                psCtn.setString(2, jobTypes[i]);
                psCtn.setString(3, range.getBegin());
                psCtn.setString(4, range.getEnd());

                ResultSet rsCtn = psCtn.executeQuery();

                List<UnCodeco.EQD> eqds = new LinkedList<>();

                int gidId = 0;

                while (rsCtn.next()) {

                    String ctnStatus = rsCtn.getString("ctn_status");
                    String ctnCategory = rsCtn.getString("ctn_category");

                    if ("HLC".equalsIgnoreCase(customer)) {

                        UnCodeco.GIDS gids = codeco.new GIDS();

                        //危险品或温控箱
                        if ("Y".equalsIgnoreCase(rsCtn.getString("hazard_flag"))
                                || "Y".equalsIgnoreCase(rsCtn.getString("reefer_flag"))) {
                            gidId++;

                            UnCodeco.GIDS.GID gid = codeco.new GIDS().new GID();
                            gid.setGidId(String.valueOf(gidId));
                            gids.GID(gid);
                        }

                        //温控箱
                        if ("Y".equalsIgnoreCase(rsCtn.getString("reefer_flag"))) {
                            UnCodeco.GIDS.TMP tmp_ = codeco.new GIDS().new TMP();
                            tmp_.setTmpVal(rsCtn.getString("temperature_setting"));
                            gids.TMP(tmp_);

                            UnCodeco.GIDS.SGP sgp = codeco.new GIDS().new SGP();
                            sgp.setCtnNo(rsCtn.getString("ctn_no"));
                            gids.SGP(sgp);
                        }

                        //危险品
                        if ("Y".equalsIgnoreCase(rsCtn.getString("hazard_flag"))) {
                            UnCodeco.GIDS.DGS dgs = codeco.new GIDS().new DGS();
                            dgs.setDgsCode(rsCtn.getString("undg_no"));
                            gids.DGS(dgs);
                        }

                        codeco.getGids().add(gids);

                    }

                    //EQD
                    UnCodeco.EQD eqd = codeco.new EQD();
                    eqd.setEqpId(rsCtn.getString("ctn_no"));
                    eqd.setEqpSizeType(rsCtn.getString("ctn_size") + rsCtn.getString("ctn_type"));
                    eqd.setEqpStatus(ctnCategory);
                    eqd.setEfIndicator("E".equals(ctnStatus) ? "4" : "5");

                    String bnbmfg = "BM";
                    if ("COS".equalsIgnoreCase(customer)) {
                        if (StringUtil.isNotEmpty(ctnStatus) && StringUtil.isNotEmpty(ctnCategory)) {
                            if (ctnCategory.equals("2")) {//出口
                                bnbmfg = "BN";
                            } else {
                                bnbmfg = "BM";
                            }
                        }
                    }

                    String billNo = rsCtn.getString("bill_no");
                    if ("HMM".equalsIgnoreCase(customer)) {
                        if (StringUtil.blankIfEmpty(billNo).length() == 16) {
                            bnbmfg = "BM";
                        } else if (StringUtil.blankIfEmpty(billNo).length() > 12) {
                            bnbmfg = "BN";
                            billNo = billNo.substring(4);
                        } else {
                            bnbmfg = "BN";
                        }
                    }

                    if ("ONE".equalsIgnoreCase(customer)) {
                        bnbmfg = "BN";
                        if (StringUtil.blankIfEmpty(billNo).length() >= 14) {
                            billNo = billNo.substring(4);
                        }
                    }

//                    if ("HLC".equalsIgnoreCase(customer)) {
//                        if ("F".equals(ctnStatus)) {
//                            bnbmfg = "BM";
//                        }
//                        //提空
//                        if ("E".equals(ctnStatus) && jobTypes[i].contains("OUT")) {
//                            bnbmfg = "BN";
//                        }
//                    }
//                    if ("EVG".equalsIgnoreCase(customer)) {
//                        bnbmfg = "BN";
//                        if (!("E".equals(ctnStatus) && jobTypes[i].contains("OUT"))) {
//                            if (StringUtil.isNotEmpty(billNo) && billNo.length() > 4) {
//                                billNo = billNo.substring(4);
//                            }
//                        }
//                    }
                    //RFF
                    UnCodeco.EQD.RFF rff = codeco.new EQD().new RFF();
                    rff.setRefNo(billNo);
                    rff.setRefQua(bnbmfg);
                    eqd.RFF(rff);

                    //DTM
                    UnCodeco.EQD.DTM dtm = codeco.new EQD().new DTM();
                    dtm.setDtm(rsCtn.getString("out_yard_time"));
                    eqd.DTM(dtm);

                    //LOC
                    if ("COS".equalsIgnoreCase(customer)) {
                        UnCodeco.EQD.LOC loc = codeco.new EQD().new LOC();
                        loc.setLocId("TAICANG");
                        eqd.LOC(loc);
                    }
                    if ("EVG".equalsIgnoreCase(customer)) {
                        UnCodeco.EQD.LOC loc = codeco.new EQD().new LOC();
                        loc.setLocId("CNTACT");
                        loc.setLocOther("CNTAG:TER:ZZZ");
                        eqd.LOC(loc);
                    }
                    if ("HMM".equalsIgnoreCase(customer) || "TCLC".equalsIgnoreCase(customer)) {
                        UnCodeco.EQD.LOC loc = codeco.new EQD().new LOC();
                        loc.setLocId("CNTACT");
                        eqd.LOC(loc);
                    }
                    if ("HLC".equalsIgnoreCase(customer)) {
                        UnCodeco.EQD.LOC loc = codeco.new EQD().new LOC();
                        loc.setLocId("CNTAG");
                        eqd.LOC(loc);
                    }

                    //MEA
                    UnCodeco.EQD.MEA mea = codeco.new EQD().new MEA();
                    if (ctnStatus.equals("F")) {
                        mea.setMeaVal(rsCtn.getString("gross_weight"));
                    } else {
                        mea.setMeaVal("0");
                    }
                    eqd.MEA(mea);

                    //TMP
//                    if (StringUtil.isNotEmpty(StringUtil.blankIfNull(rsCtn.getString("")))) {
//                        UnCodeco.EQD.TMP tmp = codeco.new EQD().new TMP();
//                        tmp.setTmpVal(rsCtn.getString(""));
//                        tmp.setMeaUnitQua(rsCtn.getString(""));
//                        eqd.TMP(tmp);
//                    }
                    //DGS
//                    if (StringUtil.isNotEmpty(StringUtil.blankIfNull(rsCtn.getString("")))) {
//                        UnCodeco.EQD.DGS dgs = codeco.new EQD().new DGS();
//                        dgs.setHazrdCodeId(rsCtn.getString(""));
//                        dgs.setUndgNo(rsCtn.getString(""));
//                        eqd.DGS(dgs);
//                    }
                    //SEL
                    String sealNo = rsCtn.getString("seal_no");
                    if (StringUtil.isNotEmpty(sealNo)) {
                        UnCodeco.EQD.SEL sel = eqd.new SEL();
                        sel.setSealNo(sealNo);
                        eqd.getSels().add(sel);
                    }

                    //DAM
                    if ("Y".equalsIgnoreCase(rsCtn.getString("damage_flag"))) {
                        UnCodeco.EQD.DAM dam = codeco.new EQD().new DAM();
                        eqd.DAM(dam);
                    }

//                    if ("HLC".equalsIgnoreCase(customer) && "F".equals(ctnStatus)) {
//                        UnCodeco.EQD.TDT tdt = codeco.new EQD().new TDT();
//                        tdt.setRefNo(rsCtn.getString("vessel_code") + StringUtil.blankIfEmpty(rsCtn.getString("voyage")));
//                        eqd.TDT(tdt);
//                    }
                    //NAD
                    if (!"HLC".equalsIgnoreCase(customer)) {
                        UnCodeco.EQD.NAD nad_ = codeco.new EQD().new NAD();
                        nad_.setPartyId(rsCtn.getString("ctn_operator"));
                        eqd.NAD(nad_);
                    }

                    eqds.add(eqd);

                    MsgCtnDetailLog log = new MsgCtnDetailLog();

                    log.setCustomer(customer);
                    log.setMsgName(jobTypes[i]);
                    log.setMsgType("codeco");
                    log.setCtnNo(rsCtn.getString("ctn_no"));
                    log.setInQuayTime((rsCtn.getString("in_yard_time") == null) ? null : DatetimeUtil.toTimestamp(DatetimeUtil.toCalendar(rsCtn.getString("in_yard_time"))));
                    log.setOutQuayTime((rsCtn.getString("out_yard_time") == null) ? null : DatetimeUtil.toTimestamp(DatetimeUtil.toCalendar(rsCtn.getString("out_yard_time"))));

                    logs.add(log);

                }

                codeco.setEqds(eqds);

                //CNT
                UnCodeco.CNT cnt = codeco.new CNT();
                codeco.CNT(cnt);

                //UNT
                UnCodeco.UNT unt = codeco.new UNT();
                unt.setMsgRefNo(refNo);
                codeco.UNT(unt);

                //UNZ
                UnCodeco.UNZ unz = codeco.new UNZ();
                unz.setMsgRefNo(logId);
                codeco.UNZ(unz);

                logId = String.valueOf(EDIHelper.getLogSeq());
                if (!eqds.isEmpty()) {
                    String report = codeco.toString().replaceAll("null", "");
                    System.out.println(report);
                    String filename = buildFilename("codeco", customer, sender,
                            receiver, "DV", logId, "txt");
                    boolean ok = write(filename, report);
                    if (ok) {
                        logSend(logId, customer, "DV", "UN", filename, report, redo);
                        logMsgDetails(Long.valueOf(logId), logs);
                    }
                } else {
                    logSend(logId, customer, "DV", "UN", "", "", redo);
                }

            }

        } catch (SQLException ex) {
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        } finally {
            DbUtil.close(psCtn);
        }

    }
}
