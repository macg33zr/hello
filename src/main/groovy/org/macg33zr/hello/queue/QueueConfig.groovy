package org.macg33zr.hello.queue

import org.apache.activemq.ActiveMQConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.listener.SimpleMessageListenerContainer
import org.springframework.jms.listener.adapter.MessageListenerAdapter
import org.springframework.scheduling.annotation.EnableScheduling

import javax.jms.ConnectionFactory

@EnableScheduling
@Configuration
class QueueConfig {

    private static final String SIMPLE_QUEUE = "simple.queue";

    @Bean
    MessageListenerAdapter adapter(Consumer consumer) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(consumer);
        adapter.setDefaultListenerMethod("process");
        return adapter;
    }

    @Bean
    SimpleMessageListenerContainer container(MessageListenerAdapter adapter, ActiveMQConnectionFactory factory) {

        // Trust all packaged for serialisation
        // Future: see https://github.com/spring-projects/spring-boot/issues/5631
        factory.setTrustAllPackages(true)

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setMessageListener(adapter);
        container.setConnectionFactory(factory);
        container.setDestinationName(SIMPLE_QUEUE);
        return container;
    }

    @Bean
    Sender sender(JmsTemplate jmsTemplate) {
        return new Sender(jmsTemplate, SIMPLE_QUEUE);
    }
}
