package org.example;

public class WaitingStateExample {
  public static void main(String[] args) throws InterruptedException {
    Thread longRunningThread = new Thread(() -> {
      try {
        Thread.sleep(10000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });

    Thread shorterRunningThread = new Thread(() -> {
      try {
        Thread.sleep(2000);
        longRunningThread.join();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });

    Thread monitorThread = new Thread(() -> {
      while(shorterRunningThread.isAlive()) {
        System.out.println("State of the shorter running thread inside the monitor thread  " + shorterRunningThread.getState());
        try {
          Thread.sleep(50);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    });

    System.out.println("State of the longRunningThread before starting " + longRunningThread.getState()); // NEW
    System.out.println("State of the shortRunningThread before starting " + shorterRunningThread.getState()); // NEW
    System.out.println("State of the main thread " + Thread.currentThread().getState()); // RUNNABLE



    longRunningThread.start();
    shorterRunningThread.start();
    monitorThread.start();

    System.out.println("State of the longRunningThread after starting " + longRunningThread.getState()); // RUNNABLE or TIMED_WAITING
    System.out.println("State of the shortRunningThread after starting " + longRunningThread.getState()); // RUNNABLE TIMED_WAITING WAITING


  }
}
