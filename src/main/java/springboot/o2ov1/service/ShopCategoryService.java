package springboot.o2ov1.service;


import springboot.o2ov1.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {
    String SHOPCATEGORY_KEY = "shop_category";
    List<ShopCategory> getShopCategoryList(ShopCategory category);
}
