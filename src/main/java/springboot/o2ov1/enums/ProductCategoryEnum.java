package springboot.o2ov1.enums;

public enum ProductCategoryEnum {
    SUCCESS(1, "操作成功"), INNER_ERROR(-1001, "内部系统错误"),
    NULL_SHOPID(-1002, "ShopId为空"), NULL_PRODUCTCATEORIES(-1003, "产品类别为空"),
    FAIL(-1, "操作失败");
    private int state;
    private String stateInfo;
    ProductCategoryEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
    public static ProductCategoryEnum stateOf(int state) {
        for (ProductCategoryEnum item : values()) {
            if (item.getState() == state) {
                return item;
            }
        }
        return null;
    }
}
