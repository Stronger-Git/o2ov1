package springboot.o2ov1.interceptor;


import org.springframework.web.servlet.HandlerInterceptor;
import springboot.o2ov1.entity.Shop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author WuChangJian
 * @date 2020/3/27 19:42
 * 店家管理系统操作拦截器
 */
public class ShopPermissionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从session中获取当前选择的店铺
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 从session中获取当前用户可操作的店铺列表
        List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
        if (currentShop != null && shopList != null) {
            for (Shop shop : shopList) {
                // 如果当前店铺在可操作性的列表里则返回true，进行接下来的操作
                if (shop.getShopId() == currentShop.getShopId()) {
                    return true;
                }
            }
        }
        return false;
    }
}
