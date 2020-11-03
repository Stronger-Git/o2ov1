package springboot.o2ov1.config.service;

/**
 * @author WuChangJian
 * @date 2020/4/16 8:30
 * 对标spring-service里面的transactionManager
 * 继承TransactionManagementConfigurer是因为开启annotation-driven
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * @EnableTransactionManagement开启事务支持后，在Service方法上添加注解@Transaction即可
 */
@Configuration
@EnableTransactionManagement
public class TransactionManagementConfiguration implements TransactionManagementConfigurer {

    @Autowired
    private DataSource dataSource;

    /**
     * 事务管理，需要返回PlatformTransactionManager的实现
     * @return
     */
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
