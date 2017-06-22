package cn.test.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import me.jastz.spring.datasource.DynamicDataSource;
import me.jastz.spring.datasource.DynamicDataSourceTransactionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhiwen on 2017/6/22.
 */
@PropertySource("classpath:db.properties")
@Configuration
public class DataSourceConfig {

    @Value("${db.master.url}")
    private String masterDBUrl;

    @Value("${db.master.username}")
    private String masterDBUsername;

    @Value("${db.master.password}")
    private String masterDBPassword;

    @Value("${db.slave1.url}")
    private String slave1DBUrl;

    @Value("${db.slave1.username}")
    private String slave1DBUsername;

    @Value("${db.slave1.password}")
    private String slave1DBPassword;

    @Value("${db.slave2.url}")
    private String slave2DBUrl;

    @Value("${db.slave2.username}")
    private String slave2DBUsername;

    @Value("${db.slave2.password}")
    private String slave2DBPassword;


    /**
     * 通用配置
     *
     * @param dataSource
     */
    public void dataSourceCommonSeting(DruidDataSource dataSource) {
        //配置初始化大小、最小、最大
        dataSource.setInitialSize(1);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(20);
        //配置获取连接等待超时的时间
        dataSource.setMaxWait(60000L);
        //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        dataSource.setTimeBetweenEvictionRunsMillis(60000L);
        //配置一个连接在池中最小生存的时间，单位是毫秒
        dataSource.setMinEvictableIdleTimeMillis(30000L);

        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);

        //打开PSCache，并且指定每个连接上PSCache的大小
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);

        //配置监控统计拦截的filters
        try {
            dataSource.setFilters("stat");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DataSource dataSourceConfig(String url, String username, String password) {
        DruidDataSource dataSource = new DruidDataSource();
        //基本属性 url、user、password
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSourceCommonSeting(dataSource);
        return dataSource;
    }

    @Bean(name = "master", initMethod = "init", destroyMethod = "close")
    public DataSource masterDataSource() {
        return dataSourceConfig(masterDBUrl, masterDBUsername, masterDBPassword);
    }

    @Bean(name = "slave1", initMethod = "init", destroyMethod = "close")
    public DataSource slave1DataSource() {
        return dataSourceConfig(slave1DBUrl, slave1DBUsername, slave1DBUsername);
    }

    @Bean(name = "slave2", initMethod = "init", destroyMethod = "close")
    public DataSource slave2DataSource() {
        return dataSourceConfig(slave2DBUrl, slave2DBUsername, slave2DBUsername);
    }

    @Bean
    public DataSource dataSource() {
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setDefaultTargetDataSource(masterDataSource());
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("master", masterDataSource());
        map.put("slave1", slave1DataSource());
        map.put("slave2", slave2DataSource());
        dataSource.setTargetDataSources(map);
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        DynamicDataSourceTransactionManager dataSourceTransactionManager = new DynamicDataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());
        dataSourceTransactionManager.setSlaveDataSourceKeys(Lists.newArrayList("slave1", "slave2"));
        return dataSourceTransactionManager;
    }
}
