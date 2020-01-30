package com.local.base;

import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.plugin.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: by Mood
 * @date: 2019-08-09 13:01:15
 * @Description: 数据源配置
 * @version: 1.0
 */
@Configuration
public class DataSourceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfiguration.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private PaginationInterceptor paginationInterceptor;

    @Value("${jdbc.type}")
    private Class<? extends DataSource> dataSourceType;
    @Value("${mybatis-plus.mapper-locations}")
    private String path;

    @Bean(name = "writeDataSourceSettings")
    @ConfigurationProperties(prefix = "write.jdbc")
    public DataSourceSettings writeDataSource() {
        return new DataSourceSettings();
    }

    @Bean(name = "readDataSourceSettings")
    @ConfigurationProperties(prefix = "read.jdbc")
    public DataSourceSettings readDataSource() {
        return new DataSourceSettings();
    }

    @Bean(name = "writeDataSource")
    @Primary
    public DataSource masterDataSource(@Qualifier("writeDataSourceSettings") DataSourceSettings prop) {
        DataSource masterDataSource = DataSourceBuilder.create()
                .driverClassName(prop.getDriverClassName())
                .username(prop.getUsername())
                .password(prop.getPassword())
                .url(prop.getUrl())
                .type(dataSourceType)
                .build();
        LOGGER.info("Registered WRITE DataSource:{} ", masterDataSource);
        return masterDataSource;
    }

    @Bean(name = "readDataSource")
    public DataSource slaveDataSource(@Qualifier("readDataSourceSettings") DataSourceSettings prop) {
        DataSource slaveDataSource = DataSourceBuilder.create().
                driverClassName(prop.getDriverClassName())
                .username(prop.getUsername())
                .password(prop.getPassword())
                .url(prop.getUrl())
                .type(dataSourceType)
                .build();
        LOGGER.info("Registered READ DataSource:{} ", slaveDataSource);
        return slaveDataSource;
    }

    /**
     * 根据名称进行注入，通常是在具有相同的多个类型的实力的一个注入
     */
    @Bean
    public DynamicDataSource dataSource(@Qualifier("writeDataSource") DataSource writeDataSource,
                                        @Qualifier("readDataSource") DataSource readDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.WRITE, writeDataSource);
        targetDataSources.put(DataSourceType.READ, readDataSource);
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(readDataSource);
        return dataSource;
    }

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setLimit(10);
        page.setDialectType("mysql");
        return page;
    }

    /**
     * @Description 设置mybatisSqlSession
     */
    @Bean
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(DynamicDataSource ds) throws IOException {
        MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
        mybatisPlus.setDataSource(ds);
        mybatisPlus.setVfs(SpringBootVFS.class);
        mybatisPlus.setMapperLocations(ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(path));
        Interceptor[] plugins = {paginationInterceptor};
        mybatisPlus.setPlugins(plugins);
        return mybatisPlus;
    }

    @Bean
    public DataSourceTransactionManager tracsactionManager(DynamicDataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    @Getter
    @Setter
    @Configuration
    class DataSourceSettings {
        protected String driverClassName;
        protected String url;
        protected String username;
        protected String password;
    }
}