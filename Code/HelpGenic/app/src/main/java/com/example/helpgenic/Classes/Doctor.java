package com.example.helpgenic.Classes;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Objects;

public class Doctor extends GuestUser {

    private static Doctor singletonInstance;


    private String specialization;
    private String degree;
    private String phNum;
    private float rating;
    private boolean isSurgeon;
    private int fee;

    private ArrayList<VirtualAppointmentSchedule> vSchedule;
    private ArrayList<PhysicalAppointmentSchedule> pSchedule;
    private ArrayList<Comment> comments;

    // Private constructor to prevent direct instantiation
    private Doctor() {}

    // Public method to get the singleton instance for the doctor
    public static Doctor getInstance() {

        if (singletonInstance == null) {
            singletonInstance = new Doctor();
        }
        return singletonInstance;
    }

    public static void setInstance(Doctor d) {

        if (singletonInstance == null) {
            singletonInstance = d;
        }
    }

    public static void clearInstance() {

        singletonInstance = null;
    }

    public Doctor(String id , String email, String name , String phNum , String specialization, boolean isSurgeon  , Character gender , float rating, Date dob, int fee){
        this.id = id;
        this.email = email;
        this.name = name;
        this.phNum = phNum;
        this.specialization = specialization;
        this.isSurgeon = isSurgeon;
        this.gender = gender;
        this.rating = rating;
        this.type = 'D';
        this.dob = dob;
        this.fee = fee;
    }


    public Doctor(String name,String specialization,String id){
        this.id = id;
        this.name=name;
        this.specialization=specialization;
        this.isSurgeon=false;
        this.rating=0;
        this.pSchedule=null;
        this.vSchedule=null;
        this.email = null;
        this.phNum = null;
        this.gender = 0;
        this.type = 'D';
    }


    public Doctor(String id ,String email,String specialization, boolean isSurgeon, String accountNumber , String name  , Character gender , String meetId, float rating) {
        super();

        this.email = email;
        this.specialization = specialization;
        this.isSurgeon = isSurgeon;
        this.pSchedule=null;
        this.vSchedule=null;
        this.name = name;
        this.gender = gender;
        this.id = id;
        this.rating = rating;

    }
    public Doctor(String email , String password) {
        this.email = email;
        this.password = password;
        this.dob=null;
        this.gender = 0;
        this.specialization=null;
        this.isSurgeon=false;
        this.pSchedule=null;
        this.vSchedule=null;
        this.rating = 0;
    }
    public Doctor(String specialization, boolean isSurgeon,String accountNumber,String degree){
        this.specialization=specialization;
        this.isSurgeon=isSurgeon;
        this.degree=degree;
        this.rating=0;
        this.pSchedule=null;
        this.vSchedule=null;
    }


    public Doctor(String id, String name, String specialization, char gender, float rating, boolean isSurgeon, int fee, String email) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.gender = gender;
        this.rating = rating;
        this.isSurgeon = isSurgeon;
        this.fee = fee;
        this.email = email;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getFee() {
        return fee;
    }

    public void setVSch(ArrayList<VirtualAppointmentSchedule> vList){
        this.vSchedule = vList;
    }

    public void setVSch(VirtualAppointmentSchedule v){
        if (vSchedule != null) {
            vSchedule.add(v);
        }
    }

    public void setVSch(String day, Time sTime , Time eTime){
        if(Objects.equals(vSchedule ,null)){

            vSchedule = new  ArrayList<VirtualAppointmentSchedule>();
        }
        vSchedule.add(new VirtualAppointmentSchedule(day,sTime,eTime));
    }

    public void setPSchedule(ArrayList<PhysicalAppointmentSchedule> pList){
        this.pSchedule = pList;
    }

    public void setPSchedule(PhysicalAppointmentSchedule p){
        if (pSchedule != null) {
            pSchedule.add(p);
        }
    }



    public ArrayList<PhysicalAppointmentSchedule> getPSchedule() {
        return pSchedule;
    }

    public String getSpecialization() {
        return specialization;
    }

    public float getRating() {
        return rating;
    }

    public boolean isSurgeon() {
        return isSurgeon;
    }

    public ArrayList<VirtualAppointmentSchedule> getvSchedule() {
        return vSchedule;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }



}
