package springboot.o2ov1.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author WuChangJian
 * @date 2020/3/17 18:04
 */
public class MD5 {
    public static String getMd5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        // 将传入的字符串转换成byte数组
        byte bytes[] = s.getBytes();
        // 获取MD5加密对象
        try {
            char str[];
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // 对字节数组进行加密
            digest.update(bytes);
            // 获取加密后的数组
            byte md[] = digest.digest();
            int j = md.length;
            str = new char[j*2];
            // 将数组做位移
            int k = 0;
            // 将数组做位移
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            // 转化成String并返回
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(MD5.getMd5("123"));
    }
}
