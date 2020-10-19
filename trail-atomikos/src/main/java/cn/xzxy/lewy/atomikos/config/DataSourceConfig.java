package cn.xzxy.lewy.atomikos.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * （多）数据源配置：数据源注入
 *
 * @author lewy95
 */
@Configuration
public class DataSourceConfig {

    //---------------------------注入数据源开始-----------------------------------
    @Bean
    @ConfigurationProperties("spring.datasource.druid.first")
    public DataSource firstDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.second")
    //@ConfigurationProperties("sharding.jdbc.datasource.sharding1")
    public DataSource secondDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }
    //---------------------------注入数据源结束-----------------------------------

}
