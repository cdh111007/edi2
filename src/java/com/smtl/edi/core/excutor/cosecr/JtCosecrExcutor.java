package com.smtl.edi.core.excutor.cosecr;

import com.EDIHelper;
import static com.EDIHelper.PS_COEDOR_CUSTOMER;
import static com.EDIHelper.buildFilename;
import static com.EDIHelper.logSend;
import static com.EDIHelper.write;
import com.smtl.edi.core.SQLQueryConstants;
import com.smtl.edi.core.model.jt.JtCosecr;
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
public class JtCosecrExcutor {

    final static Logger LOGGER = Logger.getLogger(JtCosecrExcutor.class);

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
            PS_COEDOR_CUSTOMER.setString(2, "COSECR");
            ResultSet rsCust = PS_COEDOR_CUSTOMER.executeQuery();

            String sender, receiver;
            if (rsCust.next()) {
                sender = rsCust.getString("cst_sender");
                receiver = rsCust.getString("cst_receiver");
            } else {
                return;
            }

            Connection con = DbUtil.getConnection();

            try (PreparedStatement psTotal = con.prepareStatement(SQLQueryConstants.SQL_COSECR_LOG_TOTAL);
                    PreparedStatement psCoarri = con.prepareStatement(SQLQueryConstants.SQL_COSECR_COARRI_END_LIST)) {

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
                        JtCosecr cosecr = new JtCosecr();

                        JtCosecr.SEG00 seg00 = cosecr.new SEG00();
                        seg00.setSender(sender);
                        seg00.setRecipient(receiver);
                        seg00.setFileDesc("END CONFIRM REPORT");
                        seg00.setFileCreateTime(DatetimeUtil.now(DatetimeUtil.YYYYMMDDHHMMSS));
                        cosecr.SEG00(seg00);

                        JtCosecr.SEG10 seg10 = cosecr.new SEG10();
                        seg10.setVesselNameCN(rsCoarri.getString("vessel_namec"));
                        seg10.setVesselNameEN(rsCoarri.getString("vessel_namee"));

                        if ("I".equalsIgnoreCase(rsCoarri.getString("vessel_ie"))) {
                            seg10.setImportVoyage(rsCoarri.getString("voyage") + rsCoarri.getString("vessel_ie"));
                        } else {
                            seg10.setExportVoyage(rsCoarri.getString("voyage") + rsCoarri.getString("vessel_ie"));
                        }

                        seg10.setAts(DatetimeUtil.format(rsCoarri.getString("ats"), DatetimeUtil.YYYYMMDDHHMMSS));
                        seg10.setAte(DatetimeUtil.format(rsCoarri.getString("ate"), DatetimeUtil.YYYYMMDDHHMMSS));
                        seg10.setPlaceCode(customer);

                        cosecr.SEG10(seg10);

                        JtCosecr.SEG99 seg99 = cosecr.new SEG99();
                        cosecr.SEG99(seg99);

                        String logId = String.valueOf(EDIHelper.getLogSeq());

                        String report = cosecr.toString().replaceAll("null", "");
                        System.out.println(report);
                        String filename = buildFilename("cosecr", customer, sender,
                                receiver, "END CONFIRM REPORT", logId, "txt");
                        boolean ok = write(filename, report);
                        if (ok) {
                            logSend(logId, customer, "cosecr", "JT", filename, report, false);
                            logCosecr(logId, customer, rsCoarri.getString("vessel_code"), rsCoarri.getString("voyage"), rsCoarri.getString("vessel_ie"));
                        }

                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            ExceptionNotifyTask.notify(ex, new String[]{customer, "coedor"});
        }

    }

    private static void logCosecr(String logId, String customer, String vesselCode, String voyage, String vesselIE) {
        String insertSql = "insert into tc2_edi_cosecr_log"
                + "  (ecl_log_id, ecl_cst_code, ecl_vsl_code, ecl_voyage, ecl_vessel_ie)"
                + "values"
                + "  (?, ?, ?, ?, ?)";
        Connection con = DbUtil.getConnection();

        try (PreparedStatement ps = con.prepareStatement(insertSql);) {

            DbUtil.setAutoCommit(con, false);

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
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            ExceptionNotifyTask.notify(ex, new String[]{customer, "cosecr"});
        } finally {
            DbUtil.setAutoCommit(con, true);
        }

    }
}
