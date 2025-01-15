package com.example.helpgenic.Classes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.sql.Date;
import java.util.Objects;

public class AuthenticateUser {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DbHandler db;

    public void setDb(DbHandler db) {
        this.db = db;
    }

    private void setUser(String uID, String email, TaskCompletionSource<GuestUser> taskCompletionSource, Context context) {

        if (!Objects.equals(email, "admin@gmail.com")) {

            // getting user object
            db.getUserDetails(uID).addOnCompleteListener(fetchDocumentTask -> {

                if (fetchDocumentTask.isSuccessful()) {

                    DocumentSnapshot userDocument = fetchDocumentTask.getResult();


                    Log.d("Authenticate User", "Fetch Document Task Successful");
                    Log.d("Authenticate User", "User is Signed in with uID: " + uID);


                    // if document is null, then it was a rejected doctor by admin, delete his account now
                    if (userDocument == null){

                        Log.d("Authenticate User", "User Document is null");

                        FirebaseUser user = mAuth.getCurrentUser();

                        user.delete().addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {
                                Log.d("Authenticate User", "User Deleted Successfully");
                                Toast.makeText(context, "Sorry, we can't move forward with your application", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("Authenticate User", "User Deletion Failed");
                            }
                        });

                        taskCompletionSource.setResult(null);


                    }else{

                        // else it's a legit user, create relevant user object
                        String type = userDocument.getString("type");

                        if (Objects.equals("P", type)) {

                            // if user is patient
                            String name = userDocument.getString("name");
                            boolean gender = Boolean.TRUE.equals(userDocument.getBoolean("gender"));

                            java.util.Date dataInUtil = userDocument.getDate("dob");
                            Date dob = new Date(Objects.requireNonNull(dataInUtil).getTime());

                            String phNum = userDocument.getString("phoneNum");
                            String bloodGroup = userDocument.getString("bloodGroup");

                            Patient p = new Patient(uID, email, null, name, gender, dob, bloodGroup, phNum);
                            taskCompletionSource.setResult(p);

                        } else if (Objects.equals("D", type)) {

                            boolean isVerified = Boolean.TRUE.equals(userDocument.getBoolean("verified"));

                            if (isVerified) {

                                String name = userDocument.getString("name");
                                String phNum = userDocument.getString("phoneNum");

                                java.util.Date dataInUtil = userDocument.getDate("dob");
                                Date dob = new Date(Objects.requireNonNull(dataInUtil).getTime());

                                String specialization = userDocument.getString("specialization");
                                boolean isSurgeon = Boolean.TRUE.equals(userDocument.getBoolean("surgeon"));
                                char gender = Objects.requireNonNull(userDocument.getString("gender")).charAt(0);

                                float rating = Objects.requireNonNull(userDocument.getDouble("rating")).floatValue();

                                Doctor d = new Doctor(uID, email, name, phNum, specialization, isSurgeon, gender, rating, dob);
                                taskCompletionSource.setResult(d);

                            } else {
                                Toast.makeText(context, "You'll be allowed to log in once your degree is verified", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                                taskCompletionSource.setResult(null);
                            }


                        }
                    }

                } else {
                    Log.d("Authenticate User", "Fetch Document Task Failed: " + fetchDocumentTask.getResult());
                    taskCompletionSource.setResult(null);
                }
            });

        }else{
            Admin a = new Admin(uID, email);
            taskCompletionSource.setResult(a);
        }
    }


    public Task<GuestUser> validateCredentials(EditText email , EditText password , Context context ){

        TaskCompletionSource<GuestUser> taskCompletionSource = new TaskCompletionSource<>();


        if (email.length() == 0) {
            email.setError("This field is required");

            taskCompletionSource.setResult(null);
            return taskCompletionSource.getTask();

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Invalid Email!");

            taskCompletionSource.setResult(null);
            return taskCompletionSource.getTask();

        }
        if (password.length() == 0) {
            password.setError("This field is required");

            taskCompletionSource.setResult(null);
            return taskCompletionSource.getTask();

        }


        if (mAuth.getCurrentUser() == null) {

            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Authenticate User", "signInWithEmail:success");

                        if (mAuth.getCurrentUser() != null){

                            FirebaseUser user = mAuth.getCurrentUser();
                            String uID = user.getUid();
                            String email = user.getEmail();

                            setUser(uID, email, taskCompletionSource, context);
                        }

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("Authenticate User", "signInWithEmail:failure", task.getException());
                        Toast.makeText(context, "Email or Password are incorrect", Toast.LENGTH_SHORT).show();
                        taskCompletionSource.setResult(null);

                    }
                }
            });

        }
        return taskCompletionSource.getTask();

    }

    public Task<GuestUser> checkIfAlreadyLoggedIn(Context context){

        TaskCompletionSource<GuestUser> taskCompletionSource = new TaskCompletionSource<>();
        if (mAuth.getCurrentUser() != null){

            FirebaseUser user = mAuth.getCurrentUser();
            String uID = user.getUid();
            String email = user.getEmail();


            db = new DbHandler();
            setUser(uID, email, taskCompletionSource, context);

        }else{
            taskCompletionSource.setResult(null);
        }

        return taskCompletionSource.getTask();
    }

    void addToSharedPrefForPatient(int id , String type, String email ,String name , boolean gender, Date dob, String bloodGrup,String phNum,Context context) {

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

    void addToSharedPrefForDoctor(int id , String type, String email ,String name , boolean gender, Date dob, String specialization , boolean isSurgeon , String accNum ,float rating, Context context){

        SharedPreferences shrd =  context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = shrd.edit();

        myEdit.putString("type",type);
        myEdit.putInt("Id" , id);
        myEdit.putString("email", email);
        myEdit.putString("name" , name);
        myEdit.putString("dob" , dob.toString());
        myEdit.putBoolean("gender" , gender);
        myEdit.putString("specialization" , specialization);
        myEdit.putBoolean("isSurgeon" , isSurgeon);
        myEdit.putString("accNum", accNum);
        myEdit.putFloat("rating", rating);
        myEdit.apply();
    }

    void addToSharedPrefForAdmin(int id , String type, String email, Context context){

        SharedPreferences shrd =  context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = shrd.edit();

        myEdit.putString("type",type);
        myEdit.putInt("Id" , id);
        myEdit.putString("email", email);
        myEdit.apply();
    }

}
