package springboot.o2ov1.service;


import springboot.o2ov1.dto.ProductCategoryExecution;
import springboot.o2ov1.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategory> getProductCategoryList(Long shopId);
    ProductCategoryExecution addProductCategories(List<ProductCategory> productCategories);
    ProductCategoryExecution removeProductCategory(Long productCategoryId, Long shopId);
}
