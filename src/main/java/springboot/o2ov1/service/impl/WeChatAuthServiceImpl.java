package springboot.o2ov1.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.o2ov1.dao.UserDao;
import springboot.o2ov1.dao.WeChatAuthDao;
import springboot.o2ov1.dto.WeChatAuthExecution;
import springboot.o2ov1.entity.User;
import springboot.o2ov1.entity.WeChatAuth;
import springboot.o2ov1.enums.WeChatAuthEnum;
import springboot.o2ov1.service.WeChatAuthService;

import java.util.Date;

/**
 * @author WuChangJian
 * @date 2020/2/26 21:21
 */
@Service
public class WeChatAuthServiceImpl implements WeChatAuthService {
    @Autowired
    private WeChatAuthDao weChatAuthDao;
    @Autowired
    private UserDao userDao;
    @Override
    public WeChatAuth getByOpenId(String openId) {
        return weChatAuthDao.selectByOpenId(openId);
    }

    @Override
    @Transactional
    public WeChatAuthExecution addWeChatAuth(WeChatAuth weChatAuth) {
        weChatAuth.setCreateTime(new Date());
        // 如果微信账号里存在用户信息但用户ID为空，则认为该用户是第一次登陆
        try {
            if (weChatAuth.getOpenId() != null && weChatAuth.getUser().getUserId() == null) {
                User user =  weChatAuth.getUser();
                user.setEnableStatus(1);
                int i = userDao.insertUser(user);
                if (i <= 0) {
                    throw new RuntimeException("插入用户信息失败");
                }
                weChatAuth.setUser(user);
            }
            int i = weChatAuthDao.insertWeChatAuth(weChatAuth);
            if (i <= 0) {
                throw new RuntimeException("创建微信用户失败");
            } else {
                return new WeChatAuthExecution(WeChatAuthEnum.SUCCESS, weChatAuth);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new WeChatAuthExecution(WeChatAuthEnum.FAIL);
        }
    }
}
