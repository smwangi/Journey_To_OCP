package Concurrency;

import java.util.concurrent.locks.StampedLock;

public class StampedLockExample {
    private double x, y;
    private final StampedLock lock = new StampedLock();

    // Optimistic Read
    public double distanceFromOrigin() {
        long stamp = lock.tryOptimisticRead();
        double currentx = x, currenty = y;
        if (!lock.validate(stamp)) {
            // Another thread modified the data; re-read with a real lock
            stamp = lock.readLock();
            try {
                currentx = x;
                currenty = y;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentx * currentx + currenty * currenty);
    }

    // Write Operation
    public void move(double deltaX, double deltaY) {
        long stamp = lock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            lock.unlockWrite(stamp);
        }
    }
}
