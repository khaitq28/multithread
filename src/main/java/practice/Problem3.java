package practice;


import java.util.concurrent.Semaphore;

/*

Problem:
Write a program with two threads where one thread prints odd numbers,
and the other prints even numbers in sequence up to a specified maximum.
The threads should alternate their output, so the sequence is in the correct order (1, 2, 3, 4, ...).
Focus: Tests coordination between two threads and the ability to manage alternate signaling,
often using shared locks or flags.
 */
public class Problem3 {

    public static void main(String[] args) throws InterruptedException {

        new Problem3().sol1();
    }

    private Semaphore semaphore1 = new Semaphore(1);
    private Semaphore semaphore2 = new Semaphore(0);
    public void sol1() throws InterruptedException {

        Thread t1 = new Thread(new MyRunnable(true, semaphore1, semaphore2));
        Thread t2 = new Thread(new MyRunnable(false, semaphore2, semaphore1));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    static class MyRunnable implements Runnable {
        private Semaphore semaphore1;
        private Semaphore semaphore2;
        private boolean even;
        public MyRunnable(boolean even, Semaphore semaphore, Semaphore semaphore2) {
            this.even = even;
            this.semaphore1 = semaphore;
            this.semaphore2 = semaphore2;
        }
        @Override
        public void run() {
            int init = even ? 0 : 1;
            for (int i = init; i < 100; i+= 2) {
                try {
                    this.semaphore1.acquire();
                    System.out.println("i = " + i + ", by thread " + Thread.currentThread().getName());
//                    Thread.sleep(10);
                    this.semaphore2.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    //use share object

    private final Object lock = new Object();
    private boolean evenTurn = true;
    public void sol2() throws InterruptedException {

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i+=2) {
                synchronized (lock) {
                    while(!evenTurn) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("i = " + i);
                    evenTurn = false;
                    lock.notify();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 1; i < 100; i+=2) {
                synchronized (lock) {
                    while(evenTurn) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("i = " + i);
                    evenTurn = true;
                    lock.notify();
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
