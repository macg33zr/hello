package org.mcag33zr.hello

import org.macg33zr.hello.HelloApplication
import org.macg33zr.hello.model.Hello
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.TestRestTemplate
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Stepwise
import groovyx.gpars.dataflow.Dataflows
import static groovyx.gpars.dataflow.Dataflow.task

/**
 * Spock integration test for the REST API. Runs on a random port.
 */
@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = [HelloApplication.class] )
@WebIntegrationTest
@Stepwise
@IntegrationTest("server.port:0")
class HelloSpockTestsSpec extends Specification {

    // Get the port for the test
    @Value('${local.server.port}')
    int port

    // REST template helper
    RestTemplate restTemplate = new TestRestTemplate("hello","password")
    RestTemplate restTemplateAuthFail = new TestRestTemplate("foo","bar")

    // Base path for REST server
    String getBasePath() { "" }

    // URI helper for REST service
    URI serviceURI(String path = "") {
        new URI("http://localhost:$port/${basePath}${path}")
    }

    def "Get Hello from the server"() {
        given:
        RequestEntity  request = RequestEntity.get(serviceURI("/hello")).build()

        when:
        ResponseEntity<Hello> response = restTemplate.exchange(request, Hello)

        then:
        response.statusCode == HttpStatus.OK
        response.body.message.size() > 0

    }

    def "Try get hello from server with bad credentials"() {
        given:
        RequestEntity  request = RequestEntity.get(serviceURI("/hello")).build()

        when:
        ResponseEntity<Hello> response = restTemplateAuthFail.exchange(request, Hello)

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED

    }

    def "Say hello to server"() {
        given:
        Hello hello = new Hello(message: "Hello from Spock", dateStr: "Today")
        RequestEntity<Hello>  request = RequestEntity.post(serviceURI("/say")).body(hello)

        when:
        ResponseEntity<Hello> response = restTemplate.exchange(request, Hello)

        then:
        response.statusCode == HttpStatus.OK

    }

    def "Say hello to server parallel"() {
        given:
        Hello hello1 = new Hello(message: "Hello from Spock Test 1", dateStr: "Today")
        RequestEntity<Hello>  request1 = RequestEntity.post(serviceURI("/say")).body(hello1)

        Hello hello2 = new Hello(message: "Hello from Spock Test 2", dateStr: "Today")
        RequestEntity<Hello>  request2 = RequestEntity.post(serviceURI("/say")).body(hello2)

        final Dataflows responseData = new Dataflows()


        when:"Making concurrent REST calls"

        task {

            ResponseEntity<Hello> response1 = restTemplate.exchange(request1, Hello)

            responseData.hello1 = response1

            println "Task 1 DONE"
        }
        task {

            ResponseEntity<Hello> response2 = restTemplate.exchange(request2, Hello)

            responseData.hello2 = response2

            println "Task 2 DONE"
        }


        then:"One of the responses should fail"

        println "Parallel CHECK"
        assert responseData.hello1.statusCode in [HttpStatus.OK, HttpStatus.CONFLICT]
        assert responseData.hello2.statusCode in [HttpStatus.OK, HttpStatus.CONFLICT]
        responseData.hello1.statusCode != responseData.hello2.statusCode
        println "Response1 : ${responseData.hello1.statusCode}"
        println "Response2 : ${responseData.hello2.statusCode}"
    }


    def "Stop the server()"() {

        given:
        RequestEntity  request = RequestEntity.post(serviceURI("/shutdown")).build()

        when:
        ResponseEntity<String> response = restTemplate.exchange(request, String)

        then:
        response.statusCode == HttpStatus.OK
        response.body.contains("bye") == true
        sleep(2000)
    }


}
