package cn.test.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zhiwen on 2017/5/26.
 */
@Controller
public class UserController {
    @RequestMapping(value = "")
    public String index() {
        return "index";
    }
}
