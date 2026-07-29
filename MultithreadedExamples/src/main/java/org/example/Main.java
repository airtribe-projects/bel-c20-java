package org.example;

public class Main {
  public static void main(String[] args) {
    // child thread
    // By default child thread are non deamon threads
    // deamon vs deamon
//    ThreadExample thread  = new ThreadExample();
    ThreadExampleRunnable threadRunnable = new ThreadExampleRunnable();
    Thread thread = new Thread(threadRunnable);
    thread.setDaemon(true);
    thread.start();

    Thread thread1 = new Thread(() -> {
      for (int i=0;i<10000L;i++) {
        System.out.println("Running code " + i + "on thread " + Thread.currentThread().getName());
      }
    });
    thread1.setDaemon(true);
    thread1.start();

    for (int i=0;i<1000;i++) {
      System.out.println("Main code running " + i + " on thread " + Thread.currentThread().getName());
    }
  }
}
// ThreadExample
// Main