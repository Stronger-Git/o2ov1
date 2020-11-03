package springboot.o2ov1.dao;


import springboot.o2ov1.entity.WeChatAuth;

public interface WeChatAuthDao {
    WeChatAuth selectByOpenId(String id);
    int insertWeChatAuth(WeChatAuth weChatAuth);
}
