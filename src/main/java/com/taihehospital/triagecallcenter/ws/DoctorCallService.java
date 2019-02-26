package com.taihehospital.triagecallcenter.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author ydd
 * @create 2018/9/18
 */
@WebService
public interface DoctorCallService {

   @WebMethod
    void callPatient(@WebParam(name = "patientid" ,targetNamespace = "com.taihehospital.triagecallcenter.ws")String patientId, @WebParam(name = "times",targetNamespace = "com.taihehospital.triagecallcenter.ws") String times, @WebParam(name = "doctorcode",targetNamespace = "com.taihehospital.triagecallcenter.ws") String doctorCode);
}
