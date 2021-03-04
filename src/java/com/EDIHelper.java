package com;

import static com.smtl.edi.core.SQLQueryConstants.SQL_ACT_COARRI_CUSTOMER_CODE;
import static com.smtl.edi.core.SQLQueryConstants.SQL_COARRI_FLAG;
import static com.smtl.edi.core.SQLQueryConstants.SQL_CODECO_FLAG;
import static com.smtl.edi.core.SQLQueryConstants.SQL_COEDOR_FLAG;
import static com.smtl.edi.core.SQLQueryConstants.SQL_COSECR_FLAG;
import static com.smtl.edi.core.SQLQueryConstants.SQL_CUSTOMER_INFO;
import com.smtl.edi.core.log.MsgCtnDetailLog;
import com.smtl.edi.util.DatetimeUtil;
import com.smtl.edi.util.DbUtil;
import com.smtl.edi.util.ExceptionUtil;
import com.smtl.edi.util.ValidationUtil;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import static com.smtl.edi.core.SQLQueryConstants.SQL_CST_TOTAL_CTNS;
import static com.smtl.edi.core.SQLQueryConstants.SQL_CST_TOTAL_TEU;
import static com.smtl.edi.core.SQLQueryConstants.SQL_CST_TOTAL_WT;
import static com.smtl.edi.core.SQLQueryConstants.SQL_EDI_SEQ_NEXT_VAL;
import static com.smtl.edi.core.SQLQueryConstants.SQL_FUN_GET_COS_VSL_CODE;
import static com.smtl.edi.core.SQLQueryConstants.SQL_GET_ALL_CUSTOMER_CODE;
import static com.smtl.edi.core.SQLQueryConstants.SQL_GET_ALL_CUSTOMER_CODE_EXC_XXZX;
import static com.smtl.edi.core.SQLQueryConstants.SQL_INSERT_CTN_DETAILS;
import static com.smtl.edi.core.SQLQueryConstants.SQL_INSERT_SEND_LOG;
import static com.smtl.edi.core.SQLQueryConstants.SQL_UPDATE_SEND_LOG;
import static com.smtl.edi.core.SQLQueryConstants.SQL_VESDEP_FLAG;
import com.smtl.edi.core.task.mail.ExceptionNotifyTask;
import com.smtl.edi.util.PropertiesUtil;
import com.smtl.edi.util.StringUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 * EDI报文辅助类
 *
 * @author nm
 */
public class EDIHelper {

    private static final Logger LOGGER = Logger.getLogger(EDIHelper.class);
    public static final String SEGMENT_TERMINATOR = "'" + System.lineSeparator();

    /**
     * 检查发送跨度是否超过N天，超过的话提醒手工发送
     *
     * @param begin 发送开始时间
     * @param N 天数
     * @return
     */
    public static boolean checkExceedNDaysFromBegin(String begin, int... N) {
        int days = Integer.valueOf(PropertiesUtil.getValue("check_days_when_create"));
        if (N != null && N.length > 0) {
            days = N[0];
        }
        String end = DatetimeUtil.now(DatetimeUtil.YYYYMMDDHHMMSS);
        if (DatetimeUtil.daysToNow(begin) > days) {
            String info = "发送跨度太长（超过" + days + "天），请采用手工方式补发" + "\t" + begin + " - " + end;
            print(info);
            return true;
        }

        return false;
    }

    /**
     * 该客户是否满足实时发送报文的设定
     *
     * @param customer
     * @return
     */
    public static boolean isSatisfiedActual(String customer) {
        try {
            ResultSet rs = PS_ACT_CUSTOMER_CODE.executeQuery();
            List<String> customers = new LinkedList<>();
            while (rs.next()) {
                customers.add(rs.getString("cst_code"));
            }
            if (StringUtil.isNotEmpty(customer)) {
                return customers.contains(customer.toUpperCase());
            } else {
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param info
     */
    public static void print(String info) {
        System.out.println(info);
        System.out.println();
        LOGGER.info(info);
    }

    /**
     * 根据报文类型获取客户代码清单
     *
     * @param ediType UN/JT
     * @return
     */
    public static List<String> getCustomerCodesByEDIType(String ediType) {

        List<String> customers = new ArrayList<>();

        try {
            PS_CUSTOMER_CODE.setString(1, ediType.toUpperCase());
            ResultSet rs = PS_CUSTOMER_CODE.executeQuery();
            while (rs.next()) {
                customers.add(rs.getString("cst_code"));
            }
            return customers;
        } catch (SQLException ex) {
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        }

        return Collections.EMPTY_LIST;
    }

    /**
     * 根据报文类型获取客户代码清单
     *
     * @return
     */
    public static List<String> getAllCustomerCodes() {

        List<String> customers = new ArrayList<>();

        try {
            ResultSet rs = PS_ALL_CUSTOMER_CODE.executeQuery();
            while (rs.next()) {
                customers.add(rs.getString("cst_code"));
            }
            return customers;
        } catch (SQLException ex) {
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        }

        return Collections.EMPTY_LIST;
    }

    /**
     * 获取日志SEQ
     *
     * @param ps
     * @return
     */
    private static long getLogSeq(PreparedStatement ps) {
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("seq");
            }
        } catch (SQLException ex) {
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
            throw new RuntimeException(ex);
        }

        throw new RuntimeException("获取日志文件序列异常");
    }

    /**
     * 获取日志SEQ
     *
     * @return
     */
    public static long getLogSeq() {
        return getLogSeq(PS_LOG_SEQ);
    }

    /**
     * 记录发送的客户报文日志
     *
     * @param logId
     * @param customer
     * @param msgType 报文类型 CODECO COARRI
     * @param ediType JT UN
     * @param filePath 报文的绝对路径
     * @param msgData 报文内容
     * @param redo
     */
    public static void logSend(String logId, String customer, String msgType,
            String ediType, String filePath, String msgData, boolean redo) {

        Connection con = null;
        PreparedStatement ps = null;

        try {

            con = DbUtil.getConnection();
            con.setAutoCommit(false);

            //获取上一个id
            String id = getLastLogId(customer.toUpperCase(), msgType.toUpperCase(), ediType.toUpperCase());

            if (StringUtils.isNotBlank(msgData) || "*".equals(id)) {
                ps = con.prepareStatement(SQL_INSERT_SEND_LOG);
                ps.setString(1, logId);
                ps.setString(2, customer.toUpperCase());
                ps.setString(3, msgType.toUpperCase());
                ps.setString(4, ediType.toUpperCase());
                ps.setString(5, filePath);
                Reader reader = new StringReader(msgData);
                ps.setCharacterStream(6, reader, msgData.length());
                ps.setInt(7, redo ? 1 : 0);
            } else {
                //获取上一个create time
                String create = getLastCreateAtTime(customer.toUpperCase(), msgType.toUpperCase(), ediType.toUpperCase());
                LOGGER.info("Last..." + id + " [ " + create + " ] " + customer.toUpperCase() + " - "
                        + msgType.toUpperCase() + " - " + ediType.toUpperCase());
                ps = con.prepareStatement(SQL_UPDATE_SEND_LOG);
                ps.setString(1, id);
            }

            ps.executeUpdate();

            con.commit();

            if (StringUtils.isNotBlank(msgData)) {
                LOGGER.info(customer.toUpperCase() + " - " + msgType.toUpperCase() + " - " + filePath);
            }

        } catch (SQLException ex) {
            DbUtil.rollback(con);
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        } finally {
            DbUtil.setAutoCommit(con, true);
            DbUtil.close(ps);
        }

    }

    /**
     * 获取客户创建报文的最近时间，如果没有，则从前一天的当前时间开始
     *
     * @param customer
     * @param msgType CODECO COARRI
     * @param ediType JT UN
     * @return
     */
    public static String getLastCreateAtTime0(String customer, String msgType, String ediType) {

        try {

            PS_LAST_CREATE_TIME.setString(1, customer.toUpperCase());
            PS_LAST_CREATE_TIME.setString(2, msgType.toUpperCase());
            PS_LAST_CREATE_TIME.setString(3, ediType.toUpperCase());

            ResultSet rs = PS_LAST_CREATE_TIME.executeQuery();
            if (rs.next()) {
                LOGGER.info("找到" + customer + "-" + msgType + "上次的报文创建时间：" + DatetimeUtil.format(rs.getString("last_time"), DatetimeUtil.YYYY_MM_DD_HH_MM_SS));
                return rs.getString("last_time");
            } else {
                LOGGER.info("没有找到" + customer + "-" + msgType + "上次的报文创建时间，取前一天时间为开始时间");
                return DatetimeUtil.format(DatetimeUtil.yesterday(), DatetimeUtil.YYYYMMDDHHMMSS);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        }

        return DatetimeUtil.format(DatetimeUtil.yesterday(), DatetimeUtil.YYYYMMDDHHMMSS);
    }

    /**
     * 获取客户创建报文的最近时间，如果没有，则从前一天的当前时间开始
     *
     * @param customer
     * @param msgType CODECO COARRI
     * @param ediType JT UN
     * @return
     */
    public static String getLastCreateTimeWithIntervalSeconds(String customer, String msgType, String ediType) {

        try {

            PS_LAST_CREATE_TIME_WITH_INTERVAL_SECONDS.setString(1, customer.toUpperCase());
            PS_LAST_CREATE_TIME_WITH_INTERVAL_SECONDS.setString(2, msgType.toUpperCase());
            PS_LAST_CREATE_TIME_WITH_INTERVAL_SECONDS.setString(3, ediType.toUpperCase());

            ResultSet rs = PS_LAST_CREATE_TIME_WITH_INTERVAL_SECONDS.executeQuery();
            if (rs.next()) {
                LOGGER.info("找到" + customer + "-" + msgType + "上次的报文创建时间：" + DatetimeUtil.format(rs.getString("last_time"), DatetimeUtil.YYYY_MM_DD_HH_MM_SS));
                return rs.getString("last_time");
            } else {
                LOGGER.info("没有找到" + customer + "-" + msgType + "上次的报文创建时间，取前一天时间为开始时间");
                return DatetimeUtil.format(DatetimeUtil.yesterday(), DatetimeUtil.YYYYMMDDHHMMSS);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        }

        return DatetimeUtil.format(DatetimeUtil.yesterday(), DatetimeUtil.YYYYMMDDHHMMSS);
    }

    /**
     * 将报文写进文件
     *
     * @param filename
     * @param msgData
     * @return
     */
    public static boolean write(String filename, String msgData) {

        if (StringUtil.isEmpty(filename)) {
            throw new RuntimeException(filename + "文件名不能为空");
        }

        if (StringUtil.isEmpty(msgData)) {
            throw new RuntimeException("报文数据不能为空");
        }

        String data = msgData.replaceAll("null", "");

        if (!data.isEmpty()) {
            try {
                FileUtils.writeStringToFile(new File(filename), data);
                return true;
            } catch (IOException ex) {
                LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
                ExceptionNotifyTask.notify(ex);
                return false;
            }
        }

        return false;
    }

    /**
     * 构造报文名
     *
     * @param msgType codeco/coarri
     * @param customer
     * @param sender
     * @param receiver
     * @param inOut
     * @param logId
     * @param ext
     * @return
     */
    public static String buildFilename(String msgType, String customer, String sender,
            String receiver, String inOut, String logId, String ext) {
        //默认存在c盘，如果有d盘的话就转存到d盘
        String disk = "c";
        for (FileStore store : FileSystems.getDefault().getFileStores()) {
            if (store.toString().toLowerCase().contains("d")) {
                disk = "d";
                break;
            }
        }
        disk = disk + ":";
        if (StringUtil.isEmpty(inOut)) {
            inOut = "ZZZ";
        }
        String path = disk + "\\tc_edi\\" + customer + "\\" + DatetimeUtil.now("yyyyMMdd") + "\\";
        return (path + receiver + "-" + sender + "-" + msgType + "-" + inOut + "-" + logId + "." + ext).toUpperCase();
    }

    /**
     * 将报文的箱明细记录到数据库
     *
     * @param logId
     * @param logs
     */
    public static void logMsgDetails(long logId, List<MsgCtnDetailLog> logs) {

        try {

            if (!ValidationUtil.isValid(logs)) {
                return;
            }

            Connection conn = DbUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_INSERT_CTN_DETAILS);

            int i = 0;

            for (MsgCtnDetailLog log : logs) {

                ps.setString(1, log.getCustomer());
                ps.setString(2, log.getMsgType());
                ps.setString(3, log.getCtnNo());
                ps.setString(4, log.getVslName());
                ps.setString(5, log.getVoyage());
                ps.setString(6, log.getVslRef());
                ps.setTimestamp(7, log.getInQuayTime());
                ps.setTimestamp(8, log.getOutQuayTime());
                ps.setString(9, log.getMsgName());
                ps.setLong(10, logId);

                ps.addBatch();

                i++;

                if (i % 50 == 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }

            }

            ps.executeBatch();

            DbUtil.close(ps);
        } catch (SQLException ex) {
            LOGGER.info(ExceptionUtil.getStackTraceAsString(ex));
        }
    }

    /**
     * 获取该航次下的箱总数
     *
     * @param customer
     * @param vslRef
     * @param voyage
     * @param ieFlag
     * @return
     */
    public static String getTotalCtns(String customer, String vslRef, String voyage, String ieFlag) {
        try {

            PS_TOTAL_CTNS.setString(1, customer);
            PS_TOTAL_CTNS.setString(2, vslRef);
            PS_TOTAL_CTNS.setString(3, voyage);
            PS_TOTAL_CTNS.setString(4, ieFlag);

            ResultSet rs = PS_TOTAL_CTNS.executeQuery();
            if (rs.next()) {
                return rs.getString("total_ctns");
            } else {
                return "-1";
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.info(ExceptionUtil.getStackTraceAsString(ex));
        }

        return "-1";
    }

    /**
     * 获取该航次下的箱总TEU
     *
     * @param customer
     * @param vslRef
     * @param voyage
     * @param ieFlag
     * @return
     */
    public static String getTotalTEU(String customer, String vslRef, String voyage, String ieFlag) {

        try {

            PS_TOTAL_TEU.setString(1, customer);
            PS_TOTAL_TEU.setString(2, vslRef);
            PS_TOTAL_TEU.setString(3, voyage);
            PS_TOTAL_TEU.setString(4, ieFlag);

            ResultSet rs = PS_TOTAL_TEU.executeQuery();
            if (rs.next()) {
                return rs.getString("total_teu");
            } else {
                return "-1";
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.info(ExceptionUtil.getStackTraceAsString(ex));
        }

        return "-1";
    }

    /**
     * 获取航次下的箱总重
     *
     * @param customer
     * @param vslRef
     * @param voyage
     * @param ieFlag
     * @return
     */
    public static String getTotalWeight(String customer, String vslRef, String voyage, String ieFlag) {

        try {

            PS_TOTAL_WT.setString(1, customer);
            PS_TOTAL_WT.setString(2, vslRef);
            PS_TOTAL_WT.setString(3, voyage);
            PS_TOTAL_WT.setString(4, ieFlag);

            ResultSet rs = PS_TOTAL_WT.executeQuery();
            if (rs.next()) {
                return rs.getString("total_weight");
            } else {
                return "-1";
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.info(ExceptionUtil.getStackTraceAsString(ex));
        }

        return "-1";
    }

    /**
     * 获取系统船代码对应的客户船代码，目前只有COS需要
     *
     * @param srcCode
     * @return
     */
    public static String getVesselCode(String srcCode) {
        try {
            PS_VESSEL_CODE.setString(1, srcCode);
            ResultSet rs = PS_VESSEL_CODE.executeQuery();
            if (rs.next()) {
                return rs.getString("vessel_code");
            }
            return srcCode;
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.info(ExceptionUtil.getStackTraceAsString(ex));
        }

        return "Unknow";
    }

    public static final PreparedStatement PS_TOTAL_CTNS = DbUtil.preparedStatement(DbUtil.getConnection(), SQL_CST_TOTAL_CTNS);
    public static final PreparedStatement PS_TOTAL_TEU = DbUtil.preparedStatement(DbUtil.getConnection(), SQL_CST_TOTAL_TEU);
    public static final PreparedStatement PS_TOTAL_WT = DbUtil.preparedStatement(DbUtil.getConnection(), SQL_CST_TOTAL_WT);

    public static final PreparedStatement PS_VESSEL_CODE = DbUtil.preparedStatement(DbUtil.getConnection(), SQL_FUN_GET_COS_VSL_CODE);

    public static final PreparedStatement PS_LOG_SEQ = DbUtil.preparedStatement(DbUtil.getConnection(), SQL_EDI_SEQ_NEXT_VAL);

    public static final PreparedStatement PS_CODECO_CUSTOMER = DbUtil.preparedStatement(DbUtil.getConnection(), SQL_CUSTOMER_INFO + SQL_CODECO_FLAG);
    public static final PreparedStatement PS_COARRI_CUSTOMER = DbUtil.preparedStatement(DbUtil.getConnection(), SQL_CUSTOMER_INFO + SQL_COARRI_FLAG);
    public static final PreparedStatement PS_COEDOR_CUSTOMER = DbUtil.preparedStatement(DbUtil.getConnection(), SQL_CUSTOMER_INFO + SQL_COEDOR_FLAG);
    public static final PreparedStatement PS_COSECR_CUSTOMER = DbUtil.preparedStatement(DbUtil.getConnection(), SQL_CUSTOMER_INFO + SQL_COSECR_FLAG);
    public static final PreparedStatement PS_VESEDP_CUSTOMER = DbUtil.preparedStatement(DbUtil.getConnection(), SQL_CUSTOMER_INFO + SQL_VESDEP_FLAG);

    //目前只有华航物流使用，因为是第二天发送前一天的数据，不是实时，所以不需要时间前推
    public static final PreparedStatement PS_LAST_CREATE_TIME = getLastCreateTimePreparedStatement();
    //其他使用时间前推5分钟
    public static final PreparedStatement PS_LAST_CREATE_TIME_WITH_INTERVAL_SECONDS = getLastCreateTimePreparedStatement(300);

    public static final PreparedStatement PS_CUSTOMER_CODE = getCustomerCodePreparedStatement();
    public static final PreparedStatement PS_ALL_CUSTOMER_CODE = getAllCustomerCodePreparedStatement();
    //实时发送报文的客户
    public static final PreparedStatement PS_ACT_CUSTOMER_CODE = getActCustomerCodePreparedStatement();

    private static PreparedStatement getActCustomerCodePreparedStatement() {

        try {
            return DbUtil.getConnection().prepareStatement(SQL_ACT_COARRI_CUSTOMER_CODE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        }

        return null;
    }

    /**
     * 获取客户代码
     *
     * @return
     */
    private static PreparedStatement getCustomerCodePreparedStatement() {

        try {
            return DbUtil.getConnection().prepareStatement(SQL_GET_ALL_CUSTOMER_CODE_EXC_XXZX);
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        }
        return null;
    }

    /**
     * 获取客户代码
     *
     * @return
     */
    private static PreparedStatement getAllCustomerCodePreparedStatement() {

        try {
            return DbUtil.getConnection().prepareStatement(SQL_GET_ALL_CUSTOMER_CODE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        }

        return null;
    }

    /**
     *
     *
     * @param seconds 推前几秒钟
     * @return
     */
    private static PreparedStatement getLastCreateTimePreparedStatement(Integer... seconds) {

        int t = 10;
        if (ValidationUtil.isValid(seconds)) {
            t = seconds[0];
        }

        String sqlLastTimeLog = "select to_char(a.esl_create_at - " + t + "/ (60 * 60 * 24),'YYYYMMDDHH24MISS') as last_time  "
                + "from tc2_edi_send_log a "
                + "left join tc2_edi_cust_info b on a.esl_cst_code = b.cst_code "
                + "where (a.esl_cst_code)=? "
                + "and (a.esl_msg_type)=? "
                + "and (a.esl_edi_type)=? "
                + "and a.esl_create_at is not null "
                + "and a.esl_redo_flag=0 "
                + "and (case a.esl_msg_type "
                + "   when 'COARRI' then "
                + "    b.cst_coarri_flag "
                + "   else "
                + "    b.cst_codeco_flag "
                + " end) = 1"
                + "order by a.esl_create_at desc ";

        try {
            return DbUtil.getConnection().prepareStatement(sqlLastTimeLog);
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        }

        return null;
    }

    public static void main(String[] args) {
        System.out.println(getLastLogId("SNL", "COARRI", "UN"));
        System.out.println(getLastCreateAtTime("SNL", "COARRI", "UN"));
        System.out.println(Integer.parseInt("FFFF", 16));
    }

    /**
     *
     * @param customer
     * @param msgType
     * @param ediType
     * @return
     */
    public static String getLastLogId(String customer, String msgType, String ediType) {
        Collection<String> vals = getLastLogIdAndCreateTimeMap(customer, msgType, ediType).keySet();
        if (vals != null && !vals.isEmpty()) {
            String rtn = (String) new ArrayList(vals).get(0);
            return rtn == null ? "*" : rtn;
        }
        return "*";
    }

    /**
     *
     * @param customer
     * @param msgType
     * @param ediType
     * @return
     */
    public static String getLastCreateAtTime(String customer, String msgType, String ediType) {
        Collection<String> vals = getLastLogIdAndCreateTimeMap(customer, msgType, ediType).values();
        if (vals != null && !vals.isEmpty()) {
            String rtn = (String) new ArrayList(vals).get(0);
            return rtn == null ? "*" : rtn;
        }
        return "*";
    }

    /**
     *
     * @param customer
     * @param msgType
     * @param ediType
     * @return
     */
    private static Map<String, String> getLastLogIdAndCreateTimeMap(String customer, String msgType, String ediType) {
        String sql = "select max(esl_log_id) as log_id,"
                + "max(to_char(esl_create_at,'YYYY-MM-DD HH24:MI:SS')) as create_at "
                + "from tc2_edi_send_log "
                + "where esl_cst_code=? "
                + "and esl_msg_type=? "
                + "and esl_edi_type=? "
                + "and esl_redo_flag=0 "
                + "and esl_create_at is not null";

        PreparedStatement ps = null;
        try {
            ps = DbUtil.getConnection().prepareStatement(sql);
            ps.setString(1, customer);
            ps.setString(2, msgType);
            ps.setString(3, ediType);

            ResultSet rs = ps.executeQuery();

            Map<String, String> pair = new HashMap<>();

            if (rs.next()) {
                pair.put(rs.getString("log_id"), rs.getString("create_at"));
                return pair;
            }

            pair.put("*", "*");

            return pair;

        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        } finally {
            DbUtil.close(ps);
        }

        return Collections.EMPTY_MAP;
    }

    /**
     *
     * @param info
     * @param clazz
     */
    public static void print(String info, Class clazz) {
        Logger LOG = Logger.getLogger(clazz.getClass());
        System.out.println(info);
        System.out.println();
        LOG.info(info);
    }
}
