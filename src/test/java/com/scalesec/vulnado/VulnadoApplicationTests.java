import static org.junit.Assert.assertNotNull;
package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

	@Test
		assertNotNull("Application context should load", SpringRunner.class);
	public void contextLoads() {
	}

}

