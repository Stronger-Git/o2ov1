package springboot.o2ov1.dao;

import org.apache.ibatis.annotations.Param;
import springboot.o2ov1.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryDao {
    List<ProductCategory> selectByShopId(Long shopId);
    int insertBatch(List<ProductCategory> productCategories);
    int deleteById(@Param("proCatId") Long proCatId, @Param("shopId") Long shopId);
}
