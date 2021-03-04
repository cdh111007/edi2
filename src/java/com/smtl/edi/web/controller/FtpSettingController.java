package com.smtl.edi.web.controller;

import com.smtl.edi.util.BeanUtil;
import com.smtl.edi.util.StringUtil;
import com.smtl.edi.web.pojo.FtpSetting;
import com.smtl.edi.web.svc.FtpSettingService;
import com.smtl.edi.web.svc.UserService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author nm
 */
@Controller
@RequestMapping("/ftp")
public class FtpSettingController {

    @Autowired
    UserService userService;

    @Autowired
    FtpSettingService ftpService;

    /**
     *
     * @param model
     * @return
     */
    @RequestMapping("/topage")
    public String toPage(Model model) {
        model.addAttribute("users", userService.getAllCodes());
        return "ftp_add";
    }

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

        model.addAttribute("page", ftpService.getPage(QUERY_FTP_COUNT_SQL, QUERY_FTP_LIST_SQL, p));

        return "ftp";
    }

    /**
     *
     * @param cstCode
     * @param ediCate
     * @param model
     * @return
     */
    @RequestMapping(value = "/delete/{cstCode}/{ediCate}")
    public String delete(@PathVariable(value = "cstCode") String cstCode,
            @PathVariable(value = "ediCate") String ediCate, Model model) {

        String sql_delete = "delete from  tc2_edi_cust_ftp  where cst_code=:cstCode and cst_edi_category=:ediCate ";

        Map map = new HashMap();
        map.put("cstCode", cstCode);
        map.put("ediCate", ediCate);

        int count = ftpService.update(sql_delete, map);
        if (count == 0) {
            model.addAttribute("result", "删除失败" + cstCode);
        } else {
            model.addAttribute("result", "删除成功");
        }

        model.addAttribute("page", ftpService.getPage(QUERY_FTP_COUNT_SQL, QUERY_FTP_LIST_SQL, 1));
        return "redirect:/ftp/list/1";

    }

    private static final String QUERY_FTP_COUNT_SQL = "select count(*) from tc2_edi_cust_ftp   ";
    private static final String QUERY_FTP_LIST_SQL = "select * from tc2_edi_cust_ftp  order by cst_code ";
    private static final String INSERT_FTP_SQL = "insert into tc2_edi_cust_ftp (cst_code, \n"
            + "cst_ftp, "
            + "cst_ftp_user, "
            + "cst_ftp_pwd, "
            + "cst_ftp_port, "
            + "cst_ftp_path, "
            + "cst_edi_category "
            + ") values(:cstCode,:ftpIP,:ftpUser,:ftpPwd,:ftpPort,:ftpPath,:ediCate) ";

    /**
     *
     * @param ftp
     * @param model
     * @return
     */
    @RequestMapping("/add")
    public String add(FtpSetting ftp, Model model) {
        String sql = "select * from tc2_edi_cust_ftp where cst_code=:cstCode and cst_edi_category=:ediCate";
        Map<String, String> params = new HashMap<>();
        params.put("cstCode", ftp.getCstCode());
        params.put("ediCate", ftp.getEdiCate());
        if (ftpService.exists(sql, params)) {
            model.addAttribute("users", userService.getAll());
            model.addAttribute("page", ftpService.getPage(QUERY_FTP_COUNT_SQL, QUERY_FTP_LIST_SQL, 1));
            model.addAttribute("result", "已存在" + ftp.getCstCode() + "/" + ftp.getEdiCate());
            return "ftp";
        }
        ftpService.insert(ftp, INSERT_FTP_SQL);
        model.addAttribute("users", userService.getAll());
        model.addAttribute("page", ftpService.getPage(QUERY_FTP_COUNT_SQL, QUERY_FTP_LIST_SQL, 1));
        return "redirect:/ftp/list/1";
    }

    /**
     *
     * @param cstCode
     * @param ediCate
     * @param model
     * @return
     */
    @RequestMapping("/edit/{cstCode}/{ediCate}")
    public String edit(@PathVariable("cstCode") String cstCode,
            @PathVariable("ediCate") String ediCate,
            Model model) {

        String sql = "select *  from tc2_edi_cust_ftp where cst_code ='" + cstCode + "' "
                + "and cst_edi_category='" + ediCate + "'";

        FtpSetting ftp = ftpService.getOne(sql);
        model.addAttribute("ftp", ftp);
        model.addAttribute("users", userService.getAll());

        return "ftp_edit";
    }

    /**
     *
     * @param ftp
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(FtpSetting ftp, Model model) throws Exception {

        String sql = "update tc2_edi_cust_ftp set "
                + "cst_ftp=:ftpIP,"
                + "cst_ftp_user=:ftpUser,"
                + "cst_ftp_pwd=:ftpPwd,"
                + "cst_ftp_port=:ftpPort,"
                + "cst_ftp_path=:ftpPath"
                + " where cst_code=:cstCode and cst_edi_category=:ediCate";

        Map map = BeanUtil.objectToMap(ftp);
        int count = ftpService.update(sql, map);
        if (count == 0) {
            model.addAttribute("result", "更新失败" + ftp);
        } else {
            model.addAttribute("result", "更新成功");
        }

        model.addAttribute("page", ftpService.getPage(QUERY_FTP_COUNT_SQL, QUERY_FTP_LIST_SQL, 1));

        return "redirect:/ftp/list/1";
    }
}
