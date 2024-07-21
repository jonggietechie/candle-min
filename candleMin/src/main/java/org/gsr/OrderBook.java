package org.gsr;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.TreeMap;

public class OrderBook {
    private final TreeMap<Double, Double> bids = new TreeMap<>(Comparator.reverseOrder());
    private final TreeMap<Double, Double> asks = new TreeMap<>();

    public synchronized void updateData(JSONArray data){
        for (int i = 0; i < data.length(); i++) {
            JSONObject updateData = data.getJSONObject(i);
            //System.out.println(updateData);
            if (updateData.has("bids")){
                updateSide(bids, updateData.getJSONArray("bids"));
                //System.out.println("bids" + bids);
            }
            if (updateData.has("asks")){
                updateSide(asks, updateData.getJSONArray("asks"));
                //System.out.println("asks" + asks);
            }
        }
        checks();
    }


    private void updateSide(TreeMap<Double, Double> side, JSONArray updates) {
        for (int i = 0; i < updates.length(); i++) {
            JSONObject update = updates.getJSONObject(i);
            double price = update.getDouble("price");
            double quantity = update.getDouble("qty");

            if (quantity == 0) {
                side.remove(price);
            } else {
                side.put(price, quantity);
            }
        }
    }

    private void checks(){
        if(bids.firstKey() >= asks.firstKey()){
            throw new IllegalStateException("Highest bid must be less than lowest ask");
        }
        if(bids.isEmpty() && asks.isEmpty()) {
            throw new IllegalStateException("Order book need at least one bid & one ask present");
        }
    }


    public double getMidPrice() {
        if (bids.isEmpty() || asks.isEmpty()) {
            throw new IllegalStateException("Mid price can't be populated without both bids and asks.");
        }
        return (bids.firstKey() + asks.firstKey()) / 2;
    }
}
