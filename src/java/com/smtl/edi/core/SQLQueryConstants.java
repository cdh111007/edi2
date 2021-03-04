package com.smtl.edi.core;

import com.smtl.edi.util.DbUtil;
import java.sql.PreparedStatement;

/**
 *
 * @author Administrator
 */
public interface SQLQueryConstants {

    //装卸船报文
    String SQL_VSL_REF_VW = "select * from tc2_edi_coarri_vsl_ref_vw v "
            + "where close_time between ? and ?";
    String SQL_VSL_VOY_VW = "select * from tc2_edi_coarri_vsl_ref_vw v "
            + "where vessel_namec=? and voyage=? ";

    String SQL_VSL_CTN_VW = "select * from tc2_edi_coarri_vsl_ctn_vw v "
            + "where v.vessel_code=? "
            + "and v.voyage=? "
            + "and v.vessel_ie=? "
            + "and v.cst_code=? ";

    //进提箱报文 - 交通部格式
    String SQL_GATE_IN_JT = "select * from tc2_edi_codeco_vw t "
            + "where upper(t.ctn_operator)=? "
            + "and t.in_out_category=? "
            + "and t.in_yard_time between ? and ? ";

    String SQL_GATE_OUT_JT = "select * from tc2_edi_codeco_vw t "
            + "where upper(t.ctn_operator)=? "
            + "and t.in_out_category=? "
            + "and t.out_yard_time between ? and ? ";

    //进提箱报文 - 联合国格式
    String SQL_GATE_IN_UN = "select * from tc2_edi_codeco_vw t "
            + "where upper(t.cst_code)=? "
            + "and upper(t.in_out_category)=? "
            + "and t.in_yard_time between ? and ? ";

    String SQL_GATE_OUT_UN = "select * from tc2_edi_codeco_vw t "
            + "where upper(t.cst_code)=? "
            + "and upper(t.in_out_category)=? "
            + "and t.out_yard_time between ? and ? ";

    //装卸船报文 - 实时推送
    String SQL_ACT_VSL_REF_VW = "select * from tc2_edi_coarri_vsl_ref_act_vw v where vessel_code=? "
            + "and voyage=? "
            + "and ie_flag=?";
    String SQL_ACT_VSL_CTN_VW = "select *"
            + "  from tc2_edi_coarri_vsl_ctn_vw v"
            + " where v.vessel_code = ?"
            + "       and v.voyage = ?"
            + "       and v.vessel_ie = ?"
            + "       and v.cst_code = ?"
            + "       and (case v.vessel_ie"
            + "         when 'I' then"
            + "          in_yard_time"
            + "         else"
            + "          out_yard_time"
            + "       end) between ? and ? ";
    String SQL_ACT_IE_CTN_VW = "select *"
            + "  from tc2_edi_coarri_vsl_ctn_vw v"
            + " where v.vessel_ie = ? "
            + "       and v.cst_code = ?"
            + "       and (case v.vessel_ie"
            + "         when 'I' then"
            + "          in_yard_time"
            + "         else"
            + "          out_yard_time"
            + "       end) between ? and ? ";

    //清洁数据
    String SQL_XXZX_VSL_CTN_VW = "select * from tc2_edi_coarri_vsl_ctn_xxzx_vw v "
            + "where v.vessel_code=? "
            + "and v.voyage=? "
            + "and v.vessel_ie=? ";
    String SQL_XXZX_VSL_REF_VW = "select * from tc2_edi_coarri_vsl_ref_xxzx_vw v "
            + "where close_time between ? and ?";
    String SQL_XXZX_VSL_VOY_VW = "select * from tc2_edi_coarri_vsl_ref_xxzx_vw v "
            + "where vessel_namec=? and voyage=? ";

    //客户持箱人清单
    String SQL_CTN_OPERATOR = "select distinct t.ctn_operator "
            + "from tc2_edi_cust_opt_vw t "
            + "where upper(t.cst_code)=? "
            + "order by t.ctn_operator desc ";

    //客户清单
    String SQL_CUSTOMER_INFO = "select * from tc2_edi_cust_map_view where cst_code = ? and cst_edi_category=? ";
    //客户清单 - 发送装卸船
    String SQL_COARRI_FLAG = "and cst_coarri_flag = 1 ";
    //客户清单 - 发送进出门
    String SQL_CODECO_FLAG = "and cst_codeco_flag = 1 ";

    //客户清单 - 场存
    String SQL_COEDOR_FLAG = "and cst_coedor_flag = 1 ";
    //客户清单 - 完船
    String SQL_COSECR_FLAG = "and cst_cosecr_flag = 1 ";
    //客户清单 - 靠离港
    String SQL_VESDEP_FLAG = "and cst_vesedp_flag = 1 ";

    //客户清单 - 实时发送装卸船
    String SQL_ACT_COARRI_CUSTOMER_CODE = "select cst_code  from tc2_edi_cust_info  "
            + "where cst_coarri_flag=1 and cst_act_flag=1 ";

    //客户清单 - 除xxzx
    String SQL_GET_ALL_CUSTOMER_CODE_EXC_XXZX = "select cst_code from tc2_edi_cust_info "
            + "where (cst_edi_type)=?  and cst_code<>'XXZX' ";
    //客户清单 - 所有
    String SQL_GET_ALL_CUSTOMER_CODE = "select cst_code,cst_edi_type from tc2_edi_cust_info ";

    //拆装箱
    String SQL_CODECO_DEV_VAN_CTN = "select * from tc2_edi_codeco_van_dev_vm t "
            + "where t.cst_code=? and t.in_out_category=? and out_yard_time between ? and ?  ";
    String SQL_CODECO_DEV_VAN_CTN_1 = "select * from tc2_edi_codeco_van_dev_1_vm t "
            + "where t.cst_code=? and t.in_out_category=? and out_yard_time between ? and ?  ";

    //edi seq
    String SQL_EDI_SEQ_NEXT_VAL = "select tc2_edi_seq.nextval as seq from dual ";

    //cos船代码转换
    String SQL_FUN_GET_COS_VSL_CODE = "select tc2_edi_cos_get_vsl_code(?) vessel_code from dual ";

    //发送日志
    String SQL_INSERT_SEND_LOG = "insert into tc2_edi_send_log(esl_log_id,"
            + "esl_cst_code,"
            + "esl_msg_type,"
            + "esl_edi_type,"
            + "esl_file_path,"
            + "esl_file_content, "
            + "esl_redo_flag) "
            + "values(?,?,?,?,?,?,?)";
    //更新发送日志的最后一次创建时间：之前抓取箱数据时，如果没有箱数据，就不更新该时间，
    //但有个别用户几个月才有一次业务数据，这会导致查询从几个月前开始到现在的数据，时间跨度太大，耗时长，占用系统资源不释放
    //（因为业务数据也需要执行时间，暂时在前一次时间的基础上往前推5分钟，有可能会出现箱号重复发送的情况）
    String SQL_UPDATE_SEND_LOG = "update tc2_edi_send_log set esl_create_at=sysdate-5/(60*24) where esl_log_id=?";

    //插入箱明细
    String SQL_INSERT_CTN_DETAILS = "insert into tc2_edi_ctn_details(cst_code, "
            + "msg_type, "
            + "ctn_no, "
            + "vsl_name, "
            + "voyage, "
            + "vsl_ref_no, "
            + "in_quay_time, "
            + "out_quay_time, "
            + "msg_name,"
            + "log_id) values(?,?,?,?,?,?,?,?,?,?)";

    //*** 统计 ***
    //统计该航次下的装卸箱总量
    String SQL_CST_TOTAL_CTNS = "select fn_tc2_edi_get_cst_total_ctns(?,?,?,?) total_ctns from dual ";
    //统计该航次下的teu数量
    String SQL_CST_TOTAL_TEU = "select fn_tc2_edi_get_cst_total_teu(?,?,?,?) total_teu from dual ";
    //统计该航次的箱重总量
    String SQL_CST_TOTAL_WT = "select fn_tc2_edi_get_cst_total_wt(?,?,?,?) total_weight from dual ";

    //统计该航次下的装卸箱总量
    String SQL_XXZX_TOTAL_CTNS = "select fn_tc2_edi_get_total_ctns(?,?,?) total_ctns from dual ";
    //统计该航次下的teu数量
    String SQL_XXZX_TOTAL_TEU = "select fn_tc2_edi_get_total_teu(?,?,?) total_teu from dual ";
    //统计该航次的箱重总量
    String SQL_XXZX_TOTAL_WT = "select fn_tc2_edi_get_total_wt(?,?,?) total_weight from dual ";

    String SQL_COEDOR = "select * from tc2_edi_coedor_vm v where v.cst_code=?";
    String SQL_VESDEP_COARRI_DEPARTURE_LIST = "select * from tc2_edi_coarri_departure_vw t where t.cst_code = ?";
    String SQL_VESDEP_LOG_TOTAL = "select count(*) as total"
            + "  from tc2_edi_vesdep_log lg"
            + " where lg.evl_vsl_code = ?"
            + "       and lg.evl_voyage = ?"
            + "       and lg.evl_vessel_ie = ?"
            + "       and lg.evl_cst_code = ?";
    String SQL_COSECR_COARRI_END_LIST = "select * from tc2_edi_coarri_end_vw t where t.cst_code = ?";
    String SQL_COSECR_LOG_TOTAL = "select count(*) as total"
            + "  from tc2_edi_cosecr_log lg"
            + " where lg.ecl_vsl_code = ?"
            + "       and lg.ecl_voyage = ?"
            + "       and lg.ecl_vessel_ie = ?"
            + "       and lg.ecl_cst_code = ?";

    PreparedStatement PS_ACT_VSL_REF = DbUtil.preparedStatement(DbUtil.getConnection(), SQL_ACT_VSL_REF_VW);
    PreparedStatement PS_ACT_IE_CTN = DbUtil.preparedStatement(DbUtil.getConnection(), SQL_ACT_IE_CTN_VW);
}
