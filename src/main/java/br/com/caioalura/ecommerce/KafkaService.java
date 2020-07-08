package br.com.caioalura.ecommerce;

import br.com.caioalura.util.GsonDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

class KafkaService<T> implements Closeable {
    private final KafkaConsumer<String, T> consumer;
    private ConsumerFunction parse;


    KafkaService(String groupName, String topic, ConsumerFunction parse, Class<T> type, Map<String, String> properties) {
        this(parse, groupName, type, properties);
        consumer.subscribe(Collections.singletonList(topic));
    }

    KafkaService(String groupName, Pattern topic, ConsumerFunction parse,  Class<T> type, Map<String, String> properties) {
        this(parse, groupName, type, properties);
        consumer.subscribe(topic);
    }

    private KafkaService(ConsumerFunction parse, String groupName, Class<T> type, Map<String, String> properties) {
        this.parse = parse;
        this.consumer = new KafkaConsumer<>(getProperties(groupName, type, properties));
    }

    public void run() {
        while (true) {
            ConsumerRecords<String, T> records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                System.out.println("Encontrei " + records.count() + " registros");
                for (var record : records) {
                    parse.consume(record);
                }
            }
        }
    }

    private Properties getProperties(String groupName, Class<T> type, Map<String, String> overrideProperties) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupName);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());
        properties.putAll(overrideProperties);
        return properties;
    }

    @Override
    public void close(){
        consumer.close();
    }
}
