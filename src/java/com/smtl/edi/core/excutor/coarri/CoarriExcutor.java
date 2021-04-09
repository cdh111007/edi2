package com.smtl.edi.core.excutor.coarri;

import com.EDIHelper;
import static com.EDIHelper.getTotalCtns;
import static com.EDIHelper.getTotalTEU;
import static com.EDIHelper.getTotalWeight;
import static com.EDIHelper.getVesselCode;
import static com.EDIHelper.logSend;
import static com.EDIHelper.write;
import com.smtl.edi.core.SQLQueryConstants;
import com.smtl.edi.core.model.jt.JtXxzxCoarri;
import com.smtl.edi.core.log.MsgCtnDetailLog;
import com.smtl.edi.core.task.mail.ExceptionNotifyTask;
import com.smtl.edi.core.model.un.UnCoarri;
import com.smtl.edi.core.model.un.UnCoarri.BGM;
import com.smtl.edi.core.model.un.UnCoarri.CNT;
import com.smtl.edi.core.model.un.UnCoarri.DTM132;
import com.smtl.edi.core.model.un.UnCoarri.DTM133;
import com.smtl.edi.core.model.un.UnCoarri.EQD;
import com.smtl.edi.core.model.un.UnCoarri.EQD.DAM;
import com.smtl.edi.core.model.un.UnCoarri.EQD.DGS;
import com.smtl.edi.core.model.un.UnCoarri.EQD.DTM;
import com.smtl.edi.core.model.un.UnCoarri.EQD.LOC9;
import com.smtl.edi.core.model.un.UnCoarri.EQD.MEA;
import com.smtl.edi.core.model.un.UnCoarri.EQD.RFF;
import com.smtl.edi.core.model.un.UnCoarri.EQD.SEL;
import com.smtl.edi.core.model.un.UnCoarri.EQD.TMD;
import com.smtl.edi.core.model.un.UnCoarri.EQD.TMP;
import com.smtl.edi.core.model.un.UnCoarri.LOC11;
import com.smtl.edi.core.model.un.UnCoarri.NAD_CA;
import com.smtl.edi.core.model.un.UnCoarri.TDT;
import com.smtl.edi.core.model.un.UnCoarri.UNB;
import com.smtl.edi.core.model.un.UnCoarri.UNH;
import com.smtl.edi.core.model.un.UnCoarri.UNT;
import com.smtl.edi.core.model.un.UnCoarri.UNZ;
import com.smtl.edi.util.DatetimeUtil;
import com.smtl.edi.util.DbUtil;
import com.smtl.edi.util.ValidationUtil;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import static com.EDIHelper.buildFilename;
import static com.EDIHelper.logMsgDetails;
import static com.EDIHelper.PS_COARRI_CUSTOMER;
import com.smtl.edi.core.model.jt.JtCoarri;
import com.smtl.edi.util.StringUtil;
import static com.smtl.edi.util.StringUtil.buildSqlInClause;
import org.apache.log4j.Logger;
import static com.smtl.edi.core.SQLQueryConstants.SQL_ACT_VSL_CTN_VW;
import com.smtl.edi.vo.DateRange;

/**
 * 交通部格式与联合国格式装卸船报文入口（电子口岸清洁数据除外，独立）
 *
 * @author nm
 */
public class CoarriExcutor {

    final static Logger LOGGER = Logger.getLogger(CoarriExcutor.class);

    /**
     * 交通部格式装卸船报文（电子口岸版本）
     *
     * @param customer
     * @param range
     * @param rsVsl
     * @param instant
     * @param redo
     * @param ctnNos
     */
    public static void jt(String customer, DateRange range,
            ResultSet rsVsl, Boolean instant, boolean redo, String... ctnNos) {

        String sqlVslCtnVw = instant ? SQL_ACT_VSL_CTN_VW : SQLQueryConstants.SQL_VSL_CTN_VW;

        if (ValidationUtil.isValid(ctnNos)) {
            sqlVslCtnVw = sqlVslCtnVw + " and ctn_no in(" + buildSqlInClause(ctnNos) + ") ";
        }

        try {

            processJt(customer, range, sqlVslCtnVw,
                    rsVsl, instant, redo);

        } catch (SQLException ex) {
            ExceptionNotifyTask.notify(ex, new String[]{customer, "coarri"});
        }
    }

    /**
     *
     * @param customer
     * @param sqlVslCtnVw
     * @param rsVsl
     * @param instant 如果非null且为true，则使用range参数，其他值一律忽略
     * @param redo
     * @throws SQLException
     */
    private static void processJtStd(String customer,
            DateRange range, String sqlVslCtnVw,
            ResultSet rsVsl, Boolean instant, boolean redo) throws SQLException {

        customer = customer.toUpperCase();

        PS_COARRI_CUSTOMER.setString(1, customer);
        PS_COARRI_CUSTOMER.setString(2, "COARRI");
        ResultSet rsCustomer = PS_COARRI_CUSTOMER.executeQuery();

        String sender, receiver;
        if (rsCustomer.next()) {
            sender = rsCustomer.getString("cst_sender");
            receiver = rsCustomer.getString("cst_receiver");
        } else {
            return;
        }

        List<MsgCtnDetailLog> logs = new LinkedList<>();

        JtCoarri coarri = new JtCoarri();

        //00
        JtCoarri.SEG00 seg00 = coarri.new SEG00();
        String msgName = "";
        String ieFlag = rsVsl.getString("ie_flag");
        if ("I".equals(ieFlag)) {
            seg00.setFileDesc("DISCHARGE REPORT");
            msgName = "DISCHARGE REPORT";
        } else {
            seg00.setFileDesc("LOAD REPORT");
            msgName = "LOAD REPORT";
        }
        seg00.setSender(sender);
        seg00.setReceiver(receiver);
        seg00.setFileCreateTime(DatetimeUtil.now(DatetimeUtil.YYMMDDHHMM));

        coarri.SEG00(seg00);

        //10
        String startLoadingTime = "";
        String completeLoadingTime = "";
        String startDischargingTime = "";
        String completeDischargingTime = "";

        if ("E".equals(ieFlag)) {
            startLoadingTime = rsVsl.getString("ats");
            completeLoadingTime = rsVsl.getString("ate");
        } else {
            startDischargingTime = rsVsl.getString("ats");
            completeDischargingTime = rsVsl.getString("ate");
        }

        JtCoarri.SEG10 seg10 = coarri.new SEG10();

        seg10.setVesselCode(rsVsl.getString("vessel_code"));
        seg10.setVesselName(rsVsl.getString("vessel_namec"));
        seg10.setVoyage(rsVsl.getString("voyage"));
        seg10.setCountryCode(rsVsl.getString("country_code"));
        seg10.setLinerType(rsVsl.getString("liner_id"));
        seg10.setBerthingTime(rsVsl.getString("ata"));
        seg10.setDepartureTime(rsVsl.getString("atd"));
        seg10.setStartDischargingTime(startDischargingTime);
        seg10.setCompleteDischargingTime(completeDischargingTime);
        seg10.setStartLoadingTime(startLoadingTime);
        seg10.setCompleteLoadingTime(completeLoadingTime);
        seg10.setTotalCtns(getTotalCtns(customer, rsVsl.getString("vessel_reference"), rsVsl.getString("voyage"), rsVsl.getString("ie_flag")));

        coarri.SEG10(seg10);

        try (PreparedStatement psCtn = DbUtil.getConnection().prepareStatement(sqlVslCtnVw)) {

            psCtn.setString(1, rsVsl.getString("vessel_code"));
            psCtn.setString(2, rsVsl.getString("voyage"));
            psCtn.setString(3, rsVsl.getString("ie_flag"));
            psCtn.setString(4, customer);

            if (instant != null && instant) {
                psCtn.setString(5, range.getBegin());
                psCtn.setString(6, range.getEnd());
            }

            ResultSet rsLoopInCtn = psCtn.executeQuery();

            while (rsLoopInCtn.next()) {

                JtCoarri.SEG5X seg5x = coarri.new SEG5X();

                //50
                JtCoarri.SEG5X.SEG50 seg50 = coarri.new SEG5X().new SEG50();

                seg50.setCtnNo(rsLoopInCtn.getString("ctn_no"));
                seg50.setCtnSizeType(rsLoopInCtn.getString("ctn_size") + rsLoopInCtn.getString("ctn_type"));
                seg50.setCtnOperatorCode(rsLoopInCtn.getString("ctn_operator_code"));
                seg50.setCtnOperator(rsLoopInCtn.getString("ctn_operator"));
                seg50.setCtnStatus(rsLoopInCtn.getString("ctn_status"));
                seg50.setBillNo(rsLoopInCtn.getString("bl_no"));
                seg50.setSealNo(rsLoopInCtn.getString("seal_no"));
                seg50.setStowageLoc(rsLoopInCtn.getString("stowage_loc"));

                if ("E".equals(ieFlag)) {
                    seg50.setInOutYardTime(rsLoopInCtn.getString("out_yard_time"));
                } else {
                    seg50.setInOutYardTime(rsLoopInCtn.getString("in_yard_time"));
                }

                seg5x.SEG50(seg50);

                //52
                JtCoarri.SEG5X.SEG52 seg52 = coarri.new SEG5X().new SEG52();
                seg52.setDischargePortCode(rsLoopInCtn.getString("discharge_port_code"));
                seg52.setDischargePortName(rsLoopInCtn.getString("discharge_port"));
                seg52.setLoadPortCode(rsLoopInCtn.getString("load_port_code"));
                seg52.setLoadPortName(rsLoopInCtn.getString("load_port"));
                seg52.setDestPortCode(rsLoopInCtn.getString("destination_port_code"));
                seg52.setDestPortName(rsLoopInCtn.getString("destination_port"));
                seg52.setGrossWeight(rsLoopInCtn.getString("ctn_weight"));

                seg5x.SEG52(seg52);

                coarri.getSeg5xs().add(seg5x);

                MsgCtnDetailLog log = new MsgCtnDetailLog();
                log.setVslName(rsVsl.getString("vessel_namec"));
                log.setVoyage(rsVsl.getString("voyage"));
                log.setCustomer(customer);
                log.setVslRef(rsVsl.getString("vessel_reference"));
                log.setMsgName(msgName);
                log.setMsgType("coarri");
                log.setCtnNo(rsLoopInCtn.getString("ctn_no"));
                log.setInQuayTime((rsLoopInCtn.getString("in_yard_time") == null) ? null : DatetimeUtil.toTimestamp(DatetimeUtil.toCalendar(rsLoopInCtn.getString("in_yard_time"))));
                log.setOutQuayTime((rsLoopInCtn.getString("out_yard_time") == null) ? null : DatetimeUtil.toTimestamp(DatetimeUtil.toCalendar(rsLoopInCtn.getString("out_yard_time"))));

                logs.add(log);

            }

        }

        JtCoarri.SEG99 seg99 = coarri.new SEG99();
        coarri.SEG99(seg99);

        String logId = String.valueOf(EDIHelper.getLogSeq());

        if (!coarri.getSeg5xs().isEmpty()) {
            String report = coarri.toString().replaceAll("null", "");
            System.out.println(report);
            String filename = buildFilename("coarri", customer, sender, receiver, msgName, logId, "txt");
            boolean ok = write(filename, report);
            if (ok) {
                logSend(logId, customer, "coarri", "JT", filename, report, redo);
                logMsgDetails(Long.valueOf(logId), logs);
            }
        } else {
            //在该时间段内，有关闭航次或者离港但没有该客户的作业数据时才会执行
            //如果一直没有关闭航次或离泊发生，当前客户的最近一次
            //业务数据的时间就不会被更新
            logSend(logId, customer, "coarri", "JT", "", "", redo);
        }
    }

    /**
     *
     * @param customer
     * @param sqlVslCtnVw
     * @param rsVsl
     * @param instant 如果非null且为true，则使用range参数，其他值一律忽略
     * @param redo
     * @throws SQLException
     */
    private static void processJt(String customer,
            DateRange range, String sqlVslCtnVw,
            ResultSet rsVsl, Boolean instant, boolean redo) throws SQLException {

        customer = customer.toUpperCase();

        PS_COARRI_CUSTOMER.setString(1, customer);
        PS_COARRI_CUSTOMER.setString(2, "COARRI");
        ResultSet rsCustomer = PS_COARRI_CUSTOMER.executeQuery();

        String sender, receiver;
        if (rsCustomer.next()) {
            sender = rsCustomer.getString("cst_sender");
            receiver = rsCustomer.getString("cst_receiver");
        } else {
            return;
        }

        List<MsgCtnDetailLog> logs = new LinkedList<>();

        JtXxzxCoarri coarri = new JtXxzxCoarri();

        //00
        JtXxzxCoarri.SEG00 seg00 = coarri.new SEG00();
        String msgName = "";
        String ieFlag = rsVsl.getString("ie_flag");
        if ("I".equals(ieFlag)) {
            seg00.setFileDesc("DISCHARGE REPORT");
            msgName = "DISCHARGE REPORT";
        } else {
            seg00.setFileDesc("LOAD REPORT");
            msgName = "LOAD REPORT";
        }
        seg00.setSender(sender);
        seg00.setReceiver(receiver);
        seg00.setFileCreateTime(DatetimeUtil.now(DatetimeUtil.YYMMDDHHMM));

        coarri.SEG00(seg00);

        //10
        String startLoadingTime = "";
        String completeLoadingTime = "";
        String startDischargingTime = "";
        String completeDischargingTime = "";

        if ("E".equals(ieFlag)) {
            startLoadingTime = rsVsl.getString("ats");
            completeLoadingTime = "xxzx".equalsIgnoreCase(customer) ? rsVsl.getString("close_time") : rsVsl.getString("ate");
        } else {
            startDischargingTime = rsVsl.getString("ats");
            completeDischargingTime = "xxzx".equalsIgnoreCase(customer) ? rsVsl.getString("close_time") : rsVsl.getString("ate");
        }

        JtXxzxCoarri.SEG10 seg10 = coarri.new SEG10();

        seg10.setVesselCode(rsVsl.getString("vessel_code"));
        if ("ZGXL".equalsIgnoreCase(customer)) {
            seg10.setVesselName(rsVsl.getString("vessel_namec"));
        } else {
            seg10.setVesselName(rsVsl.getString("vessel_code"));
        }
        seg10.setVoyage(rsVsl.getString("voyage"));
        seg10.setCountryCode(rsVsl.getString("country_code"));
        seg10.setLinerType(rsVsl.getString("liner_id"));
        seg10.setBerthingTime(rsVsl.getString("ata"));
        seg10.setDepartureTime(rsVsl.getString("atd"));
        seg10.setStartDischargingTime(startDischargingTime);
        seg10.setCompleteDischargingTime(completeDischargingTime);
        seg10.setStartLoadingTime(startLoadingTime);
        seg10.setCompleteLoadingTime(completeLoadingTime);
        seg10.setTotalCtns(getTotalCtns(customer, rsVsl.getString("vessel_reference"), rsVsl.getString("voyage"), rsVsl.getString("ie_flag")));
        seg10.setBerthNo(rsVsl.getString("berth_reference"));
        seg10.setIMO(rsVsl.getString("call_sign"));
        seg10.setBerthingDraft(rsVsl.getString("arrival_draft"));
        seg10.setDepartureDraft(rsVsl.getString("departure_draft"));
        seg10.setCargoName(rsVsl.getString("cargo_name"));
        seg10.setTotalWeight(getTotalWeight(customer, rsVsl.getString("vessel_reference"), rsVsl.getString("voyage"), rsVsl.getString("ie_flag")));
        seg10.setTeu(getTotalTEU(customer, rsVsl.getString("vessel_reference"), rsVsl.getString("voyage"), rsVsl.getString("ie_flag")));

        seg10.setYqb(rsVsl.getString("yqb"));
        seg10.setZkl(rsVsl.getString("zkl"));
        seg10.setSxk(rsVsl.getString("sxk"));

        coarri.SEG10(seg10);

        try (PreparedStatement psCtn = DbUtil.getConnection().prepareStatement(sqlVslCtnVw)) {

            psCtn.setString(1, rsVsl.getString("vessel_code"));
            psCtn.setString(2, rsVsl.getString("voyage"));
            psCtn.setString(3, rsVsl.getString("ie_flag"));
            psCtn.setString(4, customer);

            if (instant != null && instant) {
                psCtn.setString(5, range.getBegin());
                psCtn.setString(6, range.getEnd());
            }

            ResultSet rsLoopInCtn = psCtn.executeQuery();

            while (rsLoopInCtn.next()) {

                JtXxzxCoarri.SEG5X seg5x = coarri.new SEG5X();

                //50
                JtXxzxCoarri.SEG5X.SEG50 seg50 = coarri.new SEG5X().new SEG50();

                seg50.setCtnNo(rsLoopInCtn.getString("ctn_no"));
                seg50.setCtnSizeType(rsLoopInCtn.getString("ctn_size") + rsLoopInCtn.getString("ctn_type"));
                seg50.setCtnOperatorCode(rsLoopInCtn.getString("ctn_operator_code"));
                seg50.setCtnOperator(rsLoopInCtn.getString("ctn_operator"));
                seg50.setCtnStatus(rsLoopInCtn.getString("ctn_status"));
                seg50.setBillNo(rsLoopInCtn.getString("bl_no"));
                seg50.setSealNo(rsLoopInCtn.getString("seal_no"));
                seg50.setStowageLoc(rsLoopInCtn.getString("stowage_loc"));
                seg50.setCargoName(rsLoopInCtn.getString("cargo_desc"));
                seg50.setHazFlag(rsLoopInCtn.getString("hazard_flag"));
                seg50.setCargoWeight(rsLoopInCtn.getString("cargo_weight"));

                //中转标记 T 国内中转 Z 国际中转 E 退运
                String tranship = rsLoopInCtn.getString("tranship_flag");
                if (tranship != null && ("T".equals(tranship) || "Z".equals(tranship))) {
                    seg50.setTranshipFlag("Y");
                    seg50.setFirstVessel(rsLoopInCtn.getString("first_vessel_code"));
                    seg50.setFirstVoyage(rsLoopInCtn.getString("first_voyage"));
                }
                seg50.setTradeType(rsLoopInCtn.getString("trade_type"));

                if ("E".equals(ieFlag)) {
                    seg50.setInOutQuayTime(rsLoopInCtn.getString("out_yard_time"));
                } else {
                    seg50.setInOutQuayTime(rsLoopInCtn.getString("in_yard_time"));
                }

                seg5x.SEG50(seg50);

                //52
                JtXxzxCoarri.SEG5X.SEG52 seg52 = coarri.new SEG5X().new SEG52();
                seg52.setDischargePortCode(rsLoopInCtn.getString("discharge_port_code"));
                seg52.setDischargePortName(rsLoopInCtn.getString("discharge_port"));
                seg52.setLoadPortCode(rsLoopInCtn.getString("load_port_code"));
                seg52.setLoadPortName(rsLoopInCtn.getString("load_port"));
                seg52.setDestPortCode(rsLoopInCtn.getString("destination_port_code"));
                seg52.setDestPortName(rsLoopInCtn.getString("destination_port"));
                seg52.setGrossWeight(rsLoopInCtn.getString("ctn_weight"));

                seg5x.SEG52(seg52);

                coarri.getSeg5xs().add(seg5x);

                MsgCtnDetailLog log = new MsgCtnDetailLog();
                log.setVslName(rsVsl.getString("vessel_namec"));
                log.setVoyage(rsVsl.getString("voyage"));
                log.setCustomer(customer);
                log.setVslRef(rsVsl.getString("vessel_reference"));
                log.setMsgName(msgName);
                log.setMsgType("coarri");
                log.setCtnNo(rsLoopInCtn.getString("ctn_no"));
                log.setInQuayTime((rsLoopInCtn.getString("in_yard_time") == null) ? null : DatetimeUtil.toTimestamp(DatetimeUtil.toCalendar(rsLoopInCtn.getString("in_yard_time"))));
                log.setOutQuayTime((rsLoopInCtn.getString("out_yard_time") == null) ? null : DatetimeUtil.toTimestamp(DatetimeUtil.toCalendar(rsLoopInCtn.getString("out_yard_time"))));

                logs.add(log);

            }

        }

        //统计每种箱型的空/重/危数量
        String sqlCtnStats = "{ call sp_tc2_edi_cst_ctn_stats(?,?,?,?,?)}";

        try (CallableStatement callCtnStats = DbUtil.getConnection().prepareCall(sqlCtnStats)) {

            callCtnStats.setString(1, customer);
            callCtnStats.setString(2, rsVsl.getString("vessel_reference"));
            callCtnStats.setString(3, rsVsl.getString("voyage"));
            callCtnStats.setString(4, rsVsl.getString("ie_flag"));
            callCtnStats.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);

            callCtnStats.execute();

            ResultSet rsCtnStats = (ResultSet) callCtnStats.getObject(5);

            while (rsCtnStats.next()) {

                JtXxzxCoarri.SEG6X seg6x = coarri.new SEG6X();

                JtXxzxCoarri.SEG6X.SEG60 seg60 = coarri.new SEG6X().new SEG60();

                seg60.setCtnSize(rsCtnStats.getString("ctn_size"));
                seg60.setEmptyCtnCount(rsCtnStats.getString("full_ctns"));
                seg60.setFullCtnCount(rsCtnStats.getString("empty_ctns"));
                if (Integer.parseInt(rsCtnStats.getString("hazard_ctns")) > 0) {
                    seg60.setHazCargoFlag("Y");
                }

                seg6x.SEG60(seg60);

                coarri.getSeg6xs().add(seg6x);

            }
        }

        JtXxzxCoarri.SEG99 seg99 = coarri.new SEG99();
        coarri.SEG99(seg99);

        String logId = String.valueOf(EDIHelper.getLogSeq());

        if (!coarri.getSeg5xs().isEmpty()) {
            String report = coarri.toString().replaceAll("null", "");
            System.out.println(report);
            String filename = buildFilename("coarri", customer, sender, receiver, msgName, logId, "txt");
            boolean ok = write(filename, report);
            if (ok) {
                logSend(logId, customer, "coarri", "JT", filename, report, redo);
                logMsgDetails(Long.valueOf(logId), logs);
            }
        } else {
            //在该时间段内，有关闭航次或者离港但没有该客户的作业数据时才会执行
            //如果一直没有关闭航次或离泊发生，当前客户的最近一次
            //业务数据的时间就不会被更新
            logSend(logId, customer, "coarri", "JT", "", "", redo);
        }
    }

    /**
     * 交通部格式装卸船报文（电子口岸版本）
     *
     * @param customer
     * @param rsVsl
     * @param redo
     * @param ctnNos
     */
    public static void jt0(String customer, ResultSet rsVsl, boolean redo, String... ctnNos) {

        String sqlVslCtnVw = SQLQueryConstants.SQL_VSL_CTN_VW;

        if (ValidationUtil.isValid(ctnNos)) {
            sqlVslCtnVw = sqlVslCtnVw + " and ctn_no in(" + buildSqlInClause(ctnNos) + ") ";
        }

        try {

            processJt(customer, null, sqlVslCtnVw, rsVsl, null, redo);

        } catch (SQLException ex) {
            ExceptionNotifyTask.notify(ex, new String[]{customer, "coarri"});
        }
    }

    /**
     * 交通部标准格式报文
     *
     * @param customer
     * @param range
     * @param rsVsl
     * @param instant
     * @param redo
     * @param ctnNos
     */
    public static void jtStd(String customer, DateRange range,
            ResultSet rsVsl, boolean instant, boolean redo, String... ctnNos) {

        String sqlVslCtnVw = instant ? SQL_ACT_VSL_CTN_VW : SQLQueryConstants.SQL_VSL_CTN_VW;

        if (ValidationUtil.isValid(ctnNos)) {
            sqlVslCtnVw = sqlVslCtnVw + " and ctn_no in(" + buildSqlInClause(ctnNos) + ") ";
        }

        try {

            processJtStd(customer, range, sqlVslCtnVw,
                    rsVsl, instant, redo);

        } catch (SQLException ex) {
            ExceptionNotifyTask.notify(ex, new String[]{customer, "coarri"});
        }
    }

    /**
     * 交通部标准格式报文
     *
     * @param customer
     * @param rsVsl
     * @param redo
     * @param ctnNos
     */
    public static void jtStd0(String customer, ResultSet rsVsl, boolean redo, String... ctnNos) {

        String sqlVslCtnVw = SQLQueryConstants.SQL_VSL_CTN_VW;

        if (ValidationUtil.isValid(ctnNos)) {
            sqlVslCtnVw = sqlVslCtnVw + " and ctn_no in(" + buildSqlInClause(ctnNos) + ") ";
        }

        try {

            processJtStd(customer, null, sqlVslCtnVw, rsVsl, null, redo);

        } catch (SQLException ex) {
            ExceptionNotifyTask.notify(ex, new String[]{customer, "coarri"});
        }
    }

    /**
     * 联合国格式报文
     *
     * @param customer
     * @param range
     * @param rsVsl
     * @param instant
     * @param redo
     * @param ctnNos
     */
    public static void un(String customer, DateRange range, ResultSet rsVsl, boolean instant,
            boolean redo, String... ctnNos) {

        String sqlVslCtnVw = instant ? SQL_ACT_VSL_CTN_VW : SQLQueryConstants.SQL_VSL_CTN_VW;

        if (ValidationUtil.isValid(ctnNos)) {
            sqlVslCtnVw = sqlVslCtnVw + " and ctn_no in(" + buildSqlInClause(ctnNos) + ") ";
        }

        try {

            processUn(customer, range, sqlVslCtnVw, rsVsl, instant, redo);

        } catch (SQLException ex) {
            ex.printStackTrace();
            ExceptionNotifyTask.notify(ex, new String[]{customer, "coarri"});
        }
    }

    /**
     *
     * @param customer
     * @param sqlVslCtnVw
     * @param rsVsl
     * @param instant 如果非null且为true，则使用range参数，其他值一律忽略
     * @param redo
     * @throws SQLException
     */
    private static void processUn(String customer,
            DateRange range, String sqlVslCtnVw,
            ResultSet rsVsl, Boolean instant, boolean redo) throws SQLException {

        customer = customer.toUpperCase();

        PS_COARRI_CUSTOMER.setString(1, customer);
        PS_COARRI_CUSTOMER.setString(2, "COARRI");
        ResultSet rsCustomer = PS_COARRI_CUSTOMER.executeQuery();

        String sender, receiver;
        if (rsCustomer.next()) {
            sender = rsCustomer.getString("cst_sender");
            receiver = rsCustomer.getString("cst_receiver");
        } else {
            return;
        }

        List<MsgCtnDetailLog> logs = new LinkedList<>();

        String logId = String.valueOf(EDIHelper.getLogSeq());

        UnCoarri coarri = new UnCoarri();

        //****************UNB******************
        UnCoarri.UNB unb = coarri.new UNB();
        unb.setSenderId(sender);
        unb.setRecipientId(receiver);
        unb.setDateTimePreparation(DatetimeUtil.now(DatetimeUtil.YYYYMMDD_HHMM));
        unb.setCtrlRef(logId);
        if ("COS".equalsIgnoreCase(customer) || "HSD".equalsIgnoreCase(customer)) {
            unb.setSyntaxVer("1");
        }
        coarri.UNB(unb);

        String refNo = DatetimeUtil.now(DatetimeUtil.YYYYMMDDHHMMSS);

        //****************UNH******************
        UnCoarri.UNH unh = coarri.new UNH();
        unh.setMsgRefNo(refNo);
        if ("HSD".equalsIgnoreCase(customer)) {
            unh.setAssignedCode("SMDG16");
        } else if ("PIL".equalsIgnoreCase(customer)) {
            unh.setAssignedCode("ITG14");
        }
        coarri.UNH(unh);

        //****************BGM******************
        UnCoarri.BGM bgm = coarri.new BGM();
        String ieFlag = rsVsl.getString("ie_flag");
        String msgName = "";
        if ("I".equals(ieFlag)) {
            bgm.setMsgName("98");//44 Discharge
            msgName = "DISCHARGE REPORT";
            if ("HSD".equalsIgnoreCase(customer)) {
                bgm.setMsgName("44:::DISCHARGE REPORT");
            }
        } else {
            bgm.setMsgName("270");//46 Load
            msgName = "LOAD REPORT";
            if ("HSD".equalsIgnoreCase(customer)) {
                bgm.setMsgName("46:::LOADING REPORT");
            }
        }

        bgm.setMsgNo(logId);
        coarri.BGM(bgm);

        //****************TDT******************
        UnCoarri.TDT tdt = coarri.new TDT();
        tdt.setVoyage(rsVsl.getString("voyage"));
        tdt.setCarrier(rsVsl.getString("vessel_code"));
        if ("COS".equalsIgnoreCase(customer)) {
            String vslCode = getVesselCode(rsVsl.getString("vessel_code"));
            tdt.setCarrier(vslCode);
            tdt.setTransportVesselCallSign(vslCode);
            tdt.setTransportVesselName(vslCode);
        } else {
            tdt.setTransportVesselName(rsVsl.getString("vessel_code"));
        }
        if ("HLC".equalsIgnoreCase(customer)) {
            tdt.setCountryCode(rsVsl.getString("country_code"));
            tdt.setCarrierQua("172:20");
            tdt.setTransportCodeQua("146");
            tdt.setTransportNo("11");
        }
        if ("HMM".equalsIgnoreCase(customer)) {
            tdt.setCarrierQua("172:ZZZ");
        }
        coarri.TDT(tdt);

        //RFF
        if ("HLC".equalsIgnoreCase(customer)) {
            UnCoarri.RFF rff = coarri.new RFF();
            rff.setVoyage(rsVsl.getString("voyage"));
            coarri.RFF(rff);
        }

        //****************LOC******************
        if ("HSD".equalsIgnoreCase(customer)) {
            String ldFlag;
            UnCoarri.LOC loc = coarri.new LOC();
            if (ieFlag.equals("I")) {
                ldFlag = "11";
            } else {
                ldFlag = "9";
            }
            loc.setLocQua(ldFlag);
            loc.setLocId("CNTAG:139:6+TIC:TER:306");
            loc.setLocCodeQua(null);
            loc.setRespAgcy(null);
            coarri.LOC(loc);
        } else if ("PIL".equalsIgnoreCase(customer)) {
            UnCoarri.LOC9 loc9 = coarri.new LOC9();
            loc9.setLocId("CNSHA");
            coarri.LOC9(loc9);

            UnCoarri.LOC11 loc11 = coarri.new LOC11();
            loc11.setLocId("CNTAG");
            coarri.LOC11(loc11);
        } else if ("HLC".equalsIgnoreCase(customer)) {
            String ldFlag;
            UnCoarri.LOC loc = coarri.new LOC();
            if (ieFlag.equals("I")) {
                ldFlag = "11";
            } else {
                ldFlag = "9";
            }
            loc.setLocQua(ldFlag);
            loc.setLocId("CNTAC");
            coarri.LOC(loc);
        }

        //****************DTM132******************
        UnCoarri.DTM132 dtm132 = coarri.new DTM132();
        dtm132.setDtm(rsVsl.getString("ata"));
        coarri.DTM(dtm132);

        //****************DTM133******************
        if (!"HLC".equalsIgnoreCase(customer)) {
            UnCoarri.DTM133 dtm133 = coarri.new DTM133();
            dtm133.setDtm(rsVsl.getString("atd"));
            coarri.DTM(dtm133);
        }

        //****************NAD******************
        if ("PIL".equalsIgnoreCase(customer)) {
            UnCoarri.NAD_CF nadCF = coarri.new NAD_CF();
            nadCF.setPartyId(customer);
            nadCF.setPartyIdCode("160");
            nadCF.setPartyRespAgcyCode("87");
            coarri.NAD_CF(nadCF);
        } else if ("EVG".equalsIgnoreCase(customer)) {
            UnCoarri.NAD_CF nadCF = coarri.new NAD_CF();
            nadCF.setPartyId(customer);
            nadCF.setPartyIdCode("160");
            nadCF.setPartyRespAgcyCode("ZZZ");
            coarri.NAD_CF(nadCF);

            UnCoarri.NAD_CA nadCA = coarri.new NAD_CA();
            nadCA.setPartyId("EMC");
            nadCA.setPartyIdCode("160");
            nadCA.setPartyRespAgcyCode("ZZZ");
            coarri.NAD_CA(nadCA);
        } else if ("HSD".equalsIgnoreCase(customer)) {
            UnCoarri.NAD_CA nadCA = coarri.new NAD_CA();
            nadCA.setPartyId(customer);
            nadCA.setPartyIdCode("172");
            nadCA.setPartyRespAgcyCode("87");
            coarri.NAD_CA(nadCA);
        } else if ("HMM".equalsIgnoreCase(customer)) {
            UnCoarri.NAD_CA nadCA = coarri.new NAD_CA();
            nadCA.setPartyId("EMC");
            nadCA.setPartyIdCode("160");
            nadCA.setPartyRespAgcyCode("ZZZ");
            coarri.NAD_CA(nadCA);
        } else {
            if (!"HLC".equalsIgnoreCase(customer)) {
                UnCoarri.NAD_CA nadCA = coarri.new NAD_CA();
                nadCA.setPartyId(customer);
                coarri.NAD_CA(nadCA);
            }
        }

        int eqdSize = 0;

        try (PreparedStatement psCtn = DbUtil.preparedStatement(DbUtil.getConnection(), sqlVslCtnVw)) {

            psCtn.setString(1, rsVsl.getString("vessel_code"));
            psCtn.setString(2, rsVsl.getString("voyage"));
            psCtn.setString(3, rsVsl.getString("ie_flag"));
            psCtn.setString(4, customer.toUpperCase());

            if (instant != null && instant) {
                psCtn.setString(5, range.getBegin());
                psCtn.setString(6, range.getEnd());
            }

            ResultSet rsCtn = psCtn.executeQuery();

            List<UnCoarri.EQD> eqds = new LinkedList<>();

            while (rsCtn.next()) {

                //****************EQD******************
                UnCoarri.EQD eqd = coarri.new EQD();

                eqd.setEqpId(rsCtn.getString("ctn_no"));
                eqd.setEqpSizeType(rsCtn.getString("ctn_size") + rsCtn.getString("ctn_type"));

                if ("E".equals(rsCtn.getString("vessel_ie"))) {
                    eqd.setEqpStatus("2");//5:船上倒箱 6:中转箱
                } else {
                    eqd.setEqpStatus("3");
                }
                if ("F".equals(rsCtn.getString("ctn_status"))) {
                    eqd.setEfIndicator("5");
                } else {
                    eqd.setEfIndicator("4");
                }

                String iefg = rsCtn.getString("vessel_ie"), effg = rsCtn.getString("ctn_status");
                //BN:Booking Reference
                //BM:B/L Number
                String bnbmfg = "BM";

                //COSCO要求
                if ("COS".equalsIgnoreCase(customer)) {
                    if ("E".equals(iefg)) {//出口
                        if (effg.equals("F")) {//重箱
                            bnbmfg = "BN";
                        } else {//空箱
                            bnbmfg = "EP";
                        }
                    } else {
                        if ("F".equals(effg)) {//重箱
                            bnbmfg = "BM";
                        } else {//空箱
                            bnbmfg = "EP";
                        }
                    }
                }

                String billNo = rsCtn.getString("bl_no");

                //****************RFF******************
                UnCoarri.EQD.RFF rff = coarri.new EQD().new RFF();
                rff.setRefQua(bnbmfg);
                if ("EVG".equalsIgnoreCase(customer)) {
                    if (StringUtil.isNotEmpty(billNo) && billNo.length() > 4) {
                        billNo = billNo.substring(4);
                    }
                }
                rff.setRefNo(billNo);
                eqd.RFF(rff);

                if ("EVG".equalsIgnoreCase(customer)) {
                    UnCoarri.EQD.TMD tmd = coarri.new EQD().new TMD();
                    eqd.TMD(tmd);
                }

                //****************DTM******************
                UnCoarri.EQD.DTM dtm = coarri.new EQD().new DTM();
                if ("E".equals(iefg)) {
                    dtm.setDtm(rsCtn.getString("out_yard_time"));
                } else {
                    dtm.setDtm(rsCtn.getString("in_yard_time"));
                }
                if ("EVG".equalsIgnoreCase(customer) || "HLC".equalsIgnoreCase(customer)) {
                    dtm.setDtmQua("203");
                }
                eqd.DTM(dtm);

                //****************LOC******************
                if ("HSD".equalsIgnoreCase(customer)) {
                    if ("E".equals(iefg)) {
                        UnCoarri.EQD.LOC9 loc9 = coarri.new EQD().new LOC9();
                        loc9.setLoadPort(rsCtn.getString("load_port_code"));
                        eqd.LOC(loc9);
                    } else {
                        UnCoarri.EQD.LOC11 loc11 = coarri.new EQD().new LOC11();
                        loc11.setDiscPort(rsCtn.getString("discharge_port_code"));
                        eqd.LOC(loc11);
                    }
                } else if ("HLC".equalsIgnoreCase(customer)) {
                    if ("E".equals(iefg) && "S".equalsIgnoreCase(rsCtn.getString("ctn_category"))) {
                        //****************LOC9******************
                        UnCoarri.EQD.LOC9 loc9 = coarri.new EQD().new LOC9();
                        loc9.setLoadPort("CNTAC");
                        eqd.LOC(loc9);
                    } else {
                        //****************LOC9******************
                        UnCoarri.EQD.LOC9 loc9 = coarri.new EQD().new LOC9();
                        loc9.setLoadPort(rsCtn.getString("load_port_code").replace("CNTAG", "CNTAC"));
                        eqd.LOC(loc9);
                    }
                    if ("I".equals(iefg) && "S".equalsIgnoreCase(rsCtn.getString("ctn_category"))) {
                        //****************LOC11******************
                        UnCoarri.EQD.LOC11 loc11 = coarri.new EQD().new LOC11();
                        loc11.setDiscPort("CNTAC");
                        eqd.LOC(loc11);
                    } else {
                        //****************LOC11******************
                        UnCoarri.EQD.LOC11 loc11 = coarri.new EQD().new LOC11();
                        loc11.setDiscPort(rsCtn.getString("discharge_port_code").replace("CNTAG", "CNTAC"));
                        eqd.LOC(loc11);
                    }

                } else {
                    //****************LOC9******************
                    UnCoarri.EQD.LOC9 loc9 = coarri.new EQD().new LOC9();
                    loc9.setLoadPort(rsCtn.getString("load_port_code"));
                    eqd.LOC(loc9);
                    //****************LOC11******************
                    UnCoarri.EQD.LOC11 loc11 = coarri.new EQD().new LOC11();
                    loc11.setDiscPort(rsCtn.getString("discharge_port_code"));
                    eqd.LOC(loc11);
                }

                //****************MEA******************
                UnCoarri.EQD.MEA mea = coarri.new EQD().new MEA();
                if ("HLC".equalsIgnoreCase(customer) && "E".equals(rsCtn.getString("ctn_status"))) {
                    mea.setMeaVal("0");
                } else {
                    mea.setMeaVal(rsCtn.getString("ctn_weight"));
                }
                eqd.MEA(mea);

                //****************TMP******************
                if ("HLC".equalsIgnoreCase(customer)) {
                    if ("Y".equalsIgnoreCase(rsCtn.getString("reefer_flag"))) {
                        UnCoarri.EQD.TMP tmp = coarri.new EQD().new TMP();
                        tmp.setTmpVal(rsCtn.getString("temperature_setting"));
                        eqd.TMP(tmp);
                    }
                }

                //****************DGS******************
                if ("HLC".equalsIgnoreCase(customer)) {
                    if ("Y".equalsIgnoreCase(rsCtn.getString("hazard_flag"))) {
                        UnCoarri.EQD.DGS dgs = coarri.new EQD().new DGS();
                        dgs.setDgsCodeId(rsCtn.getString("class"));
                        dgs.setDgsNo(rsCtn.getString("undg_no"));
                        eqd.DGS(dgs);
                    }
                }

                //****************SEL******************
                String sealNo = rsCtn.getString("seal_no");
                if (StringUtil.isNotEmpty(sealNo)) {
                    //****************SEL******************
                    UnCoarri.EQD.SEL sel = coarri.new EQD().new SEL();
                    sel.setSealNo(sealNo);
                    eqd.SEL(sel);
                }

                //****************DAM******************
                if ("HLC".equalsIgnoreCase(customer)) {
                    if ("Y".equalsIgnoreCase(rsCtn.getString("damage_flag"))) {
                        UnCoarri.EQD.DAM dam = coarri.new EQD().new DAM();
                        dam.setDamDetailsQua(rsCtn.getString("damage_reason_code"));
                        eqd.DAM(dam);
                    }
                }

                eqds.add(eqd);

                MsgCtnDetailLog log = new MsgCtnDetailLog();
                log.setVslName(rsVsl.getString("vessel_namec"));
                log.setVoyage(rsVsl.getString("voyage"));
                log.setCustomer(customer);
                log.setVslRef(rsVsl.getString("vessel_reference"));
                log.setMsgName(msgName);
                log.setMsgType("coarri");
                log.setCtnNo(rsCtn.getString("ctn_no"));
                log.setInQuayTime((rsCtn.getString("in_yard_time") == null) ? null : DatetimeUtil.toTimestamp(DatetimeUtil.toCalendar(rsCtn.getString("in_yard_time"))));
                log.setOutQuayTime((rsCtn.getString("out_yard_time") == null) ? null : DatetimeUtil.toTimestamp(DatetimeUtil.toCalendar(rsCtn.getString("out_yard_time"))));

                logs.add(log);
            }

            coarri.setEqds(eqds);

            //****************CNT******************
            UnCoarri.CNT cnt = coarri.new CNT();
            coarri.CNT(cnt);

            //****************UNT******************
            UnCoarri.UNT unt = coarri.new UNT();
            unt.setMsgRefNo(refNo);
            coarri.UNT(unt);

            //****************UNZ******************
            UnCoarri.UNZ unz = coarri.new UNZ();
            unz.setMsgRefNo(logId);
            coarri.UNZ(unz);

            logId = String.valueOf(EDIHelper.getLogSeq());
            String filename = buildFilename("coarri", customer, sender, receiver, msgName, logId, "txt");

            eqdSize = eqds.size();

            if (!eqds.isEmpty()) {
                String report = coarri.toString().replaceAll("null", "");
                System.out.println(report);
                boolean ok = write(filename, report);
                if (ok) {
                    logSend(logId, customer, "coarri", "UN", filename, report, redo);
                    logMsgDetails(Long.valueOf(logId), logs);
                }
            }

        }

        //在该时间段内，有关闭航次或者离港但没有该客户的作业数据时才会执行
        //如果一直没有关闭航次或离泊发生，当前客户的最近一次
        //业务数据的时间就不会被更新
        if (eqdSize == 0) {
            logSend(logId, customer, "coarri", "UN", "", "", redo);
        }
    }

    /**
     * 联合国格式报文
     *
     * @param customer
     * @param rsVsl
     * @param redo
     * @param ctnNos
     */
    public static void un0(String customer, ResultSet rsVsl, boolean redo, String... ctnNos) {

        String sqlVslCtnVw = SQLQueryConstants.SQL_VSL_CTN_VW;

        if (ValidationUtil.isValid(ctnNos)) {
            sqlVslCtnVw = sqlVslCtnVw + "and ctn_no in(" + buildSqlInClause(ctnNos) + ") ";
        }

        try {

            processUn(customer, null, sqlVslCtnVw, rsVsl, null, redo);

        } catch (SQLException ex) {
            ex.printStackTrace();
            ExceptionNotifyTask.notify(ex, new String[]{customer, "coarri"});
        }
    }

}
