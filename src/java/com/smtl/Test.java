package com.smtl;

import com.smtl.edi.util.DatetimeUtil;
import com.smtl.edi.util.DbUtil;
import static com.smtl.edi.util.StringUtil.buildSqlInClause;
import com.smtl.edi.vo.VesselVoyage;
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

        String test = "{ call sp_tc2_edi_vsl_voy_day_diff(?,?)}";

        try (CallableStatement call = con.prepareCall(test)) {

            for (int i = 1; i < 3; i++) {

                System.out.println((i) + "------------" + DatetimeUtil.format(DatetimeUtil.daysAgo(i)) + "----------------");

                call.setInt(1, i);
                call.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);

                call.execute();

                ResultSet rs = (ResultSet) call.getObject(2);

                while (rs.next()) {
                    System.out.println(rs.getString("vessel_namec") + "/" + rs.getString("voyage"));
                    EDIRedo.coarriByVslNameAndVoyage0("xxzx", new VesselVoyage(rs.getString("vessel_namec"), rs.getString("voyage")));
//                    List<String> users = getCustomerCodes("un");
//                    users.addAll(getCustomerCodes("jt"));
//                    users.add("xxzx");
//                    //实时发送报文的不按船名航次发
//                    for (String user : users) {
//                        if (isSatisfiedAct(user)) {
//                            continue;
//                        }
//                        System.out.println(user);
//                        EDIRedo.coarriByVslNameAndVoyage0(user, rs.getString("vessel_namec"), rs.getString("voyage"));
//                    }
                }

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
//        test();
//        codeco();
//        coarri();
        String[] s = {"1", "2", "3"};

        System.out.println(buildSqlInClause(s));

    }

}
