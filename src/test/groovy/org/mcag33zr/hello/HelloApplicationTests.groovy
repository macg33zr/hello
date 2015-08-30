package org.mcag33zr.hello

import org.junit.Test
import org.junit.runner.RunWith
import org.macg33zr.hello.HelloApplication
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner)
@SpringApplicationConfiguration(classes = HelloApplication)
@WebAppConfiguration
class HelloApplicationTests {

	@Test
	void contextLoads() {
	}

}
