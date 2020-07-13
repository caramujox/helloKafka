package br.com.caioalura.ecommerce.ecommerce;

import br.com.caioalura.ecommerce.models.Order;
import org.apache.kafka.clients.consumer.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class FraudeDetectorService {
    public static void main(String[] args) {
        FraudeDetectorService fraudeDetectorService = new FraudeDetectorService();
        try (KafkaService service = new KafkaService<>(FraudeDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER",
                fraudeDetectorService::parse, Order.class, new HashMap<>())) {
            service.run();
        }
    }

    private final KafkaDispatcher<Order> orderKafkaDispatcher = new KafkaDispatcher<>();

    void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException {
        System.out.println("###########################################");
        System.out.println("Processing new order, checking for fraud");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Order order = record.value();
        if(isFraud(order)) {
            System.out.println("Order " + order.getOrderId() + " denied! Suspected fraud");
            orderKafkaDispatcher.send("ECOMMERCE_ORDER_REJECTED", order.getEmail(), order);
        }
        else {
            System.out.println("Order " + order.toString() + " Approved and Processed!");
            orderKafkaDispatcher.send("ECOMMERCE_ORDER_APPROVED", order.getEmail(), order);
        }
        System.out.println("###########################################");
    }

    private boolean isFraud(Order order) {
        return order.getValue().compareTo(new BigDecimal("4500")) >= 0;
    }
}
