package com.taihehospital.triagecallcenter.mqhandle;

import com.taihehospital.triagecallcenter.config.rabbitmq.RabbitMqQueue;
import com.taihehospital.triagecallcenter.po.Patient;
import com.taihehospital.triagecallcenter.service.PatientResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ydd
 * @create 2018/9/18
 */
@Component
@Slf4j
public class CallPatientHandle {
    @Autowired
    private  PatientResourceService service;

    @RabbitListener(queues = RabbitMqQueue.TRIAGE_CALL_PATIENT)
    public void process(Patient msg)
    {
        try {
            log.debug("从Queue:{}接收到了消息:{}",RabbitMqQueue.TRIAGE_CALL_PATIENT,msg);
            service.sentVoiceString(msg);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
