package com.example.helpgenic.Classes;

import java.sql.Time;

public class VirtualAppointmentSchedule extends AppointmentSchedule{

    public VirtualAppointmentSchedule( String id,String day, Time sTime,Time eTime) {

        this.day = day;
        this.sTime = sTime;
        this.eTime = eTime;
        this.id = id;

    }


    public VirtualAppointmentSchedule( String day, Time sTime,Time eTime) {

        this.day = day;
        this.sTime = sTime;
        this.eTime = eTime;

    }


}
