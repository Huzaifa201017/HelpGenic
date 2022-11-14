package com.example.helpgenic.Classes;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbHandler {

    private Connection connection = null;


    String url="jdbc:mysql://sda.mysql.database.azure.com:3306/helpgenic?useSSL=true&loginTimeout=30";

    public void  connectToDb(Context ptr){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        try {

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, "azureuser", "Muhammad167");

            Toast.makeText(ptr,"Success", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {

            Log.e(String.valueOf(1), "Error occured here 1!");
            Toast.makeText(ptr, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
