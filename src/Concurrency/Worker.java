package Concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Worker implements Runnable {
    private ReentrantLock lock = new ReentrantLock();
    private String name;
    
    public Worker(ReentrantLock lock, String name) {
        this.lock = lock;
        this.name = name;
    }

    @Override
    public void run() {
        while (!lock.tryLock()) {
            // TODO Auto-generated method stub
            System.out.println(name + " is waiting for the lock");
        }

        System.out.println(name + " has acquired the lock");

        // Release the lock after some work
        lock.unlock();
        System.out.println(name + " has released the lock");
    }
    
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Lock acquisition by multiple threads
        executorService.execute(new Worker(lock, "Worker 1"));
        executorService.execute(new Worker(lock, "Worker 2"));
        
        // Shutdown the executor service
        executorService.shutdown();
    }
}
