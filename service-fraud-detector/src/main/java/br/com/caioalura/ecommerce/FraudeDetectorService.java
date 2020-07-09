package br.com.caioalura.ecommerce;

import br.com.caioalura.models.Order;
import org.apache.kafka.clients.consumer.*;

import java.util.HashMap;


public class FraudeDetectorService {
    public static void main(String[] args) {
        FraudeDetectorService fraudeDetectorService = new FraudeDetectorService();
        try (KafkaService service = new KafkaService<Order>(FraudeDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER",
                fraudeDetectorService::parse, Order.class, new HashMap<>())) {
            service.run();
        }
    }

    void parse(ConsumerRecord<String, Order> record) {
        System.out.println("###########################################");
        System.out.println("Processing new order, checking for fraud");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Order Processed");
        System.out.println("###########################################");
    }
}
