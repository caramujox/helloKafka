package br.com.caioalura.ecommerce;

import br.com.caioalura.models.Email;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.HashMap;


public class EmailService {
    public static void main(String[] args) {
        EmailService emailService = new EmailService();
        try (KafkaService service = new KafkaService(EmailService.class.getSimpleName(), "ECOMMERCE_SEND_EMAIL",
                emailService::parse, Email.class, new HashMap<>())) {
            service.run();
        }

    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("###########################################");
        System.out.println("Sending Email");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Order Processed");
        System.out.println("###########################################");
    }
}
