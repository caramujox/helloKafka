package br.com.caioalura.ecommerce.ecommerce;

import br.com.caioalura.models.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CreateUserService {

    private final Connection connection;

    CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        this.connection = DriverManager.getConnection(url);
        try {
            connection.createStatement().execute("CREATE TABLE users (" +
                    "uuid varchar(200) primary key," +
                    "email varchar (200))");
        } catch (SQLException ex){
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) throws SQLException {
        CreateUserService userService = new CreateUserService();
        try (KafkaService service = new KafkaService<>(CreateUserService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER",
                userService::parse, Order.class, new HashMap<>())) {
            service.run();
        }
    }

    private final KafkaDispatcher<Order> orderKafkaDispatcher = new KafkaDispatcher<>();

    void parse(ConsumerRecord<String, Message<Order>> record) throws SQLException {
        System.out.println("###########################################");
        System.out.println("Processing new order, checking for new users");
        Message message = record.value();
        Order order = (Order) message.getPayload();
        if(isNewUser(order.getEmail())){
            insertNewUser(order.getEmail());
            System.out.println("User "+ order.getEmail() + " has been successfully registered!");
        }
        System.out.println("#####################~#####################");
    }

    private void insertNewUser( String email) throws SQLException {
        var insert = connection.prepareStatement("insert into users (uuid, email)" +
                "values (?,?)");
        insert.setString(1, UUID.randomUUID().toString());
        insert.setString(2, email);
        insert.execute();
        System.out.println("Usuário uuid e "+ email + " adicionado");
    }

    private boolean isNewUser(String email) throws SQLException {
        var exists = connection.prepareStatement("select uuid from users where email = ? limit 1");
        exists.setString(1, email);
        var results = exists.executeQuery();
        return !results.next();
    }

}

