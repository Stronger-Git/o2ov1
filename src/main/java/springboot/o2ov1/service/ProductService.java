package springboot.o2ov1.service;


import springboot.o2ov1.dto.ImageHolder;
import springboot.o2ov1.dto.ProductExecution;
import springboot.o2ov1.entity.Product;

import java.util.List;

public interface ProductService {
    ProductExecution getAllProductsByProduct(Product product, int pageIndex, int pageSize);
    ProductExecution getProduct(Long productId);
    /**
     * 添加产品 表单包括产品基本信息和图片列表
     * 在没有重构之前 Product 实体包含 List<ProductImage>
     * 函参列表参数就很多，不利于处理，所以说这时候提取代码，参数进行一步封装
     */
    //addProduct(Product product, InputStream thumbnail, String thumbnailName, List<InputStream> imgs, List<String> imgsName);
    ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> imageList);

    /**
     *更新商品信息以及图片处理
     */
    ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> imageList);

}
