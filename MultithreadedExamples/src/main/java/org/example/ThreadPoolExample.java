package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadPoolExample {
  public static void main(String[] args) {
    ExecutorService executorService = Executors.newCachedThreadPool();
    for (int i=0;i<100;i++) {
      int task = i;
      executorService.submit(() -> {
        System.out.println("Task " + task + " is being executed by " + Thread.currentThread().getName());
        try {
          Thread.sleep(1000); // Simulating some work
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
    }
  }
}
