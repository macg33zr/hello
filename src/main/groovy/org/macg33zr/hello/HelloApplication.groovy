package org.macg33zr.hello

import org.macg33zr.hello.config.HelloConfiguration
import org.macg33zr.hello.queue.Consumer
import org.macg33zr.hello.queue.Sender
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling

import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.listener.SimpleMessageListenerContainer
import org.springframework.jms.listener.adapter.MessageListenerAdapter
import javax.jms.ConnectionFactory


@EnableScheduling
@EnableConfigurationProperties(HelloConfiguration.class)
@SpringBootApplication
class HelloApplication {

    private static final String SIMPLE_QUEUE = "simple.queue";

    @Bean
    MessageListenerAdapter adapter(Consumer consumer) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(consumer);
        adapter.setDefaultListenerMethod("process");
        return adapter;
    }

    @Bean
    SimpleMessageListenerContainer container(MessageListenerAdapter adapter, ConnectionFactory factory) {
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

    static void main(String[] args) {
        SpringApplication.run HelloApplication, args
    }
}
