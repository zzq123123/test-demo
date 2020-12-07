package com.leyou.trade.config;
         import com.zaxxer.hikari.HikariDataSource;
        import io.seata.rm.datasource.DataSourceProxy;
         import org.springframework.beans.factory.annotation.Value;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.context.annotation.Primary;

        import javax.sql.DataSource;
@Configuration
public class DataSourceProxyConfig {


    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
   /* @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        // 订单服务中引入了mybatis-plus，所以要使用特殊的SqlSessionFactoryBean
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        // 代理数据源
        sqlSessionFactoryBean.setDataSource(new DataSourceProxy(dataSource));
        // 生成SqlSessionFactory
        return sqlSessionFactoryBean.getObject();
    }*/

   @Bean
   @Primary
    public DataSource dataSource(){
       HikariDataSource hikariDataSource = new HikariDataSource();
       hikariDataSource.setUsername(username);
       hikariDataSource.setPassword(password);
       hikariDataSource.setJdbcUrl(url);
       hikariDataSource.setDriverClassName(driverClassName);
       return new DataSourceProxy(hikariDataSource);
   }
}
