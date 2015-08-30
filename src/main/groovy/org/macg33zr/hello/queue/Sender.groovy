package org.macg33zr.hello.queue

import org.macg33zr.hello.model.Hello
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.core.JmsTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service


public class Sender  {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final String queue;

    private final JmsTemplate jmsTemplate

    public Sender(JmsTemplate jmsTemplate, String queue) {
        this.jmsTemplate = jmsTemplate
        this.queue = queue
    }

    @Scheduled(fixedRate = 10000L)
    public void generate() {

        Hello item = new Hello()
        item.message = "Generated on queue"
        item.dateStr = new Date().getDateTimeString()

        log.info("Sending: ${item.toString()}")

        jmsTemplate.convertAndSend(queue, item);
    }

    public void send(Hello hello) {
        log.info("Sending: ${hello.toString()}")
        jmsTemplate.convertAndSend(queue, hello);
    }
}
