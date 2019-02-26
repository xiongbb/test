package com.taihehospital.triagecallcenter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taihehospital.triagecallcenter.config.db.SqlStatementConfig;
import com.taihehospital.triagecallcenter.config.rabbitmq.RabbitMqExchange;
import com.taihehospital.triagecallcenter.config.rabbitmq.RabbitMqQueue;
import com.taihehospital.triagecallcenter.po.Patient;
import com.taihehospital.triagecallcenter.po.PatientVisit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author ydd
 * @create 2018/9/18
 */
@Service
@Slf4j
public class PatientResourceService {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${triagecall-expired-second}")
    private Long triageCallExpiredSecond;


    @Autowired
    private ObjectMapper mapper;


    @Autowired
    @Qualifier("hisNamedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate hisNamedParameterJdbcTemplate;

    @Autowired
    private SqlStatementConfig sqlStatementConfig;


    public void sentPatient(Patient patient)
    {
        MessageProperties props = new MessageProperties();
        props.setExpiration(Long.toString(triageCallExpiredSecond * 1000));
        props.setContentType(MessageProperties.CONTENT_TYPE_JSON);

        String msg = null;
        try {
            msg = mapper.writeValueAsString(patient);
        } catch (JsonProcessingException e) {
            log.error("意外的错误,对象{}序列化错误:{}",msg,e.getMessage());
        }

        Message toSend = new Message(msg.getBytes(), props);

        rabbitTemplate.send(RabbitMqExchange.TRIAGE_CALL_CENTER,RabbitMqQueue.TRIAGE_CALL_PATIENT,toSend);

        log.debug("发送了消息:{}到Queue:{}",patient,RabbitMqQueue.TRIAGE_CALL_PATIENT);
    }

    public void sentVoiceString(Patient patient)
    {
        PatientVisit patientVisit=getPatientVisit(patient);
        if (patientVisit != null) {
            String  msg=getCallVoiceString(patientVisit);
            String queueName=String.format("%s.%s",RabbitMqQueue.TRIAGE_CALL_VOICE,patientVisit.getClinicGroup().trim());

            //设置消息属性
            Message toSend = MessageBuilder.withBody(msg.getBytes()).setExpiration(Long.toString(triageCallExpiredSecond * 1000)).setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("utf-8").build();


            rabbitTemplate.send(RabbitMqExchange.TRIAGE_CALL_CENTER,queueName,toSend);

            log.info("发送了叫号语句:\"{}\",目标Queue:{}.",msg,queueName);
        }
        else
        {
            log.error("未找到{}的有效分诊记录",patient);
        }
    }

    private String getCallVoiceString(PatientVisit patientVisit) {
        return  String.format("请%s%s号患者%s到%s诊室就诊",patientVisit.getRoomNo(),patientVisit.getGhSequence(),patientVisit.getPatientName().trim(),patientVisit.getRoomName().trim());
    }

    private  PatientVisit getPatientVisit(Patient patient)
    {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("pid",patient.getPatientId());
        map.addValue("times",patient.getTimes());
        map.addValue("doctorCode",patient.getDoctorCode());
        List<PatientVisit> visits= hisNamedParameterJdbcTemplate.query(sqlStatementConfig.getMzVisitTable(),map,new BeanPropertyRowMapper(PatientVisit.class));
        if (visits.size()==1)
        {
            PatientVisit patientVisit=visits.get(0);
            return patientVisit;
        }
        else
        {
            return null;
        }
    }
}
