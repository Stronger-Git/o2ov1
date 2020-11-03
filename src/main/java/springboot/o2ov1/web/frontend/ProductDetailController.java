package springboot.o2ov1.web.frontend;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springboot.o2ov1.dto.ProductExecution;
import springboot.o2ov1.enums.ProductEnum;
import springboot.o2ov1.service.ProductService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ProductDetailController {
    @Autowired
    private ProductService productService;
    @RequestMapping(value = "/listproductdetail", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listProductDetail(Long productId) {
        Map<String, Object> modelMap = new HashMap<>();
        if (productId != null) {
            ProductExecution productExecution = productService.getProduct(productId);
            if (productExecution.getState() == ProductEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
                modelMap.put("product", productExecution.getProduct());
            } else {
                modelMap.put("success", false);
                modelMap.put("product", productExecution.getStateInfo());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty productId");
        }
        return modelMap;
    }
}
