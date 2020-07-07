package br.com.caioalura.ecommerce;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (KafkaDispatcher dispatcher = new KafkaDispatcher()) {
            ;
            for (int i = 0; i < 10; i++) {
                String rkey = UUID.randomUUID().toString();

                String value = "1234, Caio, 12.50";
                dispatcher.send("ECOMMERCE_NEW_ORDER", rkey, value);


                String email = "Thank u! We are processing ur order!";
                dispatcher.send("ECOMMERCE_SEND_EMAIL", rkey, email);
            }
        }
    }

}
