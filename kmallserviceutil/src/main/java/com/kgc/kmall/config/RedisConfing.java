package com.kgc.kmall.config;

import com.kgc.kmall.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shkstart
 * @create 2021-01-04 14:32
 */
@Configuration
public class RedisConfing {

    @Value("${spring.redis.host:disabled}")
    private String host;
    @Value("${spring.redis.port:6379}")
    private Integer port;
    @Value("${spring.redis.database:0}")
    private Integer database;

    @Bean
    public RedisUtils getRedisUtils(){
        if(host.equals("disabled")){
            return null;
        }
        RedisUtils redisUtils = new RedisUtils();
        redisUtils.initPool(host,port,database);
        return redisUtils;
    }
}
