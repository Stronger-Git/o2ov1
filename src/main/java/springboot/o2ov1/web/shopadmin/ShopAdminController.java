package springboot.o2ov1.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "shopadmin", method = RequestMethod.GET)
public class ShopAdminController {

    @RequestMapping("/shopoperation")
    public String shopOperation() {
        return "shop/shopoperation";
    }
    @RequestMapping("/shoplist")
    public String shopList() {
        return "shop/shoplist";
    }
    @RequestMapping("/shopmanage")
    public String shopManage() {
        return "shop/shopmanagement";
    }
    @RequestMapping("/productcategorymanage")
    public String productCategoryManage() {
        return "shop/productcategorylist";
    }
    @RequestMapping("/productmanagement")
    public String productManagement() {
        return "shop/productmanagement";
    }
    @RequestMapping("/productoperation")
    public String productManage() {
        return "shop/productoperation";
    }
}
