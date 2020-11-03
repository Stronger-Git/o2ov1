package springboot.o2ov1.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author WuChangJian
 * @date 2020/3/18 17:23
 */
@Controller
@RequestMapping("/local")
public class LocalController {
    @RequestMapping(value = "/accountbind")
    public String accountBind() {
        return "local/accountbind";
    }

    @RequestMapping(value = "/updatepwd")
    public String updatelocalpwd() {
        return "local/updatepwd";
    }

    @RequestMapping(value = "/login")
    public String logout() {
        return "local/login";
    }
}
