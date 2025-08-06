# Custom Single Message Transform (SMT) for Debezium

Background:
One important aspect to consider when implementing data streaming with Debezium and Kinesis Data Streams is that the partition_key must not exceed the 256-character limit. Previously, I used Debezium's built-in function (debezium.transforms.ExtractNewRecordState.field=id), which only allows specifying a single column as the partition_key. However, in practice, not all database tables use an id column as the primary key — each table may have a different unique structure. To address this limitation and provide more flexibility in determining the key, I developed a custom SMT (Single Message Transform) that enables dynamic column selection based on priority.



This project demonstrates how to create a custom SMT (Single Message Transform) for [Debezium](https://debezium.io/documentation/reference/stable/operations/debezium-server.html) using Maven.


## Overview

The custom SMT implemented here selects the `id` field if available; otherwise, it falls back to using the `_id` field as the partition key (or you can add another column). This is useful when working with sinks like Amazon Kinesis that require custom partition key logic.

## Project Structure

```
.
├── pom.xml
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   └── ExtractIdOrOrderIdPartitionKey.java
```

## How to Build

Make sure Maven is installed on your machine. Then run:

```bash
mvn clean package
```

This will generate a fat JAR with dependencies located at:

```
target/custom-smt-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Deploy to Debezium

To use this SMT in Debezium, copy the JAR to Debezium's `lib` folder:

```bash
cp target/custom-smt-1.0-SNAPSHOT-jar-with-dependencies.jar ../debezium/lib/
```

## Example Configuration

Configure the custom SMT in your Debezium connector configuration (e.g., `application.properties`):

```properties
debezium.transforms=unwrap,extractKey

# Extract the actual payload
debezium.transforms.unwrap.type=io.debezium.transforms.ExtractNewRecordState

# Use custom SMT for partition key extraction
debezium.transforms.extractKey.type=com.example.ExtractIdOrOrderIdPartitionKey
```
