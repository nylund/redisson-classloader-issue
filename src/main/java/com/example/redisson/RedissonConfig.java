package com.example.redisson;

import java.util.Collections;
import org.redisson.Redisson;
import org.redisson.RedissonNode;
import org.redisson.api.RedissonClient;
import org.redisson.codec.MarshallingCodec;
import org.redisson.config.Config;
import org.redisson.config.RedissonNodeConfig;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

  public static final String EXECUTOR_NAME = "spring_test";

  @Bean(destroyMethod = "shutdown")
  RedissonNode redissonNode(BeanFactory beanFactory) {
    Config config = new Config();
    config.useSingleServer()
        .setAddress("redis://127.0.0.1:6379");
    RedissonNodeConfig nodeConfig = new RedissonNodeConfig(config);
    nodeConfig.setExecutorServiceWorkers(Collections.singletonMap(EXECUTOR_NAME, 1));
    nodeConfig.setBeanFactory(beanFactory);

    nodeConfig.setCodec(new MarshallingCodec(this.getClass().getClassLoader()));

    RedissonNode node = RedissonNode.create(nodeConfig);
    node.start();
    return node;
  }

  @Bean
  RedissonClient redissonClient() {
    Config config = new Config();
    config.useSingleServer()
        .setAddress("redis://127.0.0.1:6379");

    config.setCodec(new MarshallingCodec(this.getClass().getClassLoader()));

    RedissonClient redisson = Redisson.create(config);
    return redisson;
  }

}
