package practice;
/*

Write a simple Java program that creates and runs two threads.
Each thread should print numbers from 1 to 10 with
a small delay between each print. Use both Thread class and Runnable interface for this task.
 */
public class Problem1 {

    public static void main(String[] args) throws InterruptedException {
        sol1();
    }

    private static void sol1() throws InterruptedException {
        Runnable r = () -> {
            for(int i = 1; i <= 120; i++) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(", thread = " + Thread.currentThread().getName() + ", i = " + i);
            }
        };

        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
    }

    private static void sol2() {
        new CountThread().start();
        new CountThread().start();
    }

    static class CountThread extends Thread {
        public void run() {
            for(int i = 1; i <= 10; i++) {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(", thread = " + Thread.currentThread().getName() + ", i = " + i);
            }
        }
    }

}
