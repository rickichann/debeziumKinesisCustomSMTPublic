# debeziumKinesisCustomSMTPublic

# Custom Single Message Transform (SMT) for Debezium using Maven

This project contains a custom SMT (Single Message Transform) implementation for [Debezium](https://debezium.io/) that allows the use of a fallback field as a partition key, particularly useful when integrating with systems like Amazon Kinesis.

## Use Case

When streaming data from databases using Debezium and pushing it to a system like Kinesis, it is often necessary to use a unique field as a partition key. However, not all records may contain the preferred field (e.g., `id`). This SMT implementation allows you to:

- Use `id` as the primary partition key.
- Fallback to `order_id` when `id` is not present.

## File Structure

```
.
├── pom.xml
├── src
│   └── main
│       └── java
│           └── com
│               └── example
│                   └── ExtractIdOrOrderIdPartitionKey.java
└── target
    └── custom-smt-1.0-SNAPSHOT.jar
```

## Building the Project

Ensure you have Maven installed. Then run:

```bash
mvn clean package
```

This will generate a `.jar` file in the `target/` directory.

## Usage

To use the SMT in Debezium, configure your connector with the following SMT configuration:

```json
"transforms": "ExtractKey",
"transforms.ExtractKey.type": "com.example.ExtractIdOrOrderIdPartitionKey",
```

Make sure to copy the `.jar` file into the Debezium connector plugins path.

## Notes

- This SMT only works with payloads that have an `id` or `order_id` field.
- You can modify the class to support other fallback strategies.
