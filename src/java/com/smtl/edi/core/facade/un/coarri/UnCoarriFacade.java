package com.smtl.edi.core.facade.un.coarri;

import com.smtl.edi.util.DbUtil;
import com.smtl.edi.util.ExceptionUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.smtl.edi.core.SQLQueryConstants;
import com.smtl.edi.core.excutor.coarri.CoarriExcutor;
import com.smtl.edi.core.task.mail.ExceptionNotifyTask;
import org.apache.log4j.Logger;
import static com.EDIHelper.isSatisfiedActual;
import com.smtl.edi.vo.DateRange;
import com.smtl.edi.vo.VesselVoyage;

/**
 * 根据航次关闭时间/或者离泊时间发送报文，通过视图参数控制
 *
 * @author nm
 */
public class UnCoarriFacade {

    final static Logger LOGGER = Logger.getLogger(UnCoarriFacade.class);

    /**
     * 根据时间周期创建联合国格式装卸船报文
     *
     * @param customer
     * @param range
     * @param redo
     * @param ctnNos
     */
    public static void prepareAndProcess(String customer, DateRange range, boolean redo, String... ctnNos) {

        //如果是实时报文，并且不是补发的
        if (isSatisfiedActual(customer) && !redo) {
            UnActCoarriFacade.prepareAndProcess(customer, range, redo);
            return;
        }

        try {

            try (PreparedStatement psVsl = DbUtil.preparedStatement(DbUtil.getConnection(),
                    SQLQueryConstants.SQL_VSL_REF_VW)) {

                psVsl.setString(1, range.getBegin());
                psVsl.setString(2, range.getEnd());

                ResultSet rsVsl = psVsl.executeQuery();

                while (rsVsl.next()) {
                    CoarriExcutor.un(customer, range, rsVsl, false, redo, ctnNos);
                }

            }

        } catch (SQLException ex) {
            LOGGER.info(ExceptionUtil.getStackTraceAsString(ex));
            ExceptionNotifyTask.notify(ex, new String[]{customer, "coarri"});
        }

    }

    /**
     * 按照船名航次创建联合国格式装卸船报文
     *
     * @param customer
     * @param vessel
     * @param redo
     * @param ctnNos
     */
    public static void prepareAndProcess0(String customer, VesselVoyage vessel, boolean redo, String... ctnNos) {

        try {

            try (PreparedStatement psVsl = DbUtil.preparedStatement(DbUtil.getConnection(),
                    SQLQueryConstants.SQL_VSL_VOY_VW)) {

                psVsl.setString(1, vessel.getVessel());
                psVsl.setString(2, vessel.getVoyage());

                ResultSet rsVsl = psVsl.executeQuery();

                while (rsVsl.next()) {
                    CoarriExcutor.un0(customer, rsVsl, redo, ctnNos);
                }

            }

        } catch (SQLException ex) {
            LOGGER.info(ExceptionUtil.getStackTraceAsString(ex));
            ExceptionNotifyTask.notify(ex, new String[]{customer, "coarri"});
        }

    }
}
