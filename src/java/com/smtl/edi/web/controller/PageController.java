package com.smtl.edi.web.controller;

import com.smtl.edi.web.page.Page;
import com.smtl.edi.web.svc.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author nm
 */
@Controller
public class PageController {

    @Autowired
    UserService userService;

    /**
     *
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "/{page}")
    public String page(@PathVariable String page, Model model) {
        if (page.contains("coarri")) {
            model.addAttribute("page", new Page<>());
            model.addAttribute("users", userService.getAllCodes());
        }
        return page;
    }
}
