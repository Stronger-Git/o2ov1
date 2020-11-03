package springboot.o2ov1.dao;

import org.apache.ibatis.annotations.Param;
import springboot.o2ov1.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryDao {
    List<ShopCategory> selectByShopCategory(@Param("category") ShopCategory category);
}
