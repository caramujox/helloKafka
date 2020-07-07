package br.com.caioalura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class LogService {
    public static void main(String[] args) {
        LogService logService = new LogService();
        KafkaService kafkaService = new KafkaService(LogService.class.getSimpleName(), "ECOMMERCE.*", logService::parse);
        kafkaService.run();
    }

    void parse(ConsumerRecord<String, String> record){
                    System.out.println("###########################################");
                    System.out.println("LOG: "+ record.topic());
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
