package springboot.o2ov1.config.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springboot.o2ov1.util.DESUtil;

import java.beans.PropertyVetoException;

/**
 * @author WuChangJian
 * @date 2020/4/12 15:02
 * 配置DataSource到Ioc容器中
 */
@Configuration
@ConfigurationProperties(prefix = "c3p0.datasource")
public class DataSourceConfiguration {

    private String url;
    private String username;
    private String password;
    private String driverClassName;

    /**
     * 生成与spring-dao.xml对应的dataSource bean放入Ioc
     * @return
     */
    @Bean(name = "dataSource")
    public ComboPooledDataSource createDataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        // 设置连接池相关属性
        dataSource.setDriverClass(driverClassName);
        dataSource.setJdbcUrl(url);
        // 因为对数据库连接的用户名做了加密，所以需要解密
        dataSource.setUser(DESUtil.getDecryptString(username));
        dataSource.setPassword(DESUtil.getDecryptString(password));
        // 配置c3p0连接池的私有属性
        dataSource.setMaxPoolSize(30);
        dataSource.setMinPoolSize(10);
        dataSource.setInitialPoolSize(10);
        // 关闭连接后不自动commit
        dataSource.setAutoCommitOnClose(false);
        // 连接失败重试次数
        dataSource.setAcquireRetryAttempts(2);
        return dataSource;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
