package org.example;

public class RaceCondition {
  public static void main(String[] args) {
    IncrementCounter incrementCounter = new IncrementCounter();
    for (int i=0;i<1000;i++) {
      Thread thread = new Thread(() -> {
        System.out.println("Thread name: " + Thread.currentThread().getName());
        incrementCounter.increment();
      });
      thread.start();
    }
  }
}
