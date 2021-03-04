package com.smtl.edi.core.excutor.codeco;

import com.EDIHelper;
import static com.EDIHelper.write;
import com.smtl.edi.core.model.jt.JtCodeco;
import com.smtl.edi.core.log.MsgCtnDetailLog;
import com.smtl.edi.util.DatetimeUtil;
import com.smtl.edi.util.DbUtil;
import com.smtl.edi.util.ExceptionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import static com.EDIHelper.logSend;
import com.smtl.edi.core.SQLQueryConstants;
import com.smtl.edi.core.task.mail.ExceptionNotifyTask;
import static com.EDIHelper.buildFilename;
import static com.EDIHelper.logMsgDetails;
import static com.EDIHelper.PS_CODECO_CUSTOMER;
import com.smtl.edi.util.StringUtil;
import static com.smtl.edi.util.StringUtil.buildSqlInClause;
import com.smtl.edi.util.ValidationUtil;
import com.smtl.edi.vo.DateRange;

/**
 * 交通部进出门报文入口
 *
 * @author nm
 */
public class JtCodecoExcutor {

    final static Logger LOGGER = Logger.getLogger(JtCodecoExcutor.class);

    /**
     * 创建交通部格式进出门报文
     *
     * @param customer
     * @param range
     * @param redo
     * @param ctnNos
     */
    public static void doHandle(String customer, DateRange range, boolean redo, String... ctnNos) {

        String sqlGateIn = SQLQueryConstants.SQL_GATE_IN_JT;
        String sqlGateOut = SQLQueryConstants.SQL_GATE_OUT_JT;
        String sqlCtnOperator = SQLQueryConstants.SQL_CTN_OPERATOR;

        if (ValidationUtil.isValid(ctnNos)) {
            sqlGateIn = sqlGateIn + "and ctn_no in(" + buildSqlInClause(ctnNos) + ") ";
            sqlGateOut = sqlGateOut + "and ctn_no in(" + buildSqlInClause(ctnNos) + ") ";
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

                PreparedStatement psCtn = null;
                if (jobTypes[i].contains("GATE-IN")) {
                    psCtn = con.prepareStatement(sqlGateIn);
                } else {
                    psCtn = con.prepareStatement(sqlGateOut);
                }

                try (PreparedStatement psCtnOperator = DbUtil.preparedStatement(con, sqlCtnOperator)) {

                    psCtnOperator.setString(1, customer);
                    ResultSet rsCtnOperator = psCtnOperator.executeQuery();

                    while (rsCtnOperator.next()) {

                        String ctnOperator = rsCtnOperator.getString("ctn_operator");
                        if (!StringUtil.isEmpty(ctnOperator)) {
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

                        psCtn.setString(1, ctnOperator);
                        psCtn.setString(2, jobTypes[i]);
                        psCtn.setString(3, range.getBegin());
                        psCtn.setString(4, range.getEnd());

                        List<MsgCtnDetailLog> logs = new LinkedList<>();

                        ResultSet rsCtn = psCtn.executeQuery();

                        while (rsCtn.next()) {

                            String billNo = rsCtn.getString("bill_no");
                            String sealNo = rsCtn.getString("seal_no");
                            String ctnType = rsCtn.getString("ctn_size") + rsCtn.getString("ctn_type");
                            String ctnStatus = rsCtn.getString("ctn_status");
                            String purpose = rsCtn.getString("ctn_category");

                            JtCodeco.SEG5X seg5x = codeco_.new SEG5X();

                            JtCodeco.SEG5X.SEG50 seg50 = seg5x.new SEG50();
                            seg50.setBillNo(billNo);
                            seg50.setGrossWeight(rsCtn.getString("gross_weight"));
                            seg50.setCtnNo(rsCtn.getString("ctn_no"));
                            seg50.setCtnStatus(ctnStatus);
                            seg50.setCtnType(ctnType);
                            if ("2".equals(purpose)) {
                                seg50.setInOutGatePurpose("E");//I进口 E出口 V装 D拆
                            } else {
                                seg50.setInOutGatePurpose("I");//I进口 E出口 V装 D拆
                            }
                            if (i == 0) {
                                seg50.setInGateTime(rsCtn.getString("in_yard_time"));
                            } else {
                                seg50.setOutGateTime(rsCtn.getString("out_yard_time"));
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
                            String filename = buildFilename("codeco", customer, sender,
                                    receiver, jobTypes[i].replaceAll("REPORT", "").trim(), logId, "txt");
                            boolean ok = write(filename, report);
                            if (ok) {
                                logSend(logId, customer, "codeco", "JT", filename, report, redo);
                                logMsgDetails(Long.valueOf(logId), logs);
                            }
                        } else {
                            logSend(logId, customer, "codeco", "JT", "", "", redo);
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
}
