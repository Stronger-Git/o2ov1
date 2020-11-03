package springboot.o2ov1.service;


import springboot.o2ov1.dto.WeChatAuthExecution;
import springboot.o2ov1.entity.WeChatAuth;

/**
 * @author WuChangJian
 * @date 2020/2/26 21:04
 */
public interface WeChatAuthService {
    WeChatAuth getByOpenId(String openId);
    WeChatAuthExecution addWeChatAuth(WeChatAuth weChatAuth);
}
