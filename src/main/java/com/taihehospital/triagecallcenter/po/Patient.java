package com.taihehospital.triagecallcenter.po;

import lombok.Getter;
import lombok.Setter;

import java.util.StringJoiner;

/**
 * @author ydd
 * @create 2018/9/18
 */
@Getter
@Setter
public class Patient {
    private String patientId;
    private Integer times;
    private String doctorCode;

    @Override
    public String toString() {
        return new StringJoiner(", ", Patient.class.getSimpleName() + "[", "]")
                .add("patientId='" + patientId + "'")
                .add("times=" + times)
                .add("doctorCode='" + doctorCode + "'")
                .toString();
    }
}
