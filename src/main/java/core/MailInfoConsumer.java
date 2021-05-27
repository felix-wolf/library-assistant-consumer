package core;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import database.DatabaseHandler;
import models.MailInfo;
import models.Operation;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static java.time.temporal.ChronoUnit.SECONDS;

public class MailInfoConsumer {

    private static Consumer<Long, String> createConsumer() {
        final Properties props = new Properties();
        // props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        // props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        final Consumer<Long, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singleton(Constants.MailInfoTopic));
        return consumer;
    }

    static void runConsumer() {
        final Consumer<Long, String> consumer = createConsumer();
        final int giveUp = -1;
        int noRecordsCount = 0;
        while (true) {
            final ConsumerRecords<Long, String> consumerRecords = consumer.poll(Duration.of(10, SECONDS));
            System.out.println(consumerRecords.count() + " consumerRecords found");
            if (consumerRecords.count() == 0) {
                noRecordsCount++;
                if (noRecordsCount == giveUp) break;
                else continue;
            }
            consumerRecords.forEach(MailInfoConsumer::processRecord);
            consumer.commitAsync();
        }
        consumer.close();
    }

    @SuppressWarnings("unchecked")
    private static void processRecord(ConsumerRecord<Long, String> record) {
        System.out.printf("Consumer Record:(key: %d, value: %s, partition: %d, offset: %d)\n",
                record.key(), record.value(),
                record.partition(), record.offset());
        Gson gson = new Gson();
        Operation operation = gson.fromJson(record.value(), Operation.class);
        switch (operation.getOperationType()) {
            case DELETE: {
                DatabaseHandler.getInstance().deleteAllMailServerInfo();
                break;
            }
            case INSERT: {
                LinkedTreeMap<Object, Object> treeMap = (LinkedTreeMap<Object, Object>) operation.getObject();

                String emailId = treeMap.get("emailId").toString();
                String password = treeMap.get("password").toString();
                String mailServer = treeMap.get("mailServer").toString();
                int port = (int) Double.parseDouble(treeMap.get("port").toString());
                boolean sslEnabled = Boolean.parseBoolean(treeMap.get("sslEnabled").toString());

                MailInfo mailInfo = new MailInfo(mailServer, port, emailId, password, sslEnabled);
                DatabaseHandler.getInstance().insertMailServerInfo(mailInfo);
                break;
            }
            case UPDATE: {
                // should never occur
                break;
            }
        }
    }
}