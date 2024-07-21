# Kraken 1m Candle Data Aggregation with Kafka Integration
This application will integrate with Kraken Exchange to fetch tick-level order book data, aggregate the data into 1-minute candles and log the output. It will publishes the candle data to Kafka and consumes the same data from Kafka, logging it to the console too.

### Prerequisites
- Docker and Docker Compose
- Java 17
- Maven

## Start Kafka and Zookeeper
Navigate to the directory containing the docker-compose.yml file and run the following command:
```
docker-compose up -d
```
### Create Kafka Topic
Create a Kafka topic named candle-topic by executing the following command:
```
docker exec -it <kafka-container-id> kafka-topics.sh --create --topic candle-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```
Replace <kafka-container-id> with the actual container ID of your Kafka container. You can find the container ID by running:
```
docker PS
```
Verify that the topic was created by listing the topics with the command:
```
docker exec -it <kafka-container-id> kafka-topics.sh --list --bootstrap-server localhost:9092
```

## Run the application
Run the command below to build the project:
```
mvn clean install
```
Run the main class to start the application

## Outputs and Screenshots
**Logs of candle output** 
```
org.gsr.Candle -- Candle: timestamp=1721558068, open=0.53565, high=0.53575, low=0.53550, close=0.53550, ticks=61
```
**Send message to topic**

![image](https://github.com/user-attachments/assets/93267ef0-d2b5-4274-a5fa-eded9972ca61)

**Consume message**

![image](https://github.com/user-attachments/assets/ede17e1b-e97c-4c34-ad62-690386c5709b)

