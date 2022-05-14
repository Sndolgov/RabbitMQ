package com.example.rabbit.configuration;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RabbitConfiguration {
    //настраиваем соединение с RabbitMQ
    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("localhost");
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    //объявляем очередь с именем queue1
    @Bean
    public Queue myQueue1() {
        return new Queue("queue1");
    }

    //объявляем контейнер, который будет содержать листенер для сообщений
//    @Bean
    public SimpleMessageListenerContainer messageListenerContainer1() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames("queue1");
        container.setMessageListener(new MessageListener() {
            //тут ловим сообщения из queue1
            public void onMessage(Message message) {
                log.info("received from queue1 : " + new String(message.getBody()));
            }
        });
        return container;
    }

    //-------------------------------------------- The message is processed by two queues in turn

    @Bean
    public Queue myQueue2() {
        return new Queue("query-example-2");
    }

    //-------------------------------------------- Message is handled by two queues in parallel

    @Bean
    public Queue myQueue31() {
        return new Queue("query-example-3-1");
    }

    @Bean
    public Queue myQueue32() {
        return new Queue("query-example-3-2");
    }

    @Bean
    public FanoutExchange fanoutExchangeA(){
        return new FanoutExchange("exchange-example-3");
    }

    @Bean
    public Binding binding1(){
        return BindingBuilder.bind(myQueue31()).to(fanoutExchangeA());
    }

    @Bean
    public Binding binding2(){
        return BindingBuilder.bind(myQueue32()).to(fanoutExchangeA());
    }

    //-------------------------------------------- Message is handled by two queues by routing key

    @Bean
    public Queue myQueue41() {
        return new Queue("query-example-4-1");
    }

    @Bean
    public Queue myQueue42() {
        return new Queue("query-example-4-2");
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("direct-exchange-example-4");
    }

    @Bean
    public Binding errorBinding1(){
        return BindingBuilder.bind(myQueue41()).to(directExchange()).with("error");
    }

    @Bean
    public Binding errorBinding2(){
        return BindingBuilder.bind(myQueue42()).to(directExchange()).with("error");
    }

    @Bean
    public Binding infoBinding(){
        return BindingBuilder.bind(myQueue42()).to(directExchange()).with("info");
    }

    @Bean
    public Binding warningBinding(){
        return BindingBuilder.bind(myQueue42()).to(directExchange()).with("warning");
    }

    //-------------------------------------------- Message is handled by two queues by topic

    @Bean
    public Queue myQueue51() {
        return new Queue("query-example-5-1");
    }

    @Bean
    public Queue myQueue52() {
        return new Queue("query-example-5-2");
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("topic-exchange-example-5");
    }

    @Bean
    public Binding binding51(){
        return BindingBuilder.bind(myQueue51()).to(topicExchange()).with("*.orange.*"); // * - one word before and after fast.orange.fox
    }

    @Bean
    public Binding binding52(){
        return BindingBuilder.bind(myQueue52()).to(topicExchange()).with("*.*.rabbit"); // small.funny.rabbit
    }

    @Bean
    public Binding binding53(){
        return BindingBuilder.bind(myQueue52()).to(topicExchange()).with("lazy.#"); // # - zero or more words
    }

    //-------------------------------------------- Receive message and reply

    @Bean
    public Queue myQueue6() {
        return new Queue("query-example-6");
    }

}
