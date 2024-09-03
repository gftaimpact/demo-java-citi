package com.scalesec.vulnado;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {
@Autowired

private ApplicationContext context;
	@Test
	public void contextLoads() {
Assert.assertNotNull(context);
	}

}

