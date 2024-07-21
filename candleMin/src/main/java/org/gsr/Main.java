package org.gsr;

import org.gsr.kafka.Consumer;
import org.gsr.kafka.Producer;

public class Main {
    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();
        KrakenWebSocket webSocket = new KrakenWebSocket(orderBook);
        Producer kafkaProducer = new Producer("candle-topic");
        Candle candle = new Candle(orderBook, kafkaProducer);
        Consumer kafkaConsumer = new Consumer("candle-topic");

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
            kafkaProducer.close();
            kafkaConsumer.close();
        }));

        new Thread(kafkaConsumer::consume).start();

        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}