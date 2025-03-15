package Concurrency;

public class Semaphore {
    class Shared {
        static int count = 0;
    }

    class MyThread extends Thread {
        java.util.concurrent.Semaphore semaphore;
        String threadName;

        public MyThread(java.util.concurrent.Semaphore semaphore, String threadName) {
            super(threadName);
            this.semaphore = semaphore;
            this.threadName = threadName;
        }

        @Override
        public void run() {
            if (this.getName().equals("A")) {
                try {
                    semaphore.acquire();
                    for (int i = 0; i < 5; i++) {
                        Shared.count++;
                        System.out.println(threadName + ": " + Shared.count);
                        Thread.sleep(10);
                    }
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                semaphore.release();
            } else {
                // Thread B or others cab follow similar pattern
            }
        }
    }

    public static void main(String[] args) {
        java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(2);
        MyThread threadA = new Semaphore().new MyThread(semaphore, "A");
        MyThread threadB = new Semaphore().new MyThread(semaphore, "B");

        threadA.start();
        threadB.start();

        Sender sender = new Semaphore().new Sender();
        ThreadSend t1 = new Semaphore().new ThreadSend("Hello", sender);
        ThreadSend t2 =  new Semaphore().new ThreadSend("World", sender);

        t1.start();
        t2.start();
    }

    class Sender {
        synchronized void send(String msg) {
            System.out.println("Sending\t" +msg);
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
                System.out.println("Thread Interrupted.");
            }
            System.out.println("\n" + msg + " Sent");
        }
    }

    class ThreadSend extends Thread {
        private String msg;
        private Sender sender;

        ThreadSend (String m, Sender obj) {
            msg = m;
            sender = obj;
        }

        public void run() {
            synchronized (sender) {
                sender.send(msg);
            }
        }
    }
}
