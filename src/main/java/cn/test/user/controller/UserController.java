package cn.test.user.controller;

import cn.test.user.form.LoginForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
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
    @RequestMapping(value = "")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    public String login(Model model) {
        return "user/login";

    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public void doLogin(LoginForm loginForm, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.setAttribute("name", loginForm.getName());
    }
}
