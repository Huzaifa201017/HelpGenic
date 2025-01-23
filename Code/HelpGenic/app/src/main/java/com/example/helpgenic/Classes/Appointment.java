package com.example.helpgenic.Classes;

import java.io.Serializable;
import java.util.Date;
import java.sql.Time;
import java.util.ArrayList;

public class Appointment implements Serializable {

    private final Date appDate;
    private final Doctor doc;
    private final Patient p;
    private Time sTime , eTime ;
    private int appId;
    private String aptId;
    private Prescription pres;
    private ArrayList<Document> documents;

    public int getAppId() {
        return appId;
    }


    public Appointment(Date appDate, Doctor doc, Patient p, Time sTime, Time eTime , int id) {
        this.appDate = appDate;
        this.doc = doc;
        this.p = p;
        this.sTime = sTime;
        this.eTime = eTime;
        this.appId = id;
    }

    public Appointment(Date appDate, Doctor doc, Time sTime, Time eTime , String id) {
        this.appDate = appDate;
        this.doc = doc;
        this.p = null;
        this.sTime = sTime;
        this.eTime = eTime;
        this.aptId = id;
    }

    public Date getAppDate() {
        return appDate;
    }

    public Doctor getDoc() {
        return doc;
    }

    public Patient getP() {
        return p;
    }

    public Time getsTime() {
        return sTime;
    }

    public Time geteTime() {
        return eTime;
    }


}
