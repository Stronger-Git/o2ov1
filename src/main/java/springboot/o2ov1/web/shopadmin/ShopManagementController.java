package springboot.o2ov1.web.shopadmin;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import springboot.o2ov1.dto.ImageHolder;
import springboot.o2ov1.dto.Result;
import springboot.o2ov1.dto.ShopExecution;
import springboot.o2ov1.entity.Area;
import springboot.o2ov1.entity.Shop;
import springboot.o2ov1.entity.ShopCategory;
import springboot.o2ov1.entity.User;
import springboot.o2ov1.enums.ShopStateEnum;
import springboot.o2ov1.service.AreaService;
import springboot.o2ov1.service.ShopCategoryService;
import springboot.o2ov1.service.ShopService;
import springboot.o2ov1.util.CodeUtil;
import springboot.o2ov1.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/shopadmin")
public class ShopManagementController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService categoryService;
    @Autowired
    private AreaService areaService;

    /**
     * 从session根据ID查找shop，若查找到则保存到session中，否则调用底层根据shopId查询shop并保存
     * 保证之后的操作都是在该店铺下进行的
     * @param shopId
     * @param session
     */
    @RequestMapping(value = "/saveShopInfoToSession", method = RequestMethod.GET)
    @ResponseBody
    public Result saveShopInfoToSession(@RequestParam("shopId") Integer shopId, HttpSession session) {
        if (shopId != null) {
            Shop currentShop = (Shop) session.getAttribute("shop" + shopId.toString());
            if (currentShop != null) {
                session.setAttribute("currentShop", currentShop);
                return new Result<String>(true, currentShop.getShopId().toString());
            }
            ShopExecution se = shopService.getShopById((long) shopId);
            if (se.getState() == 1) {
                session.setAttribute("currentShop", se.getShop());
                return new Result<String>(true, se.getShop().getShopId().toString());
            }
        } else {
            Shop shop = (Shop) session.getAttribute("currentShop");
            Result result = null;
            if (shop != null) {
                result =  new Result<String>(true, shop.getShopId().toString());
            } else
                result =  new Result<String>(false, "内部系统错误");
            return result;
        }
        return null;
    }

    /*
        两种情况：
            一种是进入店铺后，直接点击店铺信息，这时候地址栏url包含shopid
            另一种是进入店铺后，点击商品管理，进入商品管理后返回，这时候url不包含该shopid
            但是这个shop已经存在了，所以说如果再次点击店铺信息，取到的shopId为null，就相当于执行注册店铺
            的操作，而不是修改该店铺信息了，原本的意愿是修改该店铺信息
            所以说应该从session中取然后再做判断
     */
    @RequestMapping(value = "getshopbyid", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        int shopId = HttpServletRequestUtil.getInt(request, "shopId");
        if (shopId > -1) {
            ShopExecution se = shopService.getShopById((long) shopId);
            List<Area> areaList = areaService.getAreaList();
            if (se.getState() == 1) {
                modelMap.put("success", true);
                modelMap.put("areaList", areaList);
                modelMap.put("shop", se.getShop());
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", se.getStateInfo());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "ShopId empty");
            return modelMap;
        }
    }
    @RequestMapping(value = "getshopinitinfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<>();
        List<Area> areaList = areaService.getAreaList();
        List<ShopCategory> shopCategoryList = categoryService.getShopCategoryList(null);
        modelMap.put("areaList", areaList);
        modelMap.put("shopCategoryList", shopCategoryList);
        modelMap.put("success", true);
        return modelMap;
    }

    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // String shopStr = request.getParameter("shopStr");
        // verify code
        boolean codeRes = CodeUtil.checkVerifyCode(request);
        if (!codeRes) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码不正确");
            return modelMap;
        }
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper objectMapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = objectMapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiReq = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multiReq.getFile("shopImg");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }
        // 注册店铺
        if (shop != null && shopImg != null) {
            User user = (User) request.getSession().getAttribute("user");
            shop.setOwner(user);
            ShopExecution se = null;
            try {
                ImageHolder thumbnail = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
                se = shopService.addShop(shop, thumbnail);
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
            if (se.getState() == ShopStateEnum.CHECK.getState()) {
                // 将创建的店铺信息保存到session中 以后取得时候根据shopID值取
                List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                if (shopList == null || shopList.size() == 0) {
                    shopList = new ArrayList<>();
                }
                shopList.add(se.getShop());
                request.getSession().setAttribute("shopList" , se.getShop());
                request.getSession().setAttribute("shop"+se.getShop().getShopId().toString(), se.getShop());

                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", se.getStateInfo());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
    }
    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        boolean codeRes = CodeUtil.checkVerifyCode(request);
        if (!codeRes) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码不正确");
            return modelMap;
        }
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper objectMapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = objectMapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiReq = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multiReq.getFile("shopImg");
        }
        if (shop != null && shop.getShopId() != null) {
            User user = (User) request.getSession().getAttribute("user");
            shop.setOwner(user);
            ShopExecution se = null;
            try {
                if (shopImg == null) {
                    se = shopService.modifyShop(shop, null);
                } else {
                    ImageHolder thumbnail = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
                    se = shopService.modifyShop(shop, thumbnail);
                }
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
            if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", se.getStateInfo());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺ID");
            return modelMap;
        }
    }
    @RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        User user = (User) request.getSession().getAttribute("user");
        Shop shop = new Shop();
        shop.setOwner(user);
        // 保留了分页功能 并没有去用
        ShopExecution se = shopService.getShopList(shop, 0, 100);
        if (se.getState() == 1) {
            modelMap.put("success", true);
            modelMap.put("shopList", se.getShopList());
            // 存入session 作为权限验证依据
            request.getSession().setAttribute("shopList", se.getShopList());
            modelMap.put("user", user);
            modelMap.put("count", se.getCount());
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", se.getState());
        }
        return modelMap;
    }

}
