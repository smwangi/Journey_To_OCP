package Concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockCondition extends Thread{
    ReentrantLock lock = new ReentrantLock();

    // Condition for even/odd
    Condition evenCondition = lock.newCondition();
    Condition oddCondition = lock.newCondition();

    int counter;

    ReentrantLockCondition(int counter) {
        this.counter = counter;
    }
    int MAX = 10;

    public void run() {
        while (counter <= MAX) {
            // Acquire lock
            lock.lock();

            try {
                if (counter % 2 == 1 && Thread.currentThread().getName().equals("Even")) {
                    // Wait for even condition
                    System.out.println("Odd thread waiting for even condition");
                    evenCondition.await();
                } else if (counter % 2 == 0 && Thread.currentThread().getName().equals("Odd")) {
                    // Wait for odd condition
                    System.out.println("Even thread waiting for odd condition");
                    oddCondition.await();
                } else{
                    System.out.println(Thread.currentThread().getName() + " Counter: " + counter);
                    counter++;

                    if (counter % 2 == 0) {
                        // Signal odd condition
                        evenCondition.signal();
                    } else {
                        // Signal even condition
                        oddCondition.signal();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public void doSomethingInterruptibly() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            // Critical section: do something
            System.out.println(Thread.currentThread().getName() + " is doing something interruptibly");
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLockCondition reentrantLockCondition = new ReentrantLockCondition(0);
        Thread even = new Thread(reentrantLockCondition, "Even");
        Thread odd = new Thread(reentrantLockCondition, "Odd");

        var counter = new Counter();

        var thread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                counter.increment();
            }
        });
        thread.start();

        var thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                counter.increment();
            }
        });
        thread2.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        even.start();
        odd.start();
    }
}

class Counter {
    private final ReentrantLock lock = new ReentrantLock();
    private int counter = 0;

    public void increment() {
        lock.lock(); // acquire the lock
        try {

            // Critical section: increment the counter and print its value
            counter++;
            System.out.println("Counter: " + counter);
        } finally {
            lock.unlock(); // Always unlock in finally block
        }
    }
}
