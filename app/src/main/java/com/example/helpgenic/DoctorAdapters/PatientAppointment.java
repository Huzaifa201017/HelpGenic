package com.example.helpgenic.DoctorAdapters;

public class PatientAppointment{

    public String date;
    public String time;
    public String week;
    public String patientName;
    public int id;

    public PatientAppointment(String date, String time, String week, String patientName , int id) {
        this.date = date;
        this.time = time;
        this.week = week;
        this.patientName = patientName;
        this.id = id;
    }
}
