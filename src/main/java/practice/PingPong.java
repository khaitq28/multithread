package practice;

public class PingPong {


    public static void main(String[] args) {
        new PingPong().doPingPong();
    }
    private final Object lock = new Object();
    private boolean ping = true;
    private void doPingPong() {

        Runnable r1 = () -> {
            for (int i = 0; i < 100; i++){
                synchronized (lock) {
                    while(!ping) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("Ping " + i);
                    ping = false;
                    lock.notifyAll();
                }
            }
        };

        Runnable r2 = () -> {
            for (int i = 0; i < 100; i++){
                synchronized (lock) {
                    while(ping) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("Pong " + i);
                    ping = true;
                    lock.notifyAll();
                }
            }
        };

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
    }
}
