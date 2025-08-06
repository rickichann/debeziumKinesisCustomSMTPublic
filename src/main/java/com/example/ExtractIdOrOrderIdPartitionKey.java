package com.example;

import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.transforms.Transformation;

import java.util.Map;

public class ExtractIdOrOrderIdPartitionKey<R extends ConnectRecord<R>> implements Transformation<R> {

    @Override
    public R apply(R record) {
        if (record.value() == null || !(record.value() instanceof Struct)) {
            return record;
        }

        Struct valueStruct = (Struct) record.value();
        Schema schema = valueStruct.schema();  // ✅ access schema here

        Object id = null;
        if (schema.field("id") != null) {
            id = valueStruct.get("id");
        } else if (schema.field("_id") != null) {
            id = valueStruct.get("_id");
        } 
        //  else if (schema.field("other_column....") != null) {
        //     id = valueStruct.get("other_column....");
        // }

        if (id == null) {
            return record;  // fallback gagal, biarkan lewat
        }

        return record.newRecord(
            record.topic(),
            record.kafkaPartition(),
            Schema.STRING_SCHEMA,   // ✅ string key schema
            id.toString(),          // ✅ string key value
            record.valueSchema(),
            record.value(),
            record.timestamp()
        );
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // no config needed
    }

    @Override
    public void close() {
        // nothing to close
    }

    @Override
    public org.apache.kafka.common.config.ConfigDef config() {
        return new org.apache.kafka.common.config.ConfigDef();
    }
}
