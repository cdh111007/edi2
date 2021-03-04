package com.smtl.edi.web.controller;

import com.smtl.edi.web.page.Page;
import com.smtl.edi.web.pojo.Coarri;
import com.smtl.edi.web.svc.CoarriService;
import com.smtl.edi.web.svc.UserService;
import com.smtl.edi.util.StringUtil;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author nm
 */
@Controller
@RequestMapping("/coarri")
public class CoarriController {

    @Autowired
    CoarriService coarriSvc;
    @Autowired
    UserService userService;
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     *
     * @param logid
     * @param model
     * @return
     */
    @RequestMapping("/view/{logid}")
    public String view(@PathVariable("logid") String logid, final Model model) {

        String sql_coarri_log = "select esl_file_path,esl_file_content from tc2_edi_send_log where esl_log_id = ? ";

        final LobHandler lobHandler = new DefaultLobHandler();

        jdbcTemplate.query(
                sql_coarri_log, new Object[]{logid},
                new AbstractLobStreamingResultSetExtractor() {
            @Override
            public void streamData(ResultSet rs) throws SQLException, IOException {

                String content = lobHandler.getClobAsString(rs, "esl_file_content");
                String filename = rs.getString("esl_file_path");

                model.addAttribute("content", content);
                model.addAttribute("filename", filename);
            }
        }
        );

        return "msg_view";

    }

    /**
     *
     * @param page
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/list/{page}")
    public String list(@PathVariable("page") String page, Model model, HttpServletRequest request) {

        String sql_coarri_list = "select distinct cst_code,"
                + "vsl_name,voyage,"
                + "create_at,"
                + "msg_name,"
                + "log_id,"
                + "esl_file_path,"
                + "esl_send_flag "
                + "from tc2_edi_ctn_details,tc2_edi_send_log  "
                + "where msg_type='COARRI' and log_id = esl_log_id ";
        String sql_coarri_count = "select count(*) from (" + sql_coarri_list + " ) ";
        String sql_coarri_list_order_by = "order by create_at desc,cst_code";

        String cstCode = request.getParameter("cstcode");
        String vslName = request.getParameter("vslname");
        String voyage = request.getParameter("voyage");

        if (StringUtil.isNotEmpty(cstCode)) {
            sql_coarri_list = sql_coarri_list + " and cst_code='" + cstCode + "'";
            sql_coarri_count = "select count(*) from (" + sql_coarri_list + " ) ";
        }
        if (StringUtil.isNotEmpty(vslName)) {
            sql_coarri_list = sql_coarri_list + " and vsl_name='" + vslName + "'";
            sql_coarri_count = "select count(*) from (" + sql_coarri_list + " ) ";
        }
        if (StringUtil.isNotEmpty(voyage)) {
            sql_coarri_list = sql_coarri_list + " and voyage='" + voyage + "'";
            sql_coarri_count = "select count(*) from (" + sql_coarri_list + " ) ";
        }

        int p = 1;
        if (page != null && StringUtil.isNumeric(page)) {
            p = Integer.parseInt(page);
        }

        Page<Coarri> pg = coarriSvc.getPage(sql_coarri_count, sql_coarri_list + sql_coarri_list_order_by, p);
        model.addAttribute("page", pg);
        model.addAttribute("users", userService.getAllCodes());
        model.addAttribute("cstcode", cstCode);
        model.addAttribute("vslname", vslName);
        model.addAttribute("voyage", voyage);

        return "coarri";

    }

    /**
     *
     * @param page
     * @param vslname
     * @param voyage
     * @param cstcode
     * @param model
     * @return
     */
    @RequestMapping("/search/{page}")
    public String search(@PathVariable("page") String page, @RequestParam(value = "vslname") String vslname,
            @RequestParam(value = "voyage") String voyage, @RequestParam(value = "cstcode") String cstcode, Model model) {

        String sql_coarri_list = "select distinct cst_code,"
                + "vsl_name,voyage,"
                + "create_at,"
                + "msg_name,"
                + "log_id,"
                + "esl_file_path,"
                + "esl_send_flag "
                + "from tc2_edi_ctn_details,tc2_edi_send_log  "
                + "where msg_type='COARRI' and log_id = esl_log_id ";
        String sql_coarri_count = "select count(*) from (" + sql_coarri_list + " ) ";
        String sql_coarri_list_order_by = "order by create_at desc,cst_code";

        int p = 1;
        if (page != null && StringUtil.isNumeric(page)) {
            p = Integer.parseInt(page);
        }

        if (StringUtil.isNotEmpty(vslname)) {
            sql_coarri_list = sql_coarri_list + " and vsl_name='" + vslname.trim() + "'";
            sql_coarri_count = "select count(*) from (" + sql_coarri_list + " ) ";
            model.addAttribute("vslname", vslname.trim());
        }
        if (StringUtil.isNotEmpty(voyage)) {
            sql_coarri_list = sql_coarri_list + " and voyage='" + voyage.trim() + "'";
            sql_coarri_count = "select count(*) from (" + sql_coarri_list + " ) ";
            model.addAttribute("voyage", voyage.trim());
        }
        if (StringUtil.isNotEmpty(cstcode)) {
            sql_coarri_list = sql_coarri_list + " and cst_code='" + cstcode.trim() + "'";
            sql_coarri_count = "select count(*) from (" + sql_coarri_list + " ) ";
            model.addAttribute("cstcode", cstcode.trim());
        }

        Page<Coarri> pg = coarriSvc.getPage(sql_coarri_count, sql_coarri_list + sql_coarri_list_order_by, p);
        model.addAttribute("page", pg);
        model.addAttribute("users", userService.getAllCodes());

        return "coarri";
    }

    /**
     *
     * @param vslname
     * @param voyage
     * @param cstcode
     * @param day
     * @param ctnNo
     * @param model
     * @return
     */
    @RequestMapping("/get")
    public String getCoarri(@RequestParam(value = "vslname") String vslname,
            @RequestParam(value = "voyage") String voyage, @RequestParam(value = "cstcode") String cstcode, 
            @RequestParam(value = "day") String day, @RequestParam(value = "ctnNo") String ctnNo, Model model) {

        String sql_coarri_list = "select distinct cst_code,"
                + "vsl_name,voyage,"
                + "create_at,"
                + "msg_name,"
                + "log_id,"
                + "esl_file_path,"
                + "esl_send_flag "
                + "from tc2_edi_ctn_details,tc2_edi_send_log  "
                + "where msg_type='COARRI' and log_id = esl_log_id ";
        String sql_coarri_count = "select count(*) from (" + sql_coarri_list + " ) ";
        String sql_coarri_list_order_by = "order by create_at desc,cst_code";

        if (StringUtil.isNotEmpty(vslname)) {
            sql_coarri_list = sql_coarri_list + " and vsl_name='" + vslname.trim() + "'";
            sql_coarri_count = "select count(*) from (" + sql_coarri_list + " ) ";
            model.addAttribute("vslname", vslname.trim());
        }
        if (StringUtil.isNotEmpty(voyage)) {
            sql_coarri_list = sql_coarri_list + " and voyage='" + voyage.trim() + "'";
            sql_coarri_count = "select count(*) from (" + sql_coarri_list + " ) ";
            model.addAttribute("voyage", voyage.trim());
        }
        if (StringUtil.isNotEmpty(cstcode)) {
            sql_coarri_list = sql_coarri_list + " and cst_code='" + cstcode.trim() + "'";
            sql_coarri_count = "select count(*) from (" + sql_coarri_list + " ) ";
            model.addAttribute("cstcode", cstcode.trim());
        }
        if (StringUtil.isNotEmpty(day)) {
            sql_coarri_list = sql_coarri_list + " and to_char(esl_create_at, 'yyyy-MM-dd')='" + day + "'";
            sql_coarri_count = "select count(*) from (" + sql_coarri_list + " ) ";
            model.addAttribute("day", day);
        }
        if (StringUtil.isNotEmpty(ctnNo)) {
            sql_coarri_list = sql_coarri_list + " and dbms_lob.instr(esl_file_content,'" + ctnNo.trim() + "')>0";
            sql_coarri_count = "select count(*) from (" + sql_coarri_list + " ) ";
            model.addAttribute("ctnNo", ctnNo.trim());
        }
        Page<Coarri> pg = coarriSvc.getPage(sql_coarri_count, sql_coarri_list + sql_coarri_list_order_by, 1);
        model.addAttribute("page", pg);
        model.addAttribute("users", userService.getAllCodes());

        return "get_coarri";
    }
}
