package cn.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Created by zhiwen on 2017/5/25.
 */
@Configuration
public class TestConfig {
    @Bean
    public LettuceConnectionFactory connectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory();
//        factory.setHostName("192.168.99.100");
        return factory;
    }

}
