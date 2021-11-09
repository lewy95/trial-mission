package cn.xzxy.lewy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    /**
     * fd 网站根目录请求
     */
    @RequestMapping("/fd")
    public ModelAndView root(HttpSession session) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }

    /**
     * 注销
     */
    @RequestMapping("/logout")
    public String logout() {
        return "redirect:https://hanmy.com:8443/cas/logout";
    }
}
