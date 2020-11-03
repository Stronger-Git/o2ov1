package springboot.o2ov1.dto;


import springboot.o2ov1.entity.Shop;
import springboot.o2ov1.enums.ShopStateEnum;

import java.util.List;

/**
 * 存储店铺信息，结果状态和状态标识
 * dto包的含义也就随之而解了
 */
public class ShopExecution {
    private int state;
    private String stateInfo;
    // 店铺数量
    private long count;
    // 操作的shop(增删改店铺的时候用到)
    private Shop shop;
    // shop列表(查询店铺列表的时候使用)
    private List<Shop> shopList;

    public ShopExecution(){}

    // 店铺操作失败使用的构造器
    public ShopExecution(ShopStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    // 店铺操作成功使用的构造器
    public ShopExecution(ShopStateEnum stateEnum, Shop shop) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shop = shop;
    }

    public ShopExecution(ShopStateEnum stateEnum, List<Shop> shopList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopList = shopList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
