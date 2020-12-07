/*
package com.leyou.item.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DataSourceProxyConfig {



 @Bean
     public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
         // 订单服务中引入了mybatis-plus，所以要使用特殊的SqlSessionFactoryBean
         MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
         // 代理数据源
         sqlSessionFactoryBean.setDataSource(new DataSourceProxy(dataSource));
         // 生成SqlSessionFactory
         return sqlSessionFactoryBean.getObject();
     }

    //@Value("${...}")
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Bean
    @Primary
    public DataSource dataSource(){
        System.out.println("==========================77777777777777777777777777777777777777777777777777777777"+username+password+driverClassName);

 HikariConfig config = new HikariConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setJdbcUrl(url);
        config.setDriverClassName(driverClassName);

        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        hikariDataSource.setJdbcUrl(url);
        hikariDataSource.setDriverClassName(driverClassName);

         return new DataSourceProxy(hikariDataSource);
    }
}
*/
