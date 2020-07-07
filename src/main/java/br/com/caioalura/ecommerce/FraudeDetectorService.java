package br.com.caioalura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class FraudeDetectorService {
    public static void main(String[] args) {
        KafkaConsumer kafkaConsumer = new KafkaConsumer<String, String>(properties());
        kafkaConsumer.subscribe(Collections.singletonList("ECOMMERCE_NEW_ORDER"));
        ConsumerRecords records = kafkaConsumer.poll(Duration.ofMillis(100));
        if (records.isEmpty()) {
            System.out.println("Não encontrei Registros");
            return;
        }
        for (int i = 0, i < records.count(), i++) {
            records[i];
            System.out.println("###########################################");
            System.out.println("Processing new order, checking for fraud");
            System.out.println(record.key()); //erro aqui  cannot find symbol, symbol:   method key(), location: variable record of type java.lang.Object
            System.out.println(record.value()); //erro aqui  cannot find symbol, symbol:   method value(), location: variable record of type java.lang.Object
            System.out.println(record.partition()); //erro aqui  cannot find symbol, symbol:   method partition(), location: variable record of type java.lang.Object
            System.out.println(record.offset()); //erro aqui  cannot find symbol, symbol:   method offset(), location: variable record of type java.lang.Object
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Order Processed");
            System.out.println("###########################################");
        }
    }


    private static Properties properties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return properties;
    }
}
