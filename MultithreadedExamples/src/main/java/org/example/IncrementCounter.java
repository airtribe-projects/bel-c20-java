package org.example;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class IncrementCounter {

  // thread safe integers
  private AtomicInteger counter;

  private final Lock lock = new ReentrantLock();

  // 0 0 0 0
  // 1 1 1 1
  public void increment() {
    System.out.println("Thread " + Thread.currentThread().getName());
    counter.incrementAndGet();
    System.out.println(Thread.currentThread().getName() + " incremented count to : " + counter);
  }
}

// syncrhonized
// object as lock
// reenterant lock
// Atomic Variables
// thread safe datastuctures -> HashMap, Set,. List

