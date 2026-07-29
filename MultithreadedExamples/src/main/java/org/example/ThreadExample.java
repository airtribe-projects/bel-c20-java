package org.example;
// extends thread
// implements runnable
// lambda expressions
public class ThreadExample extends Thread {
  @Override
  public void run() {
    for (int i=0;i<10000L;i++) {
      System.out.println("Running code " + i + "on thread " + Thread.currentThread().getName());
    }
  }
}


// Types of thread pool

// fixed size thread pool -> 10
// cached thread pool -> elastic thread pool
// scheduled thread pool executor -> 7pm
// single threaded thread pool -> crons