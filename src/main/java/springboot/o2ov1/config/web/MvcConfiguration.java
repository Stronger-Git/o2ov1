package springboot.o2ov1.config.web;


import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author WuChangJian
 * @date 2020/4/20 12:57
 * 开启mvc，自动注入spring容器，WebMvcConfigurerAdapter(已过时)：配置视图解析器
 * 当一个类实现了这个接口（ApplicationContextAware）之后，这个类就可以方便的获得ApplicationContext
 */
@Configuration
@ConfigurationProperties(prefix = "kaptcha")
public class MvcConfiguration implements WebMvcConfigurer {

    @Value("${linux-path}")
    private String linuxPath;
    @Value("${win-path}")
    private String winPath;
    private String border;
    private String imageHeight;
    private String imageWidth;
    private String fontColor;
    private String fontSize;
    private String fontNames;
    private String randomStr;
    private String length;
    private String noiseColor;

    /**
     * 静态资源配置
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //将外部url映射到本地资源，必须添加file前缀，符合url规范
        String os = System.getProperty("os.name");
        String mappingPath = "";
        if (os.toLowerCase().startsWith("win")) {
            mappingPath = winPath;
        } else {
            mappingPath = linuxPath;
        }
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + mappingPath+ "/upload/");
    }

    /**
     * 定义默认的请求处理器
     * @param configurer
     */
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * 文件上传解析器
     */
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver createMultipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("utf-8");
        // 1024*1024*20 = 20M
        multipartResolver.setMaxUploadSize(1024*1024*20);
        multipartResolver.setMaxInMemorySize(1024*1024*20);
        return multipartResolver;
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*String baseUrl = "/shopadmin/**";
        InterceptorRegistration loginInterceptor = registry.addInterceptor(new ShopInterceptor());
        loginInterceptor.addPathPatterns(baseUrl);
        InterceptorRegistration permissionInterceptor = registry.addInterceptor(new ShopPermissionInterceptor());
        permissionInterceptor.addPathPatterns(baseUrl);
        permissionInterceptor.excludePathPatterns("/shopadmin/shoplist");
        permissionInterceptor.excludePathPatterns("/shopadmin/getshoplist");
        permissionInterceptor.excludePathPatterns("/shopadmin/saveShopInfoToSession");
        permissionInterceptor.excludePathPatterns("/shopadmin/getshopinitinfo");
        permissionInterceptor.excludePathPatterns("/shopadmin/registershop");
        permissionInterceptor.excludePathPatterns("/shopadmin/shopoperation");
        permissionInterceptor.excludePathPatterns("/shopadmin/shopmanage");
        permissionInterceptor.excludePathPatterns("/shopadmin/getshopbyid");*/
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        //new ServletRegistrationBean<>(Servlet, UrlMapping);不要省略UrlMapping,否则访问任何一个页面都会返回验证码
        ServletRegistrationBean<KaptchaServlet> kaptchaServlet = new ServletRegistrationBean<>(new KaptchaServlet(), "/kaptcha");
        kaptchaServlet.addInitParameter("kaptcha.border", border);
        kaptchaServlet.addInitParameter("kaptcha.image.height", imageHeight);
        kaptchaServlet.addInitParameter("kaptcha.image.width", imageWidth);
        kaptchaServlet.addInitParameter("kaptcha.textproducer.font.color", fontColor);
        kaptchaServlet.addInitParameter("kaptcha.textproducer.font.size", fontSize);
        kaptchaServlet.addInitParameter("kaptcha.textproducer.font.names", fontNames);
        kaptchaServlet.addInitParameter("kaptcha.textproducer.char.string", randomStr);
        kaptchaServlet.addInitParameter("kaptcha.textproducer.char.length", length);
        kaptchaServlet.addInitParameter("kaptcha.noise.color", noiseColor);
        return kaptchaServlet;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public void setFontNames(String fontNames) {
        this.fontNames = fontNames;
    }

    public void setRandomStr(String randomStr) {
        this.randomStr = randomStr;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setNoiseColor(String noiseColor) {
        this.noiseColor = noiseColor;
    }
}
