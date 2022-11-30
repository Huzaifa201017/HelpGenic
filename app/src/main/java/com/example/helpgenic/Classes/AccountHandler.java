package com.example.helpgenic.Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;


public class AccountHandler {



    DbHandler db;

    public void setDb(DbHandler db) {
        this.db = db;
    }

    public GuestUser validatePatientCredentials(EditText name , EditText email, EditText phoneNumber ,EditText gender ,EditText password ,EditText password2 ,EditText dob,EditText bloodGroup ,Context context ){

        if (name.length()==0)
        {
            name.setError("This field is required");
            return null;
        }
        if (email.length() == 0) {
            email.setError("This field is required");
            return null;
        }
        if (phoneNumber.length() == 0) {
            phoneNumber.setError("This field is required");
            return null;
        }
        if (password.length() == 0) {
            password.setError("This field is required");
            return null;
        }
        if (password2.length() == 0) {
            password2.setError("This field is required");
            return null;
        }if (dob.length() == 0) {
            dob.setError("This field is required");
            return null;
        }
        if(!password.getText().toString().equals(password2.getText().toString())){
            password2.setError("Passwords don't match");
            return null;
        }
        if(gender.length() == 0){
            gender.setError("This field is required");
            return null;
        }

        // also check the format of date


        db = new DbHandler();
        // create connection
        if (!db.isConnectionOpen()) {
            // =============== connect with db ===============
            db.connectToDb(context);
            // ==================================================
        }



        boolean isAlreadyExists = db.matchPatientCredentials(email.getText().toString()  ,context);





        if (!isAlreadyExists) {


            boolean g = false;
            if(Objects.equals(gender.getText().toString(),"Male") || Objects.equals(gender.getText().toString(),"male")){
                g = true;
            }

            Date dateOfBirth = null;
            DateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date temp = null;

            try {
                temp = (java.util.Date) formatter.parse(dob.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateOfBirth = new Date(temp.getTime());


            Toast.makeText(context, "Inserted", Toast.LENGTH_SHORT).show();
            int id = db.insertPatientDetailsInDb(name.getText().toString(), email.getText().toString(), password.getText().toString(),g ,dateOfBirth,bloodGroup.getText().toString(),phoneNumber.getText().toString(),context );


            // closing connection
            try {
                db.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }


            addToSharedPrefForPatient(id,"P",email.getText().toString(),name.getText().toString(),g,dateOfBirth,bloodGroup.getText().toString(),phoneNumber.getText().toString() , context);

            return new Patient(id,email.getText().toString(),null,name.getText().toString(),g,dateOfBirth,bloodGroup.getText().toString(),phoneNumber.getText().toString());

        }

        return null;

    }

    void addToSharedPrefForPatient(int id , String type, String email , String name , boolean gender, Date dob, String bloodGrup, String phNum, Context context) {

        SharedPreferences shrd =  context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = shrd.edit();

        myEdit.putString("type",type);
        myEdit.putInt("Id" , id);
        myEdit.putString("email", email);
        myEdit.putString("name" , name);
        myEdit.putString("dob" , dob.toString());
        myEdit.putBoolean("gender" , gender);
        myEdit.putString("bloodGroup" , bloodGrup);
        myEdit.putString("phNum" , phNum);
        myEdit.apply();


    }


}
