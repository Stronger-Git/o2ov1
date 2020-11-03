package springboot.o2ov1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.o2ov1.dao.UserDao;
import springboot.o2ov1.entity.User;
import springboot.o2ov1.service.UserService;


/**
 * @author WuChangJian
 * @date 2020/2/26 21:37
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(Long id) {
        return userDao.selectById(id);
    }
}
