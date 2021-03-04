package com.smtl.edi.core.facade.jt.coarri;

import com.smtl.edi.util.DbUtil;
import com.smtl.edi.util.ExceptionUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import com.smtl.edi.core.SQLQueryConstants;
import com.smtl.edi.core.excutor.coarri.CoarriExcutor;
import com.smtl.edi.core.task.mail.ExceptionNotifyTask;
import com.smtl.edi.util.PropertiesUtil;
import com.smtl.edi.util.StringUtil;
import java.util.Arrays;
import java.util.List;
import static com.EDIHelper.isSatisfiedActual;
import com.smtl.edi.vo.DateRange;

/**
 * 交通部标准格式装卸船报文入口，按照航次关闭时间/离泊时间发送，由视图参数控制
 *
 * @author nm
 */
public class JtCoarriFacade {

    final static Logger LOGGER = Logger.getLogger(JtCoarriFacade.class);

    /**
     * 根据时间周期创建交通部标准格式的装卸船报文
     *
     * @param customer
     * @param range
     * @param redo
     * @param ctnNos
     */
    public static void doHandle(String customer, DateRange range, boolean redo, String... ctnNos) {

        customer = customer.toUpperCase();

        //如果是实时报文，并且不是补发的
        if (isSatisfiedActual(customer) && !redo) {
            JtActCoarriFacade.doHandle(customer, range, redo);
            return;
        }

        String csts = PropertiesUtil.getValue("jt_coarri_std_cst_code");
        if (StringUtil.isEmpty(csts)) {
            csts = "";
        }
        List<String> stdJtCsts = Arrays.asList(csts.toUpperCase().split(","));

        LOGGER.info("读取使用标准交通部格式报文的用户..." + stdJtCsts);

        try {

            try (PreparedStatement psVslRef = DbUtil.preparedStatement(DbUtil.getConnection(),
                    SQLQueryConstants.SQL_VSL_REF_VW)) {

                psVslRef.setString(1, range.getBegin());
                psVslRef.setString(2, range.getEnd());

                ResultSet rsVsl = psVslRef.executeQuery();

                while (rsVsl.next()) {
                    if (stdJtCsts.contains(customer)) {
                        CoarriExcutor.jtStd(customer, range, rsVsl, false, redo, ctnNos);
                    } else {
                        CoarriExcutor.jt(customer, range, rsVsl, false, redo, ctnNos);
                    }
                }
            }

        } catch (SQLException ex) {
            LOGGER.info(ExceptionUtil.getStackTraceAsString(ex));
            ExceptionNotifyTask.notify(ex, new String[]{customer, "coarri"});
        }

    }

    /**
     * 根据船名航次创建交通部标准格式的装卸船报文
     *
     * @param customer
     * @param vslName
     * @param voyage
     * @param redo
     * @param ctnNos
     */
    public static void doHandle0(String customer, String vslName, String voyage, boolean redo, String... ctnNos) {

        customer = customer.toUpperCase();

        String csts = PropertiesUtil.getValue("jt_coarri_std_cst_code");
        if (StringUtil.isEmpty(csts)) {
            csts = "";
        }
        List<String> stdJtCsts = Arrays.asList(csts.toUpperCase().split(","));

        LOGGER.info("读取使用标准交通部格式报文的用户..." + stdJtCsts);

        try {

            try (PreparedStatement psVslRef = DbUtil.preparedStatement(DbUtil.getConnection(),
                    SQLQueryConstants.SQL_VSL_VOY_VW)) {

                psVslRef.setString(1, vslName);
                psVslRef.setString(2, voyage);

                ResultSet rsVsl = psVslRef.executeQuery();

                while (rsVsl.next()) {
                    if (stdJtCsts.contains(customer)) {
                        CoarriExcutor.jtStd0(customer, rsVsl, redo, ctnNos);
                    } else {
                        CoarriExcutor.jt0(customer, rsVsl, redo, ctnNos);
                    }
                }
            }

        } catch (SQLException ex) {
            LOGGER.info(ExceptionUtil.getStackTraceAsString(ex));
            ExceptionNotifyTask.notify(ex, new String[]{customer, "coarri"});
        }

    }
}
