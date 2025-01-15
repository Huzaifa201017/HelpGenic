package com.example.helpgenic.Classes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;


public class AccountHandler {


    private FirebaseAuth mAuth;
    DbHandler db;


    public void setDb(DbHandler db) {
        this.db = db;
    }

    public Task<Boolean>  validatePatientCredentials(EditText name , EditText email, EditText phoneNumber , EditText gender , EditText password , EditText password2 , Date dob, AutoCompleteTextView bloodGroup , Context context ){


        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        if (name.length()==0)
        {
            name.setError("This field is required");
            taskCompletionSource.setResult(false);
            return taskCompletionSource.getTask();
        }
        if (email.length() == 0) {
            email.setError("This field is required");
            taskCompletionSource.setResult(false);
            return taskCompletionSource.getTask();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Invalid Email!");
            taskCompletionSource.setResult(false);
            return taskCompletionSource.getTask();
        }
        if (phoneNumber.length() == 0) {
            phoneNumber.setError("This field is required");
            taskCompletionSource.setResult(false);
            return taskCompletionSource.getTask();
        }
        if (password.length() == 0) {
            password.setError("This field is required");
            taskCompletionSource.setResult(false);
            return taskCompletionSource.getTask();
        }
        if (password2.length() == 0) {
            password2.setError("This field is required");
            taskCompletionSource.setResult(false);
            return taskCompletionSource.getTask();

        }if (dob== null) {
            Toast.makeText(context, "DOB can't be empty", Toast.LENGTH_SHORT).show();
            taskCompletionSource.setResult(false);
            return taskCompletionSource.getTask();
        }
        if(!password.getText().toString().equals(password2.getText().toString())){
            password2.setError("Passwords don't match");
            taskCompletionSource.setResult(false);
            return taskCompletionSource.getTask();
        }
        if(gender.length() == 0){
            gender.setError("This field is required");
            taskCompletionSource.setResult(false);
            return taskCompletionSource.getTask();
        }

        if(gender.length() != 0){


            if( ! (Objects.equals(gender.getText().toString(),"Male") || Objects.equals(gender.getText().toString(),"male") || Objects.equals(gender.getText().toString(),"Female") || Objects.equals(gender.getText().toString(),"female")) ){
                gender.setError("Invalid gender entered");
                taskCompletionSource.setResult(false);
                return taskCompletionSource.getTask();
            }

        }

        if(dob != null) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            String date = sdf.format(calendar.getTime());

            java.util.Date currdate1 = null;
            try {
                currdate1 =new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            assert currdate1 != null;
            if(currdate1.compareTo(dob) < 0){

                Toast.makeText(context, "Invalid DOB !", Toast.LENGTH_SHORT).show();
                taskCompletionSource.setResult(false);
                return taskCompletionSource.getTask();
            }
        }

        // TODO: check gender field

        mAuth = FirebaseAuth.getInstance();



        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    // Sign in success
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.d("Accounts Handler", "User Created Successfully with ID: " + user.getUid());

                    if (user != null){
                        Log.d("Accounts Handler", "User is Signed in");
                        mAuth.signOut();
                        Log.d("Accounts Handler", "User is Logged Out");
                    }



                    // Inserting data in database

                    // Parsing Gender
                    char g = 'F';
                    if(Objects.equals(gender.getText().toString(),"Male") || Objects.equals(gender.getText().toString(),"male")){
                        g = 'M';
                    }


                    // Parsing Date of Birth

                    Date dateOfBirth = null;
                    SimpleDateFormat formatter;
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date temp = null;

                    try {
                        temp = (java.util.Date) formatter.parse(dob.toString());
                    } catch (ParseException e) {
                        Log.d("Accounts Handler", "Date Parsing Error");
                    }
                    dateOfBirth = new Date(temp.getTime());

                    // Now Inserting ...
                    db = new DbHandler();
                    db.insertPatientDetailsInDb(user.getUid().toString(),name.getText().toString(), g ,dateOfBirth,bloodGroup.getText().toString(),phoneNumber.getText().toString() );

                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    taskCompletionSource.setResult(true);


                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("Accounts Handler", "Failure in User Creation", task.getException());
                    taskCompletionSource.setResult(false);

                }
            }
        });

        return taskCompletionSource.getTask();

    }


    public boolean verifyCredentials(EditText name, EditText email, EditText password, EditText gender, EditText phoneNo, Date _date, EditText _addr, Context c) {
        boolean flag = true;
        if (name.length() == 0) {
            name.setError("Name cannot be empty!");
            flag = false;
        } else if (name.length() <= 4) {
            name.setError("Minimum length should be 5!");
            flag = false;
        }

        if (email.length() == 0) {
            email.setError("Email cannot be empty!");
            flag = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Invalid Email!");
            flag = false;
        }
        if (password.length() == 0) {
            password.setError("Password cannot be empty!");
            flag = false;
        } else if (password.length() <= 8) {
            password.setError("Password must contain at least 8 characters!");
            flag = false;
        }
        if (!gender.getText().toString().equals("M") && !gender.getText().toString().equals("F")) {
            gender.setError("Please enter either M or F");
            flag = false;
        }
        if (phoneNo.length() != 11) {
            phoneNo.setError("Phone no must contain exactly 11 characters!");
            flag = false;
        }
        if (_addr.length() == 0) {
            _addr.setError("Address cannot be null");
            flag = false;
        }
        if (_date == null){
            Toast.makeText(c, "DOB Missing", Toast.LENGTH_SHORT).show();
            flag=false;
        }
        else if (_date!=null)
        {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            String date = sdf.format(calendar.getTime());
            java.util.Date currdate1 = null;
            try {
               currdate1 =new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            java.util.Date d = new Date(_date.getTime());

            assert currdate1 != null;
            if(currdate1.compareTo(_date) < 0){

                Toast.makeText(c, "Invalid DOB !", Toast.LENGTH_SHORT).show();
                flag=false;
            }
        }
        return flag;
    }

}
