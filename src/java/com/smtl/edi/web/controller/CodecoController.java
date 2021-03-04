package com.smtl.edi.web.controller;

import com.smtl.edi.web.page.Page;
import com.smtl.edi.web.pojo.Codeco;
import com.smtl.edi.web.svc.CodecoService;
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
@RequestMapping("/codeco")
public class CodecoController {

    @Autowired
    CodecoService codecoSvc;
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

        String sql_codeco_list = "select distinct cst_code,"
                + "ctn_no,"
                + "create_at,"
                + "esl_msg_type,"
                + "msg_name,"
                + "log_id,"
                + "esl_file_path,"
                + "in_quay_time,"
                + "out_quay_time,"
                + "esl_send_flag "
                + "from tc2_edi_ctn_details,tc2_edi_send_log  "
                + "where msg_type='CODECO' and log_id = esl_log_id ";
        String sql_codeco_count = "select count(*) from (" + sql_codeco_list + ") ";

        String sql_codeco_list_order_by = "order by create_at desc,cst_code";

        String cstCode = request.getParameter("cstcode");
        String cntrno = request.getParameter("cntrno");

        if (StringUtil.isNotEmpty(cstCode)) {
            sql_codeco_list = sql_codeco_list + " and cst_code='" + cstCode + "'";
            sql_codeco_count = "select count(*) from (" + sql_codeco_list + ") ";
        }

        if (StringUtil.isNotEmpty(cntrno)) {
            sql_codeco_list = sql_codeco_list + " and ctn_no='" + cntrno + "'";
            sql_codeco_count = "select count(*) from (" + sql_codeco_list + ") ";
        }

        int p = 1;
        if (page != null && StringUtil.isNumeric(page)) {
            p = Integer.parseInt(page);
        }

        Page<Codeco> pg = codecoSvc.getPage(sql_codeco_count, sql_codeco_list + sql_codeco_list_order_by, p);
        model.addAttribute("page", pg);
        model.addAttribute("users", userService.getAllCodes());
        model.addAttribute("cstcode", cstCode);
        model.addAttribute("cntrno", cntrno);

        return "codeco";
    }

    /**
     *
     * @param page
     * @param cntrno
     * @param cstcode
     * @param model
     * @return
     */
    @RequestMapping("/search/{page}")
    public String search(@PathVariable("page") String page, @RequestParam(value = "cntrno", required = true) String cntrno,
            @RequestParam(value = "cstcode") String cstcode,
            Model model) {

        String sql_codeco_list = "select distinct cst_code,"
                + "ctn_no,"
                + "create_at,"
                + "msg_name,"
                + "esl_msg_type,"
                + "log_id,"
                + "esl_file_path,"
                + "in_quay_time,"
                + "out_quay_time,"
                + "esl_send_flag "
                + "from tc2_edi_ctn_details,tc2_edi_send_log  "
                + "where msg_type='CODECO' and log_id = esl_log_id ";
        String sql_codeco_count = "select count(*) from (" + sql_codeco_list + ") ";

        String sql_codeco_list_order_by = "order by create_at desc,cst_code";

        int p = 1;
        if (page != null && StringUtil.isNumeric(page)) {
            p = Integer.parseInt(page);
        }

        if (StringUtil.isNotEmpty(cntrno)) {
            sql_codeco_list = sql_codeco_list + " and ctn_no='" + cntrno.trim() + "'";
            sql_codeco_count = "select count(*) from (" + sql_codeco_list + ") ";
            model.addAttribute("ctn_no", cntrno.trim());
        }
        if (StringUtil.isNotEmpty(cstcode)) {
            sql_codeco_list = sql_codeco_list + " and cst_code='" + cstcode.trim() + "'";
            sql_codeco_count = "select count(*) from (" + sql_codeco_list + ") ";
            model.addAttribute("cstcode", cstcode.trim());
        }

        Page<Codeco> pg = codecoSvc.getPage(sql_codeco_count, sql_codeco_list + sql_codeco_list_order_by, p);
        model.addAttribute("page", pg);
        model.addAttribute("users", userService.getAllCodes());

        return "codeco";
    }
}
