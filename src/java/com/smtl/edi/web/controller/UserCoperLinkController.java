package com.smtl.edi.web.controller;

import com.smtl.edi.web.page.Page;
import com.smtl.edi.web.pojo.UserCoperLink;
import com.smtl.edi.web.svc.UserCoperLinkService;
import com.smtl.edi.web.svc.UserService;
import com.smtl.edi.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/link")
public class UserCoperLinkController {

    @Autowired
    UserCoperLinkService linkSvc;
    @Autowired
    UserService userService;

    /**
     *
     * @param model
     * @return
     */
    @RequestMapping("/topage")
    public String toPage(Model model) {
        model.addAttribute("users", userService.getAllCodes());
        return "link_add";
    }

    /**
     *
     * @param page
     * @param model
     * @return
     */
    @RequestMapping("/list/{page}")
    public String list(@PathVariable("page") String page, Model model) {

        String sql_link_count = "select count(*) from tc2_edi_cust_opt_link ";
        String sql_link_list = "select * from tc2_edi_cust_opt_link order by col_cst_code ";

        int p = 1;
        if (page != null && StringUtil.isNumeric(page)) {
            p = Integer.parseInt(page);
        }

        model.addAttribute("page", linkSvc.getPage(sql_link_count, sql_link_list, p));

        return "link";
    }

    /**
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/add")
    public String add(HttpServletRequest request, Model model) {

        String sql_link_count = "select count(*) from tc2_edi_cust_opt_link ";
        String sql_link_list = "select * from tc2_edi_cust_opt_link  order by col_cst_code ";
        String sql_operator = "select *  from tc2_edi_cust_opt_link "
                + "where col_cst_code='" + request.getParameter("cstCode") + "' "
                + "and col_ctn_operator='" + request.getParameter("ctnOperator") + "'";

        UserCoperLink lnk = linkSvc.getOne(sql_operator);
        if (lnk != null) {
            model.addAttribute("result", "插入失败，已存在关联关系");
            return "link";
        }

        String sql_insert_ctn_operator = "insert into "
                + "tc2_edi_cust_opt_link (col_cst_code,col_ctn_operator) "
                + "values(:cstCode,:ctnOperator) ";

        int count = linkSvc.update(new UserCoperLink(request.getParameter("cstCode"), request.getParameter("ctnOperator")), sql_insert_ctn_operator);
        if (count == 0) {
            model.addAttribute("result", "插入失败");
        } else {
            model.addAttribute("result", "插入成功");
        }

        model.addAttribute("page", linkSvc.getPage(sql_link_count, sql_link_list, 1));

        return "redirect:/link/list/1";
    }

    /**
     *
     * @param cstcode
     * @param copercd
     * @param model
     * @return
     */
    @RequestMapping("/delete/{cstcode}/{copercd}")
    public String delete(@PathVariable("cstcode") String cstcode, @PathVariable("copercd") String copercd, Model model) {

        String sql_link_count = "select count(*) from tc2_edi_cust_opt_link ";
        String sql_link_list = "select * from tc2_edi_cust_opt_link  order by col_cst_code ";
        String sql_delete = "delete from  tc2_edi_cust_opt_link  "
                + "where col_cst_code=:cstCode "
                + "and col_ctn_operator=:ctnOperator";

        Map map = new HashMap();
        map.put("cstCode", cstcode);
        map.put("ctnOperator", copercd);

        int count = linkSvc.update(sql_delete, map);
        if (count == 0) {
            model.addAttribute("result", "删除失败" + cstcode + "-" + copercd);
        } else {
            model.addAttribute("result", "删除成功");
        }

        model.addAttribute("page", linkSvc.getPage(sql_link_count, sql_link_list, 1));

        return "redirect:/link/list/1";
    }

    /**
     *
     * @param page
     * @param code
     * @param model
     * @return
     */
    @RequestMapping("/search/{page}")
    public String search(@PathVariable("page") String page, @RequestParam(value = "code", required = true) String code,
            Model model) {

        String sql_link_count = "select count(*) from tc2_edi_cust_opt_link where 1=1 ";
        String sql_link_list = "select * from tc2_edi_cust_opt_link where 1=1 ";
        String order_by = "order by col_cst_code";

        int p = 1;
        if (page != null && StringUtil.isNumeric(page)) {
            p = Integer.parseInt(page);
        }

        if (StringUtil.isNotEmpty(code)) {
            sql_link_count = sql_link_count + " and col_cst_code='" + code + "'";
            sql_link_list = sql_link_list + " and col_cst_code='" + code + "'";
            model.addAttribute("code", code);
        }

        Page<UserCoperLink> pg = linkSvc.getPage(sql_link_count, sql_link_list + order_by, p);
        model.addAttribute("page", pg);

        return "link";
    }

}
