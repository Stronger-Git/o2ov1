package springboot.o2ov1.web.local;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springboot.o2ov1.dto.LocalAuthExecution;
import springboot.o2ov1.entity.LocalAuth;
import springboot.o2ov1.entity.User;
import springboot.o2ov1.enums.LocalAuthEnum;
import springboot.o2ov1.service.LocalAuthService;
import springboot.o2ov1.util.CodeUtil;
import springboot.o2ov1.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WuChangJian
 * @date 2020/3/18 8:31
 */
@RestController
@RequestMapping("/local")
public class LocalAuthController {
    @Autowired
    private LocalAuthService authService;

    @RequestMapping(value = "/bindlocalauth", method = RequestMethod.POST)
    public Map<String, Object> bindLocalAuth(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errorMsg", "验证码错误");
            return modelMap;
        }
        String username = HttpServletRequestUtil.getString(request, "username");
        String password = HttpServletRequestUtil.getString(request, "password");
        User user = (User) request.getSession().getAttribute("user");
        if (user != null && user.getUserId() != null && username != null && password != null) {
            LocalAuth localAuth = new LocalAuth();
            localAuth.setUser(user);
            localAuth.setUsername(username);
            localAuth.setPassword(password);
            LocalAuthExecution localAuthExecution = authService.bindLocalAuth(localAuth);
            if (localAuthExecution.getState() == LocalAuthEnum.LOCAL_AUTH_BIND_SUCCESS.getState()) {
                modelMap.put("success", true);
                modelMap.put("localAuth", localAuthExecution.getLocalAuth());
            } else {
                modelMap.put("success", false);
                modelMap.put("errorMsg", localAuthExecution.getStateInfo());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errorMsg", "userId不允许为空");
            return modelMap;
        }
    }

    /**
     * 更新本地账户密码
     * 前台updatepwd.js做了一定的判断
     * @param request
     * @return
     */
    @RequestMapping(value = "/updatelocalpwd", method = RequestMethod.POST)
    public Map<String, Object> updateLocalPassword(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errorMsg", "验证码错误");
            return modelMap;
        }
        String username = HttpServletRequestUtil.getString(request, "username");
        String password = HttpServletRequestUtil.getString(request, "password");
        String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
        User user = (User) request.getSession().getAttribute("user");
        if (user != null && user.getUserId() != null && username != null&& password != null
                && !password.equals(newPassword)) {
            LocalAuth localAuth = authService.getLocalAuthByUserId(user.getUserId());
            // 判断当前操作是否是本地账号
            if (!localAuth.getUsername().equals(username)) {
                modelMap.put("success", false);
                modelMap.put("errorMsg", "当前操作的账号不属于该用户");
                return modelMap;
            }
            LocalAuthExecution localAuthExecution = authService.modifyLocalAuth(user.getUserId(), password, newPassword);
            if (localAuthExecution.getState() == LocalAuthEnum.LOCAL_AUTH_UPDATE_SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errorMsg", localAuthExecution.getStateInfo());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errorMsg", "新密码和原密码不能相同或者userId不能为空！");
            return modelMap;
        }
    }

    /**
     * 登录验证
     * 密码三次输入错误，要求输入验证码
     * @param request
     * @return
     */
    @RequestMapping(value = "/logincheck", method = RequestMethod.POST)
    public Map<String, Object> loginCheck(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
        if (needVerify && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errorMsg", "验证码错误");
            return modelMap;
        }
        String username = HttpServletRequestUtil.getString(request, "username");
        String password = HttpServletRequestUtil.getString(request, "password");
        if (username != null && password != null) {
            LocalAuth localAuth = authService.getLocalAuthByNameAndPwd(username, password);
            if (localAuth != null) {
                request.getSession().setAttribute("user", localAuth.getUser());
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errorMsg", "用户名或密码错误");
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errorMsg", "用户名或密码不能为空");
            return modelMap;
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Map<String, Object> logout(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        HttpSession session = request.getSession();
        session.setAttribute("user", null);
        modelMap.put("success", true);
        return modelMap;
    }
}