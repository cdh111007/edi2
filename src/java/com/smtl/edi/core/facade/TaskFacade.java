package com.smtl.edi.core.facade;

import com.EDIHelper;
import com.smtl.edi.core.excutor.codeco.DVCodecoExcutor;
import com.smtl.edi.core.excutor.codeco.HhwlCodecoExcutor;
import com.smtl.edi.core.facade.jt.coarri.JtCoarriFacade;
import com.smtl.edi.core.excutor.coarri.XxzxCoarriExcutor;
import com.smtl.edi.core.excutor.codeco.JtCodecoExcutor;
import com.smtl.edi.core.facade.un.coarri.UnCoarriFacade;
import com.smtl.edi.core.excutor.codeco.UnCodecoExcutor;
import com.smtl.edi.util.DatetimeUtil;
import com.smtl.edi.util.StringUtil;
import java.util.List;
import static com.EDIHelper.getLastCreateTimeWithIntervalSeconds;
import static com.EDIHelper.print;
import static com.smtl.EDIRedo.xxzxYesterdayLostCoarri;
import com.smtl.edi.core.excutor.coedor.JtCoedorExcutor;
import com.smtl.edi.core.excutor.cosecr.JtCosecrExcutor;
import com.smtl.edi.core.excutor.vesdep.JtVesdepExcutor;

import com.smtl.edi.util.PropertiesUtil;
import java.util.Arrays;
import static com.EDIHelper.getCustomerCodesByEDIType;
import static com.EDIHelper.getLastCreateAtTime0;
import com.smtl.edi.vo.DateRange;

/**
 *
 * @author nm
 */
public class TaskFacade {

    /**
     * 联合国格式拆装箱报文
     */
    static void unDvCodeco() {

        List<String> customers = getCustomerCodesByEDIType("un");

        for (String customer : customers) {

            List<String> dvCsts = Arrays.asList(PropertiesUtil.getValue("un_dv_codeco_cst_code").split(","));

            if (!dvCsts.contains(customer)) {
                continue;
            }

            String begin = getLastCreateTimeWithIntervalSeconds(customer, "DV", "UN");
            if (EDIHelper.checkExceedNDaysFromBegin(begin)) {
                continue;
            }
            String end = DatetimeUtil.now(DatetimeUtil.YYYYMMDDHHMMSS);

            print("写DV报文数据..." + customer + "\t" + DatetimeUtil.format(begin, DatetimeUtil.YYYY_MM_DD_HH_MM_SS)
                    + " - " + DatetimeUtil.now(DatetimeUtil.YYYY_MM_DD_HH_MM_SS));

            DVCodecoExcutor.un(customer, new DateRange(begin, end), false);

            print("OK!");
        }

    }

    /**
     * 交通部格式拆装箱报文
     */
    static void jtDvCodeco() {

        List<String> customers = getCustomerCodesByEDIType("jt");

        for (String customer : customers) {

            List<String> dvCsts = Arrays.asList(PropertiesUtil.getValue("jt_dv_codeco_cst_code").split(","));

            if (!dvCsts.contains(customer)) {
                continue;
            }

            String begin = getLastCreateTimeWithIntervalSeconds(customer, "DV", "JT");
            if (EDIHelper.checkExceedNDaysFromBegin(begin)) {
                continue;
            }
            String end = DatetimeUtil.now(DatetimeUtil.YYYYMMDDHHMMSS);

            print("写DV报文数据..." + customer + "\t" + DatetimeUtil.format(begin, DatetimeUtil.YYYY_MM_DD_HH_MM_SS)
                    + " - " + DatetimeUtil.now(DatetimeUtil.YYYY_MM_DD_HH_MM_SS));

            DVCodecoExcutor.jt(customer, new DateRange(begin, end), false);

            print("OK!");
        }

    }

    /**
     * 华航进出门 自定义格式
     */
    static void hhwlCodeco() {

        String from = getLastCreateAtTime0("HHWL", "CODECO", "C");

        //如果第一次发，那么直接发前天的报文
        if (StringUtil.isEmpty(from)) {
            HhwlCodecoExcutor.process();
            return;
        }

        int days = 0;
        if (StringUtil.isNotEmpty(from)) {
            days = DatetimeUtil.daysToNow(from);
        }

        //如果之前发过报文，检测从上次开始到现在是否过去1整天
        if (days == 1) {
            print("写CODECO报文数据..." + "HHWL" + " " + DatetimeUtil.now("yyyyMMdd"));
            HhwlCodecoExcutor.process();
            print("OK!");
            return;
        }

        //如果中间有某一天漏掉的，需要补上，一直补到当天
        if (days > 1) {

            for (int i = 0; i < days; i++) {
                print("写CODECO报文数据..." + "HHWL" + " 补：" + DatetimeUtil.format(DatetimeUtil.daysAgo(i + 1), "yyyyMMdd"));
                HhwlCodecoExcutor.process(i + 1);
            }

            print("OK!");

        }

    }

    /**
     * 电子口岸装卸船报文
     *
     */
    static void xxzxCoarri() {

        String begin = getLastCreateTimeWithIntervalSeconds("XXZX", "COARRI", "JT");
        if (EDIHelper.checkExceedNDaysFromBegin(begin)) {
            return;
        }
        String end = DatetimeUtil.now(DatetimeUtil.YYYYMMDDHHMMSS);

        print("写COARRI报文数据...XXZX" + "\t" + DatetimeUtil.format(begin, DatetimeUtil.YYYY_MM_DD_HH_MM_SS)
                + " - " + DatetimeUtil.now(DatetimeUtil.YYYY_MM_DD_HH_MM_SS));

        XxzxCoarriExcutor.prepareAndProcess(new DateRange(begin, end), false);

        print("OK!");

        //每天凌晨1点，检查前一天遗漏的清洁数据
        if (DatetimeUtil.hour() == 01) {
            xxzxYesterdayLostCoarri();
        }
    }

    /**
     * codeco 入口
     */
    public static void codeco() {
        hhwlCodeco();
        codeco("un");
        codeco("jt");
        dvCodeco();
    }

    /**
     * coarri 入口
     */
    public static void coarri() {
        coarri("un");
        coarri("jt");
        xxzxCoarri();
    }

    /**
     * 拆装箱报文 入口
     */
    private static void dvCodeco() {
        jtDvCodeco();
        unDvCodeco();
    }

    /**
     * 装卸船
     *
     * @param ediType un/jt
     */
    static void coarri(String ediType) {
        List<String> customers = getCustomerCodesByEDIType(ediType);

        for (String customer : customers) {
            String begin = getLastCreateTimeWithIntervalSeconds(customer, "COARRI", ediType.toUpperCase());
            if (EDIHelper.checkExceedNDaysFromBegin(begin)) {
                continue;
            }
            String end = DatetimeUtil.now(DatetimeUtil.YYYYMMDDHHMMSS);

            print("写COARRI报文数据..." + customer + "\t" + DatetimeUtil.format(begin, DatetimeUtil.YYYY_MM_DD_HH_MM_SS)
                    + " - " + DatetimeUtil.now(DatetimeUtil.YYYY_MM_DD_HH_MM_SS));

            if ("un".equalsIgnoreCase(ediType)) {
                UnCoarriFacade.prepareAndProcess(customer, new DateRange(begin, end), false);
            } else {
                JtCoarriFacade.prepareAndProcess(customer, new DateRange(begin, end), false);
            }

            print("OK!");
        }
    }

    /**
     * 进出门
     *
     * @param ediType un/jt
     */
    static void codeco(String ediType) {
        List<String> customers = getCustomerCodesByEDIType(ediType);

        for (String customer : customers) {
            String begin = getLastCreateTimeWithIntervalSeconds(customer, "CODECO", ediType.toUpperCase());
            if (EDIHelper.checkExceedNDaysFromBegin(begin)) {
                continue;
            }
            String end = DatetimeUtil.now(DatetimeUtil.YYYYMMDDHHMMSS);

            print("写CODECO报文数据..." + customer + "\t" + DatetimeUtil.format(begin, DatetimeUtil.YYYY_MM_DD_HH_MM_SS)
                    + " - " + DatetimeUtil.now(DatetimeUtil.YYYY_MM_DD_HH_MM_SS));

            if ("un".equalsIgnoreCase(ediType)) {
                UnCodecoExcutor.process(customer, new DateRange(begin, end), false);
            } else {
                JtCodecoExcutor.process(customer, new DateRange(begin, end), false);
            }

            print("OK!");
        }
    }

    /**
     * 场存
     */
    public static void coedor() {
        List<String> customers = getCustomerCodesByEDIType("JT");

        for (String customer : customers) {
            if (!"ZGXL".equalsIgnoreCase(customer)) {
                continue;
            }
            String begin = getLastCreateTimeWithIntervalSeconds(customer, "COEDOR", "JT");
            begin = DatetimeUtil.format(begin, DatetimeUtil.YYYY_MM_DD_HH_MM_SS);
            String end = DatetimeUtil.now(DatetimeUtil.YYYY_MM_DD_HH_MM_SS);

            print("写COEDOR报文数据..." + customer + "\t" + begin + " - " + end);
            JtCoedorExcutor.doHandle(customer);
            print("OK!");
        }
    }

    /**
     * 离港
     *
     */
    public static void vesdep() {
        List<String> customers = getCustomerCodesByEDIType("JT");

        for (String customer : customers) {
            if (!"ZGXL".equalsIgnoreCase(customer)) {
                continue;
            }
            String begin = getLastCreateTimeWithIntervalSeconds(customer, "VESDEP", "JT");
            begin = DatetimeUtil.format(begin, DatetimeUtil.YYYY_MM_DD_HH_MM_SS);
            String end = DatetimeUtil.now(DatetimeUtil.YYYY_MM_DD_HH_MM_SS);

            print("写VESDEP报文数据..." + customer + "\t" + begin + " - " + end);
            JtVesdepExcutor.doHandle(customer);
            print("OK!");
        }
    }

    /**
     * 完船
     */
    public static void cosecr() {
        List<String> customers = getCustomerCodesByEDIType("JT");

        for (String customer : customers) {
            if (!"ZGXL".equalsIgnoreCase(customer)) {
                continue;
            }
            String begin = getLastCreateTimeWithIntervalSeconds(customer, "COSECR", "JT");
            begin = DatetimeUtil.format(begin, DatetimeUtil.YYYY_MM_DD_HH_MM_SS);
            String end = DatetimeUtil.now(DatetimeUtil.YYYY_MM_DD_HH_MM_SS);

            print("写COSECR报文数据..." + customer + "\t" + begin + " - " + end);
            JtCosecrExcutor.doHandle(customer);
            print("OK!");
        }
    }
}
