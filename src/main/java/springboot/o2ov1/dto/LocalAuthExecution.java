package springboot.o2ov1.dto;


import springboot.o2ov1.entity.LocalAuth;
import springboot.o2ov1.enums.LocalAuthEnum;

import java.util.List;

/**
 * @author WuChangJian
 * @date 2020/3/17 16:30
 */
public class LocalAuthExecution {
    private int state;
    private int count;
    private String stateInfo;
    private LocalAuth localAuth;
    private List<LocalAuth> localAuthList;

    public LocalAuthExecution() {
    }
    public LocalAuthExecution(LocalAuthEnum localAuthEnum, LocalAuth localAuth) {
        this.state = localAuthEnum.getState();
        this.stateInfo = localAuthEnum.getStateInfo();
        this.localAuth = localAuth;
    }

    /**
     * 暂时还没有用到，估计后台管理界面需要用到，因为需要对所有人员进行管理
     * @param localAuthEnum
     * @param localAuthList
     */
    public LocalAuthExecution(LocalAuthEnum localAuthEnum, List<LocalAuth> localAuthList) {
        this.state = localAuthEnum.getState();
        this.stateInfo = localAuthEnum.getStateInfo();
        this.localAuthList = localAuthList;
    }

    public LocalAuthExecution(LocalAuthEnum nullAuthInfo) {
        this.state = nullAuthInfo.getState();
        this.stateInfo = nullAuthInfo.getStateInfo();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public LocalAuth getLocalAuth() {
        return localAuth;
    }

    public void setLocalAuth(LocalAuth localAuth) {
        this.localAuth = localAuth;
    }

    public List<LocalAuth> getLocalAuthList() {
        return localAuthList;
    }

    public void setLocalAuthList(List<LocalAuth> localAuthList) {
        this.localAuthList = localAuthList;
    }
}
