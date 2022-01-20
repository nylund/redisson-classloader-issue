package com.example.redisson;

import java.io.Serializable;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

public class Task implements Runnable, Serializable {

  @Autowired private RedissonClient redissonClient;
  @Autowired private SomeService someService;

  public Task() {
  }

  @Override
  public void run() {
    someService.doSomething();

    var lock = redissonClient.getLock("lock");
    lock.forceUnlock();
  }
}
