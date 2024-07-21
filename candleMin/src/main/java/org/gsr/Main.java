package org.gsr;

public class Main {
    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();
        KrakenWebSocket webSocket = new KrakenWebSocket(orderBook);
        Candle candle = new Candle(orderBook);

        webSocket.connect();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        candle.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            webSocket.close();
            candle.stop();
        }));

        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}