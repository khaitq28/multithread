package multithread;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.*;

public class ExeService {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(4); // One thread for AAPL data processing
        BlockingQueue<MarketData> marketDataQueue = new LinkedBlockingQueue<>();

        Runnable dataProducer = () -> {
            System.out.println("Producer thread started...");
            try {
                for (int i = 0; i < 15; i++) {
                    System.out.println("Produce data: " + (100 + i));
                    marketDataQueue.put(new MarketData(System.currentTimeMillis(), 100 + i, 1000));
                    Thread.sleep(100); // simulate time gap between incoming data
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable consumer = () -> {
            System.out.println("Consumer thread started...");
            while (true) {
                MarketData data = null;
                try {
                    data = marketDataQueue.poll(2, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (data != null) {
                    System.out.println("Process data: " + data.getTimestamp() + " " + data.price);
                } else {
                    System.out.println("No new data for 2 seconds. Consumer finishing.");
                    break;
                }
            }
        };


        executor.submit(dataProducer);
        executor.submit(consumer);

        executor.shutdown();

    }


    static class MarketData {
        private long timestamp; // Timestamp of the market data
        private double price;   // Price of the stock
        private double volume;  // Volume of the trade
        public MarketData(long timestamp, double price, double volume) {
            this.timestamp = timestamp;
            this.price = price;
            this.volume = volume;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
