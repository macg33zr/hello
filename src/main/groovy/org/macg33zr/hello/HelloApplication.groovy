package org.macg33zr.hello

import org.macg33zr.hello.config.HelloConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.scheduling.annotation.EnableScheduling


@EnableConfigurationProperties(HelloConfiguration.class)
@SpringBootApplication
class HelloApplication {

    static void main(String[] args) {
        SpringApplication.run HelloApplication, args
    }
}
