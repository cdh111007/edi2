package com.smtl.edi.core.task.mail;

import com.smtl.edi.util.DbUtil;
import com.smtl.edi.util.ExceptionUtil;
import com.smtl.edi.util.MailUtil;
import com.smtl.edi.util.PropertiesUtil;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author nm
 */
public class ExceptionNotifyTask {

    public static void main(String[] args) {
        notify(new RuntimeException("test"), new String[]{"1", "2"});
    }

    /**
     * 邮件通知：由于在该服务器中无法发送邮件，暂时通过记录异常信息到数据表中
     *
     * @param ex
     * @param extra
     */
    public static void notify(final Exception ex, final String... extra) {

        String sqlInsertErrLog = "insert into tc2_edi_err_log(cst_code,"
                + "msg_type,"
                + "err_info) "
                + "values(?,?,?)";

        Connection con = DbUtil.getConnection();

        try {

            if (extra != null && extra.length > 0) {
                MailUtil.sendPlainText(PropertiesUtil.getValue("mail.ex.subject"), PropertiesUtil.getValue("mail.to"), extra[0] + "***" + ExceptionUtil.getStackTraceAsString(ex));
            } else {
                MailUtil.sendPlainText(PropertiesUtil.getValue("mail.ex.subject"), PropertiesUtil.getValue("mail.to"), ExceptionUtil.getStackTraceAsString(ex));
            }

            DbUtil.setAutoCommit(con, false);

            PreparedStatement ps = con.prepareStatement(sqlInsertErrLog);

            if (extra != null && extra.length >= 2) {
                ps.setString(1, extra[0].toUpperCase());
                ps.setString(2, extra[1].toUpperCase());
            } else {
                ps.setString(1, "*");
                ps.setString(2, "*");
            }

            Reader reader = new StringReader(ExceptionUtil.getStackTraceAsString(ex));
            ps.setCharacterStream(3, reader, ExceptionUtil.getStackTraceAsString(ex).length());

            ps.executeUpdate();

            con.commit();
        } catch (SQLException ex1) {
            ex1.printStackTrace();
            DbUtil.rollback(con);
        } finally {
            DbUtil.setAutoCommit(con, true);
        }
    }
}
