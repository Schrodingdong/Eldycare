package com.eldycare.reminder.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	@Value("${amqp.queue}")
	private String reminderQueue;

	@Value("${amqp.routingkey}")
	private String reminderRoutingkey;

	@Value("${amqp.exchange}")
	private String reminderExchange;

	@Bean
	public Queue reminderQueue() {
		return new Queue(reminderQueue, true);
	}

	@Bean
	public TopicExchange reminderExchange() {
		return new TopicExchange(reminderExchange);
	}

	@Bean
	public Binding bindingReminder(Queue reminderQueue, TopicExchange exchange) {
		return BindingBuilder.bind(reminderQueue).to(exchange).with(reminderExchange);
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jsonMessageConverter) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter);
		return rabbitTemplate;
	}

	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
