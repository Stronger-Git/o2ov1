package springboot.o2ov1.web.wechat;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springboot.o2ov1.dto.UserAccessToken;
import springboot.o2ov1.dto.WeChatAuthExecution;
import springboot.o2ov1.dto.WechatUser;
import springboot.o2ov1.entity.User;
import springboot.o2ov1.entity.WeChatAuth;
import springboot.o2ov1.enums.WeChatAuthEnum;
import springboot.o2ov1.service.UserService;
import springboot.o2ov1.service.WeChatAuthService;
import springboot.o2ov1.util.wechat.WechatUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 获取关注公众号之后的微信用户信息的接口，如果在微信浏览器里访问 通过微信开发者工具访问
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe1df25dad9a4b6e3&redirect_uri=http://39.97.181.76/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 * 则这里将会获取到code,之后再可以通过code获取到access_token 进而获取到用户信息
 *测试号信息
 * appID
 * wxe1df25dad9a4b6e3
 * appsecret
 * 5cb17d576949c0d1ee865b2f1c8fbc35
 *
 */
@Controller
@RequestMapping("/wechatlogin")
public class WechatLoginController {

    private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);
    private static final String FRONTEND = "1";
    private static final String SHOPEND = "2";

    @Autowired
    private WeChatAuthService weChatAuthService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/logincheck", method = { RequestMethod.GET })
    public String doGet(HttpServletRequest request, HttpServletResponse response) {
        log.debug("weixin login get...");
        // 获取微信公众号传输过来的code,通过code可获取access_token,进而获取用户信息
        String code = request.getParameter("code");
        // 这个state可以用来传我们自定义的信息，方便程序调用，这里也可以不用
        // 若state=1 则跳转到前台 2 跳转到店家管理界面
        String state = request.getParameter("state");
        // String roleType = request.getParameter("state");
        log.debug("weixin login code:" + code);
        WechatUser user = null;
        WeChatAuth auth = null;
        String openId = null;
        if (null != code) {
            UserAccessToken token;
            try {
                // 通过code获取access_token
                token = WechatUtil.getUserAccessToken(code);
                log.debug("weixin login token:" + token.toString());
                // 通过token获取accessToken
                String accessToken = token.getAccessToken();
                // 通过token获取openId
                openId = token.getOpenId();
                // 通过access_token和openId获取用户昵称等信息
                user = WechatUtil.getUserInfo(accessToken, openId);
                log.debug("weixin login user:" + user.toString());
                request.getSession().setAttribute("openId", openId);
                auth = weChatAuthService.getByOpenId(openId);
            } catch (IOException e) {
                log.error("error in getUserAccessToken or getUserInfo or findByOpenId: " + e.toString());
                e.printStackTrace();
            }
        }
        // ======todo begin======
        // 前面咱们获取到openId后，可以通过它去数据库判断该微信帐号是否在我们网站里有对应的帐号了，
        // 没有的话这里可以自动创建上，直接实现微信与咱们网站的无缝对接。
        // ======todo end======
        if (auth == null) {
            User u = WechatUtil.getUserFromWeChat(user);
            if (FRONTEND.equals(state))
                u.setUserType(1);
            else
                u.setUserType(2);
            auth = new WeChatAuth();
            auth.setOpenId(openId);
            auth.setUser(u);
            WeChatAuthExecution weChatAuthExecution = weChatAuthService.addWeChatAuth(auth);
            if (weChatAuthExecution.getState() != WeChatAuthEnum.SUCCESS.getState()) {
                return null;
            } else {
                request.getSession().setAttribute("user", userService.getUserById(auth.getUser().getUserId()));
            }
        }
        // 若用户点击的是前端展示系统按钮则进入前端
        if (FRONTEND.equals(state)) {
            // 获取到微信验证的信息后返回到指定的路由（需要自己设定）
            return "frontend/index";
        } else {
            return "shop/shoplist";
        }
    }
}
