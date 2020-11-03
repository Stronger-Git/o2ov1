package springboot.o2ov1.web.frontend;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springboot.o2ov1.entity.Headline;
import springboot.o2ov1.entity.ShopCategory;
import springboot.o2ov1.service.HeadlineService;
import springboot.o2ov1.service.ShopCategoryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class MainPageController {

    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private HeadlineService headlineService;

    /**
     * 初始化前端展示系统的主页信息，包括获取一级店铺类别列表以及头条列表
     * @return
     */
    @RequestMapping(value = "/listmainpageinfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listMainPageInfo() {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategoryList(null);
            modelMap.put("shopCategoryList", shopCategoryList);
            Headline headline = new Headline();
            headline.setEnableStatus(1);
            List<Headline> headlineList = headlineService.getHeadlineList(headline);
            modelMap.put("headlineList", headlineList);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }
}
