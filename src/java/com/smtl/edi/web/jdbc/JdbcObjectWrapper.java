package com.smtl.edi.web.jdbc;

import com.smtl.edi.web.pojo.Coarri;
import com.smtl.edi.web.pojo.Codeco;
import com.smtl.edi.web.pojo.FtpSetting;
import com.smtl.edi.web.pojo.SenderSetting;
import com.smtl.edi.web.pojo.User;
import com.smtl.edi.web.pojo.UserCoperLink;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author nm
 */
public class JdbcObjectWrapper {

    /**
     * 用户设置
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public static User userWrapper(ResultSet rs) throws SQLException {
        User user = new User();
        user.setCode(rs.getString("cst_code"));
        user.setCcmail(rs.getString("cst_email_cc"));
        user.setMail(rs.getString("cst_email_to"));
        user.setName(rs.getString("cst_name"));
        user.setEdiType(rs.getString("cst_edi_type"));
        user.setCoarriFlag(rs.getString("cst_coarri_flag"));
        user.setCodecoFlag(rs.getString("cst_codeco_flag"));
        user.setCoedorFlag(rs.getString("cst_coedor_flag"));
        user.setCosecrFlag(rs.getString("cst_cosecr_flag"));
        user.setVesdepFlag(rs.getString("cst_vesdep_flag"));
        user.setActFlag(rs.getString("cst_act_flag"));
        return user;
    }

    /**
     * 用户代码和用户名
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public static User userCodeWrapper(ResultSet rs) throws SQLException {
        User user = new User();
        user.setCode(rs.getString("cst_code"));
        user.setName(rs.getString("cst_name"));
        return user;
    }

    /**
     * 客户和持箱人设置
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public static UserCoperLink userCoperLinkWrapper(ResultSet rs) throws SQLException {
        UserCoperLink link = new UserCoperLink();
        link.setCtnOperator(rs.getString("col_ctn_operator"));
        link.setCstCode(rs.getString("col_cst_code"));
        return link;
    }

    /**
     * 进出门报文调度日志
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public static Codeco codecoWrapper(ResultSet rs) throws SQLException {
        Codeco codeco = new Codeco();
        codeco.setCtnNo(rs.getString("ctn_no"));
        codeco.setCreateAt(rs.getTimestamp("create_at"));
        codeco.setCstCode(rs.getString("cst_code"));
        codeco.setInoutfg(rs.getString("msg_name"));
        codeco.setLogId(rs.getString("log_id"));
        codeco.setInOutMode(rs.getString("msg_name"));
        codeco.setInYardTime(rs.getString("in_quay_time"));
        codeco.setOutYardTime(rs.getString("out_quay_time"));
        codeco.setSendFlag(rs.getString("esl_send_flag"));
        codeco.setFilename(rs.getString("esl_file_path"));
        codeco.setMsgType(rs.getString("esl_msg_type"));
        return codeco;
    }

    /**
     * 装卸船报文调度日志
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public static Coarri coarriWrapper(ResultSet rs) throws SQLException {
        Coarri coarri = new Coarri();
        coarri.setCreatedAt(rs.getTimestamp("create_at"));
        coarri.setCstCode(rs.getString("cst_code"));
        coarri.setIefg("");
        coarri.setLdfg(rs.getString("msg_name"));
        coarri.setVslName(rs.getString("vsl_name"));
        coarri.setVoyage(rs.getString("voyage"));
        coarri.setLogId(rs.getString("log_id"));
        coarri.setSendFlag(rs.getString("esl_send_flag"));
        coarri.setFilename(rs.getString("esl_file_path"));
        return coarri;
    }

    /**
     * 客户报文发送方接收方代码设置
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public static SenderSetting senderWrapper(ResultSet rs) throws SQLException {
        SenderSetting setting = new SenderSetting();
        setting.setCstCode(rs.getString("cst_code"));
        setting.setCstReceiver(rs.getString("cst_receiver"));
        setting.setCstSender(rs.getString("cst_sender"));
        setting.setEdiCate(rs.getString("cst_edi_category"));
        return setting;
    }

    /**
     * 客户ftp参数设置
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public static FtpSetting ftpWrapper(ResultSet rs) throws SQLException {
        FtpSetting setting = new FtpSetting();
        setting.setCstCode(rs.getString("cst_code"));
        setting.setFtpIP(rs.getString("cst_ftp"));
        setting.setFtpPath(rs.getString("cst_ftp_path"));
        setting.setFtpPort(rs.getString("cst_ftp_port"));
        setting.setFtpPwd(rs.getString("cst_ftp_pwd"));
        setting.setFtpUser(rs.getString("cst_ftp_user"));
        setting.setEdiCate(rs.getString("cst_edi_category"));
        return setting;
    }
}
