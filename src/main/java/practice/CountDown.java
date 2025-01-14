package practice;

public class CountDown {
    public static void main(String[] args) throws InterruptedException {
        new CountDown().doCountDownToZero();
    }

    private final Object lock = new Object();
    private boolean finish = false;
    private void doCountDownToZero() throws InterruptedException {

        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                while (!finish) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("FINISH");
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                for (int i = 10; i >= 0 ; i--) {
                    System.out.println(i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                this.finish = true;
                lock.notify();
            }
        });

        t1.start();t2.start();
        t1.join();
        t2.join();

    }
}
