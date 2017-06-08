package cn.test.user.controller;

import cn.test.user.form.LoginForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by zhiwen on 2017/5/26.
 */
@Controller
public class UserController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "")
    public String index() {
        logger.info("access index");
        return "index";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    public String login(Model model) {
        logger.info("access login");
        return "user/login";

    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public void doLogin(LoginForm loginForm, HttpServletRequest request, HttpServletResponse response) {
        logger.info("do login");
        HttpSession session = request.getSession();
        session.setAttribute("name", loginForm.getName());
    }
}
