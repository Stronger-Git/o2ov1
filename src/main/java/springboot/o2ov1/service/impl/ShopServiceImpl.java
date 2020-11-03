package springboot.o2ov1.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.o2ov1.dao.ShopDao;
import springboot.o2ov1.dto.ImageHolder;
import springboot.o2ov1.dto.ShopExecution;
import springboot.o2ov1.entity.Shop;
import springboot.o2ov1.enums.ShopStateEnum;
import springboot.o2ov1.service.ShopService;
import springboot.o2ov1.util.ImageUtil;
import springboot.o2ov1.util.PathUtil;

import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;

    /**
     * 添加店铺
     * 业务逻辑：
     * 判空 设置店铺基本信息（注册店铺时，没有暴露在页面的字段，
     * 比如设置enableStatus，createTime..) dao插入shop信息后获得自增主键值
     * 商家图片处理 获得url更新shop image_addr字段
     * 注：上述操作必须要有事务支持，每一步有错误，就算更新失败，必须依赖spring
     * 事务管理
     * @return dto
     */
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, ImageHolder thumbnail) {
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            shop.setEnableStatus(ShopStateEnum.CHECK.getState());
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            int effectRows = shopDao.insertShop(shop);
            if (effectRows <= 0) {
                // 注册失败 必须抛出RuntimeException才可以得到spring的事务管理
                throw new RuntimeException("添加店铺失败");
            } else {
                storeImage(shop, thumbnail);
                effectRows = shopDao.updateShop(shop);
                if (effectRows <= 0) {
                    // 注册失败 必须抛出RuntimeException才可以得到spring的事务管理
                    throw new RuntimeException("插入图片失败");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("addShop error:" + e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    @Override
    public ShopExecution getShopById(Long id) {
        Shop shop = shopDao.selectById(id);
        return new ShopExecution(ShopStateEnum.SUCCESS, shop);
    }

    /**
     * 更新店铺信息
     * 约定店铺类型不可修改 若上传了店铺图片则更新店铺图片
     * 并将以前的图片删除
     * @param shop 店铺实体
     * @param thumbnail 店铺缩略图信息包含了IO流和图片名称
     * @return
     */
    @Override
    public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) {
        ShopExecution se = null;
        if (shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOPID);
        } else {
            if (thumbnail != null) {
                Shop sp = shopDao.selectById(shop.getShopId());
                String imgPath = sp.getShopImg();
                ImageUtil.deleteFile(imgPath);
                storeImage(shop, thumbnail);
            }
            shop.setLastEditTime(new Date());
            int effectedNum = shopDao.updateShop(shop);
            if (effectedNum <= 0) {
                se = new ShopExecution(ShopStateEnum.INNER_ERROR);
            } else {
                se = new ShopExecution(ShopStateEnum.SUCCESS, shop);
            }
            return se;
        }
    }

    @Override
    public ShopExecution getShopList(Shop shop, Integer pageIndex, Integer pageSize) {
        ShopExecution se = new ShopExecution();
        if (shop !=null) {
            Integer offset = pageIndexToOffset(pageIndex, pageSize);
            List<Shop> shops = shopDao.selectAllByShop(shop, offset, pageSize);
            Long count = shopDao.selectCountByShop(shop);
            if (shops != null && shops.size() >0) {
                se.setShopList(shops);
                se.setCount(count);
                se.setState(ShopStateEnum.SUCCESS.getState());
            }
        } else {
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return se;
    }

    private Integer pageIndexToOffset(Integer pageIndex, Integer pageSize) {
        return pageIndex < 1?0 : (pageIndex - 1)*pageSize;
    }

    /**
     * 保存店家商铺图片
     * @param shop
     * @param thumbnail
     */
    private void storeImage(Shop shop, ImageHolder thumbnail) {
        String imagePath = ImageUtil.generateThumbnail(thumbnail, PathUtil.getShopImagePath(shop.getShopId()));
        shop.setShopImg(imagePath);
    }
}
