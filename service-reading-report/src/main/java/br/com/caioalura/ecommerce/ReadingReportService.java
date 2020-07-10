package br.com.caioalura.ecommerce;

import br.com.caioalura.ecommerce.ecommerce.KafkaService;
import br.com.caioalura.models.IO;
import br.com.caioalura.models.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ReadingReportService {

    private static final Path SOURCE = new File("src/main/resources/report.txt").toPath();
    public static void main(String[] args) {
        ReadingReportService reportService = new ReadingReportService();
        try (KafkaService service = new KafkaService<>(ReadingReportService.class.getSimpleName(), "USER_GENERATE_READING_REPORT",
                reportService::parse, User.class, new HashMap<>())) {
            service.run();
        }
    }


    void parse(ConsumerRecord<String, User> record) throws ExecutionException, InterruptedException, IOException {
        System.out.println("###########################################");
        System.out.println("Processing report for " + record.value());
        User user = record.value();

        File target = new File(user.getReportPath());
        IO.copyTo(SOURCE, target);

        IO.append(target, "Created for " + user.getUuid());

        System.out.println("File created: "+ target.getAbsolutePath());
;
        System.out.println("###########################################");
    }

}
