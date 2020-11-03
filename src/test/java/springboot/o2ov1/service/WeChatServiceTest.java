package springboot.o2ov1.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.o2ov1.dto.WeChatAuthExecution;
import springboot.o2ov1.entity.User;
import springboot.o2ov1.entity.WeChatAuth;

import java.util.Date;

/**
 * @author WuChangJian
 * @date 2020/2/26 21:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WeChatServiceTest{
    @Autowired
    private WeChatAuthService weChatAuthService;
    @Test
    public void testRegister() {
        User user = new User();
        user.setName("springbootTest");
        user.setGender("男");
        user.setProfileImg("url:img");
        user.setCreateTime(new Date());
        user.setLastEditTime(new Date());
        user.setUserType(1);
        WeChatAuth weChatAuth = new WeChatAuth();
        weChatAuth.setOpenId("08qw0as0jkl");
        weChatAuth.setUser(user);
        weChatAuth.setCreateTime(new Date());
        WeChatAuthExecution weChatAuthExecution = weChatAuthService.addWeChatAuth(weChatAuth);
        System.out.println(weChatAuthExecution.getStateInfo());
    }
}
