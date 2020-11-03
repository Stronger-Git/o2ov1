package springboot.o2ov1.dao;

import org.apache.ibatis.annotations.Param;
import springboot.o2ov1.entity.Shop;

import java.util.List;


public interface ShopDao {
    int insertShop(Shop shop);
    int updateShop(Shop shop);
    Shop selectById(Long id);

    /**
     * 查询店家所注册的所有店铺 (分页查询）
     * 商家和店铺一对多的关系
     * 商家可以根据店铺类别 所属区域 店铺名称来搜索自己相应的店铺
     * @param shop shop实体
     * @param offset 偏移量
     * @param limit 行数
     * @return
     */
    List<Shop> selectAllByShop(@Param("shop") Shop shop, @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 根据商家的同等条件查询出所有的数量 对分页来说相当于总量
     * @param shop
     * @return
     */
    Long selectCountByShop(Shop shop);
}
