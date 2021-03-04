package com.smtl.edi.web.controller;

import com.smtl.EDIRedo;

import com.smtl.edi.web.svc.UserService;
import com.smtl.edi.util.ValidationUtil;
import com.smtl.edi.util.StringUtil;
import com.smtl.edi.vo.DateRange;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author nm
 */
@Controller
@RequestMapping("/codeco_resend")
public class CodecoResendController {

    @Autowired
    UserService userService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     *
     * @param model
     * @return
     */
    @RequestMapping("/topage")
    public String toPage(Model model) {
        model.addAttribute("users", userService.getAllCodes());
        return "codeco_resend";
    }

    /**
     * 按照箱号发送
     *
     * @param request
     * @param model
     * @return
     * @throws ParseException
     */
    @RequestMapping("/send")
    @ResponseBody
    public String send(HttpServletRequest request, Model model) throws ParseException {

        String cstcode = request.getParameter("cstcode");
        String cntrno = request.getParameter("cntrno");

        String begin = request.getParameter("begin").replaceAll(" ", "").replaceAll("-", "").replaceAll(":", "");
        String end = request.getParameter("end").replaceAll(" ", "").replaceAll("-", "").replaceAll(":", "");

        model.addAttribute("cstcode", cstcode);
        model.addAttribute("cntrno", cntrno);
        model.addAttribute("begin", begin);
        model.addAttribute("end", end);

        String[] ctnNos = new String[0];
        if (StringUtil.isNotEmpty(cntrno)) {
            ctnNos = cntrno.trim().split(" ");
        }

        if (ValidationUtil.isValid(ctnNos)) {
            return EDIRedo.codeco(cstcode, new DateRange(begin, end), ctnNos);
        } else {
            return EDIRedo.codeco(cstcode, new DateRange(begin, end));
        }

    }

}
