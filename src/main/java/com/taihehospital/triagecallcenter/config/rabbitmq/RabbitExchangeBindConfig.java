package com.taihehospital.triagecallcenter.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ydd
 * @create 2018/9/18
 */
@Configuration
public class RabbitExchangeBindConfig {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    /**
     * Direct交换机
     * @return
     */
    @Bean
    DirectExchange patientCallExchange(){
        DirectExchange patientCallExchange=new DirectExchange(RabbitMqExchange.TRIAGE_CALL_CENTER);
        rabbitAdmin.declareExchange(patientCallExchange);
        return patientCallExchange;
    }

    @Bean
    Queue patientCallQueue(){
        Queue queue=new Queue(RabbitMqQueue.TRIAGE_CALL_PATIENT,true);
        rabbitAdmin.declareQueue(queue);
        return queue;
    }

    @Bean
    Binding bindingExchangepatientCall(Queue patientCallQueue, DirectExchange patientCallExchange){
        Binding binding= BindingBuilder.bind(patientCallQueue).to(patientCallExchange).with(RabbitMqQueue.TRIAGE_CALL_PATIENT);
        rabbitAdmin.declareBinding(binding);
        return binding;
    }
}
