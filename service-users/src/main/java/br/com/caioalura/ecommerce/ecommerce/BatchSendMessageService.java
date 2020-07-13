package br.com.caioalura.ecommerce.ecommerce;

import br.com.caioalura.models.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BatchSendMessageService {

    private final Connection connection;

    BatchSendMessageService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        this.connection = DriverManager.getConnection(url);
        try {
            connection.createStatement().execute("CREATE TABLE users (" +
                    "uuid varchar(200) primary key," +
                    "email varchar (200))");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) throws SQLException {
        BatchSendMessageService batchSendMessageService = new BatchSendMessageService();
        try (KafkaService service = new KafkaService<>(CreateUserService.class.getSimpleName(), "SEND_MESSAGE_TO_ALL_USERS",
                batchSendMessageService::parse,
                String.class,
                new HashMap<>())) {
            service.run();
        }
    }

    private final KafkaDispatcher<User> userDispatcher = new KafkaDispatcher<>();
    void parse(ConsumerRecord<String, Message<String>> record) throws SQLException, ExecutionException, InterruptedException {
        System.out.println("###########################################");
        System.out.println("Processing new batch");
        var message = record.value();
        System.out.println("Topic: " + message.getPayload());
        for (User user : getAllUsers()) {
            userDispatcher.send(message.getPayload(), user.getUuid(), user);
        }

        System.out.println("#####################~#####################");
    }

    private List<User> getAllUsers() throws SQLException {
        var result = connection.prepareStatement("select uuid from Users").executeQuery();
        List<User> users = new ArrayList<>();
        while(result.next()){
            users.add(new User(result.getString(1)));
        }
        return users;
    }
}
