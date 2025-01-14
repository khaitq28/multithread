package practice;

import java.util.concurrent.atomic.AtomicInteger;

/*
Create a program with two threads that both increment a shared counter 1,000 times.
Use AtomicInteger for the counter.
Then, modify it to use synchronized blocks to ensure thread safety without using AtomicInteger.
 */
public class IncrementThread {
    public static void main(String[] args) throws InterruptedException {


        new IncrementThread().sol2();

//        new IncrementThread().sol1();
    }

    private Integer i = 0;
    private void sol2() throws InterruptedException {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < 1000; j++) {
                    synchronized (this) {
                        i++;
                        System.out.println(Thread.currentThread().getName()+  ", i plus = " + i);
                    }
                }
            }
        };

        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("i = " + i);
    }

    private static void sol1() throws InterruptedException {
        AtomicInteger i = new AtomicInteger(0);
        Runnable r = () -> {
            for (int j = 0; j < 1000; j++) {
                System.out.println(Thread.currentThread().getName()+  ", i.incrementAndGet() = " + i.incrementAndGet());
            }
        };
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();

        t1.join();t2.join();
        System.out.println("i = " + i);
    }
}
