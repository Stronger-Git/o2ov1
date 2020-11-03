package springboot.o2ov1.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.o2ov1.cache.JedisUtil;
import springboot.o2ov1.dao.ShopCategoryDao;
import springboot.o2ov1.entity.ShopCategory;
import springboot.o2ov1.service.ShopCategoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
    @Autowired
    private ShopCategoryDao categoryDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;

    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory category) {
        String key = SHOPCATEGORY_KEY;
        List<ShopCategory> shopCategories = null;
        ObjectMapper mapper = null;
        if (category != null) {
            // 1)全部商店 2)指定菜单下的子菜单
            if (category.getParent() != null && category.getParent().getShopCategoryId() != null) {
                key = key + "_parent" + category.getParent().getShopCategoryId();
            } else {
                key = key + "_all_second_level";
            }
        } else {
            key = key + "_all_first_level";
        }
        String value = null;
        if (!jedisKeys.exists(key)) {
            mapper = new ObjectMapper();
            shopCategories = categoryDao.selectByShopCategory(category);
            try {
                value = mapper.writeValueAsString(shopCategories);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            jedisStrings.set(key, value);
        } else {
            mapper = new ObjectMapper();
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
            value = jedisStrings.get(key);
            try {
                shopCategories = mapper.readValue(value, javaType);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return shopCategories;
    }
}
