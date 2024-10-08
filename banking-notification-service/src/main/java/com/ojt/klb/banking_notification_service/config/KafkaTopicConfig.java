package com.ojt.klb.banking_notification_service.config;

import com.ojt.klb.banking_notification_service.dto.consumer.*;
import com.ojt.klb.banking_notification_service.dto.consumer.account.AccountData;
import com.ojt.klb.banking_notification_service.dto.consumer.loan.LoanDueDate;
import com.ojt.klb.banking_notification_service.dto.consumer.loan.LoanApplicationNotification;
import com.ojt.klb.banking_notification_service.dto.consumer.loan.LoanDisbursementNotification;
import com.ojt.klb.banking_notification_service.dto.consumer.loan.LoanOverdue;
import com.ojt.klb.banking_notification_service.dto.consumer.trans.TransData;
import com.ojt.klb.banking_notification_service.dto.consumer.trans.TransactionInternalData;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class KafkaTopicConfig {
    // Consumer Factory cho OtpEmailRequestDto
    @Bean
    public ConsumerFactory<String, OtpEmailRequestDto> otpConsumerFactory() {
        return createConsumerFactory(OtpEmailRequestDto.class, "otp_group");
    }

    // Consumer Factory cho AccountData
    @Bean
    public ConsumerFactory<String, AccountData> accountDataConsumerFactory() {
        return createConsumerFactory(AccountData.class, "account_group");
    }

    @Bean
    public ConsumerFactory<String, LoanDueDate> loanDataConsumerFactory() {
        return createConsumerFactory(LoanDueDate.class, "loan_group");
    }

    @Bean
    public ConsumerFactory<String, LoanApplicationNotification> loanAppConsumerFactory() {
        return createConsumerFactory(LoanApplicationNotification.class, "loan_group");
    }

    @Bean
    public ConsumerFactory<String, LoanDisbursementNotification> loanDisbursementNotificationConsumerFactory() {
        return createConsumerFactory(LoanDisbursementNotification.class, "loan_group");
    }

    @Bean
    public ConsumerFactory<String, LoanOverdue> loanOverdueConsumerFactory() {
        return createConsumerFactory(LoanOverdue.class, "loan_group");
    }

    @Bean
    public ConsumerFactory<String, TransactionInternalData> transactionInternalDataConsumerFactory() {
        return createConsumerFactory(TransactionInternalData.class, "trans_group");
    }
    @Bean
    public ConsumerFactory<String, TransData> transDataConsumerFactory() {
        return createConsumerFactory(TransData.class, "trans_group");
    }
    // Phương thức chung để tạo ConsumerFactory cho các lớp khác nhau
    private <T> ConsumerFactory<String, T> createConsumerFactory(Class<T> targetType, String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);  // Thiết lập group-id cho từng factory
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new GenericDeserializer<>(targetType));
    }

    // KafkaListenerContainerFactory cho OtpEmailRequestDto
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OtpEmailRequestDto> otpKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OtpEmailRequestDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(otpConsumerFactory());
        return factory;
    }

    // KafkaListenerContainerFactory cho AccountData
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AccountData> accountDataKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AccountData> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(accountDataConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LoanDueDate> loanDataKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LoanDueDate> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(loanDataConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LoanApplicationNotification> loanAppKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LoanApplicationNotification> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(loanAppConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LoanDisbursementNotification> loanDisbursementNotificationConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LoanDisbursementNotification> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(loanDisbursementNotificationConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LoanOverdue> loanOverdueConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, LoanOverdue> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(loanOverdueConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionInternalData> transactionInternalDataConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransactionInternalData> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transactionInternalDataConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransData> transactionDataKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransData> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transDataConsumerFactory());
        return factory;
    }

    @Bean
    public NewTopic loanTopic() {
        return new NewTopic("repayment_due_notification", 1, (short) 1);  // 1 partition, 1 replication-factor
    }
    @Bean
    public NewTopic accountTopic() {
        return new NewTopic("repayment_overdue_notification", 1, (short) 1);  // 1 partition, 1 replication-factor
    }
}
