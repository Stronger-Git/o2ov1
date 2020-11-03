package springboot.o2ov1.interceptor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import springboot.o2ov1.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author WuChangJian
 * @date 2020/3/27 18:43
 * 店家管理系统登录验证拦截器
 */
public class ShopInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(ShopInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("user");
        logger.debug("session-->"+request.getSession().getId());
        logger.debug("user--->"+user);
        if (user != null) {
            // 对用户身份进行判断
            User user0 = (User) user;
            if (user0 != null && user0.getUserId() != null && user0.getUserType() == 2 && user0.getEnableStatus() == 1) {
                // 验证成功则返回true 放行
                return true;
            }
        }
        // 若不满足登录验证，则直接跳转到账号登录页面
        response.sendRedirect(request.getContextPath() + "/local/login?userType=2");
        return false;
    }
}
