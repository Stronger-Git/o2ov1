package springboot.o2ov1.dto;

import springboot.o2ov1.entity.WeChatAuth;
import springboot.o2ov1.enums.WeChatAuthEnum;

/**
 * @author WuChangJian
 * @date 2020/2/26 21:07
 */
public class WeChatAuthExecution {
    private int state;
    private String stateInfo;

    public WeChatAuth getWeChatAuth() {
        return weChatAuth;
    }

    public void setWeChatAuth(WeChatAuth weChatAuth) {
        this.weChatAuth = weChatAuth;
    }

    private WeChatAuth weChatAuth;

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

    public WeChatAuthExecution() {}

    public WeChatAuthExecution(WeChatAuthEnum weChatAuthEnum) {
        this.state = weChatAuthEnum.getState();
        this.stateInfo = weChatAuthEnum.getStateInfo();
    }

    public WeChatAuthExecution(WeChatAuthEnum weChatAuthEnum, WeChatAuth weChatAuth) {
        this.state = weChatAuthEnum.getState();
        this.stateInfo = weChatAuthEnum.getStateInfo();
        this.weChatAuth = weChatAuth;
    }
}
