package org.gsr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class Candle {
    private static final Logger LOGGER = LoggerFactory.getLogger(Candle.class);
    private final OrderBook orderBook;
    private final Timer timer = new Timer();
    private double open, high, low, close;
    private int ticks;
    private long startTime;

    public Candle (OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public void start() {
        startTime = System.currentTimeMillis() /1000;
        try {
            resetCandle();
        } catch (IllegalStateException e) {
            LOGGER.error("Failed to reset candle: {}", e.getMessage());
            return;
        }

        //Create candle
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                createCandle();
            }
        }, 60000, 60000); // 60 seconds interval

        //Update candle
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateCandle();
            }
        }, 0, 1000); // 1 seconds interval
    }

    public void stop() {
        timer.cancel();
        LOGGER.info("Candle will stop generating.");
    }

    private void resetCandle() {
        double currentPrice = orderBook.getMidPrice();
        open = high = low = close = currentPrice;
        ticks = 1;
    }

    private void createCandle() {
        close = orderBook.getMidPrice();
        long timestamp = startTime;
        String candleData = String.format("Candle: timestamp=%d, open=%.5f, high=%.5f, low=%.5f, close=%.5f, ticks=%d",
                timestamp, open, high, low, close, ticks);

        LOGGER.info(candleData);

        resetCandle();
        startTime = System.currentTimeMillis() /1000;
    }

    public void updateCandle() {
        double currentPrice = orderBook.getMidPrice();
        high = Math.max(high, currentPrice);
        low = Math.min(low, currentPrice);
        close = currentPrice;
        ticks++;
    }
}
