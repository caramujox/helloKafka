package br.com.caioalura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class EmailService {
    public static void main(String[] args) {
        KafkaConsumer kafkaConsumer = new KafkaConsumer<String, String>(properties());
        kafkaConsumer.subscribe(Collections.singletonList("ECOMMERCE_SEND_EMAIL"));
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                System.out.println("Encontrei " + records.count() + " registros");
                for (var record : records) {
                    System.out.println("###########################################");
                    System.out.println("Sending Email");
                    System.out.println(record.key()); //erro aqui  cannot find symbol, symbol:   method key(), location: variable record of type java.lang.Object
                    System.out.println(record.value()); //erro aqui  cannot find symbol, symbol:   method value(), location: variable record of type java.lang.Object
                    System.out.println(record.partition()); //erro aqui  cannot find symbol, symbol:   method partition(), location: variable record of type java.lang.Object
                    System.out.println(record.offset()); //erro aqui  cannot find symbol, symbol:   method offset(), location: variable record of type java.lang.Object
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Order Processed");
                    System.out.println("###########################################");
                }
            }
        }
    }


    private static Properties properties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, EmailService.class.getSimpleName());
        return properties;
    }
}
