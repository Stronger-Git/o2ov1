package springboot.o2ov1.service;


import springboot.o2ov1.dto.ImageHolder;
import springboot.o2ov1.dto.ShopExecution;
import springboot.o2ov1.entity.Shop;

public interface ShopService {
    ShopExecution addShop(Shop shop, ImageHolder thumbnail);
    ShopExecution getShopById(Long id);
    ShopExecution modifyShop(Shop shop, ImageHolder thumbnail);
    ShopExecution getShopList(Shop shop, Integer pageIndex, Integer pageSize);
}
