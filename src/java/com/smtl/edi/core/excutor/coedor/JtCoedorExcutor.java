package com.smtl.edi.core.excutor.coedor;

import com.EDIHelper;
import static com.EDIHelper.PS_COEDOR_CUSTOMER;
import static com.EDIHelper.buildFilename;
import static com.EDIHelper.logSend;
import static com.EDIHelper.write;
import com.smtl.edi.core.SQLQueryConstants;
import com.smtl.edi.core.model.jt.JtCoedor;
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
public class JtCoedorExcutor {

    final static Logger LOGGER = Logger.getLogger(JtCoedorExcutor.class);

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
            PS_COEDOR_CUSTOMER.setString(2, "COEDOR");
            ResultSet rsCust = PS_COEDOR_CUSTOMER.executeQuery();

            String sender, receiver;
            if (rsCust.next()) {
                sender = rsCust.getString("cst_sender");
                receiver = rsCust.getString("cst_receiver");
            } else {
                return;
            }

            Connection con = DbUtil.getConnection();

            try (PreparedStatement psCtn = con.prepareStatement(SQLQueryConstants.SQL_COEDOR)) {

                JtCoedor coedor = new JtCoedor();

                JtCoedor.SEG00 seg00 = coedor.new SEG00();
                seg00.setSender(sender);
                seg00.setRecipient(receiver);
                seg00.setFileDesc("STOCK REPORT");
                seg00.setFileCreateTime(DatetimeUtil.now(DatetimeUtil.YYYYMMDDHHMMSS));
                coedor.SEG00(seg00);

                psCtn.setString(1, customer);
                ResultSet rsCtn = psCtn.executeQuery();

                while (rsCtn.next()) {

                    JtCoedor.SEG5X seg5x = coedor.new SEG5X();

                    JtCoedor.SEG5X.SEG50 seg50 = seg5x.new SEG50();

                    seg50.setCtnNo(rsCtn.getString("ctn_no"));
                    seg50.setCtnStatus(rsCtn.getString("ctn_status"));
                    seg50.setCtnType(rsCtn.getString("ctn_size_type"));
                    seg50.setStockDays(rsCtn.getString("stock_days"));
                    seg50.setStockLocation(rsCtn.getString("stock_location"));
                    seg50.setInboundVessel(rsCtn.getString("in_vsl_name"));
                    seg50.setInboundVoyage(rsCtn.getString("inbound_voyage"));
                    seg50.setOutboundVessel(rsCtn.getString("out_vsl_name"));
                    seg50.setOutboundVoyage(rsCtn.getString("outbound_voyage"));
                    seg50.setBillNo(rsCtn.getString("bl_no"));
                    seg50.setDischargeTime(rsCtn.getString("discharge_time"));
                    seg50.setInGateTime(rsCtn.getString("in_yard_time"));
                    seg50.setVesselIE(rsCtn.getString("vessel_ie"));

                    seg5x.SEG50(seg50);

                    coedor.getSeg5xs().add(seg5x);

                }

                JtCoedor.SEG99 seg99 = coedor.new SEG99();
                coedor.SEG99(seg99);

                String logId = String.valueOf(EDIHelper.getLogSeq());

                if (!coedor.getSeg5xs().isEmpty()) {
                    String report = coedor.toString().replaceAll("null", "");
                    System.out.println(report);
                    String filename = buildFilename("coedor", customer, sender,
                            receiver, "STOCK REPORT", logId, "txt");
                    boolean ok = write(filename, report);
                    if (ok) {
                        logSend(logId, customer, "coedor", "JT", filename, report, false);
                    }
                } else {
                    logSend(logId, customer, "coedor", "JT", "", "", false);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            ExceptionNotifyTask.notify(ex, new String[]{customer, "coedor"});
        }

    }
}
