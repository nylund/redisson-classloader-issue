package com.example.redisson;

import static com.example.redisson.RedissonConfig.EXECUTOR_NAME;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.redisson.api.WorkerOptions;
import org.redisson.api.executor.TaskFailureListener;
import org.redisson.api.executor.TaskStartedListener;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskStarter implements InitializingBean {

  @Autowired
  BeanFactory beanFactory;

  @Autowired
  RedissonClient redissonClient;

  @Override
  public void afterPropertiesSet() throws Exception {
    WorkerOptions options = WorkerOptions
        .defaults()
        .beanFactory(beanFactory)
        .workers(1)
        .addListener((TaskStartedListener) taskId -> log.info("Scheduled task with taskId={} started", taskId))
        .addListener((TaskFailureListener) (taskId, exception) -> log.info("Scheduled task with taskId={} failed", taskId))
        .taskTimeout(60, TimeUnit.SECONDS);

    var executor = redissonClient.getExecutorService(EXECUTOR_NAME);
    executor.registerWorkers(options);

    executor.schedule(new Task(), 1, TimeUnit.SECONDS);
  }
}
