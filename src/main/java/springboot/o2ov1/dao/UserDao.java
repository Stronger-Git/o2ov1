package springboot.o2ov1.dao;


import springboot.o2ov1.entity.User;

public interface UserDao {
    User selectById(Long id);
    int insertUser(User user);
}
