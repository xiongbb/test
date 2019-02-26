package com.taihehospital.triagecallcenter.ws;

import com.taihehospital.triagecallcenter.po.Patient;
import com.taihehospital.triagecallcenter.service.PatientResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 * 医师呼叫分诊接口
 *
 * @author ydd
 * @create 2018/9/18
 */
@WebService(name = "DoctorCallService",
        targetNamespace = "com.taihehospital.triagecallcenter.ws",
        endpointInterface = "com.taihehospital.triagecallcenter.ws.DoctorCallService")
@Component
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Slf4j
public class DoctorCallServiceImpl implements DoctorCallService{

    @Autowired
    private PatientResourceService service;

    @Override
    public void callPatient(String patientId,  String times,  String doctorCode)
    {
        Patient patient=new Patient();
        patient.setPatientId(patientId);
        patient.setTimes(Integer.valueOf(times));
        patient.setDoctorCode(doctorCode);
        log.info("接收到WebService调用,入参{}-{}-{}",patientId,times,doctorCode);

        service.sentPatient(patient);
    }
}
