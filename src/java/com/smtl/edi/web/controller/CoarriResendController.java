package com.smtl.edi.web.controller;

import com.smtl.EDIRedo;
import com.smtl.edi.util.ValidationUtil;
import com.smtl.edi.web.svc.UserService;
import com.smtl.edi.util.StringUtil;
import com.smtl.edi.vo.DateRange;
import java.io.UnsupportedEncodingException;

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
@RequestMapping("/coarri_resend")
public class CoarriResendController {
    
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
        return "coarri_resend";
    }
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     *
     *
     * @param request
     * @param model
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    @RequestMapping("/send")
    @ResponseBody
    public String send(HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        
        String cstcode = request.getParameter("cstcode");
        String cntrno = request.getParameter("cntrno");
        String typecheck = request.getParameter("typecheck");
        String vslName = request.getParameter("vslName");
        String voyage = request.getParameter("voyage");
        String begin = request.getParameter("begin").replaceAll(" ", "").replaceAll("-", "").replaceAll(":", "");
        String end = request.getParameter("end").replaceAll(" ", "").replaceAll("-", "").replaceAll(":", "");
        
        vslName = new String(vslName.getBytes("ISO8859_1"), "utf-8");
        
        model.addAttribute("cstcode", cstcode);
        model.addAttribute("vslname", vslName);
        model.addAttribute("voyage", voyage);
        model.addAttribute("cntrno", cntrno);
        model.addAttribute("begin", begin);
        model.addAttribute("end", end);
        
        String[] ctnNos = new String[0];
        if (StringUtil.isNotEmpty(cntrno)) {
            ctnNos = cntrno.trim().split(" ");
        }

        //根据船名航次发送报文
        if ("v".equalsIgnoreCase(typecheck)) {
            if (ValidationUtil.isValid(ctnNos)) {
                return EDIRedo.coarriByVslNameAndVoyage0(cstcode, vslName, voyage, ctnNos);
            } else {
                return EDIRedo.coarriByVslNameAndVoyage0(cstcode, vslName, voyage);
            }
            
        }
        if ("t".equalsIgnoreCase(typecheck)) {
            if (ValidationUtil.isValid(ctnNos)) {
                return EDIRedo.coarriByActTime(cstcode, new DateRange(begin, end), ctnNos);
            } else {
                return EDIRedo.coarriByActTime(cstcode, new DateRange(begin, end));
            }
            
        }
        
        return "N";
    }
}
