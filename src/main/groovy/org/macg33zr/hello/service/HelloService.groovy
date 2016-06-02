package org.macg33zr.hello.service

import groovy.transform.CompileStatic
import org.macg33zr.hello.model.Hello
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.retry.RetryCallback
import org.springframework.retry.RetryContext
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Service

@CompileStatic
@Service
class HelloService {

    /**
     * The configured retry template for retrying-operations
     */
    @Autowired
    private RetryTemplate retryTemplate

    /**
     * A logger for the service
     */
    private final Logger log = LoggerFactory.getLogger(this.getClass())

    /**
     * The simple hello method
     *
     * @return
     */
    Hello doHello() {
        return new Hello(message:"Service says hello from REST endpoint", dateStr:new Date().getDateTimeString())
    }

    /**
     * A method that has retry with annotation.
     * The annotation can define the max attempts and backoff policy with increasing time delay etc.
     * Problem is all this config is hard-coded so not configurable post build / deployment.
     * But of hard coded config suffices, it is very easy to use and does not require the retryTemplate.
     *
     * @return
     */
    @Retryable(maxAttempts = 5, backoff = @Backoff(value = 2000L, delay = 1000L, maxDelay = 60000L, multiplier = 2.5d))
    Hello doHelloRetry(int tries) {

        // If they get lucky
        return doHelloUnReliable(tries)
    }

    /**
     * Unreliable private method that can throw
     *
     * @param tries
     * @return
     */
    private Hello doHelloUnReliable(int tries) throws Exception {

        log.info("doHelloRetry try: tries: $tries")

        // Throw an exception for each try. Hokey I know..
        if(tries > 3) {

            throw new Exception("It keeps failing - try $tries")
        }

        // If they get lucky
        return new Hello(message:"Service says hello retried from REST endpoint", dateStr:new Date().getDateTimeString())
    }

    /**
     * Do it with a template
     * @return
     */
    Hello doHelloWithTemplate(final int _tries) {

        Hello result = retryTemplate.execute(new RetryCallback<Hello, Exception>() {

            @Override
            public Hello doWithRetry(RetryContext context) throws Exception {

                log.info("Retry count: ${context.retryCount}")
                log.info("Retry last exception: ${context.lastThrowable.getMessage()}")

                return doHelloUnReliable( _tries );
            }


        }) as Hello

        return result
    }



}

