package com.example.helpgenic;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.ReportsHandler;

public class DisplayImage extends AppCompatActivity {

    String docId = "";
    ImageView iv;
    Button reject , verify;
    DbHandler db = new DbHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        reject= findViewById(R.id.reject);
        verify= findViewById(R.id.accept);

        docId = getIntent().getStringExtra("docId");

        iv = findViewById(R.id.document);


        ReportsHandler rh = new ReportsHandler();

        db.getDocument(docId).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                byte[] bytesData = task.getResult();
                rh.loadImageFromByteData(iv,bytesData);
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.updateDoctorVerifyStatus(docId).addOnCompleteListener(task -> {

                    if(task.isSuccessful() && task.getResult()){

                        Toast.makeText(DisplayImage.this, "Operation Successful", Toast.LENGTH_SHORT).show();
                        finish(); // return back

                    }else{

                        Toast.makeText(DisplayImage.this, "Operation Unsuccessful", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.removeDoctor(docId).addOnCompleteListener(task -> {

                    if(task.isSuccessful() && task.getResult()){

                        Toast.makeText(DisplayImage.this, "Operation Successful", Toast.LENGTH_SHORT).show();
                        finish(); // return back

                    }else{

                        Toast.makeText(DisplayImage.this, "Operation Unsuccessful", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });




    }
}