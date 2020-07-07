package br.com.caioalura.ecommerce;

import org.apache.kafka.clients.consumer.*;


public class FraudeDetectorService {
    public static void main(String[] args) {
        FraudeDetectorService fraudeDetectorService = new FraudeDetectorService();
        try (KafkaService service = new KafkaService(FraudeDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER", fraudeDetectorService::parse);) {
            service.run();
        }
    }

    void parse(ConsumerRecord<String, String> record) {
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
