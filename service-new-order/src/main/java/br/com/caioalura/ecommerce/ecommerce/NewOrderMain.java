package br.com.caioalura.ecommerce.ecommerce;

import br.com.caioalura.ecommerce.models.Email;
import br.com.caioalura.ecommerce.models.Order;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (KafkaDispatcher orderKafkaDispatcher = new KafkaDispatcher<Order>()) {
            try (KafkaDispatcher emailDispatcher = new KafkaDispatcher<String>()) {
                String email = Math.random()+ "@email.com";
                for (int i = 0; i < 10; i++) {
                    String orderId = UUID.randomUUID().toString();
                    BigDecimal amount = new BigDecimal(Math.random() * 5000 + 1);


                    Order order = new Order(orderId, amount, email);
                    orderKafkaDispatcher.send("ECOMMERCE_NEW_ORDER", email, order);


                    Email emailCode = new Email("Caio", "Thank u! We are processing ur order!");
                    emailDispatcher.send("ECOMMERCE_SEND_EMAIL", email, emailCode.toString());
                }
            }
        }
    }

}
