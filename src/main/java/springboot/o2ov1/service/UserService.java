package springboot.o2ov1.service;


import springboot.o2ov1.entity.User;

/**
 * @author WuChangJian
 * @date 2020/2/26 21:35
 */
public interface UserService {
    User getUserById(Long id);
}
