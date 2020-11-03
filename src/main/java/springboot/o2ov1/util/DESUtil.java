package springboot.o2ov1.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

/**
 * @author WuChangJian
 * @date 2020/2/27 9:29
 */
public class DESUtil {
    private static Key key;
    // 设置加密秘钥
    private static final String KEY_STR = "stronger-key";
    private static final String ENCODE = "UTF-8";
    // 设置加密算法
    private static final String ALGORITHM = "DES";
    static {
        try {
            // 生成DES算法对象
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
            // 使用SHA1安全策略
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            // 设置秘钥种子
            secureRandom.setSeed(KEY_STR.getBytes());
            // 初始化基于SHA1的算法对象
            generator.init(secureRandom);
            // 生成秘钥对象
            key = generator.generateKey();
            generator = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取加密后的信息
     * @param data 需要加密的数据
     * @return 加密后的字符串
     */
    private static String getEncryptString(String data) {
        // 基于BASE64编码，接受byte[] 并转化为String
        BASE64Encoder base64Encoder = new BASE64Encoder();
        try {
            byte[] bytes = data.getBytes(ENCODE);
            // 获取加密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 初始化密码信息
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密
            byte[] doFinal = cipher.doFinal(bytes);
            return base64Encoder.encode(doFinal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取解密后的数据
     * @param data
     * @return
     */
    public static String getDecryptString(String data) {
        // 基于BASE64编码，接受byte[] 并将其转化为String
        BASE64Decoder base64Decoder = new BASE64Decoder();
        try {
            byte[] bytes = base64Decoder.decodeBuffer(data);
            // 获取解密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 初始化解密信息
            cipher.init(Cipher.DECRYPT_MODE, key);
            // 解密
            byte[] doFinal = cipher.doFinal(bytes);
            return new String(doFinal, ENCODE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(getEncryptString("root"));
        System.out.println(getEncryptString("root"));
        System.out.println(getEncryptString("stronger"));
        System.out.println(getEncryptString("Stronger!"));
    }

}
