package org.macg33zr.hello.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix ='hello')
class HelloConfiguration {
    String user
    String password
}
