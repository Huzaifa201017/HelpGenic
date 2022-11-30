package com.example.helpgenic;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;

public class myAmplify {

    public  void initalizeAmplify(Context context){

        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(context);
            Toast.makeText(context, "Successfully amplified !", Toast.LENGTH_SHORT).show();
            Log.i("MyAmplifyApp", "Initialized myAmplify");

        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize myAmplify", error);
        }
    }

    public  void uploadFile(Context context, String filePath) {


        File exampleFile = new File(filePath);



        Amplify.Storage.uploadFile(
                "ExampleKey2",
                exampleFile,
                result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
        );
    }
}
