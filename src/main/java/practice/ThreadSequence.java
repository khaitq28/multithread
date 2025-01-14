package practice;


/*
Start three threads (T1, T2, T3) that print messages in a specific order:
T1 prints first.
T2 prints second.
T3 prints third.
Use wait/notify to ensure the threads execute in the correct sequence.
 */
public class ThreadSequence {

    public static void main(String[] args) throws InterruptedException {
        new ThreadSequence().SequenceThread2();
    }

    private final Object lock = new Object();
    boolean t1 = true;
    boolean t2 = false;
    boolean t3 = false;


    private void SequenceThread2() throws InterruptedException {
        Thread v1 = createThread(1);
        Thread v2 = createThread(2);
        Thread v3 = createThread(3);
        v1.start(); v2.start(); v3.start();
        v1.join();
        v2.join();
        v3.join();
    }
    private int curThread = 1;

    private Thread createThread(int id) {
        return new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                synchronized (lock) {
                    while(curThread != id) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("T " + id + ", " + i);
                    curThread = curThread % 3 + 1;
                    lock.notifyAll();
                }
            }
        });
    }

    private void SequenceThread() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                synchronized (lock) {
                    while(!this.t1) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("T1 " + i);
                    this.t1 = false;
                    this.t2 = true;
                    this.t3 = false;
                    lock.notifyAll();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                synchronized (lock) {
                    while(!this.t2) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("T2 " + i);
                    this.t1 = false;
                    this.t3 = true;
                    this.t2 = false;
                    lock.notifyAll();
                }
            }
        });

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                synchronized (lock) {
                    while(!this.t3) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("T3 " + i);
                    this.t1 = true;
                    this.t3 = false;
                    this.t2 = false;
                    lock.notifyAll();
                }
            }
        });

        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();

    }

}
