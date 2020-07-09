package br.com.caioalura.ecommerce.models;

import java.math.BigDecimal;

public class Order {
    private final String orderId;
    private final BigDecimal value;
    private final String email;

    public Order(String orderId, BigDecimal value, String email) {
        this.orderId = orderId;
        this.value = value;
        this.email = email;
    }

    public String getOrderId() {
        return orderId;
    }


    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", value=" + value +
                ", email='" + email + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }
}
