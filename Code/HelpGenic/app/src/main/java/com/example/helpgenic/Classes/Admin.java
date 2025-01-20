package com.example.helpgenic.Classes;

public class Admin  extends GuestUser {

    private static Admin singletonInstance;

    public Admin( String id, String email ) {
        this.id = id;
        this.email = email;
        this.name = null;
        this.password = null;
        this.dob=null;
        this.gender = 0;
    }

    // Private constructor to prevent direct instantiation
    private Admin() {
        this.email = null;
        this.name = null;
        this.password = null;
        this.dob=null;
        this.gender = 0;
    }

    // Public method to get the singleton instance for the doctor
    public static Admin getInstance() {

        if (singletonInstance == null) {
            singletonInstance = new Admin();
        }
        return singletonInstance;
    }

    public static void setInstance(Admin d) {

        if (singletonInstance == null) {
            singletonInstance = d;
        }
    }

    public String getMail(){
        return email;
    }

}



