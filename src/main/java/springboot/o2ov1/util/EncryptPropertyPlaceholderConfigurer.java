package springboot.o2ov1.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @author WuChangJian
 * @date 2020/2/27 9:56
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    // 需要加密的字段数组
    private String[] encryptPropNames = {"jdbc.username", "jdbc.password"};

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (isEncryptProp(propertyName)) {
            // 对加密的字段进行解密
            String decryptValue = DESUtil.getDecryptString(propertyValue);
            return decryptValue;
        } else {
            return propertyValue;
        }
    }

    private boolean isEncryptProp(String propertyName) {
        for (String encryptPropName : encryptPropNames) {
            if (encryptPropName.equals(propertyName))
                return true;
        }
        return false;
    }
}
