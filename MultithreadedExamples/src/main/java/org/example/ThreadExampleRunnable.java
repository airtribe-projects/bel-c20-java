package org.example;

public class ThreadExampleRunnable implements Runnable  {
  @Override
  public void run() {
    for (int i=0;i<10000L;i++) {
      System.out.println("Running code " + i + "on thread " + Thread.currentThread().getState());
    }
  }
}
