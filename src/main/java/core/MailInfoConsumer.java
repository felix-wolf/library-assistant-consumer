package core;

import com.google.gson.Gson;
import database.DatabaseHandler;
import dto.mailInfo.MailInfoDTO;
import dto.mailInfo.MailInfoDTOConverter;
import dto.member.MemberDTO;
import dto.member.MemberDTOConverter;
import models.MailInfo;
import models.Member;
import models.Operation;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static java.time.temporal.ChronoUnit.SECONDS;

public class MailInfoConsumer {

    private static Consumer<Long, String> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        final Consumer<Long, String> consumer = new KafkaConsumer<>(props);
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
        Gson gson = new Gson();
        Operation operation = gson.fromJson(record.value(), Operation.class);
        switch (operation.getOperationType()) {
            case INSERT:
                MemberDTO memberDTO = gson.fromJson(operation.getObject().toString(), MemberDTO.class);
                Member member = MemberDTOConverter.toEntity(memberDTO);
                DatabaseHandler.getInstance().insertMember(member);
                break;
            case UPDATE: break;
            case DELETE: break;
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