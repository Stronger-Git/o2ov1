package springboot.o2ov1.service;


import springboot.o2ov1.dto.LocalAuthExecution;
import springboot.o2ov1.entity.LocalAuth;

/**
 * @author WuChangJian
 * @date 2020/3/17 16:28
 */
public interface LocalAuthService {
    LocalAuth getLocalAuthByNameAndPwd(String username, String password);
    LocalAuth getLocalAuthByUserId(Long userId);
    /**
     * 关联本地用户
     * 业务逻辑：根据localAuth里面设置的userId去调用LocalAuthDao下的selectByUserId方法查找
     * 当前userId是否已经绑定了本地用户，目的保证平台账户唯一性，若未绑定，执行绑定操作。
     * @param localAuth
     * @return LocalAuthExecution
     */
    LocalAuthExecution bindLocalAuth(LocalAuth localAuth);
    LocalAuthExecution modifyLocalAuth(Long userId, String password, String newPassword);
}
