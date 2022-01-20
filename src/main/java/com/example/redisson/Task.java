package com.example.redisson;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;

public class Task implements Runnable, Serializable {

  @Autowired private SomeService someService;

  public Task() {
  }

  @Override
  public void run() {
    someService.doSomething();
  }
}
