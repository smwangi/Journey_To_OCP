package Concurrency;

/**
 * Semaphore
 * Demonstrates the two main ways of guarding shared state in Java:
 *
 * 1. {@link java.util.concurrent.Semaphore} - a counting lock. It holds a fixed
 *    number of permits; acquire() takes one (blocking if none are left) and
 *    release() hands one back. Useful for limiting how many threads may use a
 *    resource at once, rather than restricting it to exactly one.
 * 2. Intrinsic locks (the {@code synchronized} keyword) - every object has one
 *    monitor, and only one thread may hold it at a time. See {@link Sender} and
 *    {@link ThreadSend} below.
 *
 * Note: this class is named Semaphore, so java.util.concurrent.Semaphore has to
 * be referred to by its fully qualified name throughout.
 */
public class Semaphore {
    /** Mutable state shared across threads; the counter the semaphore protects. */
    class Shared {
        static int count = 0;
    }

    /**
     * A thread that increments the shared counter while holding a permit.
     */
    class MyThread extends Thread {
        java.util.concurrent.Semaphore semaphore;
        String threadName;

        public MyThread(java.util.concurrent.Semaphore semaphore, String threadName) {
            super(threadName); // names the thread so getName() works below
            this.semaphore = semaphore;
            this.threadName = threadName;
        }

        @Override
        public void run() {
            if (this.getName().equals("A")) {
                try {
                    semaphore.acquire(); // blocks until a permit is available
                    for (int i = 0; i < 5; i++) {
                        Shared.count++;
                        System.out.println(threadName + ": " + Shared.count);
                        Thread.sleep(10); // hold the permit long enough to be observable
                    }
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                // NOTE: release() belongs in a finally block. As written, if acquire()
                // itself is interrupted the catch swallows the exception and release()
                // still runs, handing back a permit that was never taken.
                semaphore.release();
            } else {
                // Thread B or others can follow the same acquire/work/release pattern.
            }
        }
    }

    public static void main(String[] args) {
        // Two permits, so both threads could hold one simultaneously - the semaphore
        // is throttling concurrency here, not enforcing mutual exclusion. Construct
        // it with 1 permit to turn it into a binary lock.
        java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(2);

        // MyThread/Sender/ThreadSend are inner (non-static) classes, so each needs an
        // enclosing Semaphore instance: outer.new Inner(...).
        MyThread threadA = new Semaphore().new MyThread(semaphore, "A");
        MyThread threadB = new Semaphore().new MyThread(semaphore, "B");

        threadA.start();
        threadB.start(); // only A does work; B falls into the empty else branch

        // Second demo: both threads target the same Sender, so they serialise on it.
        Sender sender = new Semaphore().new Sender();
        ThreadSend t1 = new Semaphore().new ThreadSend("Hello", sender);
        ThreadSend t2 =  new Semaphore().new ThreadSend("World", sender);

        t1.start();
        t2.start();
    }

    /**
     * Shared resource guarded by an intrinsic lock. A synchronized instance method
     * locks {@code this}, so two threads calling send() on the same Sender run one
     * after the other - the second waits out the first's one-second sleep.
     */
    class Sender {
        synchronized void send(String msg) {
            System.out.println("Sending\t" +msg);
            try {
                Thread.sleep(1000); // simulates slow I/O while holding the lock
            } catch (Exception ex) {
                System.out.println("Thread Interrupted.");
            }
            System.out.println("\n" + msg + " Sent");
        }
    }

    /**
     * Sends one message through a shared {@link Sender}.
     */
    class ThreadSend extends Thread {
        private String msg;
        private Sender sender;

        ThreadSend (String m, Sender obj) {
            msg = m;
            sender = obj;
        }

        public void run() {
            // Locks the same monitor that send() locks. Redundant here (send() is
            // already synchronized), but it shows the block form: it keeps the whole
            // message atomic if more calls were added inside the block.
            synchronized (sender) {
                sender.send(msg);
            }
        }
    }
}
