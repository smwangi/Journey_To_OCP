package Concurrency;

import java.util.concurrent.locks.ReentrantLock;

public class Interrupts {

    static void sleepLearn() {
        for (int i = 0; i < 10; i++) {
            try {
                // pause 4 secs
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
                return;
            }

            System.out.println("Woke up " + i);
        }
    }

    static class SimpleThreads {
        // Display a message preceded by the name of the current thread
        static void threadMessage(String message) {
            String threadName = Thread.currentThread().getName();
            System.out.format("%s: %sn", threadName, message);
        }
    }

    static class MessageLoop implements Runnable {

        @Override
        public void run() {
            String[] importantInfo = {
                    "Mares eat oats",
                    "Does eat oats",
                    "Little lambs eat ivy",
                    "A kid will eat ivy too"
            };
             try {
                    for (String info : importantInfo) {
                        // Pause for 4 seconds
                        Thread.sleep(4000);
                        // Print a message
                        System.out.println(info);
                    }
             } catch (InterruptedException e) {
                    System.out.println("I wasn't done!");

             }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        /*Thread t = new Thread(Interrupts::sleepLearn);
        t.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.interrupt();*/

        // Delay in milliseconds before we interrupt MessageLoop
        long patience = 1000 * 60;

        // if commandline is present, give patience in seconds
        if (args.length > 0) {
            try {
                patience = Long.parseLong(args[0]) * 1000;
            } catch (NumberFormatException e) {
                System.err.println("Argument must be an integer.");
                System.exit(1);
            }
        }

        SimpleThreads.threadMessage("Starting MessageLoop thread ");
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(new MessageLoop());
        t.start();

        SimpleThreads.threadMessage("Waiting for MessageLoop thread to finish ");
        // loop until MessageLoop
        // thread exits
        while (t.isAlive()) {
            SimpleThreads.threadMessage("Still waiting...");
            // Wait maximum of 1 second for MessageLoop thread to finish
            t.join(1_000);
            if (((System.currentTimeMillis() - startTime) > patience) && t.isAlive()) {
                SimpleThreads.threadMessage("Tired of waiting!");
                t.interrupt();
                // Shouldn't be long now -- wait indefinitely
                t.join();
            }
        }
        SimpleThreads.threadMessage("Finally!");

        InterruptibleExample.doSomethingInterruptibly();
        InterruptibleExample.performTaskWithTryLock();
    }
}

class InterruptibleExample {
    private static final ReentrantLock reentrantLock = new ReentrantLock();

    public static void doSomethingInterruptibly() throws InterruptedException {
        // Thread 1 Grabs the lock and holds it indefinitely
        Thread t1 = new Thread(() -> {
            reentrantLock.lock();

            try {
                System.out.println(Thread.currentThread().getName() + " has acquired the lock and is holding it indefinitely");
                Thread.sleep(Long.MAX_VALUE); // Never releases the lock
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " was interrupted while holding the lock");
            } finally {
                reentrantLock.unlock();
            }
        });

        // Thread 2 tries to acquire the lock interruptibly
        Thread t2 = new Thread(() -> {
            try {
                reentrantLock.lockInterruptibly();
                try {
                    System.out.println(Thread.currentThread().getName() + " has acquired the lock interruptibly");
                } finally {
                    reentrantLock.unlock();
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " was interrupted while waiting for the lock");
            }
        });

        t1.start();
        Thread.sleep(500); // Give t1 a chance to acquire the lock
        t2.start();
        Thread.sleep(2000); // Let thread 2 wait in line for 2 seconds

        // Main thread decided Thread 2 has waited long enough and interrupts it
        t2.interrupt();
    }

    public static void performTaskWithTryLock() {
        // Check the lock immediately, returns true if acquired, false otherwise
        if (reentrantLock.tryLock()) { // reentrantLock.tryLock(5, TimeUnit.SECONDS) could be used for timed tryLock
            try {
                System.out.println(Thread.currentThread().getName() + " acquired the lock using tryLock");
            } finally {
                reentrantLock.unlock();
            }
        } else {
            // The lock was busy, but we didn't block and waited for it
            System.out.println(Thread.currentThread().getName() + " could not acquire the lock using tryLock");
        }
    }
}