package com.newbie.core;

import com.newbie.core.util.concurrent.ThreadUtil;
import com.newbie.core.util.concurrent.threadpool.AbortPolicyWithReport;
import com.newbie.core.util.concurrent.threadpool.ThreadPoolUtil;

import org.junit.Test;

import java.time.Instant;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UtilDemo{

  @Test
  public  void test() {
    ThreadFactory factory = ThreadPoolUtil.buildThreadFactory("ws");
    final int CORE_POOL_SIZE = 5;//线程池的核心线程数量
    final int MAX_POOL_SIZE = 10;//线程池的最大线程数
    final int QUEUE_CAPACITY = 100;//任务队列大小，用来储存等待执行任务的队列
    final Long KEEP_ALIVE_TIME = 1L;//当线程数大于核心线程数时，多余的空闲线程存活的最长时间
    ThreadPoolExecutor executor = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(QUEUE_CAPACITY),
            factory);
    for (int i = 0; i < 10; i++) {
      executor.execute(() -> {
        ThreadUtil.sleep(2000);
        System.out.println("CurrentThread name:" + Thread.currentThread().getName() + "date：" + Instant.now());
      });
    }
    executor.shutdown();
    try {
      executor.awaitTermination(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    System.out.println("Finished all threads");
  }
}
