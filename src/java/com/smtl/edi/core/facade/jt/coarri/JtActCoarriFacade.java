package com.smtl.edi.core.facade.jt.coarri;

import static com.smtl.edi.core.SQLQueryConstants.PS_ACT_IE_CTN;
import static com.smtl.edi.core.SQLQueryConstants.PS_ACT_VSL_REF;
import com.smtl.edi.core.excutor.coarri.CoarriExcutor;
import com.smtl.edi.core.task.mail.ExceptionNotifyTask;
import com.smtl.edi.util.ExceptionUtil;
import com.smtl.edi.util.PropertiesUtil;
import com.smtl.edi.util.StringUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import com.smtl.edi.vo.DateRange;

/**
 * 交通部格式装卸船报文实时报文入口
 *
 * @author nm
 */
public class JtActCoarriFacade {

    final static Logger LOGGER = Logger.getLogger(JtActCoarriFacade.class);

    /**
     *
     * 根据时间周期创建交通部标准格式的实时装卸船报文
     *
     * @param customer
     * @param range
     * @param redo
     * @param ctnNos
     */
    public static void doHandle(String customer, DateRange range, boolean redo, String... ctnNos) {

        try {

            String csts = PropertiesUtil.getValue("jt_coarri_std_cst_code");
            if (StringUtil.isEmpty(csts)) {
                csts = "";
            }
            List<String> stdJtCsts = Arrays.asList(csts.toUpperCase().split(","));

            LOGGER.info("读取使用标准交通部格式报文的用户..." + stdJtCsts);

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

                //存储船名、航次、进出口标志，用来判断是否已处理
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

                    //根据船代码，航次和进出口标志获取00，10段数据正文
                    while (rsVsl.next()) {
                        if (stdJtCsts.contains(customer)) {
                            CoarriExcutor.jtStd(customer, range, rsVsl, true, redo, ctnNos);
                        } else {
                            CoarriExcutor.jt(customer, range, rsVsl, true, redo, ctnNos);
                        }
                    }
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            LOGGER.info(ExceptionUtil.getStackTraceAsString(ex));
            ExceptionNotifyTask.notify(ex, new String[]{customer, "coarri"});
        }

    }

}
