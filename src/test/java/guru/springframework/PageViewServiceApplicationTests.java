package guru.springframework;

import guru.springframework.integration.RabbitConfig;
import guru.springframework.model.events.PageViewEvent;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.UUID;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("mysql")
public class PageViewServiceApplicationTests {

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Ignore
	@Test
	public void sendMessage() throws InterruptedException {

		PageViewEvent pageViewEvent = new PageViewEvent();
		pageViewEvent.setPageUrl("test/url");
		pageViewEvent.setPageViewDate(new Date());
		pageViewEvent.setCorrelationId(UUID.randomUUID().toString());

		Writer w = new StringWriter();
		JAXB.marshal(pageViewEvent, w);
		String xmlString =  xmlString = w.toString();

		System.out.println(xmlString);

		rabbitTemplate.convertAndSend(RabbitConfig.INBOUND_QUEUE_NAME, xmlString);

		Thread.sleep(3000);
	}

}
