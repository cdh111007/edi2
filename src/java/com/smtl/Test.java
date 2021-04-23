package com.smtl;

import com.smtl.edi.util.DbUtil;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

/**
 *
 * @author Administrator
 */
public class Test {

    static void test() throws SQLException {

        Connection con = DbUtil.getConnection();

        String test = "{ call sp_tc2_rpt_opened_bil(?,?,?,?,?,?)}";

        try (CallableStatement call = con.prepareCall(test)) {

            call.setString(1, "2021-03-02");
            call.setString(2, "2021-03-03");
            call.setString(3, null);
            call.setString(4, null);
            call.setString(5, null);
            call.registerOutParameter(6, oracle.jdbc.OracleTypes.CURSOR);

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(6);

            while (rs.next()) {
                System.out.println(rs.getString("username") + "/" + rs.getString("itemtotal"));
            }

            con.close();

        }
    }

    public static void main(String[] args) throws SQLException, ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        Date date = sdf.parse("2020-10-01 00:01:10");
//
//        Calendar begin = DatetimeUtil.toCalendar(date);
//        Calendar end = DatetimeUtil.toCalendar(date);
//
//        begin.add(Calendar.SECOND, -3);
//        end.add(Calendar.SECOND, 3);
//
//        System.out.println(DatetimeUtil.format(begin, DatetimeUtil.YYYYMMDDHHMMSS));
//        System.out.println(DatetimeUtil.format(end, DatetimeUtil.YYYYMMDDHHMMSS));
        test();
//        codeco();
//        coarri();
//        String[] s = {"1", "2", "3"};

//        System.out.println(buildSqlInClause(s));
    }

}
