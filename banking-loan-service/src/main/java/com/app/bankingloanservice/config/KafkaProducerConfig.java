package com.app.bankingloanservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public NewTopic loanApplicationTopic() {
        return new NewTopic("loan_application", 1, (short) 1);  // 1 partition, 1 replication-factor
    }

    // Topic for loan disbursement notifications
    @Bean
    public NewTopic loanDisbursementNotificationTopic() {
        return new NewTopic("loan-disbursement-notification", 1, (short) 1);  // 1 partition, 1 replication-factor
    }

    // Topic for upcoming due repayment notifications
    @Bean
    public NewTopic dueRepaymentNotificationTopic() {
        return new NewTopic("repayment_due_notification", 1, (short) 1);  // 1 partition, 1 replication-factor
    }

    // Topic for overdue repayment notifications
    @Bean
    public NewTopic overdueRepaymentNotificationTopic() {
        return new NewTopic("repayment_overdue_notification", 1, (short) 1);  // 1 partition, 1 replication-factor
    }
}
