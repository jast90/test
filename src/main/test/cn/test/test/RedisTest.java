package cn.test.test;

import cn.test.config.SessionConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zhiwen on 2017/5/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SessionConfig.class})
public class RedisTest {
    @Autowired
    private LettuceConnectionFactory lettuce;

    @Test
    public void pong() {
        lettuce.getConnection();
        lettuce.validateConnection();
    }


}
