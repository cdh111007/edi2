package com.smtl.edi.core.excutor.codeco;

import com.EDIHelper;
import static com.EDIHelper.write;
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
import com.smtl.edi.core.task.mail.ExceptionNotifyTask;
import static com.EDIHelper.buildFilename;
import static com.EDIHelper.logMsgDetails;
import static com.EDIHelper.PS_CODECO_CUSTOMER;
import com.smtl.edi.util.StringUtil;

/**
 * 华航物流 每天一次 发送前一天报文数据
 *
 * @author nm
 */
public class HhwlCodecoExcutor {

    final static Logger LOGGER = Logger.getLogger(HhwlCodecoExcutor.class);

    public static void main(String[] args) {
        doHandle();
    }

    /**
     * 几天前报文
     *
     * @param whichDay
     */
    public static void doHandle(Integer whichDay) {
        doHandle("HHWL", false, whichDay);
    }

    /**
     *
     */
    public static void doHandle() {
        doHandle("HHWL", false);
    }

    /**
     *
     * @param redo
     * @param whichDay
     */
    public static void doHandle(boolean redo, Integer... whichDay) {
        doHandle("HHWL", redo, whichDay);
    }

    /**
     * 创建华航进出门报文
     *
     * @param customer 客户代码
     * @param redo 是否重发
     * @param whichDay 从今天开始往前推几天
     */
    public static void doHandle(String customer, boolean redo, Integer... whichDay) {

        //箱明细
        String sqlGateIn = "select * from tc2_edi_codeco_vw t "
                + "where upper(t.cst_code)=? "
                + "and upper(t.in_out_category)=? "
                + "and substr(t.in_yard_time,1,8) = "
                + "to_char(sysdate-%s,'YYYYMMDD')";

        String sqlGateOut = "select * from tc2_edi_codeco_vw t "
                + "where upper(t.cst_code)=? "
                + "and upper(t.in_out_category)=? "
                + "and substr(t.out_yard_time,1,8) = "
                + "to_char(sysdate-%s,'YYYYMMDD')";

        if (null != whichDay && whichDay.length > 0) {
            sqlGateIn = String.format(sqlGateIn, whichDay[0]);
            sqlGateOut = String.format(sqlGateOut, whichDay[0]);
        } else {
            sqlGateIn = String.format(sqlGateIn, 1);
            sqlGateOut = String.format(sqlGateOut, 1);
        }

        customer = customer.toUpperCase();

        PreparedStatement psCtn = null;

        try {

            PS_CODECO_CUSTOMER.setString(1, customer);
            PS_CODECO_CUSTOMER.setString(2, "CODECO");
            ResultSet rsCustomer = PS_CODECO_CUSTOMER.executeQuery();

            String sender = "", receiver = "";
            if (rsCustomer.next()) {
                sender = rsCustomer.getString("cst_sender");
                receiver = rsCustomer.getString("cst_receiver");
            } else {
            }

            Connection con = DbUtil.getConnection();

            String[] jobTypes = {"GATE-IN REPORT", "GATE-OUT REPORT"};

            for (int i = 0; i < jobTypes.length; i++) {

                List<MsgCtnDetailLog> logs = new LinkedList<>();

                if (jobTypes[i].equals("GATE-IN REPORT")) {
                    psCtn = con.prepareStatement(sqlGateIn);
                } else {
                    psCtn = con.prepareStatement(sqlGateOut);
                }

                StringBuilder codeco = new StringBuilder();

                codeco.append("FM ")
                        .append(sender)
                        .append(" ")
                        .append(DatetimeUtil.now(DatetimeUtil.YYYYMMDD))
                        .append(" ")
                        .append(DatetimeUtil.now(DatetimeUtil.HHMMMSS))
                        .append(System.lineSeparator());

                psCtn.setString(1, customer);
                psCtn.setString(2, jobTypes[i]);

                ResultSet rsCtn = psCtn.executeQuery();

                int count = 0;

                while (rsCtn.next()) {

                    String billNo = rsCtn.getString("bill_no");
                    String dvFlag = rsCtn.getString("cfs_flag");//拆装箱
                    String ctnStatus = rsCtn.getString("ctn_status");
                    String reportType = rsCtn.getString("in_out_category");

                    String rcvType = " ";
                    String from = " ";
                    String to = " ";

                    if (ctnStatus.equalsIgnoreCase("E")) {
                        if (reportType.equals("GATE-IN REPORT")) {
                            rcvType = "RCVE";
                            from = "CNEE";
                            to = sender;
                        } else {
                            rcvType = "STSH";
                            from = sender;
                            to = "SHPR";
                        }
                    } else {
                        if (reportType.equals("GATE-IN REPORT")) {
                            rcvType = "RCVF";
                            from = "SHPR";
                            to = sender;
                        } else {
                            rcvType = "STCO";
                            from = sender;
                            to = "CNEE";
                        }
                    }

                    //拆箱
                    if (StringUtil.isNotEmpty(dvFlag)) {
                        if (dvFlag.equals("E") && reportType.equals("GATE-IN REPORT")) {
                            rcvType = "DEVN";
                        }
                    }

                    String vessel = " ", voyage = " ";

                    //根据提单号提取船名航次
                    if (!StringUtil.isEmpty(billNo) && billNo.length() > 10) {

                        if (billNo.contains("-")) {
                            billNo = billNo.substring(0, billNo.indexOf("-"));
                        } else if (billNo.contains("/")) {
                            billNo = billNo.substring(0, billNo.indexOf("/"));
                        }

                        int len = billNo.length();

                        char chr = billNo.charAt(len - 1);
                        if (chr >= 65) {//最后一位包含字符
                            voyage = billNo.substring(len - 10, len - 5);
                        } else {
                            voyage = billNo.substring(len - 9, len - 4);
                        }

                        if (billNo.startsWith("HH")) {
                            vessel = "HH1";
                        } else if (billNo.startsWith("XJ") || billNo.startsWith("HX")) {
                            vessel = "XJ ";//最后一个空格必须要
                        }
                    }

                    String inOutTime;
                    if (jobTypes[i].equals("GATE-IN REPORT")) {
                        inOutTime = rsCtn.getString("in_yard_time");
                    } else {
                        inOutTime = rsCtn.getString("out_yard_time");
                    }

                    codeco.append(String.format("%1$-4s", rcvType)).append(" ")
                            .append(String.format("%1$-8s", inOutTime.substring(0, 8))).append(" ")
                            .append(String.format("%1$-4s", inOutTime.substring(8)))
                            .append("00").append(" ")
                            .append(String.format("%1$-11s", rsCtn.getString("ctn_no"))).append(" ")
                            .append(String.format("%1$-1s", ctnStatus)).append(" ")
                            .append(String.format("%1$-2s", "AV")).append(" ")
                            .append(String.format("%1$-4s", rsCtn.getString("ctn_size") + rsCtn.getString("ctn_type"))).append(" ")
                            .append(String.format("%1$-3s", " ")).append(" ")//箱主
                            .append(String.format("%1$-3s", " ")).append(" ")//营运人
                            .append(String.format("%1$-6s", vessel)).append(" ")//船名
                            .append(String.format("%1$-5s", voyage)).append(" ")//航次
                            .append(String.format("%1$-20s", StringUtil.isEmpty(billNo) ? " " : billNo)).append(" ")
                            .append(String.format("%1$-6s", from)).append(" ")
                            .append(String.format("%1$-6s", to)).append(System.getProperty("line.separator"));

                    count++;

                    MsgCtnDetailLog log = new MsgCtnDetailLog();

                    log.setCustomer(customer);
                    log.setMsgName(jobTypes[i]);
                    log.setMsgType("codeco");
                    log.setCtnNo(rsCtn.getString("ctn_no"));
                    log.setInQuayTime((rsCtn.getString("in_yard_time") == null) ? null : DatetimeUtil.toTimestamp(DatetimeUtil.toCalendar(rsCtn.getString("in_yard_time"))));
                    log.setOutQuayTime((rsCtn.getString("out_yard_time") == null) ? null : DatetimeUtil.toTimestamp(DatetimeUtil.toCalendar(rsCtn.getString("out_yard_time"))));

                    logs.add(log);

                }

                String logId = String.valueOf(EDIHelper.getLogSeq());
                if (codeco.length() > 0 && count > 0) {
                    String report = codeco.toString().replaceAll("null", "");
                    System.out.println(report);
                    String filename = buildFilename("codeco", customer, sender, receiver, jobTypes[i], logId, "txt");
                    boolean ok = write(filename, report);
                    if (ok) {
                        logSend(logId, customer, "codeco", "C", filename, report, redo);
                        logMsgDetails(Long.valueOf(logId), logs);
                    }
                } else {
                    logSend(logId, customer, "codeco", "C", "", "", redo);
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            ExceptionNotifyTask.notify(ex, new String[]{"hhwl", "codeco"});
        } finally {
            DbUtil.close(psCtn);
        }

    }
}
