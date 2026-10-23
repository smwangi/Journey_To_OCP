package Concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class TypesOfExecutors {

    public static void main(String[] args) {
        
        // Creates an ExecutorService that uses a single worker thread (tasks are executed sequentially)
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

        // Creates a thread pool with a fixed number of threads (3 here)
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

        // Creates a thread pool that can create new threads as needed, and reuses idle threads
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        // Creates a thread pool that can schedule commands to run after a delay or periodically
        // (You can use this for scheduled or delayed tasks)
        ExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(2);

        ExecutorService exec = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5; i++) {
            final int taskNumber = i;
            exec.submit(() -> {
                System.out.println("Task " + taskNumber + " in " + Thread.currentThread().getName());
            });
        }

        // Shutdown the executor service
        exec.shutdown();


        // Example usage of each executor
        singleThreadExecutor.execute(() -> System.out.println("Single Thread Executor Task"));
        fixedThreadPool.execute(() -> System.out.println("Fixed Thread Pool Task"));
        cachedThreadPool.execute(() -> System.out.println("Cached Thread Pool Task"));
        scheduledThreadPool.execute(() -> System.out.println("Scheduled Thread Pool Task"));

        scheduledThreadPool.execute(() -> {
            try {
                Thread.sleep(2000); // Simulate a delay
                System.out.println("Scheduled Task Executed After Delay");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Scheduled Task Interrupted");
            }
        });

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println("Scheduled Task Running at Fixed Rate");
        }, 0, 5, java.util.concurrent.TimeUnit.SECONDS);

        // Example of submitting multiple tasks to the fixed and cached thread pools    

        for (int i = 0; i < 5; i++) {
            final int taskNumber = i;
            fixedThreadPool.execute(() -> System.out.println("Fixed Thread Pool Task " + taskNumber));
            cachedThreadPool.execute(() -> System.out.println("Cached Thread Pool Task " + taskNumber));
        }

        // Shutdown the executors
        singleThreadExecutor.shutdown();
        fixedThreadPool.shutdown();
        cachedThreadPool.shutdown();
        scheduledThreadPool.shutdown();

        Callable<String> callableTask = () -> {
            Thread.sleep(1000); // Simulate some work
            return "Callable Task Result";
        }; 

        ExecutorService callableExecutor2 = Executors.newSingleThreadExecutor();
        Future<String> future = callableExecutor2.submit(callableTask);
        try {
            String result = future.get(); // This will block until the callable task is complete
            System.out.println("Callable Task Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            callableExecutor2.shutdown();
        }

        ExecutorService callableExecutor = Executors.newFixedThreadPool(2);
        try {
            String result = callableExecutor.submit(callableTask).get();
            System.out.println("Callable Task Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            callableExecutor.shutdown();
        }
    }
}
