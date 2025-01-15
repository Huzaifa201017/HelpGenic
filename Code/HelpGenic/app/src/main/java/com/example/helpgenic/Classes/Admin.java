package com.example.helpgenic.Classes;

public class Admin  extends GuestUser {

    public Admin(){
        this.email = null;
        this.name = null;
        this.password = null;
        this.dob=null;
        this.gender = 0;
    }
    public Admin( String id, String email ) {
        this.id = id;
        this.email = email;
        this.name = null;
        this.password = null;
        this.dob=null;
        this.gender = 0;
    }
    public String getMail(){
        return email;
    }

}



