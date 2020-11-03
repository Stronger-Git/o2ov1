package springboot.o2ov1.web.frontend;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springboot.o2ov1.dto.ShopExecution;
import springboot.o2ov1.entity.Area;
import springboot.o2ov1.entity.Shop;
import springboot.o2ov1.entity.ShopCategory;
import springboot.o2ov1.enums.ShopStateEnum;
import springboot.o2ov1.service.AreaService;
import springboot.o2ov1.service.ShopCategoryService;
import springboot.o2ov1.service.ShopService;
import springboot.o2ov1.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ShopListController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;
    /*
        处理店铺列表页头信息 一二级类别 区域
     */
    @RequestMapping(value = "/listshoppageinfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listShopPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList = null;
        Long parentId = HttpServletRequestUtil.getLong(request, "parentId");
        try {
            if (parentId != -1) {
                // 首页某一个类别下的子类别
                ShopCategory shopCategory = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategory.setParent(parent);
                Shop shop = new Shop();
                shop.setShopCategory(shopCategory);
                shopCategoryList = shopCategoryService.getShopCategoryList(shopCategory);
            } else {
                // 首页 全部商店
                ShopCategory shopCategory = new ShopCategory();
                shopCategoryList = shopCategoryService.getShopCategoryList(shopCategory);
                shopCategory = null;
            }
            modelMap.put("shopCategoryList", shopCategoryList);
            List<Area> areaList = areaService.getAreaList();
            modelMap.put("areaList", areaList);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg",  e.getMessage());
        }
        return modelMap;
    }

    /**
     * 获取指定查询条件下的店铺列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/listshops", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listShops(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        if (pageIndex > -1 && pageSize > -1) {
            String shopName = HttpServletRequestUtil.getString(request, "shopName");
            long parentId = HttpServletRequestUtil.getLong(request, "parentId");
            long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
            int areaId = HttpServletRequestUtil.getInt(request, "areaId");
            Shop shop = combineQueryCondition(shopName, parentId, shopCategoryId, areaId);
            // TODO:查询的时候没有设置店铺状态，默认没有审核通过的也会查询到
            ShopExecution shopExecution = shopService.getShopList(shop, pageIndex, pageSize);
            if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
                modelMap.put("shops", shopExecution.getShopList());
                modelMap.put("count", shopExecution.getCount());
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", shopExecution.getStateInfo());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageIndex or pageSize");
        }
        return modelMap;
    }

    private Shop combineQueryCondition(String shopName, long parentId, long shopCategoryId, int areaId) {
        Shop shop = new Shop();
        ShopCategory shopCategory = null;
        ShopCategory parent = null;
        Area area = null;
        if (shopName != null && !"".equals(shopName))
            shop.setShopName(shopName);
        if (parentId != -1) {
            shopCategory = new ShopCategory();
            parent = new ShopCategory();
            parent.setShopCategoryId(parentId);
            shopCategory.setParent(parent);
            if (shopCategoryId != -1) {
                shopCategory.setShopCategoryId(shopCategoryId);
            }
        }
        if (areaId != -1) {
            area = new Area();
            area.setAreaId(areaId);
        }
        shop.setShopCategory(shopCategory);
        shop.setArea(area);
        // 前端展示的店铺都是审核成功的店铺
        shop.setEnableStatus(1);
        return shop;
    }
}
