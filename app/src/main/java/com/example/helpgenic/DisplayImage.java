package com.example.helpgenic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.ReportsHandler;

import java.sql.SQLException;

public class DisplayImage extends AppCompatActivity {

    int documentId = 0, docId  = 0;
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        documentId = getIntent().getIntExtra("documentId" ,0);
        docId = getIntent().getIntExtra("docId" ,0);

        iv = findViewById(R.id.document);


        ReportsHandler rh = new ReportsHandler();
        DbHandler db = new DbHandler();
        db.connectToDb(DisplayImage.this);


        if(documentId != 0){
            String bytesData = db.getPatientDocumentsFromDb(documentId, this);
            rh.loadImageFromByteData(iv,bytesData);
        }else if(docId != 0){
            String byteData = db.loadDoctorDegreeImageFromDb(docId,DisplayImage.this);
            rh.loadImageFromByteData(iv,byteData);
        }


        try {
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}