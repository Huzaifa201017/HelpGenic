package com.example.helpgenic.Classes;

import android.content.Context;
import android.widget.Toast;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Objects;

public class Patient  extends GuestUser{

    private static Patient singletonInstance;

    private String bloodGroup ;
    private String phNum;

    // Private constructor to prevent direct instantiation
    private Patient() {}

    // Public method to get the singleton instance for the doctor
    public static Patient getInstance() {

        if (singletonInstance == null) {
            singletonInstance = new Patient();
        }
        return singletonInstance;
    }

    public static void setInstance(Patient d) {

        if (singletonInstance == null) {
            singletonInstance = d;
        }
    }

    public Patient( String name ,String email , String phNum , String bloodGroup , int age, boolean gender){
        this.name = name ;
        this.phNum = phNum;
        this.email = email;
        this.bloodGroup = bloodGroup;
        this.age = age;

        if(gender){
            this.gender = 'M';
        }else{
            this.gender = 'F';
        }

        this.type = 'P';
    }

    public Patient (String name , String email ,String phoneNumber, String password, Date dob) {
        this.name = name;
        this.email = email;
        this.phNum = phoneNumber;
        this.password= password;
        this.dob=dob;
        this.gender = 0;
        this.bloodGroup=null;
        this.type = 'P';
    }

    public Patient (String id , String email , String password,String name,boolean gender, Date dob, String bloodGrup,String phNum) {

        this.email = email;
        this.password = password;
        this.id = id;
        this.dob=dob;
        this.name = name;

        if(gender){
            this.gender = 'M';
        }else{
            this.gender = 'F';
        }

        this.bloodGroup=bloodGrup;
        this.phNum = phNum;
        this.type = 'P';
    }

    public Patient (String id , String email , String password,String name,Character gender, Date dob, String bloodGrup,String phNum) {

        this.email = email;
        this.password = password;
        this.id = id;
        this.dob=dob;
        this.name = name;
        this.gender = gender;
        this.bloodGroup=bloodGrup;
        this.phNum = phNum;
        this.type = 'P';
    }


    public Patient(String id , String name ){
        this.name = name;
        this.id = id;
        this.type = 'P';
    }


    public String getPhNum() {
        return phNum;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }
}
