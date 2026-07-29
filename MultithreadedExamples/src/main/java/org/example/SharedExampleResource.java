package org.example;

public class SharedExampleResource {
  // synchronization
  Object lock = new Object();
  public synchronized void useResource() {
    synchronized (lock) {
      System.out.println("Threading using the lock " + Thread.currentThread().getName());
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}


class BlockedStateExample {
  public static void main(String[] args) throws InterruptedException {
    SharedExampleResource sharedResource = new SharedExampleResource();

    Thread thread1 = new Thread(() -> {
      sharedResource.useResource();
    });

    Thread thread2 = new Thread(() -> {
      sharedResource.useResource();
    });

    System.out.println("State of the thread1 " + thread1.getState()); // NEW
    System.out.println("State of the thread2 " + thread2.getState()); // NEW

    thread1.start();
    Thread.sleep(1000);
    thread2.start();

    System.out.println("State of the thread1 after starting " + thread1.getState()); // RUNNABLE  or TIMED_WAITING
    System.out.println("State of the thread2 after starting " + thread2.getState()); // RUNNABLE or TIMED_WAITING


  }
}