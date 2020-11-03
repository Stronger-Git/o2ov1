package springboot.o2ov1.web.shopadmin;


import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springboot.o2ov1.dto.ProductCategoryExecution;
import springboot.o2ov1.dto.Result;
import springboot.o2ov1.entity.ProductCategory;
import springboot.o2ov1.entity.Shop;
import springboot.o2ov1.enums.ProductCategoryEnum;
import springboot.o2ov1.service.ProductCategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;
    @RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
    @ResponseBody
    private Result<List<ProductCategory>> getProductCategoryList(HttpSession session) {
        Shop shopSession = (Shop) session.getAttribute("currentShop");
        List<ProductCategory> productCategories = productCategoryService.getProductCategoryList(shopSession.getShopId());
        if (productCategories != null && productCategories.size() > 0) {
            return new Result<>(true, productCategories);
        } else {
            return new Result<>(false, ProductCategoryEnum.INNER_ERROR.getState(), ProductCategoryEnum.INNER_ERROR.getStateInfo());
        }
    }

    @RequestMapping(value = "/addproductcategories", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addProductCategories(@RequestBody List<ProductCategory> productCategories, HttpSession session) {
        // Session一般放一些重要的信息，利用session前后端还可以很好的解耦，比如说提前将店铺实体
        // 存入session，那么之后在提取店铺信息的时候，不在依赖于前端传过来的shop实体的相关信息
        // 直接获取当前session会话中的shop信息即可，因为session不可以跨浏览器
        Shop shop = (Shop) session.getAttribute("currentShop");
        Map<String, Object> modelMap = new HashMap<>();
        if (productCategories != null && productCategories.size() > 0) {
            for (ProductCategory productCategory : productCategories) {
                // shopId不在依赖于前端 后端为它赋值
                productCategory.setShopId(shop.getShopId());
            }
            ProductCategoryExecution productCategoryExecution = productCategoryService.addProductCategories(productCategories);
            if (productCategoryExecution.getState() == ProductCategoryEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", ProductCategoryEnum.INNER_ERROR.getStateInfo());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", ProductCategoryEnum.NULL_PRODUCTCATEORIES);
            return modelMap;
        }
    }
    @RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> removeProductCategory(@Param("productCategoryId") Long productCategoryId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (productCategoryId != null) {
            Shop shopSession = (Shop) request.getSession().getAttribute("currentShop");
            if (shopSession.getShopId() != null) {
                ProductCategoryExecution productCategoryExecution = productCategoryService.removeProductCategory(productCategoryId, shopSession.getShopId());
                if (productCategoryExecution.getState() == ProductCategoryEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", ProductCategoryEnum.INNER_ERROR);
                }
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", ProductCategoryEnum.NULL_SHOPID);
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", ProductCategoryEnum.FAIL);
            return modelMap;
        }
    }
}
