package com.smtl.edi.web.controller;

import com.smtl.edi.web.pojo.User;
import com.smtl.edi.web.svc.UserService;
import com.smtl.edi.util.BeanUtil;
import com.smtl.edi.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author nm
 */
@Controller
@RequestMapping("/user")
public class UserController {

    /**
     *
     * @param page
     * @param model
     * @return
     */
    @RequestMapping("/list/{page}")
    public String list(@PathVariable("page") String page, Model model) {

        int p = 1;
        if (page != null && StringUtil.isNumeric(page)) {
            p = Integer.parseInt(page);
        }

        model.addAttribute("page", userService.getPage(QUERY_USER_COUNT_SQL, QUERY_USER_LIST_SQL, p));

        return "user";
    }

    private static final String QUERY_USER_COUNT_SQL = "select count(*) from tc2_edi_cust_info ";
    private static final String QUERY_USER_LIST_SQL = "select * from tc2_edi_cust_info  order by cst_code ";
    private static final String INSERT_USER_SQL = "insert into "
            + "tc2_edi_cust_info ("
            + "CST_CODE,"
            + "CST_NAME,"
            + "CST_EMAIL_TO,"
            + "CST_EMAIL_CC,"
            + "CST_EDI_TYPE,"
            + "cst_codeco_flag,"
            + "cst_coarri_flag,"
            + "cst_coedor_flag,"
            + "cst_cosecr_flag,"
            + "cst_vesdep_flag,"
            + "cst_act_flag"
            + ") "
            + "values("
            + ":code,"
            + ":name,"
            + ":mail,"
            + ":ccmail,"
            + ":ediType,"
            + ":codecoFlag,"
            + ":coarriFlag,"
            + ":coedorFlag,"
            + ":cosecrFlag,"
            + ":vesdepFlag,"
            + ":actFlag"
            + ") ";
    @Autowired
    UserService userService;

    /**
     *
     * @param user
     * @param model
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(User user, Model model) {

        String sql = "select *  from tc2_edi_cust_info where CST_CODE='" + user.getCode() + "'";

        User u = userService.getOne(sql);
        if (u != null) {
            model.addAttribute("result", "插入失败，已存在客户代码：" + user.getCode());
            model.addAttribute("page", userService.getPage(QUERY_USER_COUNT_SQL, QUERY_USER_LIST_SQL, 1));
            return "user";
        }

        int count = userService.insert(user, INSERT_USER_SQL);
        if (count == 0) {
            model.addAttribute("result", "插入失败" + user.getCode());
        } else {
            model.addAttribute("result", "插入成功");
        }

        model.addAttribute("page", userService.getPage(QUERY_USER_COUNT_SQL, QUERY_USER_LIST_SQL, 1));

        return "redirect:/user/list/1";
    }

    /**
     *
     * @param user
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(User user, Model model) throws Exception {

        String sql = "update tc2_edi_cust_info set "
                + "CST_NAME=:name,"
                + "CST_EMAIL_TO=:mail,"
                + "CST_EMAIL_CC=:ccmail,"
                + "CST_EDI_TYPE=:ediType,"
                + "CST_CODECO_FLAG=:codecoFlag,"
                + "CST_COARRI_FLAG=:coarriFlag,"
                + "CST_COEDOR_FLAG=:coedorFlag,"
                + "CST_COSECR_FLAG=:cosecrFlag,"
                + "CST_VESDEP_FLAG=:vesdepFlag,"
                + "CST_ACT_FLAG=:actFlag"
                + " where CST_CODE='" + user.getCode() + "'";

        Map map = BeanUtil.objectToMap(user);
        int count = userService.update(sql, map);
        if (count == 0) {
            model.addAttribute("result", "更新失败" + user);
        } else {
            model.addAttribute("result", "更新成功");
        }

        model.addAttribute("page", userService.getPage(QUERY_USER_COUNT_SQL, QUERY_USER_LIST_SQL, 1));

        return "redirect:/user/list/1";
    }

    /**
     *
     * @param code
     * @param model
     * @return
     */
    @RequestMapping(value = "/delete/{code}", method = RequestMethod.POST)
    public String delete(@RequestParam(value = "code", required = true) String code, Model model) {

        String sql_delete = "delete from  tc2_edi_cust_info  where CST_CODE=:code";

        Map map = new HashMap();
        map.put("code", code);

        int count = userService.update(sql_delete, map);
        if (count == 0) {
            model.addAttribute("result", "删除失败" + code);
        } else {
            model.addAttribute("result", "删除成功");
        }

        model.addAttribute("page", userService.getPage(QUERY_USER_COUNT_SQL, QUERY_USER_LIST_SQL, 1));

        return "redirect:/user/list/1";
    }

    /**
     *
     * @param code
     * @param model
     * @return
     */
    @RequestMapping("/edit/{code}")
    public String edit(@PathVariable("code") String code, Model model) {

        String sql = "select *  from tc2_edi_cust_info where CST_CODE='" + code + "'";

        User user = userService.getOne(sql);
        model.addAttribute("user", user);

        return "user_edit";
    }

    /**
     *
     * @param code
     * @param model
     * @return
     */
    @RequestMapping("/reset/{code}")
    public String reset(@PathVariable("code") String code, Model model) {

        String sql_update = "update tc2_edi_cust_info set cst_DELETE=null where cst_DELETE=:code";

        Map map = new HashMap();
        map.put("code", code);

        int count = userService.update(sql_update, map);
        if (count == 0) {
            model.addAttribute("result", "更新失败" + code);
        } else {
            model.addAttribute("result", "更新成功");
        }

        model.addAttribute("page", userService.getPage(QUERY_USER_COUNT_SQL, QUERY_USER_LIST_SQL, 1));

        return "user";
    }
}
