package guru.springframework.integration;

import guru.springframework.domain.PageView;
import guru.springframework.model.events.PageViewEvent;
import guru.springframework.repositories.PageViewsRepository;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.xml.bind.JAXB;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by jt on 2/23/17.
 */
@Configuration
public class RabbitConfig {

    public static final String INBOUND_QUEUE_NAME = "pageviewqueue";
    private static final String AMQP_CHANNEL = "amqpInputChannel";

    @Bean
    Queue queue() {
        return new Queue(INBOUND_QUEUE_NAME, false);
    }

    @Bean(name = AMQP_CHANNEL)
    public MessageChannel amqpInputChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public SimpleMessageListenerContainer inboundContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames(INBOUND_QUEUE_NAME);
        return container;
    }


    @Bean
    public AmqpInboundChannelAdapter amqpInboundChannelAdapterPageView(SimpleMessageListenerContainer listenerContainer,
                                                        @Qualifier("amqpInputChannel") MessageChannel inboundChannel) {
        AmqpInboundChannelAdapter adapter = new AmqpInboundChannelAdapter(listenerContainer);
        adapter.setOutputChannel(inboundChannel);
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "amqpInputChannel")
    public MessageHandler pageViewMessageHandler(PageViewsRepository repository) {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {

                System.out.println("Got Message!");

                String xmlString = (String) message.getPayload();

                System.out.println(xmlString);

                InputStream is = new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8));

                PageViewEvent pageViewEvent =  JAXB.unmarshal(is, PageViewEvent.class);

                PageView pageView = new PageView();
                pageView.setPageUrl(pageViewEvent.getPageUrl());
                pageView.setPageViewDate(pageViewEvent.getPageViewDate());
                pageView.setCorrelationId(pageViewEvent.getCorrelationId());

                repository.save(pageView);
            }

        };
    }
}
