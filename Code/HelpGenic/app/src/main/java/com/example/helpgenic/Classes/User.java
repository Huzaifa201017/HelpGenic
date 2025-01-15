package com.example.helpgenic.Classes;

import java.io.Serializable;
import java.sql.Date;

public abstract class User implements Serializable {

    protected String name,email,password;
    protected char gender;
    protected java.sql.Date dob;
    protected String id;
    protected char type;


    protected int age;
    AuthenticateUser au ;
    BookingManager bm;
    AccountHandler ah;
    ReportsHandler rh;

    public void setRh(ReportsHandler rh) {
        this.rh = rh;
    }
    public int getAge() {
        return age;
    }

    public void setAh(AccountHandler ah) {
        this.ah = ah;
    }

    public void setBm(BookingManager bm) {
        this.bm = bm;
    }

    public void setAu(AuthenticateUser au) {
        this.au = au;
    }

    public void setType(char type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public char getGender() {
        return gender;
    }

    public Date getDob() {
        return dob;
    }

    public String getId() {
        return id;
    }

    public String getMail(){
       return email;
    }

    public char getType() {
        return type;
    }

}
