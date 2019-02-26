package com.taihehospital.triagecallcenter.po;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.StringJoiner;

/**
 * @author ydd
 * @create 2018/9/18
 */
@Getter
@Setter
public class PatientVisit extends Patient {
    private String patientName;
    private String visitDept;
    private Date visitDate;
    private Integer ghSequence;
    private String roomName;
    private Integer roomNo;
    private String clinicGroup;

    @Override
    public String toString() {
        return new StringJoiner(", ", PatientVisit.class.getSimpleName() + "[", "]")
                .add("patientName='" + patientName + "'")
                .add("visitDept='" + visitDept + "'")
                .add("visitDate=" + visitDate)
                .add("ghSequence=" + ghSequence)
                .add("roomName='" + roomName + "'")
                .add("roomNo=" + roomNo)
                .add("clinicGroup='" + clinicGroup + "'")
                .toString();
    }
}
