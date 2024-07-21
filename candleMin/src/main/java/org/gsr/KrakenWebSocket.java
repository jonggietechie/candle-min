package org.gsr;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class KrakenWebSocket extends WebSocketClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(KrakenWebSocket.class);
    private final OrderBook orderBook;

    public KrakenWebSocket(OrderBook orderBook) {
        super(URI.create("wss://ws.kraken.com/v2"));
        this.orderBook = orderBook;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        LOGGER.info("Connected to Kraken WebSocket");
        JSONObject subscribeMessage = new JSONObject();
        subscribeMessage.put("method", "subscribe");
        JSONObject params = new JSONObject();
        params.put("channel", "book");
        params.put("symbol", new JSONArray().put("MATIC/USD"));
        subscribeMessage.put("params", params);
        send(subscribeMessage.toString());
    }

    @Override
    public void onMessage(String s) {
        try {
            JSONObject message = new JSONObject(s);
            if (message.has("data") && message.has("channel") && "book".equals(message.getString("channel"))) {
                Object data = message.get("data");
                orderBook.updateData((JSONArray) data);
            }
        } catch (Exception e){
            LOGGER.error("Error processing message: {}", e.getMessage(), e);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        LOGGER.info("Connection closed: {}", s);
    }

    @Override
    public void onError(Exception e) {
        LOGGER.error("Error with WebSocket: {}", e.getMessage(), e);
    }
}
