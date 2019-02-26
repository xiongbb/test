package com.taihehospital.triagecallcenter.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;


/**
 * 数据源配置
 *
 * @author ydd
 * @create 2018/8/7
 */
@Configuration
public class DataSourceConfig {


    @Value("${spring.datasource.jdbc-url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;


    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;


    @Bean(name = "hisdbthDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hisdbth")
    public DataSource hisdbthDataSource() {
        return getDruidDataSource(username, password, dbUrl,driverClassName,"hisdbthDataSource");
    }

    private DruidDataSource getDruidDataSource(String username, String password, String url,String driverClassName,String name) {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        datasource.setName(name);

        return datasource;
    }

    @Bean(name="hisJdbcTemplate")
    @Primary
    public JdbcTemplate hisJdbcTemplate(@Qualifier("hisdbthDataSource") DataSource dataSource)
    {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name="hisNamedParameterJdbcTemplate")
    @Primary
    public NamedParameterJdbcTemplate hisNamedParameterJdbcTemplate(@Qualifier("hisdbthDataSource") DataSource dataSource)
    {
        return new NamedParameterJdbcTemplate(dataSource);
    }

}


