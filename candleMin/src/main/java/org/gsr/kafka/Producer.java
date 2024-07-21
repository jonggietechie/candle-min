package org.gsr.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Producer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);
    private final KafkaProducer<String, String> producer;
    private final String topic;

    public Producer(String topic) {
        this.topic = topic;
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);
    }

    public void send(String message) {
        producer.send(new ProducerRecord<>(topic, message), (metadata, exception) -> {
            if (exception == null) {
                LOGGER.info("Sent message to topic {}: {}", topic, message);
            } else {
                LOGGER.error("Failed to send message to topic {}: {}", topic, message, exception);
            }
        });
    }

    public void close() {
        producer.close();
    }
}
