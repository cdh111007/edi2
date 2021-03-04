package com.smtl;

import com.smtl.edi.core.excutor.codeco.DVCodecoExcutor;
import com.smtl.edi.core.facade.un.coarri.UnCoarriFacade;
import com.smtl.edi.core.facade.un.coarri.UnActCoarriFacade;
import com.smtl.edi.core.excutor.codeco.UnCodecoExcutor;
import com.smtl.edi.core.facade.jt.coarri.JtCoarriFacade;
import com.smtl.edi.core.facade.jt.coarri.JtActCoarriFacade;
import com.smtl.edi.core.excutor.coarri.XxzxCoarriExcutor;
import com.smtl.edi.core.excutor.codeco.JtCodecoExcutor;
import com.smtl.edi.util.DatetimeUtil;
import com.smtl.edi.util.DbUtil;
import com.smtl.edi.util.ExceptionUtil;
import com.smtl.edi.util.ValidationUtil;
import com.smtl.edi.vo.DateRange;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 * 补发报文
 *
 * @author nm
 */
public class EDIRedo {

    private static final Logger LOGGER = Logger.getLogger(EDIRedo.class);

    public static void main(String[] args) {
 
        DateRange range;
        for (int i = 0; i < 40; i++) {
            System.out.println(DatetimeUtil.format(DatetimeUtil.daysAgo(i + 1), DatetimeUtil.YYYYMMDDHHMMSS) + " - " + DatetimeUtil.format(DatetimeUtil.daysAgo(i), DatetimeUtil.YYYYMMDDHHMMSS));
            range = new DateRange();
            range.setBegin(DatetimeUtil.format(DatetimeUtil.daysAgo(i + 1), DatetimeUtil.YYYYMMDDHHMMSS));
            range.setEnd(DatetimeUtil.format(DatetimeUtil.daysAgo(i), DatetimeUtil.YYYYMMDDHHMMSS));
            codeco("HAS1", range);
        }
 
    }

    final static String SQL_CST_EDI_TYPE = "select t.cst_edi_type from tc2_edi_cust_info t where t.cst_code = ?";

    /**
     * 根据客户代码，船名航次重发报文，前提要关闭航次/离泊后 数据可能会重复发
     *
     * @param customer
     * @param vslName
     * @param voyage
     * @param ctnNos
     * @return
     */
    public static String coarriByVslNameAndVoyage(String customer, String vslName, String voyage, String... ctnNos) {

        try {

            customer = customer.toUpperCase();

            String timeRange = "select t.begin_time, t.end_time "
                    + "  from tc2_edi_get_close_time_gap_vm t "
                    + " where t.vsl_name = ? "
                    + "       and t.voyage = ?";

            Connection con = DbUtil.getConnection();

            PreparedStatement psTimeRange = con.prepareStatement(timeRange);
            psTimeRange.setString(1, vslName);
            psTimeRange.setString(2, voyage);

            PreparedStatement ps = con.prepareStatement(SQL_CST_EDI_TYPE);
            ps.setString(1, customer);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String ediType = rs.getString("cst_edi_type");

                if ("jt".equalsIgnoreCase(ediType)) {

                    ResultSet rsTimeRange = psTimeRange.executeQuery();

                    while (rsTimeRange.next()) {
                        if (ValidationUtil.isValid(ctnNos)) {
                            JtCoarriFacade.doHandle(customer, new DateRange(rsTimeRange.getString("begin_time"),
                                    rsTimeRange.getString("end_time")), true, ctnNos);
                        } else {
                            if ("xxzx".equalsIgnoreCase(customer)) {
                                XxzxCoarriExcutor.doHandle(new DateRange(rsTimeRange.getString("begin_time"),
                                        rsTimeRange.getString("end_time")), true);
                            } else {
                                JtCoarriFacade.doHandle(customer, new DateRange(rsTimeRange.getString("begin_time"),
                                        rsTimeRange.getString("end_time")), true);
                            }
                        }
                    }
                }

                if ("un".equalsIgnoreCase(ediType)) {

                    ResultSet rsTimeRange = psTimeRange.executeQuery();

                    while (rsTimeRange.next()) {
                        if (ValidationUtil.isValid(ctnNos)) {
                            UnCoarriFacade.doHandle(customer, new DateRange(rsTimeRange.getString("begin_time"),
                                    rsTimeRange.getString("end_time")), true, ctnNos);
                        } else {
                            UnCoarriFacade.doHandle(customer, new DateRange(rsTimeRange.getString("begin_time"),
                                    rsTimeRange.getString("end_time")), true);
                        }
                    }
                }
            }

            DbUtil.close(ps);
            DbUtil.close(psTimeRange);

        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            return "ERR";
        }

        return "OK";
    }

    /**
     * 根据客户代码，船名航次重发报文，前提要关闭航次/离泊后 数据不会重复发
     *
     * @param customer
     * @param vslName
     * @param voyage
     * @param ctnNos
     * @return
     */
    public static String coarriByVslNameAndVoyage0(String customer, String vslName, String voyage, String... ctnNos) {

        try {

            customer = customer.toUpperCase();

            Connection con = DbUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(SQL_CST_EDI_TYPE);
            ps.setString(1, customer);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String ediType = rs.getString("cst_edi_type");

                if ("jt".equalsIgnoreCase(ediType)) {

                    if (ValidationUtil.isValid(ctnNos)) {
                        JtCoarriFacade.doHandle0(customer, vslName, voyage, true, ctnNos);
                    } else {
                        if ("xxzx".equalsIgnoreCase(customer)) {
                            XxzxCoarriExcutor.doHandle0(vslName, voyage, true);
                        } else {
                            JtCoarriFacade.doHandle0(customer, vslName, voyage, true);
                        }
                    }

                }

                if ("un".equalsIgnoreCase(ediType)) {

                    if (ValidationUtil.isValid(ctnNos)) {
                        UnCoarriFacade.doHandle0(customer, vslName, voyage, true, ctnNos);
                    } else {
                        UnCoarriFacade.doHandle0(customer, vslName, voyage, true);
                    }

                }
            }

            DbUtil.close(ps);

        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            return "ERR";
        }

        return "OK";
    }

    /**
     * 根据客户代码和时间间隔发送报文，在该时间段内箱子发生的装卸都会发送，不是根据该时间段内的航次关闭/离泊时间来发（清洁数据报文除外）
     *
     * @param customer
     * @param range
     * @param ctnNos
     * @return
     */
    public static String coarriByActTime(String customer, DateRange range, String... ctnNos) {

        try {

            customer = customer.toUpperCase();

            Connection con = DbUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(SQL_CST_EDI_TYPE);
            ps.setString(1, customer);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String ediType = rs.getString("cst_edi_type");

                if ("jt".equalsIgnoreCase(ediType)) {

                    if (ValidationUtil.isValid(ctnNos)) {
                        JtActCoarriFacade.doHandle(customer, range, true, ctnNos);
                    } else {
                        if ("xxzx".equalsIgnoreCase(customer)) {
                            XxzxCoarriExcutor.doHandle(range, true);
                        } else {
                            JtActCoarriFacade.doHandle(customer, range, true);
                        }

                    }

                }

                if ("un".equalsIgnoreCase(ediType)) {

                    if (ValidationUtil.isValid(ctnNos)) {
                        UnActCoarriFacade.doHandle(customer, range, true, ctnNos);
                    } else {
                        UnActCoarriFacade.doHandle(customer, range, true);
                    }
                }

            }

            DbUtil.close(ps);

        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            return "ERR";
        }

        return "OK";
    }

    /**
     * 根据客户代码，时间段和箱号重发
     *
     * @param customer
     * @param range
     * @param ctnNos
     * @return
     */
    public static String codeco(String customer, DateRange range, String... ctnNos) {

        try {

            customer = customer.toUpperCase();

            Connection con = DbUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(SQL_CST_EDI_TYPE);
            ps.setString(1, customer);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String ediType = rs.getString("cst_edi_type");

                if ("jt".equalsIgnoreCase(ediType)) {
                    if (ValidationUtil.isValid(ctnNos)) {
                        JtCodecoExcutor.doHandle(customer, range, true, ctnNos);
                    } else {
                        JtCodecoExcutor.doHandle(customer, range, true);
                    }
                }

                if ("un".equalsIgnoreCase(ediType)) {
                    if (ValidationUtil.isValid(ctnNos)) {
                        UnCodecoExcutor.doHandle(customer, range, true, ctnNos);
                    } else {
                        UnCodecoExcutor.doHandle(customer, range, true);
                    }
                }

            }

            DbUtil.close(ps);

        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            return "ERR";
        }

        return "OK";
    }

    /**
     * 根据客户代码，时间段和箱号重发拆装箱
     *
     * @param customer
     * @param range
     * @param ctnNos
     * @return
     */
    public static String dv(String customer, DateRange range, String... ctnNos) {

        try {

            customer = customer.toUpperCase();

            Connection con = DbUtil.getConnection();

            PreparedStatement ps = con.prepareStatement(SQL_CST_EDI_TYPE);
            ps.setString(1, customer);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String ediType = rs.getString("cst_edi_type");

                if ("jt".equalsIgnoreCase(ediType)) {
                    if (ValidationUtil.isValid(ctnNos)) {
                        DVCodecoExcutor.jt(customer, range, true, ctnNos);
                    } else {
                        DVCodecoExcutor.jt(customer, range, true);
                    }
                }

                if ("un".equalsIgnoreCase(ediType)) {
                    if (ValidationUtil.isValid(ctnNos)) {
                        DVCodecoExcutor.un(customer, range, true, ctnNos);
                    } else {
                        DVCodecoExcutor.un(customer, range, true);
                    }
                }

            }

            DbUtil.close(ps);

        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            return "ERR";
        }

        return "OK";
    }

    /**
     * 补发前天遗漏的清洁数据
     */
    public static void xxzxYesterdayLostCoarri() {

        Connection con = DbUtil.getConnection();

        String diff = "{ call sp_tc2_edi_vsl_voy_day_diff(?,?)}";

        try (CallableStatement call = con.prepareCall(diff)) {

            LOGGER.info((1) + "------------" + DatetimeUtil.format(DatetimeUtil.daysAgo(1)) + "----------------");

            call.setInt(1, 1);
            call.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(2);

            while (rs.next()) {
                System.out.println(rs.getString("vessel_namec") + "/" + rs.getString("voyage"));
                EDIRedo.coarriByVslNameAndVoyage0("xxzx", rs.getString("vessel_namec"), rs.getString("voyage"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        }
    }
}
