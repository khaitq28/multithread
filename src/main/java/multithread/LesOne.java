package multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LesOne {

    // executor service example


    public static void main(String[] args) throws InterruptedException, ExecutionException {

        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Thread f1: " + Thread.currentThread().getName());
            return 100;
        });

//        Thread.sleep(1);

        System.out.println("f1.isDone() = " + f1.isDone());

    }

    public void testMultiThread() throws ExecutionException, InterruptedException {
        LesOne lesOne = new LesOne();
        long startTime = System.currentTimeMillis();
        lesOne.process();
        long endTime = System.currentTimeMillis();
        System.out.println("Time process: " + (endTime - startTime) + "ms");

        startTime = System.currentTimeMillis();
        lesOne.normalProcess();
        endTime = System.currentTimeMillis();
        System.out.println("Time normalProcess: " + (endTime - startTime) + "ms");
    }

    public void normalProcess() {
        List<Integer> numbers = IntStream.rangeClosed(1, 100000).boxed().toList();
        numbers.forEach(i -> {
            for (int j = 0; j < i * 10; j++) {
                for (int k = 0; k < i * 100; k++) {
                    int v = (int) Math.sqrt(i) ^ 2;
                }
            }
//            System.out.println("Thread: " + Thread.currentThread().getName() + " processing number: " + i + " square: " + i*i);
        });
    }

    public void process() throws InterruptedException, ExecutionException {

        // user executor service to create a thread pool and process the tasks in paralle compute sum of all numbers from 1 to 1000
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Integer> numbers = IntStream.rangeClosed(1, 100000).boxed().toList();
        List<Runnable> tasks = new ArrayList<>();
        for (int i: numbers) {
            tasks.add(() -> {
                for (int j = 0; j < i * 10; j++) {
                    for (int k = 0; k < i * 100; k++) {
                        int v = (int) Math.sqrt(i) ^ 2;
                    }
                }
//                System.out.println("Thread: " + Thread.currentThread().getName() + " processing number: " + i + " square: " + i*i);
            });
        }
        tasks.forEach(executorService::submit);

//        Future<Integer> future = executorService.submit(new Task(numbers));
//        System.out.println("future.get() = " + future.get());

        executorService.shutdown();
        executorService.awaitTermination(1, java.util.concurrent.TimeUnit.MINUTES);

    }

    static class Task implements Callable<Integer> {
        private final List<Integer> list;
        public Task(List<Integer> list) {
            this.list = list;
        }

        @Override
        public Integer call() {
            return list.stream().mapToInt(i -> i).sum();
        }
    }
}
