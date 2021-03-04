package com.smtl.edi.web.controller;

import com.smtl.edi.util.BeanUtil;
import com.smtl.edi.util.StringUtil;
import com.smtl.edi.web.pojo.SenderSetting;
import com.smtl.edi.web.svc.SenderSettingService;
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
@RequestMapping("/sender")
public class SenderSettingController {

    @Autowired
    UserService userService;

    @Autowired
    SenderSettingService senderService;

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

        model.addAttribute("page", senderService.getPage(QUERY_SENDER_COUNT_SQL, QUERY_SENDER_LIST_SQL, p));

        return "sender";
    }

    /**
     *
     * @param model
     * @return
     */
    @RequestMapping("/topage")
    public String toPage(Model model) {
        model.addAttribute("users", userService.getAllCodes());
        model.addAttribute("page", senderService.getPage(QUERY_SENDER_COUNT_SQL, QUERY_SENDER_LIST_SQL, 1));
        
        return "sender_add";
    }
    private static String QUERY_SENDER_COUNT_SQL = "select count(*) from tc2_edi_map_setting ";
    private static String QUERY_SENDER_LIST_SQL = "select * from tc2_edi_map_setting  order by cst_code ";

    /**
     *
     * @param setting
     * @param model
     * @return
     */
    @RequestMapping("/add")
    public String add(SenderSetting setting, Model model) {
        String sql = "select * from tc2_edi_map_setting where cst_code=:cstCode and cst_edi_category=:ediCate";
        
        Map<String, String> params = new HashMap<>();
        params.put("cstCode", setting.getCstCode());
        params.put("ediCate", setting.getEdiCate());
        
        if (senderService.exists(sql, params)) {
            model.addAttribute("users", userService.getAll());
            model.addAttribute("page", senderService.getPage(QUERY_SENDER_COUNT_SQL, QUERY_SENDER_LIST_SQL, 1));
            model.addAttribute("result", "已存在" + setting.getCstCode() + "/" + setting.getEdiCate());
            return "redirect:/sender/list/1";
        }
        
        String sqlInsertSenderSetting = "insert into tc2_edi_map_setting (cst_code, \n"
                + "cst_sender, "
                + "cst_receiver, "
                + "cst_edi_category "
                + ") values(:cstCode,:cstSender,:cstReceiver,:ediCate) ";
        
        senderService.insert(setting, sqlInsertSenderSetting);
        
        model.addAttribute("users", userService.getAll());
        model.addAttribute("page", senderService.getPage(QUERY_SENDER_COUNT_SQL, QUERY_SENDER_LIST_SQL, 1));
        
        return "redirect:/sender/list/1";
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

        String sql = "select *  from tc2_edi_map_setting "
                + "where cst_code ='" + cstCode + "' "
                + "and cst_edi_category='" + ediCate + "'";

        SenderSetting sender = senderService.getOne(sql);
        model.addAttribute("sender", sender);
        model.addAttribute("users", userService.getAll());

        return "sender_edit";
    }

    /**
     *
     * @param sender
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(SenderSetting sender, Model model) throws Exception {

        String sql = "update tc2_edi_map_setting set "
                + "cst_sender=:cstSender,"
                + "cst_receiver=:cstReceiver"
                + " where cst_code=:cstCode and cst_edi_category=:ediCate";

        Map map = BeanUtil.objectToMap(sender);
        int count = senderService.update(sql, map);
        if (count == 0) {
            model.addAttribute("result", "更新失败" + sender);
        } else {
            model.addAttribute("result", "更新成功");
        }

        model.addAttribute("page", senderService.getPage(QUERY_SENDER_COUNT_SQL, QUERY_SENDER_LIST_SQL, 1));

        return "redirect:/sender/list/1";
    }
}
