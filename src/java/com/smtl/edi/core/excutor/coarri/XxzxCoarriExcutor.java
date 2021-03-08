package com.smtl.edi.core.excutor.coarri;

import com.EDIHelper;
import static com.EDIHelper.logSend;
import static com.EDIHelper.write;

import com.smtl.edi.core.model.jt.JtXxzxCoarri;
import com.smtl.edi.core.log.MsgCtnDetailLog;
import com.smtl.edi.util.DatetimeUtil;
import com.smtl.edi.util.DbUtil;
import com.smtl.edi.util.ExceptionUtil;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import com.smtl.edi.core.SQLQueryConstants;
import static com.smtl.edi.core.SQLQueryConstants.SQL_XXZX_TOTAL_CTNS;
import static com.smtl.edi.core.SQLQueryConstants.SQL_XXZX_TOTAL_TEU;
import static com.smtl.edi.core.SQLQueryConstants.SQL_XXZX_TOTAL_WT;
import static com.smtl.edi.core.SQLQueryConstants.SQL_XXZX_VSL_CTN_VW;
import com.smtl.edi.core.task.mail.ExceptionNotifyTask;
import static com.EDIHelper.buildFilename;
import static com.EDIHelper.logMsgDetails;
import static com.EDIHelper.PS_COARRI_CUSTOMER;
import com.smtl.edi.vo.DateRange;
import com.smtl.edi.vo.VesselVoyage;
import org.apache.log4j.Logger;

/**
 * 电子口岸清洁数据采用离泊时间进行报文推送
 *
 * @author nm
 */
public class XxzxCoarriExcutor {

    final static Logger LOGGER = Logger.getLogger(XxzxCoarriExcutor.class);

    /**
     * 数据准备
     *
     * @param range
     * @param redo
     */
    public static void prepareAndProcess(DateRange range, boolean redo) {

        try {

            process(range, null, redo);

        } catch (SQLException ex) {
            LOGGER.info(ExceptionUtil.getStackTraceAsString(ex));
            ExceptionNotifyTask.notify(ex, new String[]{"xxzx", "coarri"});
        }

    }

    /**
     * 创建交通部电子口岸定制版格式的装卸船报文（该段时间内离泊/航次关闭的航次）
     *
     * @param sender
     * @param receiver
     * @param vslName
     * @param voyage
     * @param redo
     * @throws SQLException
     */
    private static void process(DateRange range, VesselVoyage vessel, boolean redo) throws SQLException {

        PS_COARRI_CUSTOMER.setString(1, "XXZX");
        PS_COARRI_CUSTOMER.setString(2, "COARRI");
        ResultSet rsCust = PS_COARRI_CUSTOMER.executeQuery();

        String sender, receiver;
        if (rsCust.next()) {
            sender = rsCust.getString("cst_sender");
            receiver = rsCust.getString("cst_receiver");
        } else {
            return;
        }

        Connection con = DbUtil.getConnection();

        try (PreparedStatement psVsl = DbUtil.preparedStatement(con, SQLQueryConstants.SQL_XXZX_VSL_VOY_VW);
                PreparedStatement psTotalCtns = DbUtil.preparedStatement(con, SQL_XXZX_TOTAL_CTNS);
                PreparedStatement psTotalTEU = DbUtil.preparedStatement(con, SQL_XXZX_TOTAL_TEU);
                PreparedStatement psTotalWeight = DbUtil.preparedStatement(con, SQL_XXZX_TOTAL_WT)) {

            if (range != null) {
                psVsl.setString(1, range.getBegin());
                psVsl.setString(2, range.getEnd());
            } else if (vessel != null) {
                psVsl.setString(1, vessel.getVessel());
                psVsl.setString(2, vessel.getVoyage());
            } else {
                return;
            }

            ResultSet rsVsl = psVsl.executeQuery();

            while (rsVsl.next()) {

                List<MsgCtnDetailLog> logs = new LinkedList<>();

                JtXxzxCoarri coarri = new JtXxzxCoarri();

                //00
                JtXxzxCoarri.SEG00 seg00 = coarri.new SEG00();
                String msgName = "";
                String ieFlag = rsVsl.getString("ie_flag");
                if ("I".equals(ieFlag)) {
                    seg00.setFileDesc("DISCHARGE REPORT");
                    msgName = "Discharge";
                } else {
                    seg00.setFileDesc("LOAD REPORT");
                    msgName = "Load";
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

                JtXxzxCoarri.SEG10 seg10 = coarri.new SEG10();

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
                seg10.setTotalCtns(getTotalCtns(rsVsl.getString("vessel_reference"), rsVsl.getString("voyage"), rsVsl.getString("ie_flag"), psTotalCtns));
                seg10.setBerthNo(rsVsl.getString("berth_reference"));
                seg10.setIMO(rsVsl.getString("call_sign"));
                seg10.setYqb(rsVsl.getString("yqb"));
                seg10.setBerthingDraft(rsVsl.getString("arrival_draft"));
                seg10.setDepartureDraft(rsVsl.getString("departure_draft"));
                seg10.setCargoName(rsVsl.getString("cargo_name"));
                seg10.setTotalWeight(getTotalWeight(rsVsl.getString("vessel_reference"), rsVsl.getString("voyage"), rsVsl.getString("ie_flag"), psTotalWeight));
                seg10.setTeu(getTotalTEU(rsVsl.getString("vessel_reference"), rsVsl.getString("voyage"), rsVsl.getString("ie_flag"), psTotalTEU));
                seg10.setZkl(rsVsl.getString("zkl"));
                seg10.setSxk(rsVsl.getString("sxk"));
                seg10.setPayerName(rsVsl.getString("payer_name"));

                coarri.SEG10(seg10);

                try (PreparedStatement psCtn = DbUtil.preparedStatement(con, SQL_XXZX_VSL_CTN_VW)) {

                    psCtn.setString(1, rsVsl.getString("vessel_code"));
                    psCtn.setString(2, rsVsl.getString("voyage"));
                    psCtn.setString(3, rsVsl.getString("ie_flag"));

                    ResultSet rsCtn = psCtn.executeQuery();

                    //发展局要求一箱多单的，箱号去重，提单号任选一个
                    List<String> ctnNoCaches = new LinkedList();

                    while (rsCtn.next()) {

                        JtXxzxCoarri.SEG5X seg5x = coarri.new SEG5X();

                        //50
                        JtXxzxCoarri.SEG5X.SEG50 seg50 = coarri.new SEG5X().new SEG50();

                        String ctnNo = rsCtn.getString("ctn_no");

                        if (ctnNoCaches.contains(ctnNo)) {
                            continue;
                        } else {
                            ctnNoCaches.add(ctnNo);
                        }

                        seg50.setCtnNo(ctnNo);
                        seg50.setCtnSizeType(rsCtn.getString("ctn_size") + rsCtn.getString("ctn_type"));
                        seg50.setCtnOperatorCode(rsCtn.getString("ctn_operator_code"));
                        seg50.setCtnOperator(rsCtn.getString("ctn_operator"));
                        seg50.setCtnStatus(rsCtn.getString("ctn_status"));
                        seg50.setBillNo(rsCtn.getString("bl_no"));
                        seg50.setSealNo(rsCtn.getString("seal_no"));
                        seg50.setStowageLoc(rsCtn.getString("stowage_loc"));
                        seg50.setCargoName(rsCtn.getString("cargo_desc"));
                        seg50.setHazFlag(rsCtn.getString("hazard_flag"));
                        seg50.setCargoWeight(rsCtn.getString("cargo_weight"));
                        seg50.setTradeType(rsCtn.getString("trade_type"));

                        //中转标记 T 国内中转 Z 国际中转 E 退运 R 倒箱 S过境箱
                        String tranship = rsCtn.getString("tranship_flag");
                        if (tranship != null && (("T".equals(tranship)) || "Z".equals(tranship))) {
                            seg50.setTranshipFlag("Y");
                            seg50.setFirstVessel(rsCtn.getString("first_vessel_code"));
                            seg50.setFirstVoyage(rsCtn.getString("first_voyage"));
                        }

                        if ("E".equals(ieFlag)) {
                            seg50.setInOutQuayTime(rsCtn.getString("out_yard_time"));
                        } else {
                            seg50.setInOutQuayTime(rsCtn.getString("in_yard_time"));
                        }
                        seg50.setPayerName(rsCtn.getString("payer_name"));

                        seg5x.SEG50(seg50);

                        //52
                        JtXxzxCoarri.SEG5X.SEG52 seg52 = coarri.new SEG5X().new SEG52();
                        seg52.setDischargePortCode(rsCtn.getString("discharge_port_code"));
                        seg52.setDischargePortName(rsCtn.getString("discharge_port"));
                        seg52.setLoadPortCode(rsCtn.getString("load_port_code"));
                        seg52.setLoadPortName(rsCtn.getString("load_port"));
                        seg52.setDestPortCode(rsCtn.getString("destination_port_code"));
                        seg52.setDestPortName(rsCtn.getString("destination_port"));
                        seg52.setGrossWeight(rsCtn.getString("ctn_weight"));

                        seg5x.SEG52(seg52);

                        coarri.getSeg5xs().add(seg5x);

                        MsgCtnDetailLog log = new MsgCtnDetailLog();
                        log.setVslName(rsVsl.getString("vessel_namec"));
                        log.setVoyage(rsVsl.getString("voyage"));
                        log.setCustomer("XXZX");
                        log.setVslRef(rsVsl.getString("vessel_reference"));
                        log.setMsgName(msgName);
                        log.setMsgType("coarri");
                        log.setCtnNo(ctnNo);
                        log.setInQuayTime((rsCtn.getString("in_yard_time") == null) ? null : DatetimeUtil.toTimestamp(DatetimeUtil.toCalendar(rsCtn.getString("in_yard_time"))));
                        log.setOutQuayTime((rsCtn.getString("out_yard_time") == null) ? null : DatetimeUtil.toTimestamp(DatetimeUtil.toCalendar(rsCtn.getString("out_yard_time"))));

                        logs.add(log);

                    }

                    DbUtil.close(rsCtn);
                }

                //统计每种箱型的空/重/危险品数量
                String sqlCtnStats = "{ call sp_tc2_edi_ctn_stats(?,?,?,?)}";

                try (CallableStatement callCtnStats = con.prepareCall(sqlCtnStats)) {

                    callCtnStats.setString(1, rsVsl.getString("vessel_reference"));
                    callCtnStats.setString(2, rsVsl.getString("voyage"));
                    callCtnStats.setString(3, rsVsl.getString("ie_flag"));
                    callCtnStats.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);

                    callCtnStats.execute();

                    ResultSet rsCtnStats = (ResultSet) callCtnStats.getObject(4);

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
                    String filename = buildFilename("coarri", "XXZX", sender, receiver, msgName, logId, "txt");
                    boolean ok = write(filename, report);
                    if (ok) {
                        logSend(logId, "XXZX", "coarri", "JT", filename, report, redo);
                        logMsgDetails(Long.valueOf(logId), logs);
                    }
                } else {
                    logSend(logId, "XXZX", "coarri", "JT", "", "", redo);
                }
            }
        }
    }

    /**
     * 创建交通部电子口岸定制版格式的装卸船报文（按照船名航次）
     *
     * @param vessel
     * @param redo
     */
    public static void prepareAndExcute0(VesselVoyage vessel, boolean redo) {

        try {

            process(null, vessel, redo);

        } catch (SQLException ex) {
            LOGGER.info(ExceptionUtil.getStackTraceAsString(ex));
            ExceptionNotifyTask.notify(ex, new String[]{"xxzx", "coarri"});
        }

    }

    /**
     * 获取该航次下的箱总数
     *
     * @param vslRef
     * @param voyage
     * @param ieFlag
     * @param ps
     * @return
     */
    private static String getTotalCtns(String vslRef, String voyage, String ieFlag, PreparedStatement ps) {
        try {

            ps.setString(1, vslRef);
            ps.setString(2, voyage);
            ps.setString(3, ieFlag);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("total_ctns");
            }

            DbUtil.close(rs);

        } catch (SQLException ex) {
            LOGGER.info(ExceptionUtil.getStackTraceAsString(ex));
        }

        return "-1";
    }

    /**
     * 获取该航次下的箱总TEU
     *
     * @param vslRef
     * @param voyage
     * @param ieFlag
     * @param ps
     * @return
     */
    private static String getTotalTEU(String vslRef, String voyage, String ieFlag, PreparedStatement ps) {

        try {

            ps.setString(1, vslRef);
            ps.setString(2, voyage);
            ps.setString(3, ieFlag);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("total_teu");
            }

            DbUtil.close(rs);

        } catch (SQLException ex) {
            LOGGER.info(ExceptionUtil.getStackTraceAsString(ex));
        }

        return "-1";
    }

    /**
     * 获取航次下的箱总重
     *
     * @param vslRef
     * @param voyage
     * @param ieFlag
     * @param ps
     * @return
     */
    private static String getTotalWeight(String vslRef, String voyage, String ieFlag, PreparedStatement ps) {

        try {

            ps.setString(1, vslRef);
            ps.setString(2, voyage);
            ps.setString(3, ieFlag);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("total_weight");
            }

            DbUtil.close(ps);
            DbUtil.close(rs);

        } catch (SQLException ex) {
            LOGGER.info(ExceptionUtil.getStackTraceAsString(ex));
        }

        return "-1";
    }
}
