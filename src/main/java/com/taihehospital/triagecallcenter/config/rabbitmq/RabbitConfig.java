package com.taihehospital.triagecallcenter.config.rabbitmq;


import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 通用性基础配置
 * @author ydd
 * @create 2018/7/31
 */
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Component
@Getter
@Setter
public class RabbitConfig {

    private  String host;

    private  Integer port;

    private  String username;

    private  String password;

    /**
     * 创建rabbitTemplate 消息模板类
     * prototype原型模式:每次获取Bean的时候会有一个新的实例
     *  因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调类为最后一次设置
     * @return
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate initRabbitTemplate(ConnectionFactory connectionFactory)
    {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        template.setChannelTransacted(true);

        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }


    /**
     * rabbitAdmin代理类
     * @return
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }


    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory=new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);

        /**
         * 对于每一个RabbitTemplate只支持一个ReturnCallback。
         * 对于返回消息，模板的mandatory属性必须被设定为true，
         * 它同样要求CachingConnectionFactory的publisherReturns属性被设定为true。
         * 如果客户端通过调用setReturnCallback(ReturnCallback callback)注册了RabbitTemplate.ReturnCallback，那么返回将被发送到客户端。
         * 这个回调函数必须实现下列方法：
         *void returnedMessage(Message message, intreplyCode, String replyText,String exchange, String routingKey);
         */
        // connectionFactory.setPublisherReturns(true);
        /**
         * 同样一个RabbitTemplate只支持一个ConfirmCallback。
         * 对于发布确认，template要求CachingConnectionFactory的publisherConfirms属性设置为true。
         * 如果客户端通过setConfirmCallback(ConfirmCallback callback)注册了RabbitTemplate.ConfirmCallback，那么确认消息将被发送到客户端。
         * 这个回调函数必须实现以下方法：
         * void confirm(CorrelationData correlationData, booleanack);
         */

        return connectionFactory;
    }
}
