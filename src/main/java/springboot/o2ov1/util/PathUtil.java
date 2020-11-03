package springboot.o2ov1.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathUtil {
    private static String separator = System.getProperty("file.separator");

    public static final Logger logger = LoggerFactory.getLogger(PathUtil.class);
    // 注：静态变量使用@Value不能直接注入值，必须在setXxx属性方法中添加@Value并且不能static修饰
    private static String linuxPath;

    private static String winPath;

    @Value("${linux-path}")
    public void setLinuxPath(String linuxPath) {
        PathUtil.linuxPath = linuxPath;
    }
    @Value("${win-path}")
    public void setWinPath(String winPath) {
        PathUtil.winPath = winPath;
    }

    /**
     * 根据OS的不同，制作图片根路径 win或linux/mac
     * 可以根据操作系统环境而发生变化
     * 返回图片根路径
     * @return
     */
    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String basePath = "";
        if (os.toLowerCase().startsWith("win")) {
            basePath = winPath;
        } else {
            basePath = linuxPath;
        }
        basePath = basePath.replace("\\", separator);
        return basePath;
    }

    /**
     * 返回项目图片子路径
     * @param shopId
     */
    public static String getShopImagePath(long shopId) {
        String imagePath = "/upload/item/shop/" + shopId + "/";
        return imagePath.replace("/", separator);
    }
}
