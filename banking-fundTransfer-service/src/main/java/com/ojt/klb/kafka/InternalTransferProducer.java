//package com.ojt.klb.kafka;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Service;
//
//import static org.springframework.kafka.support.KafkaHeaders.TOPIC;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class InternalTransferProducer {
//    private final KafkaTemplate<String, InternalTransferNotification> kafkaTemplate;
//
//    public void sendInternalTransferNotification(InternalTransferNotification internalTransferNotification) {
//        log.info("Sending internal transfer notification");
//        Message<InternalTransferNotification> message = MessageBuilder
//                .withPayload(internalTransferNotification)
//                .setHeader(TOPIC, "internalTransfer-topic")
//                .build();
//
//        kafkaTemplate.send(message);
//    }
//}
