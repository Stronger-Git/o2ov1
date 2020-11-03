package springboot.o2ov1.enums;

/**
 * @author WuChangJian
 * @date 2020/3/17 16:33
 */
public enum LocalAuthEnum {
    NULL_AUTH_INFO(-1, "用户信息不能为空"),ONLY_ONE_ACCOUNT(0, "用户不唯一"),LOCAL_AUTH_BIND_SUCCESS(1, "绑定成功"),
    LOCAL_AUTH_UPDATE_SUCCESS(2, "更新成功"),LOCAL_AUTH_BIND_FAIL(-2, "绑定本地账号失败"),LOCAL_AUTH_UPDATE_FAIL(-3, "更新本地账号失败");
    private int state;
    private String stateInfo;

    LocalAuthEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public LocalAuthEnum stageOf(int state) {
        for (LocalAuthEnum value : values()) {
            if (value.getState() == state) {
                return value;
            }
        }
        return null;
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
}
