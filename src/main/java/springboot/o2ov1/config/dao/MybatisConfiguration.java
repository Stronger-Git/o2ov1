package springboot.o2ov1.config.dao;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;

/**
 * Created by WuJiXian on 2020/11/1 13:44
 */
@Configuration
@MapperScan(basePackages = "springboot.o2ov1.dao")
public class MybatisConfiguration {

    @Autowired
    private MybatisProperties mybatisProperties;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sfb = new SqlSessionFactoryBean();
        sfb.setConfiguration(mybatisProperties.getConfiguration());
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String[] mapperLocations = mybatisProperties.getMapperLocations();
        // 缺陷：
        // 这里只能支持通配符路径（只能是一个通配的Mapper），对于多个Mapper并没有映射
        if (mapperLocations != null && mapperLocations.length == 1)
            sfb.setMapperLocations(resolver.getResources(mapperLocations[0]));
        sfb.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());
        sfb.setDataSource(dataSource);
        return sfb.getObject();
    }

}
