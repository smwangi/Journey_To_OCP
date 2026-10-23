package Concurrency;

import java.util.concurrent.TimeUnit;
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

        DeadlockAvoidanceExample.performTaskAvoidingDeadlockWithTryLock();
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

class DeadlockAvoidanceExample {

    // Define two shared resource locks
    private static final ReentrantLock lock1 = new ReentrantLock();
    private static final ReentrantLock lock2 = new ReentrantLock();

    public static void performTaskAvoidingDeadlock() {
    }

    public static void performTaskAvoidingDeadlockWithTryLock() {
        // Thread 1 attempts to lock A then B u
        Thread thread1 = new Thread(() -> acquireLocks(lock1, lock2, "Thread 1"));

        // THread 2 attempts to lock B then A using tryLock to avoid deadlock
        Thread thread2 = new Thread(() -> acquireLocks(lock2, lock1, "Thread 2"));

        thread1.start();
        thread2.start();
    }

    private static void acquireLocks(ReentrantLock firstLock, ReentrantLock secondLock, String threadName) {
        boolean gotFirstLock = false;
        boolean gotSecondLock = false;
        while (true) {
            try {
                // Try to acquire first lock with a 50ms timeout (simulated by tryLock without timeout)
                gotFirstLock = firstLock.tryLock(50, TimeUnit.MILLISECONDS);

                if (gotFirstLock) {
                    System.out.println(threadName + " acquired the first lock");

                    // Simulate a small delay to induce potential deadlock scenario (contention)
                    Thread.sleep(50);

                    System.out.println(threadName + " trying to acquire the second lock");
                    // Try to acquire the second lock with a 50ms timeout (simulated by tryLock without timeout)
                    gotSecondLock = secondLock.tryLock(50, TimeUnit.MILLISECONDS);
                }

                if (gotSecondLock) {
                    System.out.println(threadName + " acquired the second lock");
                    break;
                }
            } catch(InterruptedException e) {
                System.out.println(threadName + " was interrupted while trying to acquire locks");
                Thread.currentThread().interrupt();
                return;
            } finally {
                // Clean-up phase
                if (!gotSecondLock && gotFirstLock) {
                    System.out.println(threadName + " could not acquire the second lock, releasing the first lock");
                    firstLock.unlock();
                }
                else if (gotSecondLock && gotFirstLock) {
                   // Work finished successfully
                   secondLock.unlock();
                   firstLock.unlock();
                   System.out.println(threadName + " released both locks");
                   break;
                }
            }
            // Locks not acquired, retry
            // Sleep briefly before retrying to acquire locks
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}