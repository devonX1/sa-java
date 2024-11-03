package com.v1.sealert.sa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SaApplicationTests {
	@Autowired
	private NonStatic nonStatic;

	@Test
	void contextLoads() {
		//nonStatic.forHerokuDataBASE_URL_test();
	}
}
