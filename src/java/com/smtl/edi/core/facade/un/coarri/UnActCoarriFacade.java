package com.smtl.edi.core.facade.un.coarri;

import static com.smtl.edi.core.SQLQueryConstants.PS_ACT_IE_CTN;
import static com.smtl.edi.core.SQLQueryConstants.PS_ACT_VSL_REF;
import com.smtl.edi.core.excutor.coarri.CoarriExcutor;
import com.smtl.edi.core.task.mail.ExceptionNotifyTask;
import com.smtl.edi.util.ExceptionUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import com.smtl.edi.vo.DateRange;

/**
 * 按照箱子落地时间创建报文
 *
 * @author nm
 */
public class UnActCoarriFacade {

    final static Logger LOGGER = Logger.getLogger(UnActCoarriFacade.class);

    /**
     * 根据时间周期创建联合国格式装卸船实时报文
     *
     * @param customer
     * @param range
     * @param redo
     * @param ctnNos
     */
    public static void doHandle(String customer, DateRange range, boolean redo, String... ctnNos) {

        try {

            //该循环体为控制I，E
            for (int i = 0; i < 2; i++) {

                if (i == 0) {
                    PS_ACT_IE_CTN.setString(1, "I");
                } else {
                    PS_ACT_IE_CTN.setString(1, "E");
                }
                PS_ACT_IE_CTN.setString(2, customer);
                PS_ACT_IE_CTN.setString(3, range.getBegin());
                PS_ACT_IE_CTN.setString(4, range.getEnd());

                ResultSet rsActIECtn = PS_ACT_IE_CTN.executeQuery();

                String vslCode = "", voyage = "", ieFlag = "";

                Set<String> buffers = new HashSet<>();

                //该循环体为控制船代码，航次和进出口
                while (rsActIECtn.next()) {

                    vslCode = rsActIECtn.getString("vessel_code");
                    voyage = rsActIECtn.getString("voyage");
                    ieFlag = rsActIECtn.getString("vessel_ie");

                    String buffer = vslCode + "-" + voyage + "-" + ieFlag;

                    if (buffers.contains(buffer)) {
                        System.out.println("已处理 " + buffer);
                        continue;
                    } else {
                        buffers.add(buffer);
                    }

                    PS_ACT_VSL_REF.setString(1, vslCode);
                    PS_ACT_VSL_REF.setString(2, voyage);
                    PS_ACT_VSL_REF.setString(3, ieFlag);

                    ResultSet rsVsl = PS_ACT_VSL_REF.executeQuery();

                    while (rsVsl.next()) {
                        CoarriExcutor.un(customer, range, rsVsl, true, redo, ctnNos);
                    }
                }

            }

        } catch (SQLException ex) {
            LOGGER.info(ExceptionUtil.getStackTraceAsString(ex));
            ExceptionNotifyTask.notify(ex, new String[]{customer, "codeco"});
        }

    }

}
