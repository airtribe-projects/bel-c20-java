package org.example;

public class ExampleTimedWaitingState {
  public static void main(String[] args) throws InterruptedException {
    Thread thread1 = new Thread(() -> {
      System.out.println("State of the thread1 before sleeping " + Thread.currentThread().getState()); // RUNNABLE
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });

    System.out.println("State of the main thread before starting thread1 : " + Thread.currentThread().getState()); // RUNNING
    System.out.println("State of the thread1 before starting " + thread1.getState()); // NEW

    thread1.start();
    Thread.sleep(50); // main on sleep
    System.out.println("State of the thread1 before starting " + thread1.getState()); // RUNNABLE  RUNNING TIMED_WAITING
    System.out.println("State of the main thread after starting thread1 : " + Thread.currentThread().getState()); // RUNNABLE

  }
}
