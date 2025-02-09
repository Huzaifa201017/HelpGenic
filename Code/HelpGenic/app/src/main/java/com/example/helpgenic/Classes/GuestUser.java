package com.example.helpgenic.Classes;

import android.content.Context;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.gms.tasks.Task;

import java.sql.Date;


public class GuestUser extends User{


    public GuestUser(){};
    public GuestUser(String name, String mail, String password, char gender, Date d1) {
        super();
        this.name = name;
        this.email = mail;
        this.password = password;
        this.gender = gender;
        this.dob = d1;
    }

    public Task<Character> logIn(EditText email , EditText password , Context context ){


        DbHandler db = new DbHandler();


        this.au.setDb(db);
        return this.au.validateCredentials(email,password,context);


    }

    public Task<Boolean> SignUpPatient (EditText name , EditText email, EditText phoneNumber , EditText password1, EditText password2 , EditText gender , Date dob, AutoCompleteTextView bloodGroup, Context context )
    {
         return this.ah.validatePatientCredentials(name,email,phoneNumber,gender,password1, password2,dob,bloodGroup,context);

    }

}
