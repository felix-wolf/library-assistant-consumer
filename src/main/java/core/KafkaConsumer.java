package core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.DatabaseHandler;
import dto.mailInfo.MailInfoDTO;
import dto.mailInfo.MailInfoDTOConverter;
import dto.operation.OperationDTO;
import dto.operation.OperationDTOConverter;
import dto.operation.OperationDTODeserializer;
import models.MailInfo;
import models.Member;
import models.Operation;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static java.time.temporal.ChronoUnit.SECONDS;

public class KafkaConsumer {

    private static Consumer<Long, String> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        final Consumer<Long, String> consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);
        List<String> topicsToSubscribe = Arrays.asList(Constants.MAIL_SERVER_INFO_TOPIC, Constants.MEMBER_TOPIC);
        consumer.subscribe(topicsToSubscribe);
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

            consumerRecords.forEach(record -> {
                switch (record.topic()) {
                    case "mail_server_info":
                        processMailServerRecord(record);
                        break;
                    case "member":
                        processMemberRecord(record);
                        break;
                }
            });
            //consumer.commitAsync();
        }
        consumer.close();
    }

    private static void processMemberRecord(ConsumerRecord<Long, String> record) {
        System.out.println("memberRecord");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(OperationDTO.class, new OperationDTODeserializer());
        Gson gson = gsonBuilder.create();
        OperationDTO operationDTO = gson.fromJson(record.value(), OperationDTO.class);
        Operation operation = OperationDTOConverter.toEntity(operationDTO);
        assert operation != null;
        Member member = (Member) operation.getObject();
        switch (operation.getOperationType()) {
            case INSERT: {
                DatabaseHandler.getInstance().insertMember(member);
                break;
            }
            case UPDATE: {
                DatabaseHandler.getInstance().updateMember(member);
                break;
            }
            case DELETE: {
                DatabaseHandler.getInstance().deleteMember(member);
                break;
            }
        }
    }

    private static void processMailServerRecord(ConsumerRecord<Long, String> record) {
        System.out.printf("Consumer Record:(key: %d, value: %s, partition: %d, offset: %d)\n",
                record.key(), record.value(),
                record.partition(), record.offset());
        Gson gson = new Gson();
        Operation operation = gson.fromJson(record.value(), Operation.class);
        switch (operation.getOperationType()) {
            case DELETE:
                DatabaseHandler.getInstance().deleteAllMailServerInfo();
                break;
            case INSERT:
                MailInfoDTO mailInfoDTO = gson.fromJson(operation.getObject().toString(), MailInfoDTO.class);
                MailInfo mailInfo = MailInfoDTOConverter.toEntity(mailInfoDTO);
                DatabaseHandler.getInstance().insertMailServerInfo(mailInfo);
                break;
            case UPDATE:
                // should never occur
                break;
        }
    }
}