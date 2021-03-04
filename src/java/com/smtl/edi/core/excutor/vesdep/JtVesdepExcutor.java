package com.smtl.edi.core.excutor.vesdep;

import com.EDIHelper;
import static com.EDIHelper.PS_COEDOR_CUSTOMER;
import static com.EDIHelper.buildFilename;
import static com.EDIHelper.logSend;
import static com.EDIHelper.write;
import com.smtl.edi.core.SQLQueryConstants;
import com.smtl.edi.core.model.jt.JtVesdep;
import com.smtl.edi.core.task.mail.ExceptionNotifyTask;
import com.smtl.edi.util.DatetimeUtil;
import com.smtl.edi.util.DbUtil;
import com.smtl.edi.util.ExceptionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author nm
 */
public class JtVesdepExcutor {

    final static Logger LOGGER = Logger.getLogger(JtVesdepExcutor.class);

    public static void main(String[] args) {
        doHandle("ZGXL");
    }

    /**
     *
     *
     * @param customer
     *
     */
    public static void doHandle(String customer) {

        try {

            customer = customer.toUpperCase();

            PS_COEDOR_CUSTOMER.setString(1, customer);
            PS_COEDOR_CUSTOMER.setString(2, "VESDEP");
            ResultSet rsCust = PS_COEDOR_CUSTOMER.executeQuery();

            String sender, receiver;
            if (rsCust.next()) {
                sender = rsCust.getString("cst_sender");
                receiver = rsCust.getString("cst_receiver");
            } else {
                return;
            }

            Connection con = DbUtil.getConnection();

            PreparedStatement psTotal = con.prepareStatement(SQLQueryConstants.SQL_VESDEP_LOG_TOTAL);

            PreparedStatement psCoarri = con.prepareStatement(SQLQueryConstants.SQL_VESDEP_COARRI_DEPARTURE_LIST);
            psCoarri.setString(1, customer);

            ResultSet rsCoarri = psCoarri.executeQuery();

            while (rsCoarri.next()) {
                psTotal.setString(1, rsCoarri.getString("vessel_code"));
                psTotal.setString(2, rsCoarri.getString("voyage"));
                psTotal.setString(3, rsCoarri.getString("vessel_ie"));
                psTotal.setString(4, customer);

                ResultSet rsTotal = psTotal.executeQuery();
                rsTotal.next();
                if (rsTotal.getInt("total") == 0) {
                    //1、插入vesdep_log表；2、插入send_log表

                    JtVesdep vesdep = new JtVesdep();

                    JtVesdep.SEG00 seg00 = vesdep.new SEG00();
                    seg00.setSender(sender);
                    seg00.setRecipient(receiver);
                    seg00.setFileDesc("VESSEL DEPARTURE");
                    seg00.setFileCreateTime(DatetimeUtil.now(DatetimeUtil.YYYYMMDDHHMMSS));
                    vesdep.SEG00(seg00);

                    JtVesdep.SEG10 seg10 = vesdep.new SEG10();
                    seg10.setVesselCode(rsCoarri.getString("vessel_code"));
                    seg10.setVessel(rsCoarri.getString("vessel_namec"));
                    seg10.setVoyage(rsCoarri.getString("voyage") + rsCoarri.getString("vessel_ie"));
                    seg10.setCarrierCode(customer);
                    seg10.setCarrier(customer);
                    seg10.setNationalityCode("CN");
                    seg10.setSailingTime(DatetimeUtil.format(rsCoarri.getString("atd"), DatetimeUtil.YYYYMMDDHHMMSS));
                    seg10.setBerthTime(DatetimeUtil.format(rsCoarri.getString("ata"), DatetimeUtil.YYYYMMDDHHMMSS));
                    vesdep.SEG10(seg10);

                    if ("I".equals(rsCoarri.getString("vessel_ie"))) {
                        JtVesdep.SEG15 seg15 = vesdep.new SEG15();
                        seg15.setDischargeBegin(DatetimeUtil.format(rsCoarri.getString("ats"), DatetimeUtil.YYYYMMDDHHMMSS));
                        seg15.setDischargeEnd(DatetimeUtil.format(rsCoarri.getString("ate"), DatetimeUtil.YYYYMMDDHHMMSS));
                        vesdep.SEG15(seg15);
                    } else {
                        JtVesdep.SEG16 seg16 = vesdep.new SEG16();
                        seg16.setLoadingBegin(DatetimeUtil.format(rsCoarri.getString("ats"), DatetimeUtil.YYYYMMDDHHMMSS));
                        seg16.setLoadingEnd(DatetimeUtil.format(rsCoarri.getString("ate"), DatetimeUtil.YYYYMMDDHHMMSS));
                        vesdep.SEG16(seg16);
                    }

                    JtVesdep.SEG99 seg99 = vesdep.new SEG99();
                    vesdep.SEG99(seg99);

                    String logId = String.valueOf(EDIHelper.getLogSeq());

                    String report = vesdep.toString().replaceAll("null", "");
                    System.out.println(report);
                    String filename = buildFilename("vesdep", customer, sender,
                            receiver, "VESSEL DEPARTURE", logId, "txt");
                    boolean ok = write(filename, report);
                    if (ok) {
                        logSend(logId, customer, "vesdep", "JT", filename, report, false);
                        logVesdep(logId, customer, rsCoarri.getString("vessel_code"), rsCoarri.getString("voyage"), rsCoarri.getString("vessel_ie"));
                    }

                }
            }

            DbUtil.close(psCoarri);
            DbUtil.close(psTotal);

        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            ExceptionNotifyTask.notify(ex, new String[]{customer, "coedor"});
        }

    }

    private static void logVesdep(String logId, String customer, String vesselCode, String voyage, String vesselIE) {
        String insertSql = "insert into tc2_edi_vesdep_log"
                + "  (evl_log_id, evl_cst_code, evl_vsl_code, evl_voyage, evl_vessel_ie)"
                + "values"
                + "  (?, ?, ?, ?, ?)";
        Connection con = DbUtil.getConnection();
        PreparedStatement ps = null;
        try {

            DbUtil.setAutoCommit(con, false);

            ps = con.prepareStatement(insertSql);
            ps.setLong(1, Long.valueOf(logId));
            ps.setString(2, customer);
            ps.setString(3, vesselCode);
            ps.setString(4, voyage);
            ps.setString(5, vesselIE);

            ps.executeUpdate();

            con.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException ex1) {
                ex1.printStackTrace();
            }

        } finally {
            DbUtil.setAutoCommit(con, true);
            DbUtil.close(ps);
        }

    }
}
