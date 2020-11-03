package springboot.o2ov1.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.o2ov1.dao.LocalAuthDao;
import springboot.o2ov1.dto.LocalAuthExecution;
import springboot.o2ov1.entity.LocalAuth;
import springboot.o2ov1.enums.LocalAuthEnum;
import springboot.o2ov1.service.LocalAuthService;
import springboot.o2ov1.util.MD5;

import java.util.Date;

/**
 * @author WuChangJian
 * @date 2020/3/17 16:46
 */
@Service
public class LocalAuthServiceImpl implements LocalAuthService {
    @Autowired
    private LocalAuthDao authDao;
    @Override
    public LocalAuth getLocalAuthByNameAndPwd(String username, String password) {
        return authDao.selectByUsernameAndPwd(username, MD5.getMd5(password));
    }

    @Override
    public LocalAuth getLocalAuthByUserId(Long userId) {
        return authDao.selectByUserId(userId);
    }

    @Transactional
    @Override
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) {
        // 判空，特别是userId
        if (localAuth == null || localAuth.getPassword() == null || localAuth.getUsername() == null || localAuth.getUser().getUserId() == null) {
            return new LocalAuthExecution(LocalAuthEnum.NULL_AUTH_INFO);
        }
        // 查询userId是否已经绑定了平台账号,如果绑定过则直接退出,保证平台账号的唯一性
        LocalAuth tempLocalAuth = authDao.selectByUserId(localAuth.getUser().getUserId());
        if (tempLocalAuth != null) {
            return new LocalAuthExecution(LocalAuthEnum.ONLY_ONE_ACCOUNT);
        }
        try {
            // 若果之前没有绑定过平台账号，则创建一个平台账号与该用户绑定
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());
            // 对密码进行MD5加密
            localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
            int result = authDao.insert(localAuth);
            if (result < 1) {
                return new LocalAuthExecution(LocalAuthEnum.LOCAL_AUTH_BIND_FAIL);
            } else {
                return new LocalAuthExecution(LocalAuthEnum.LOCAL_AUTH_BIND_SUCCESS, localAuth);
            }
        } catch (Exception e) {
            throw new RuntimeException("LocalAuthService" + e.getMessage());
        }
    }

    @Override
    public LocalAuthExecution modifyLocalAuth(Long userId, String password, String newPassword) {
        if (userId != null && password != null && newPassword != null && !password.equals(newPassword)) {
            // 更新密码并对新密码进行MD5加密
            int result = authDao.updateUsernameAndPwd(userId, MD5.getMd5(password), MD5.getMd5(newPassword), new Date());
            if (result < 1) {
                return new LocalAuthExecution(LocalAuthEnum.LOCAL_AUTH_UPDATE_FAIL);
            } else {
                return new LocalAuthExecution(LocalAuthEnum.LOCAL_AUTH_UPDATE_SUCCESS);
            }
        } else {
            return new LocalAuthExecution(LocalAuthEnum.NULL_AUTH_INFO);
        }
    }

}
