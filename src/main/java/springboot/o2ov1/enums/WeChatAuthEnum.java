package springboot.o2ov1.enums;

/**
 * @author WuChangJian
 * @date 2020/2/26 21:08
 */
public enum WeChatAuthEnum {
    SUCCESS(1, "注册成功"), FAIL(0, "注册失败"),EMPTY_ID(-1, "open_id不能为空");
    private int state;
    private String stateInfo;
    WeChatAuthEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
    public static WeChatAuthEnum getStateInfo(int state) {
        for (WeChatAuthEnum value : values()) {
            if (value.state == state)
                return value;
        }
        return null;
    }
}
