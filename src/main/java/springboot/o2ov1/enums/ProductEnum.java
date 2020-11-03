package springboot.o2ov1.enums;

public enum ProductEnum {
    SUCCESS(1, "操作成功"), INNER_ERROR(-1001, "内部系统错误"),
    EMPTY(-1003, "产品信息为空"), FAIL(-1, "操作失败");
    private int state;
    private String stateInfo;

    ProductEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static ProductEnum stateOf(int state) {
        for (ProductEnum productEnum : values()) {
            if (productEnum.getState() == state)
                return productEnum;
        }
        return null;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public int getState() {
        return state;
    }
}
