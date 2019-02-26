package com.taihehospital.triagecallcenter.ws;

import org.apache.cxf.Bus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.xml.ws.Endpoint;
import org.apache.cxf.jaxws.EndpointImpl;

/**
 * @author ydd
 * @create 2018/8/8
 */
@Configuration
public class CxfConfig {

    @Autowired
    private Bus bus;

    @Autowired
    private DoctorCallServiceImpl service;

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus,service);
        endpoint.publish("/DoctorCallService");
        return endpoint;    }

}
